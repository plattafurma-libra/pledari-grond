/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uni_koeln.spinfo.maalr.lucene.config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.ColumnReference;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.ColumnSelector;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.ColumnSelectorOption;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.DictionaryConfiguration;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.DictionaryConfiguration.UiConfigurations;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.IndexedColumn;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.MaalrFieldType;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryBuilder;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryBuilderOption;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryKey;
import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueSpecification;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueType;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiField;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.BuildInQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.FieldFactory;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.DefaultQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.ExactMatchQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;

/**
 * Helper class to generate a lucene index and matching lucene queries from a maalr configuration.
 * It analyzes the XML defined search configuration, and generates a mapping from database-columns
 * to fields in lucene documents, based on the way queries are defined in the configuration.
 * Additionally, it instantiated and configures {@link MaalrQueryBuilder} objects to speed up
 * the time required to convert a {@link MaalrQuery} into a lucene {@link Query}.
 *  
 * @author sschwieb
 *
 */
public class LuceneIndexManager {
	
	private static Logger logger = LoggerFactory.getLogger(LuceneIndexManager.class);

	private static LuceneIndexManager indexer;
	
	private String[] allColumns;
	
	private static Set<String> errors = new HashSet<String>();
	
	/**
	 * The {@link FieldFactory}-objects required by the current search configuration.
	 * For each {@link IndexedColumn} defined in the configuration, one {@link FieldFactory}
	 * will be generated. Key of the map is the source attribute of the {@link IndexedColumn},
	 * value is the list of all {@link FieldFactory}-objects which referred to this source.
	 */
	private Map<String, List<FieldFactory>> fieldFactories = new HashMap<String, List<FieldFactory>>();
	
	/**
	 * Contains all field names which are ignored when generating the index.
	 * A field is ignored if it has not been set up in the indexConfiguration-
	 * section of the search configuration. For each ignored field, a warning
	 * message will be logged.
	 */
	private Set<String> ignored = new HashSet<String>();
	
	/**
	 * The {@link BuilderRegistry} used to generate Lucene {@link Query} parts.
	 */
	private BuilderRegistry builderRegistry = new BuilderRegistry();

	
	private HashMap<String, Set<String>> choiceIdsToFields;
	
	/**
	 * A mapping from columns to {@link MaalrQueryBuilder} instances, used for
	 * oracle queries.
	 * 
	 */
	private Map<String, MaalrQueryBuilder> oracleBuilder = new HashMap<String, MaalrQueryBuilder>();
	
	/**
	 * Returns the singleton instance of this class.
	 * @return
	 */
	public static synchronized LuceneIndexManager getInstance() {
		if(indexer == null) {
			try {
				indexer = new LuceneIndexManager(Configuration.getInstance().getDictionaryConfig());
			} catch (Exception e) {
				logger.error("Failed to initialize indexer!", e);
				if(errors.size() > 0) {
					logger.error("The following errors were detected in the configuration:");
					for (String error : errors) {
						logger.error("   " + error);
					}
				}
				throw new RuntimeException(e);
			}
		}
		return indexer;
	}
	
	/**
	 * Helper class used for querying. During startup, all query builders will be registered,
	 * such that they can be quickly found when a {@link MaalrQuery} has to be converted to a
	 * Lucene {@link Query}.
	 * 
	 * @author sschwieb
	 *
	 */
	class BuilderRegistry {
		
		/**
		 * Helper class to keep track of references between 
		 * {@link QueryBuilder}, {@link ColumnSelector} and {@link QueryKey}. 
		 */
		class QueryBuilderMetaData {

			private String queryBuilderId;
			private String columnSelectorId;
			private String queryKey;

			public QueryBuilderMetaData(String queryBuilderId, String columnSelectorId,
					String queryKey) {
				this.queryBuilderId = queryBuilderId;
				this.columnSelectorId = columnSelectorId;
				this.queryKey = queryKey;
			}
		}
		
		/**
		 * Mapping from query builder ids to {@link QueryBuilderMetaData}
		 */
		private Map<String, QueryBuilderMetaData> queryBuilderConfigurations = new HashMap<String, QueryBuilderMetaData>();
		
		/**
		 * Mapping from {@link ColumnSelector} ids to default {@link ColumnSelectorOption} ids
		 */
		private Map<String, String> defaultColumnSelectorOptions = new HashMap<String, String>();
		
		/**
		 * Mapping from {@link QueryBuilder} ids to default {@link QueryBuilderOption} ids
		 */
		private Map<String, String> defaultQueryBuilderOptions = new HashMap<String, String>();
		
		/**
		 * Contains {@link MaalrQueryBuilder} to generate queries for any registered combination
		 * of {@link QueryBuilder}s and {@link ColumnSelector}s.
		 * First key: ID of the query modifier
		 * Second key: ID of the query modifier option
		 * Third key: ID of the field choice option
		 */
		private Map<String, Map<String, Map<String, List<MaalrQueryBuilder>>>> registeredBuilder = new HashMap<String, Map<String,Map<String,List<MaalrQueryBuilder>>>>();
		
		/**
		 * A collection of all query builder ids
		 */
		private Set<String> allQueryBuilderIds = new HashSet<String>();
		
		/**
		 * Reverse mapping from column selectors to query builders
		 */
		private Map<String, String> selectorsToBuilders = new HashMap<String, String>();
		
		/**
		 * Mapping from column selectors to depdencencies
		 */
		private Map<String, String> dependencies = new HashMap<String, String>();
		
		
		public void registerBuilder(String queryBuilderId, String queryBuilderOptionId, boolean isDefaultQueryBuilderOption, String columnSelectorId, String columnSelectorOptionId, boolean isDefaultColumnSelectorOption, String queryKey, MaalrQueryBuilder builder) {
			// add the id of the set of all builder ids
			allQueryBuilderIds.add(queryBuilderId);
			// store the query builder meta data
			QueryBuilderMetaData config = new QueryBuilderMetaData(queryBuilderId, columnSelectorId, queryKey);
			queryBuilderConfigurations.put(queryBuilderId, config);
			// prepare reverse lookup from column selector to query builder
			selectorsToBuilders.put(columnSelectorId, queryBuilderId);
			// Register the query builder in the lookup map
			// First key: query builder id
			Map<String, Map<String, List<MaalrQueryBuilder>>> modifiersAndChoices = registeredBuilder.get(queryBuilderId);
			if(modifiersAndChoices == null) {
				modifiersAndChoices = new HashMap<String, Map<String,List<MaalrQueryBuilder>>>();
				registeredBuilder.put(queryBuilderId, modifiersAndChoices);
			}
			// Second key: query builder option id
			Map<String, List<MaalrQueryBuilder>> choicesAndBuilders = modifiersAndChoices.get(queryBuilderOptionId);
			if(choicesAndBuilders == null) {
				choicesAndBuilders = new HashMap<String, List<MaalrQueryBuilder>>();
				modifiersAndChoices.put(queryBuilderOptionId, choicesAndBuilders);
			}
			// Third key: column selector option id
			List<MaalrQueryBuilder> builders = choicesAndBuilders.get(columnSelectorOptionId);
			if(builders == null) {
				builders = new ArrayList<MaalrQueryBuilder>();
				choicesAndBuilders.put(columnSelectorOptionId, builders);
			}
			logger.info("Registered query builder for " + queryBuilderId + ":" + queryBuilderOptionId + ", " + columnSelectorId + ":" + columnSelectorOptionId);
			builders.add(builder);
			// Register default options
			if(isDefaultQueryBuilderOption) {
				String old = defaultQueryBuilderOptions.put(queryBuilderId, queryBuilderOptionId);
				if(old != null && !old.equals(queryBuilderOptionId)) {
					String msg = "Invalid configuration - Two or more query modifier are defined as default: '" + old + "' and '" + queryBuilderOptionId + "' for id '" + queryBuilderId + "'";
					logger.error(msg);
					errors.add(msg);
				}
			}
			if(isDefaultColumnSelectorOption) {
				String old = defaultColumnSelectorOptions.put(columnSelectorId, columnSelectorOptionId);
				if(old != null && !old.equals(columnSelectorOptionId)) {
					String msg = "Invalid configuration - Two or more field choices are defined as default: '" + old + "' and '" + columnSelectorOptionId + "' for id '" + columnSelectorId+"'";
					logger.error(msg);
					errors.add(msg);
				}
			}
		}
		
		/**
		 * Generate a set of sub-queries of of the query map for the given modifier.
		 * @param maalrQuery
		 * @param queryBuilderId
		 * @return the set of sub-queries, or null, of the query builder wasn't referenced in the query map
		 */
		private Set<Query> getQuery(Map<String, String> maalrQuery, String queryBuilderId) {
			QueryBuilderMetaData config = queryBuilderConfigurations.get(queryBuilderId);
			String columnSelectorOption = maalrQuery.get(config.columnSelectorId);
			String dependsOn = dependencies.get(config.columnSelectorId);
			// Resolve dependency - if the column selector depends on another selector,
			// the column selector option has to be overridden.
			if(dependsOn != null) {
				String referencedQueryBuilderId = selectorsToBuilders.get(dependsOn);
				QueryBuilderMetaData dependsOnMod = queryBuilderConfigurations.get(referencedQueryBuilderId);
				String selected = maalrQuery.get(dependsOnMod.columnSelectorId);
				if(selected == null) {
					selected = defaultColumnSelectorOptions.get(dependsOnMod.columnSelectorId);
				}
				columnSelectorOption = selected;
			}
			String queryString = maalrQuery.get(config.queryKey);
			if(queryString == null) return null;
			if(columnSelectorOption == null) {
				columnSelectorOption = defaultColumnSelectorOptions.get(config.columnSelectorId);
			}
			if(columnSelectorOption == null) {
				return null;
			}
			String queryBuilderOption = maalrQuery.get(config.queryBuilderId);
			if(queryBuilderOption == null) {
				queryBuilderOption = defaultQueryBuilderOptions.get(config.queryBuilderId);
			}
			if(queryBuilderOption == null) {
				return null;
			}
			Set<Query> toReturn = new HashSet<Query>();
			// Find the list of builders for the given query, and apply each of them on
			// the query string.
			List<MaalrQueryBuilder> builder = registeredBuilder.get(queryBuilderId).get(queryBuilderOption).get(columnSelectorOption);
			for (MaalrQueryBuilder mqb : builder) {
				toReturn.addAll(mqb.transform(queryString));
			}
			return toReturn;
		}
		
		public void setDependency(String selectorId, String dependsOnSelector) {
			dependencies.put(selectorId, dependsOnSelector);
		}
		
	}
	
	/**
	 * 
	 * @param configuration the search configuration, as defined in searchconfig.xml
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private LuceneIndexManager(DictionaryConfiguration configuration) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		logger.info("Expanding default fields...");
		// All column items defined in the configuration file
		List<IndexedColumn> definedColumns = configuration.getIndexedColumns();
		Set<String> uniqueNames = new HashSet<>();
		// The final set of columns
		Set<IndexedColumn> finalItems = new TreeSet<IndexedColumn>(new Comparator<IndexedColumn>() {

			@Override
			public int compare(IndexedColumn o1, IndexedColumn o2) {
				return o1.getIndexFieldName().compareTo(o2.getIndexFieldName());
			}
		});
		// Set default values for all defined columns - they are used to store the values
		// as they are, without any tokenization or modification.
		for (IndexedColumn item : definedColumns) {
			item.setIndexFieldName(item.getSource());
			item.setStored(true);
			item.setAnalyzed(false);
			item.setLowerCase(false);
			item.setUsesWhitespaceAnalyzer(false);
			if(item.getType() == null) {
				item.setType(MaalrFieldType.TEXT);
			} else {
				if(item.getType() != MaalrFieldType.CSV) {
					errors.add("Column '" + item.getSource() + "' should not explicitly define a 'type' unless it is 'CSV'.");
				}
			}
			boolean added = finalItems.add(item);
			if(!added) {
				errors.add("Column '" + item.getSource() + "' was found more than once in the list of indexed columns.");
			}
			logger.info("Required default field: " + item);
		}
		// Create fields required for static dictionary pages
		String firstLanguageMain = Configuration.getInstance().getLemmaDescription().getFirstLanguage().getMainColumn();
		String secondLanguageMain = Configuration.getInstance().getLemmaDescription().getSecondLanguage().getMainColumn();
		finalItems.add(getSortColumn(firstLanguageMain));
		finalItems.add(getSortColumn(secondLanguageMain));
		finalItems.addAll(getDictionaryItem(firstLanguageMain));
		finalItems.addAll(getDictionaryItem(secondLanguageMain));
		// Create fields required for oracles
		autoGenerateOracleFields(finalItems);
		// Keep track of all column names - required to convert from lucene documents
		// to lemma versions
		Set<String> columnNames = new TreeSet<String>();
		for (IndexedColumn item : finalItems) {
			columnNames.add(item.getSource());
		}
		if(!columnNames.contains(firstLanguageMain)) {
			errors.add("Main column '" + firstLanguageMain + "' was not found in the set of all columns.");
		}
		if(!columnNames.contains(secondLanguageMain)) {
			errors.add("Main column '" + secondLanguageMain + "' was not found in the set of all columns.");
		}
		allColumns = new String[columnNames.size()];
		columnNames.toArray(allColumns);
		// Lookup-Table to find column selectors by their id
		Map<String, ColumnSelector> columnSelectorsById = new HashMap<String, ColumnSelector>();
		if(configuration.getColumnSelectors().isEmpty()) {
			errors.add("No column selectors are defined!");
		}
		for (ColumnSelector selector : configuration.getColumnSelectors()) {
			ColumnSelector old = columnSelectorsById.put(selector.getId(), selector);
			if(old != null) {
				errors.add("Column selector id '" + selector.getId() + "' is defined more than once.");
			}
			uniqueNames.add(selector.getId());
		}
		choiceIdsToFields = new HashMap<String, Set<String>>();
		Set<String> queryKeys = new HashSet<String>();
		for (QueryKey key : configuration.getQueryKeys()) {
			queryKeys.add(key.getId());
			if(!uniqueNames.add(key.getId())) {
				errors.add("Query Key '" + key.getId() + "' does not have a unique id in the configuration");
			}
		}
		if(configuration.getQueryKeys().isEmpty()) {
			errors.add("No query keys are defined!");
		}
		// Generate all required lucene fields, by analyzing all combinations of
		// query modifier options and field choices. For each combination, one or more 
		// individual MaalrQueryBuilders will be instantiated and stored in the
		// builder registry. 
		Map<String, QueryBuilder> queryBuilderById = new HashMap<String, QueryBuilder>();
		if(configuration.getQueryModifier().isEmpty()) {
			logger.error("No query builders are defined!");
		}
		for (QueryBuilder builderConfiguration : configuration.getQueryModifier()) {
			if(!uniqueNames.add(builderConfiguration.getId())) {
				errors.add("Query Builder '" + builderConfiguration.getId() + "' does not have a unique id in the configuration");
			}
			ColumnSelector selector = columnSelectorsById.get(builderConfiguration.getColumnSelectorId());
			if(selector == null) {
				errors.add("Illegal query builder configuration '" + builderConfiguration.getId() + ": The referenced column selector does not exist.");
			}
			if(!queryKeys.contains(builderConfiguration.getQueryKeyId())) {
				errors.add("Illegal query builder configuration '" + builderConfiguration.getId() + ": The referenced query key does not exist.");
			}
			QueryBuilder old = queryBuilderById.put(builderConfiguration.getId(), builderConfiguration);
			if(old != null) {
				errors.add("Query builder id '" + old.getId() + "' is defined more than once.");
			}
			String columnSelectorId = builderConfiguration.getColumnSelectorId();
			ColumnSelector columnSelector = columnSelectorsById.get(columnSelectorId);
			if(columnSelector == null) {
				errors.add("Illegal query builder '" + builderConfiguration.getId() + "': Referenced column selector '" + columnSelectorId + "' does not exist.");
			}
			logger.info("Processing query builder '" + builderConfiguration.getId() + "', related to field choice '" + columnSelector.getId() + "' and input field '" + builderConfiguration.getQueryKeyId()+"'");
			List<QueryBuilderOption> options = builderConfiguration.getOptions();
			int defaultQueryOptionCounter = 0;
			if(options.isEmpty()) {
				errors.add("Query builder '" + builderConfiguration + "' does not define any options!");
			}
			for (QueryBuilderOption qmOption : options) {
				if(qmOption.isDefault()) {
					defaultQueryOptionCounter++;
				}
				String builderClass = qmOption.getBuilderClass();
				// Find the MaalrQueryBuilder to use for the given builder option 
				Class<?> clazz = null;
				if(builderClass != null && qmOption.getPreset() != null) {
					errors.add("Invalid query builder option: You cannot define both builderClass and preset.");
				}
				if(builderClass == null) {
					// Use a default builder
					String preset = qmOption.getPreset();
					BuildInQueryBuilder buildinBuilder = BuildInQueryBuilder.valueOf(preset);
					clazz = buildinBuilder.getClazz();
				} else {
					// Use a custom builder
					logger.info("Processing query modifier option '" + qmOption.getId() + "', custom transformer " + builderClass);
					clazz = Thread.currentThread().getContextClassLoader().loadClass(builderClass);
				}
				Set<String> mapping = choiceIdsToFields.get(builderConfiguration.getQueryKeyId());
				if(mapping == null) {
					mapping = new HashSet<String>();
					choiceIdsToFields.put(builderConfiguration.getQueryKeyId(), mapping);
				}
				String columnSelectorDependency = columnSelector.getDepends();
				if(columnSelectorDependency != null) {
					if(!columnSelectorsById.containsKey(columnSelector.getId())) {
						errors.add("Illegal selector dependency: '" + columnSelector.getId() + "' refers to non existing '" + columnSelectorDependency+"'");
					}
					ColumnSelector reference = columnSelectorsById.get(columnSelectorDependency);
					if(reference.getDepends() != null) {
						errors.add("Illegal selector configuration related to selector '" + columnSelectorDependency + "': You cannot chain column selector dependencies.");
					}
					builderRegistry.setDependency(columnSelector.getId(), columnSelectorDependency);
				}
				List<ColumnSelectorOption> selectorOptions = columnSelector.getOptions();
				int defaultColumnSelectorCounter = 0;
				if(selectorOptions.isEmpty()) {
					errors.add("Column selector '" + columnSelector.getId() + "' does not define any options!");
				}
				for (ColumnSelectorOption selectorOption : selectorOptions) {
					if(selectorOption.isDefault()) defaultColumnSelectorCounter++;
					List<ColumnReference> columnReferences = selectorOption.getColumnReferences();
					if(columnReferences.isEmpty()) {
						errors.add("Column selector option '" + columnSelector.getId() + ":" + selectorOption.getId() + "' does not refer to any columns!");
					}
					for (ColumnReference reference : columnReferences) {
						// Create a new query builder for each referenced column
						MaalrQueryBuilder builder = (MaalrQueryBuilder) clazz.newInstance();
						builder.setColumn(reference.getReference());
						// Get the indexed columns required by the builder, and add them to
						// the set of all required columns. This ensures that all columns
						// required for querying will be generated.
						Set<IndexedColumn> columns = builder.getRegisteredColumns();
						for (IndexedColumn column : columns) {
							finalItems.add(column);
							mapping.add(reference.getReference());
						}
						// Register the builder for the current query configuration
						builderRegistry.registerBuilder(builderConfiguration.getId(), qmOption.getId(), qmOption.isDefault(), columnSelectorId, selectorOption.getId(), selectorOption.isDefault(), builderConfiguration.getQueryKeyId(), builder);
						if(!columnNames.contains(reference.getReference())) {
							errors.add("Invalid column selector option '" + columnSelector.getId() + ":" + selectorOption.getId() + "': Column '" + reference.getReference() + "' does not exist!");
						}
					}
				}
				if(defaultColumnSelectorCounter != 1) {
					errors.add("Illegal column selector '" + columnSelector.getId() + "': There must be exactly one default option, but there were " + defaultColumnSelectorCounter);
				}
			}
			if(defaultQueryOptionCounter != 1) {
				errors.add("Illegal query builder '" + builderConfiguration.getId() + "': There must be exactly one default option, but there were " + defaultQueryOptionCounter);
			}
		}
		// Got all lucene fields, now create field factories to ensure that they will
		// be created and filled when a new entry is inserted.
		logger.info("Field analysis completed, index will contain the following fields:");
		for (IndexedColumn item : finalItems) {
			logger.info("   " + item.toString());
			FieldFactory factory = new FieldFactory(item);
			List<FieldFactory> factoriesBySource = fieldFactories.get(item.getSource());
			if(factoriesBySource == null) {
				factoriesBySource = new ArrayList<FieldFactory>();
				fieldFactories.put(item.getSource(), factoriesBySource);
			}
			factoriesBySource.add(factory);
		}
		// Override the explicitly defined indexed columns in the configuration with the
		// implicit ones detected above.
		Configuration.getInstance().getDictionaryConfig().getIndexedColumns().clear();
		Configuration.getInstance().getDictionaryConfig().getIndexedColumns().addAll(finalItems);
		
		UiConfigurations uiConfigs = Configuration.getInstance().getDictionaryConfig().getUiConfigurations();
		if(uiConfigs == null) {
			errors.add("No UI fields have been defined for querying!");
		} else {
			validate(uiConfigs.getEditorAdvancedUiConfiguration(), "advanced backend", uniqueNames);
			validate(uiConfigs.getEditorDefaultUiConfiguration(), "default backend", uniqueNames);
			validate(uiConfigs.getUserAdvancedUiConfiguration(), "advanced frontend", uniqueNames);
			validate(uiConfigs.getUserDefaultUiConfiguration(), "default frontend", uniqueNames);
		}
		
		if(errors.size() > 0) {
			logger.error(errors.size() + " errors have been detected in the configuration:");
			for (String error : errors) {
				logger.error("   " + error);
			}
		} else {
			logger.info("No errors have been detected in the configuration.");
		}
		
	}
	
	private void validate(UiConfiguration config, String area, Set<String> uniqueNames) {
		if(config == null) {
			errors.add("Missing ui configuration for " + area);
		} else {
			List<UiField> fields = config.getFields();
			int submitButtons = 0;
			for (UiField field : fields) {
				if(field.hasSubmitButton()) {
					submitButtons++;
				}
				if(!field.isBuildIn()) {
					if(!uniqueNames.contains(field.getId())) {
						errors.add("Invalid ui field reference: There is no query key, column selector or query builder named '" + field.getId()+"'");
					}
				}
			}
			if(submitButtons > 1) {
				errors.add("More than one submit button defined for " + area);
			}
		}
	}

	private IndexedColumn getSortColumn(String mainColumn) {
		IndexedColumn column = new IndexedColumn();
		column.setSource(mainColumn + "_sort");
		column.setIndexFieldName(mainColumn + "_sort");
		column.setStored(true);
		column.setAnalyzed(false);
		column.setLowerCase(false);
		column.setUsesWhitespaceAnalyzer(false);
		column.setType(MaalrFieldType.INTEGER);
		logger.info("Added sort field for " + mainColumn + ": " + column);
		return column;
	}
	
	private Set<IndexedColumn> getDictionaryItem(String mainColumn) {
		ExactMatchQueryBuilder builder = new ExactMatchQueryBuilder();
		builder.setColumn(mainColumn);
		Set<IndexedColumn> columns = builder.getRegisteredColumns();
		logger.info("Added fields for exact (dictionary) queries: " + columns);
		return columns;
	}

	public Query getSuggestionsQuery(String fieldName, String value) {
		MaalrQueryBuilder builder = oracleBuilder.get(fieldName);
		if(builder == null) {
			logger.warn("No query builder found for field " + fieldName);
			return null;
		}
		List<Query> parts = builder.transform(value);
		BooleanQuery bq = new BooleanQuery(true);
		for (Query part : parts) {
			bq.add(part, Occur.SHOULD);
		}
		BooleanQuery bc = new BooleanQuery();
		bc.add(bq, Occur.MUST);
		bc.add(new TermQuery(new Term(LemmaVersion.VERIFICATION, Verification.ACCEPTED.toString())),Occur.MUST);
		bq = bc;
		return bq;
	}

	private void autoGenerateOracleFields(Set<IndexedColumn> indexedColumns) {
		logger.info("Auto-generating oracle fields...");
		LemmaDescription ld = Configuration.getInstance().getLemmaDescription();
		ArrayList<ValueSpecification> values = new ArrayList<ValueSpecification>(ld.getValues(UseCase.FIELDS_FOR_ADVANCED_EDITOR));
		values.addAll(ld.getValues(UseCase.FIELDS_FOR_SIMPLE_EDITOR));
		for (ValueSpecification valueSpecification : values) {
			if(valueSpecification.getType() == ValueType.ORACLE || valueSpecification.getType() == ValueType.ENUM) {
				DefaultQueryBuilder builder = new DefaultQueryBuilder();
				builder.setColumn(valueSpecification.getInternalName());
				oracleBuilder.put(valueSpecification.getInternalName(), builder);
				Set<IndexedColumn> entries = builder.getRegisteredColumns();
				logger.info("Added fields for oracle " + valueSpecification.getInternalName() + ": " + entries);
				indexedColumns.addAll(entries);
			}
		}
	}
	
	public Query buildQuery(MaalrQuery maalrQuery) {
		long prepareStart = System.nanoTime();
		Set<String> queryBuilderIds = builderRegistry.allQueryBuilderIds;
		TreeMap<String, String> queryMap = maalrQuery.getValues();
		BooleanQuery bq = new BooleanQuery(true);
		// Build a query by iterating over all registered query builders
		for (String queryBuilderId : queryBuilderIds) {
			Set<Query> queryParts = builderRegistry.getQuery(queryMap, queryBuilderId);
			if(queryParts == null) continue;
			BooleanQuery part = new BooleanQuery(true);
			for (Query tf : queryParts) {
				part.add(tf, Occur.SHOULD);
			}
			bq.add(part, Occur.MUST);
		}
		Query query = bq;
		// Unless a user wants to see unverified suggestions, each item returned must be verified.
		if(!maalrQuery.isSuggestions()) {
			BooleanQuery bc = new BooleanQuery();
			bc.add(query, Occur.MUST);
			bc.add(new TermQuery(new Term(LemmaVersion.VERIFICATION, Verification.ACCEPTED.toString())),Occur.MUST);
			query = bc;
		}
		long prepareEnd = System.nanoTime();
		if(logger.isDebugEnabled()) {
			logger.debug("Final query: " + query + " created in " + ((prepareEnd-prepareStart)/1000000D) + " ms.");
		}
		return query;
	}
	
	public Document getDocument(LexEntry lexEntry, LemmaVersion lemmaVersion) {
		Document doc = new Document();
		Set<Entry<String, String>> entries = lemmaVersion.getEntryValues().entrySet();
		for (Entry<String, String> entry : entries) {
			if(ignored.contains(entry.getKey())) continue;
			List<IndexableField> fields = toField(entry.getKey(), entry.getValue());
			if(fields == null) {
					logger.warn("No mapping for field " + entry.getKey() + " - field will not be indexed!");
					ignored.add(entry.getKey());
				continue;
			}
			for (IndexableField field : fields) {
				doc.add(field);
			}
		}
		addMaalrFieldsToDocument(lexEntry, lemmaVersion, doc);
		return doc;
	}
	
	private List<IndexableField> toField(String key, String value) {
		List<FieldFactory> factories = fieldFactories.get(key);
		if(factories == null || factories.isEmpty()) return null;
		List<IndexableField> fields = new ArrayList<IndexableField>();
		for (FieldFactory factory : factories) {
			List<IndexableField> field = factory.getFields(value);
			if(field != null) {
				fields.addAll(field);
			}
		}
		return fields;
	}
	
	public LemmaVersion getLemmaVersion(Document document) {
		LemmaVersion lv = new LemmaVersion();
		for (String fieldName : allColumns) {
			IndexableField[] fields = document.getFields(fieldName);
			if(fields.length == 0) continue;
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < fields.length; i++) {
				sb.append(fields[i].stringValue());
				if(i < fields.length-1) {
					sb.append(",");
				}
			}
			lv.putEntryValue(fieldName, sb.toString());
		}
		addMaalrFieldsToLemmaVersion(document, lv);
		return lv;
	}
	
	/**
	 * Helper method to add the default maalr fields of a {@link LemmaVersion} to a lucene {@link Document}.
	 * @param lexEntry
	 * @param version
	 * @param document
	 */
	private void addMaalrFieldsToDocument(LexEntry lexEntry, LemmaVersion version, Document document) {
		document.add(new StringField(LexEntry.ID, lexEntry.getId(), Field.Store.YES));
		document.add(new StringField(LemmaVersion.LEXENTRY_ID, lexEntry.getId(), Field.Store.YES));
		document.add(new StringField(LemmaVersion.ID, version.getInternalId()+"", Field.Store.YES));
		document.add(new StringField(LemmaVersion.VERIFICATION, version.getVerification().toString(), Field.Store.YES));
		if(version.getEntryValue(LemmaVersion.OVERLAY_LANG1) != null) {
			document.add(new StringField(LemmaVersion.OVERLAY_LANG1, version.getEntryValue(LemmaVersion.OVERLAY_LANG1), Field.Store.YES));
		}
		if(version.getEntryValue(LemmaVersion.OVERLAY_LANG2) != null) {
			document.add(new StringField(LemmaVersion.OVERLAY_LANG2, version.getEntryValue(LemmaVersion.OVERLAY_LANG2), Field.Store.YES));
		}
	}
	
	/**
	 * Helper method to add default maalr fields from a lucene {@link Document} to a {@link LemmaVersion}.
	 * @param document
	 * @param lemmaVersion
	 */
	private void addMaalrFieldsToLemmaVersion(Document document, LemmaVersion lemmaVersion) {
		lemmaVersion.putMaalrValue(LexEntry.ID, document.get(LexEntry.ID));
		lemmaVersion.putMaalrValue(LemmaVersion.LEXENTRY_ID, document.get(LemmaVersion.LEXENTRY_ID));
		lemmaVersion.putMaalrValue(LemmaVersion.ID, document.get(LemmaVersion.ID));
		lemmaVersion.putMaalrValue(LemmaVersion.VERIFICATION, document.get(LemmaVersion.VERIFICATION));
		lemmaVersion.putEntryValue(LemmaVersion.OVERLAY_LANG1, document.get(LemmaVersion.OVERLAY_LANG1));
		lemmaVersion.putEntryValue(LemmaVersion.OVERLAY_LANG2, document.get(LemmaVersion.OVERLAY_LANG2));
	}

	public Set<String> getFieldNames(String choiceName) {
		return choiceIdsToFields.get(choiceName);
	}


}

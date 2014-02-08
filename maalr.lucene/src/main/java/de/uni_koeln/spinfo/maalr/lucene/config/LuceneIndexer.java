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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.DictionaryConfiguration;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.FieldChoice;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.FieldChoiceOption;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.IndexedItem;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.MaalrFieldType;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryModifier;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryModifierOption;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.FieldChoiceTransformer;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.FieldFactory;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrField;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.SimpleModifierTransformer;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;

/**
 * This class is responsible for creating lucene queries based on a search configuration file.
 * It involves both field-mapping (through a {@link FieldChoiceTransformer}) and query 
 * manipulation (through a {@link MaalrQueryBuilder}). As a short example, consider the case
 * that one wants to be able to select a language direction (english->german, german->english,
 * or both) and a search type (exact match, entries starting with the search phrase, or entries containing
 * the search phrase at any position). The {@link FieldChoiceTransformer} will map the selected
 * language direction onto one or more fields (such as 'english_phrase', 'german_phrase', or both),
 * whereas the {@link MaalrQueryBuilder} will further refine the field names as well as the search
 * phrase, to generate - for instance - query parts like 'english_phrase_exact:car' or 'german_phrase_exact:car'.
 * @author sschwieb
 *
 */
public class LuceneIndexer {
	
	private static Logger logger = LoggerFactory.getLogger(LuceneIndexer.class);
	
	/**
	 * The {@link FieldFactory}-objects required by the current search configuration.
	 * For each {@link IndexedItem} defined in the configuration, one {@link FieldFactory}
	 * will be generated. Key of the map is the source attribute of the {@link IndexedItem},
	 * value is the list of all {@link FieldFactory}-objects which referred to this source.
	 */
	private Map<String, List<FieldFactory>> fieldFactories = new HashMap<String, List<FieldFactory>>();
	
	
	private Map<String, Map<String, MaalrQueryBuilder>> queryModifiers;
	
	/**
	 * The {@link FieldChoiceTransformer}-objects responsible to map a user-selected
	 * search field (for instance, "language") onto one or more lucene fields (such as
	 * "german_phrase", "english_phrase", or both).
	 */
	private HashMap<String, List<Map<String, FieldChoiceTransformer>>> fieldChoices;
	
	/**
	 * Contains all options and modifiers which are marked as default in the 
	 * configuration file (by adding the argument default="true").
	 */
	private Map<String, String> defaults = new HashMap<String, String>();
	
	
	private Set<String> destFields = new HashSet<String>();
	/**
	 * Contains all field names which are ignored when generating the index.
	 * A field is ignored if it has not been set up in the indexConfiguration-
	 * section of the search configuration. For each ignored field, a warning
	 * message will be logged.
	 */
	private Set<String> ignored = new HashSet<String>();
	
	/**
	 * 
	 * @param configuration the search configuration, as defined in searchconfig.xml
	 */
	public LuceneIndexer(DictionaryConfiguration configuration) {
		List<IndexedItem> items = configuration.getIndexConfiguration().getItems();
		for (IndexedItem item : items) {
			buildMapping(item);
		}
		queryModifiers = new HashMap<String, Map<String,MaalrQueryBuilder>>();
		List<QueryModifier> modifiers = configuration.getQueryModifier();
		for (QueryModifier modifier : modifiers) {
			buildModifier(modifier);
		}
		fieldChoices = new HashMap<String, List<Map<String, FieldChoiceTransformer>>>();
		List<FieldChoice> fieldChoices = configuration.getFieldChoices();
		for (FieldChoice choice : fieldChoices) {
			buildFieldChoice(choice);
		}
	}

	/**
	 * Helper method to generate {@link FieldChoiceTransformer}-items from a
	 * {@link FieldChoice} defined in the search configuration.
	 * @param choice
	 */
	private void buildFieldChoice(FieldChoice choice) {
		String id = choice.getId();
		List<FieldChoiceOption> options = choice.getOptions();
		Map<String, FieldChoiceTransformer> items = new HashMap<String, FieldChoiceTransformer>();
		for (FieldChoiceOption option : options) {
			FieldChoiceTransformer transformer = new FieldChoiceTransformer(choice.getValueOf(), option);
			items.put(option.getId(), transformer);
			if(option.isDefault()) {
				defaults.put(id, option.getId());
			}
		}
		List<Map<String, FieldChoiceTransformer>> list = fieldChoices.get(id);
		if(list == null) {
			list = new ArrayList<Map<String,FieldChoiceTransformer>>();
			fieldChoices.put(id, list);
		}
		list.add(items);
	}

	/**
	 * Helper method to generate {@link MaalrQueryBuilder}-items from a
	 * {@link QueryModifier} defined in the search configuration.
	 * @param modifier
	 */
	private void buildModifier(QueryModifier modifier) {
		String id = modifier.getId();
		Map<String, MaalrQueryBuilder> modifierMap = new HashMap<String, MaalrQueryBuilder>();
		List<QueryModifierOption> options = modifier.getOptions();
		for (QueryModifierOption option : options) {
			String transformerClass = option.getTransformer();
			MaalrQueryBuilder transformer = null;
			if(transformerClass == null || transformerClass.trim().length() == 0) {
				transformer = new SimpleModifierTransformer();
			} else {
				try {
					Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(transformerClass);
					transformer = (MaalrQueryBuilder) cls.newInstance();
				} catch (Exception e) {
					throw new RuntimeException("Failed to instantiate query transformer " + transformerClass, e);
				}
			}
			transformer.setReference(modifier.getReference());
			transformer.setOption(option);
			modifierMap.put(option.getId(), transformer);
			if(option.isDefault()) {
				defaults.put(id, option.getId());
			}
		}
		this.queryModifiers.put(id, modifierMap);
	}

	private void buildMapping(IndexedItem item) {
		MaalrFieldType type = item.getType();
		if(type == null) throw new RuntimeException("Field type must not be null!");
		FieldFactory factory = new FieldFactory(item);
		destFields.add(item.getDest());
		List<FieldFactory> factories = fieldFactories.get(item.getSource());
		if(factories == null) {
			factories = new ArrayList<FieldFactory>();
			fieldFactories.put(item.getSource(), factories);
		}
		factories.add(factory);
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
	
	public LemmaVersion getLemmaVersion(Document document) {
		LemmaVersion lv = new LemmaVersion();
		for (String fieldName : destFields) {
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
//			IndexableField field = document.getField(fieldName);
//			if(field != null) {
//				String value = field.stringValue();
//				if(value != null) {
//					lv.putEntryValue(fieldName, value);
//				}
//			}
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

	class TransformerList extends ArrayList<MaalrField> {

		private static final long serialVersionUID = 7398895643688899578L;
		private String reference;

		public TransformerList(List<MaalrField> tmp) {
			super(tmp);
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		public String getReference() {
			return reference;
		}
		
	}
	
	class QueryList extends ArrayList<Query> {

		private static final long serialVersionUID = 7398895643688899578L;
		private String reference;

		public void setReference(String reference) {
			this.reference = reference;
		}

		public String getReference() {
			return reference;
		}
		
	}
	
	public ArrayList<String> getFieldName(String key) {
		MaalrQuery maalrQuery = new MaalrQuery();
		maalrQuery.setQueryValue(key, "anything");
		addDefaults(maalrQuery);
		Set<Entry<String, String>> params = maalrQuery.getQueryMap().entrySet();
		for (Entry<String, String> param : params) {
			List<Map<String, FieldChoiceTransformer>> list = this.fieldChoices.get(param.getKey());
			if(list != null) {
				for (Map<String, FieldChoiceTransformer> map : list) {
//					logger.info("Transformer map found: " + param.getKey() + " " + map);
					FieldChoiceTransformer transformer = map.get(param.getValue());
					if(transformer != null) {
						List<MaalrField> tmp = transformer.transform(maalrQuery);
						if(tmp != null) {
							ArrayList<String> fieldNames = new ArrayList<String>();
							for (MaalrField maalrField : tmp) {
								fieldNames.add(maalrField.getField());
//								logger.info("Field(s) found: "+maalrField.getField());
							}
							return fieldNames;
						}
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Generate a lucene {@link Query} from a {@link MaalrQuery}.
	 * @param maalrQuery
	 * @return
	 * @throws ParseException
	 */
	public Query buildQuery(MaalrQuery maalrQuery) throws ParseException {
		long prepareStart = System.nanoTime();
		if(maalrQuery.getQueryMap().size() > 0) {
			addDefaults(maalrQuery);
		}
		// Step 1: Convert the maalrQuery into a temporary list of field references.
		Map<String, List<TransformerList>> fields = new HashMap<String, List<TransformerList>>();
		Set<Entry<String, String>> params = maalrQuery.getQueryMap().entrySet();
		for (Entry<String, String> param : params) {
			List<Map<String, FieldChoiceTransformer>> list = this.fieldChoices.get(param.getKey());
			if(list != null) {
				for (Map<String, FieldChoiceTransformer> map : list) {
//					logger.info("buildQuery: Transformer map found: " + param.getKey() + " " + map);
					FieldChoiceTransformer transformer = map.get(param.getValue());
					if(transformer != null) {
						List<TransformerList> transformerLists = fields.get(param.getKey());
						if(transformerLists == null) {
							transformerLists = new ArrayList<TransformerList>();
							fields.put(param.getKey(), transformerLists);
						}
						List<MaalrField> maalrFields = transformer.transform(maalrQuery);
						if(maalrFields != null) {
							TransformerList transformerList = new TransformerList(maalrFields);
							transformerList.setReference(transformer.getValueOf());
							transformerLists.add(transformerList );
						}
					} else {
//						logger.info("No transformer found for " + param.getKey() + " and value " + param.getValue());
					}
				}
			} else {
//				logger.info("No transformer map found for parameter " + param.getKey());
			}
		}
		// Step 2: Modify the query parts
		Map<String, List<QueryList>> queryLists = new HashMap<String, List<QueryList>>();
		Map<String, String> modifierMap = maalrQuery.getQueryMap();
		if(modifierMap != null && modifierMap.size() > 0) {
			Set<Entry<String, String>> modifiers = modifierMap.entrySet();
			for (Entry<String, String> entry : modifiers) {
				if(entry.getKey() != null && entry.getValue() != null) {
					Map<String, MaalrQueryBuilder> map = queryModifiers.get(entry.getKey());
					if(map != null) {
						MaalrQueryBuilder transformer = map.get(entry.getValue());
//						System.out.println("transformerSrc: "+transformer.getSrcFieldName());
//						System.out.println("transformerRef: "+transformer.getReference());
						if(transformer != null) {
							List<TransformerList> list = fields.get(transformer.getSrcFieldName());
							if(list != null) {
								List<QueryList> list3 = new ArrayList<QueryList>();
								queryLists.put(transformer.getSrcFieldName(), list3);
								for (TransformerList list2 : list) {
									if(list2.getReference().equals(transformer.getReference())) {
										QueryList queryList = new QueryList();
										queryList.setReference(list2.getReference());
										list3.add(queryList);
										for (MaalrField field : list2) {
//											System.out.println(transformer.transform(field));
											queryList.addAll(transformer.transform(field));
										}
									}
								}
								
							}
						}
					}
				}
			}
		}
		// Step 3: Combine all query parts to a lucene query.
		List<Entry<String, List<QueryList>>> queryParts = new ArrayList<Map.Entry<String,List<QueryList>>>(queryLists.entrySet());
		BooleanQuery bq = new BooleanQuery(true);
		for(int i = 0; i < queryParts.size(); i++) {
			Entry<String, List<QueryList>> entry = queryParts.get(i);
			List<QueryList> values = entry.getValue();
			for (int j = 0; j < values.size(); j++) {
				BooleanQuery part = new BooleanQuery(true);
				List<Query> list = values.get(j);
				for (Query tf : list) {
					part.add(tf, Occur.SHOULD);
				}
				bq.add(part, Occur.MUST);
			}
		}
		
		long prepareEnd = System.nanoTime();
		Query query = bq;
		// Step 4: Unless a user wants to see unverified suggestions, each item returned must be verified.
		if(!maalrQuery.isSuggestions()) {
			BooleanQuery bc = new BooleanQuery();
			bc.add(query, Occur.MUST);
			bc.add(new TermQuery(new Term(LemmaVersion.VERIFICATION, Verification.ACCEPTED.toString())),Occur.MUST);
			query = bc;
		}
		if(logger.isDebugEnabled()) {
			logger.debug("Final query: " + query + " created in " + ((prepareEnd-prepareStart)/1000000D) + " ms.");
		}
		return query;
	}

	private void addDefaults(MaalrQuery maalrQuery) {
		Set<Entry<String, String>> defaultValues = defaults.entrySet();
		for (Entry<String, String> entry : defaultValues) {
			if(!maalrQuery.getQueryMap().containsKey(entry.getKey())) {
				maalrQuery.getQueryMap().put(entry.getKey(), entry.getValue());
			}
		}
	}

}

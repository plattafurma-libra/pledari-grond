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
package de.uni_koeln.spinfo.maalr.lucene.config.interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.ColumnSelectorOption;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.IndexedColumn;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.MaalrFieldType;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryBuilderOption;
import de.uni_koeln.spinfo.maalr.lucene.config.LuceneIndexManager;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneHelper;

/**
 * This is the base class of all query builders, which are responsible
 * to query the maalr dictionary. Query builders are used in two different
 * phases: During startup, they are instantiated and configured for each
 * selectable combination of {@link QueryBuilderOption} and {@link ColumnSelectorOption}
 * defined in maalr's configuration. Each query builder defines a set of
 * lucene fields required to perform queries, so during startup these
 * fields are used to set up the lucene index. When performing queries,
 * the {@link LuceneIndexManager} will use the information provided in 
 * a {@link MaalrQuery} to find the relevant {@link MaalrQueryBuilder},
 * such that they can be used to build the lucene query string.
 * 
 * @author sschwieb
 *
 */
public abstract class MaalrQueryBuilder {
	
	protected final Analyzer analyzer = LuceneHelper.newAnalyzer();
	protected String column;
	private final Map<String, String> finalFieldNames = new HashMap<String, String>();
	private final Set<IndexedColumn> columns = new HashSet<IndexedColumn>();
	
	/**
	 * Initialize the builder for the given column.
	 * @param column
	 */
	public void setColumn(String column) {
		this.column = column;
		buildColumnToFieldsMapping();
	}
	
	/**
	 * Subclasses must override this method and register the index fields they
	 * require to perform searches, by calling {@link MaalrQueryBuilder#registerFieldMapping(String, boolean, MaalrFieldType, boolean, boolean)}
	 * for each individual field.
	 */
	protected abstract void buildColumnToFieldsMapping();

	public abstract List<Query> transform(String field);
	
	/**
	 * Register a variation of the index field for the column the query builder
	 * was created for.
	 * @param name A symbolic name to lookup the field during querying
	 * @param analyzed lucene-specific: Whether or not the term should be analyzed
	 * @param type The type of the field
	 * @param lowercase Whether or not 
	 * @param whitespace if true, a {@link WhitespaceAnalyzer} will be used for this field. Otherwise, a {@link StandardAnalyzer} will be used.
	 * 
	 * @see LuceneHelper
	 */
	protected void registerFieldMapping(String name, boolean analyzed, MaalrFieldType type, boolean lowercase, boolean whitespace) {
		if(whitespace && !analyzed) {
			LoggerFactory.getLogger(MaalrQueryBuilder.class).warn("Invalid query builder configuration: It doesn't make sense to enable whitespace analyzer when analyzed-flag is disabled!");
		}
		IndexedColumn item = new IndexedColumn();
		item.setAnalyzed(analyzed);
		item.setLowerCase(lowercase);
		item.setStored(false);
		item.setType(type);
		item.setUsesWhitespaceAnalyzer(whitespace);
		item.setSource(column);
		String destField = column + getFieldSuffix(analyzed, whitespace, lowercase, type);
		finalFieldNames.put(name, destField);
		item.setIndexFieldName(destField);
		columns.add(item);
	}
	
	private String getFieldSuffix(boolean analyzed, boolean lowercase,
			boolean whitespace, MaalrFieldType type) {
		StringBuilder fieldSuffixBuilder = new StringBuilder();
		fieldSuffixBuilder.append("_");
		fieldSuffixBuilder.append(analyzed ? "a" : "na");
		fieldSuffixBuilder.append("_");
		fieldSuffixBuilder.append(whitespace ? "w" : "nw");
		fieldSuffixBuilder.append("_");
		fieldSuffixBuilder.append(lowercase ? "l" : "nl");
		fieldSuffixBuilder.append("_");
		fieldSuffixBuilder.append("t-" + type);
		return fieldSuffixBuilder.toString();
	}

	/**
	 * Returns all {@link IndexedColumn}s required by the 
	 * query builder.
	 * @return
	 */
	public Set<IndexedColumn> getRegisteredColumns() {
		return columns;
	}

	/**
	 * Returns the field name with the given symbolic name
	 * @param registeredName
	 * @return
	 */
	protected String getFieldName(String registeredName) {
		return finalFieldNames.get(registeredName);
	}

}

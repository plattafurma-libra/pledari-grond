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
package de.uni_koeln.spinfo.maalr.lucene.core;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.sandbox.queries.DuplicateFilter;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.IndexedColumn;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.MaalrFieldType;
import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.lucene.config.LuceneIndexManager;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.MaalrQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.ExactMatchQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.config.interpreter.modifier.SimplePrefixQueryBuilder;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneConfiguration;

/**
 * This class is responsible for managing the lucene index used by maalr,
 * and provides all required methods to perform CRUD-like operations on it.
 * Internally, two indices are managed: All read- and query-requests are
 * executed on an in-memory-index, whereas write-requests are executed on
 * a {@link NIOFSDirectory}. Index-Changes are executed in-order with the 
 * help of a {@link IndexCommandQueue}.
 * 
 * @author sschwieb
 * @author matana
 *
 */
public class Dictionary {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final NumberFormat formatter;

	private DictionaryLoader indexProvider;

	private DictionaryCreator indexCreator;

	private LuceneConfiguration environment;
	
	private LemmaDescription description;
	
	private LuceneIndexManager indexManager;

	private HashMap<String, Type> sortTypes;

	private SimplePrefixQueryBuilder langBIndexBuilder;

	private SimplePrefixQueryBuilder langAIndexBuilder;

	private ExactMatchQueryBuilder exactMatchesLangA;

	private ExactMatchQueryBuilder exactMatchesLangB;
	
	public LuceneConfiguration getEnvironment() {
		return environment;
	}

	public void setEnvironment(LuceneConfiguration environment)
			throws IOException {
		this.environment = environment;
		indexProvider = new DictionaryLoader();
		indexProvider.setEnvironment(environment);
		indexCreator = new DictionaryCreator();
		indexCreator.setEnvironment(environment);
		indexCreator.initialize();
		indexCreator.resetIndexDirectory();
		indexManager = LuceneIndexManager.getInstance();
		List<IndexedColumn> columns = Configuration.getInstance().getDictionaryConfig().getIndexedColumns();
		sortTypes = new HashMap<String, Type>();
		for (IndexedColumn item : columns) {
			sortTypes.put(item.getIndexFieldName(), getType(item.getType()));
		}
		// Create query builder for static dictionary pages
		langAIndexBuilder = new SimplePrefixQueryBuilder();
		langAIndexBuilder.setColumn(Configuration.getInstance().getLemmaDescription().getFirstLanguage().getMainColumn());
		langBIndexBuilder = new SimplePrefixQueryBuilder();
		langBIndexBuilder.setColumn(Configuration.getInstance().getLemmaDescription().getFirstLanguage().getMainColumn());
		exactMatchesLangA = new ExactMatchQueryBuilder();
		exactMatchesLangA.setColumn(Configuration.getInstance().getLemmaDescription().getFirstLanguage().getMainColumn());
		exactMatchesLangB = new ExactMatchQueryBuilder();
		exactMatchesLangB.setColumn(Configuration.getInstance().getLemmaDescription().getSecondLanguage().getMainColumn());
	}

	private Type getType(MaalrFieldType type) {
		switch(type) {
		case INTEGER: return Type.INT;
		default: return Type.STRING;
		}
	}

	public Dictionary(LuceneConfiguration configuration) throws IOException {
		this();
		setEnvironment(configuration);
	}

	public Dictionary() {
		formatter = (NumberFormat) NumberFormat.getNumberInstance().clone();
		formatter.setMaximumFractionDigits(3);
		description = Configuration.getInstance().getLemmaDescription();
		logger.info("Created new index.");
	}
	
	public QueryResult query(MaalrQuery maalrQuery) throws InvalidQueryException,
			NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		long start = System.nanoTime();
		validateQuery(maalrQuery);
		int pageSize = maalrQuery.getPageSize();
		long s1 = System.nanoTime();
		Query query = indexManager.buildQuery(maalrQuery);
		TopDocs docs = null;
		// TODO: Make this configurable!
		Sort sort = new Sort();
		String[] items = null;
		if(maalrQuery.getValue("language") != null && maalrQuery.getValue("language").equals(description.getLanguageName(false))) {
			items = description.getSortList(false);
		} else {
			items = description.getSortList(true);
		}
		SortField[] fields = new SortField[items.length+1];
		fields[0] = SortField.FIELD_SCORE;
		for(int i = 0; i < items.length; i++) {
			String item = items[i];
			fields[i+1] = new SortField(item, sortTypes.get(item));
		}
		sort.setSort(fields);
		QueryResult result = null;
		int pageNr = maalrQuery.getPageNr();
		long e1 = System.nanoTime();
		try {
			long s2 = System.nanoTime();
			docs = indexProvider.getSearcher().search(query,
					pageSize * (pageNr + 1), sort);
			long e2 = System.nanoTime();
			result = toQueryResult(docs, pageSize * pageNr, maalrQuery.getPageSize());
			if(logger.isDebugEnabled()) {
				logger.debug("Time to build query: " + (e1-s1)/1000000 + ", Time to execute query: " + ((e2-s2)/1000000));
			}
		} catch (IOException e) {
			throw new BrokenIndexException("Failed to access index", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1000000D;
		// Warn if query takes more than 100 ms.
		if (time > 100) {
			logger.warn("Slow query: " + formatter.format(time) + " ms for "
					+ maalrQuery);
		} else if (logger.isDebugEnabled()) {
			logger.debug("Processed query in " + formatter.format(time)
					+ " ms :" + maalrQuery);
		}
		return result;
	}

	private void validateQuery(MaalrQuery maalrQuery) {
		if (maalrQuery.getPageNr() < 0) {
			maalrQuery.setPageNr(0);
		}
		if (maalrQuery.getPageSize() > 200) {
			maalrQuery.setPageSize(200);
		}
		if (maalrQuery.getPageSize() < 1) {
			maalrQuery.setPageSize(1);
		}
	}
	
	private QueryResult toQueryResult(TopDocs docs, int startIndex, int pageSize)
			throws NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		final ArrayList<LemmaVersion> results = new ArrayList<LemmaVersion>(pageSize);
		final ScoreDoc[] scoreDocs = docs.scoreDocs;
		IndexSearcher searcher = indexProvider.getSearcher();
		for (int i = startIndex; i < scoreDocs.length
				&& i < startIndex + pageSize; i++) {
			Document doc = searcher.doc(scoreDocs[i].doc);
			LemmaVersion e = indexManager.getLemmaVersion(doc);
			results.add(e);
		}
		return new QueryResult(results, docs.totalHits, pageSize);
	}

	
	public QueryResult queryExact(String phrase, boolean firstLanguage) throws NoIndexAvailableException, BrokenIndexException, InvalidQueryException {
		String sortField = null;
		List<Query> queries = null;
		sortField = description.getSortOrder(firstLanguage);
		if(firstLanguage) {
			queries = exactMatchesLangA.transform(phrase);
		} else {
			queries = exactMatchesLangB.transform(phrase);
		}
		int pageSize = 120;
		try {
			BooleanQuery query = new BooleanQuery(true);
			for (Query q : queries) {
				query.add(q, Occur.SHOULD);
			}
			BooleanQuery bc = new BooleanQuery();
			bc.add(query, Occur.MUST);
			bc.add(new TermQuery(new Term(LemmaVersion.VERIFICATION, Verification.ACCEPTED.toString())),Occur.MUST);
			query = bc;
			TopDocs docs = indexProvider.getSearcher().search(query,
					null, pageSize, new Sort(new SortField(sortField, SortField.Type.LONG)));

			return toQueryResult(docs, 0, pageSize);
		} catch (IOException e) {
			throw new BrokenIndexException("Broken index!", e);
		} catch (InvalidTokenOffsetsException e) {
			throw new InvalidQueryException("Highlighting failed", e);
		}
	}

	public QueryResult getAllStartingWith(String language, String prefix,
			int page) throws NoIndexAvailableException, BrokenIndexException,
			InvalidQueryException {
		String field = null;
		String sortField = null;
		List<Query> queries = null;
		boolean firstLanguage = language.equals(description.getLanguageName(true));
		field = description.getDictField(firstLanguage);
		if(firstLanguage) {
			queries = langAIndexBuilder.transform(prefix);
			sortField = langAIndexBuilder.getIndexSortField();
		} else {
			queries = langBIndexBuilder.transform(prefix);
			sortField = langBIndexBuilder.getIndexSortField();
		}
		int pageSize = 120;
		try {
			BooleanQuery query = new BooleanQuery(true);
			for (Query q : queries) {
				query.add(q, Occur.SHOULD);
			}
			BooleanQuery bc = new BooleanQuery();
			bc.add(query, Occur.MUST);
			bc.add(new TermQuery(new Term(LemmaVersion.VERIFICATION, Verification.ACCEPTED.toString())),Occur.MUST);
			query = bc;
			TopDocs docs = indexProvider.getSearcher().search(query,
					new DuplicateFilter(field), Integer.MAX_VALUE,
					new Sort(new SortField(sortField, SortField.Type.STRING)));
			return toQueryResult(docs, page * pageSize, pageSize);
		} catch (IOException e) {
			throw new BrokenIndexException("Broken index!", e);
		} catch (InvalidTokenOffsetsException e) {
			throw new InvalidQueryException("Highlighting failed", e);
		}
	}

	public IndexStatistics getIndexStatistics() {
		final IndexStatistics statistics = new IndexStatistics();
		try {
			queue.push(new IndexOperation() {
				
				@Override
				public void execute() throws Exception {
					int all = indexProvider.getSearcher().getIndexReader().numDocs();
					int unverified = 0;
					int approved = 0;
					int unknown = 0;
					IndexReader reader = indexProvider.getSearcher().getIndexReader();
					HashMap<String, Integer> byCategory = new HashMap<String, Integer>();
					for (int i = 0; i < all; i++) {
						Document document = reader.document(i);
						String verification = document.get(LemmaVersion.VERIFICATION);
						try {
							if (Verification.ACCEPTED.equals(Verification.valueOf(verification))) {
								approved++;
							} else if (Verification.UNVERIFIED.equals(Verification.valueOf(verification))) {
								unverified++;
							} else {
								unknown++;
							}
						} catch (Exception e) {
							unknown++;
						}
						String overlayA = document.get(LemmaVersion.OVERLAY_LANG1);
						if(overlayA != null) {
							Integer old = byCategory.get(overlayA);
							if(old == null) old = 0;
							byCategory.put(overlayA, old+1);
						}
						String overlayB = document.get(LemmaVersion.OVERLAY_LANG2);
						if(overlayB != null) {
							Integer old = byCategory.get(overlayB);
							if(old == null) old = 0;
							byCategory.put(overlayB, old+1);
						}
						
					}
					statistics.setOverlayCount(byCategory);
					statistics.setNumberOfEntries(all);
					statistics.setUnverifiedEntries(unverified);
					statistics.setApprovedEntries(approved);
					statistics.setUnknown(unknown);
					statistics.setLastUpdated(indexCreator.getLastUpdated());
				}
			});
			return statistics;
		} catch (Exception e) {
			return new IndexStatistics();
		}
			
	}
	
	public ArrayList<String> getSuggestionsForField(String fieldName,
			String value, int limit) throws QueryNodeException,
			NoIndexAvailableException, IOException, ParseException {
		Query query = indexManager.getSuggestionsQuery(fieldName, value);
		if(query == null) {
			return new ArrayList<String>();
		}
		ArrayList<String> results = new ArrayList<String>();
		Set<String> allValues = new TreeSet<String>();
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(fieldName);
		for (String field : fields) {
			TopDocs docs = indexProvider.getSearcher().search(query, new DuplicateFilter(field), Integer.MAX_VALUE);
			ScoreDoc[] scoreDocs = docs.scoreDocs;
			for (int i = 0; i < scoreDocs.length; i++) {
				Document doc = indexProvider.getSearcher().doc(scoreDocs[i].doc);
				IndexableField[] indexableFields = doc.getFields(field);
				// FIXME: Don't split always - instead, implement MaalrFieldType.CSV!
				for (IndexableField indexedField : indexableFields) {
					String[] parts = indexedField.stringValue().split(", ");//TODO: FieldType.CSV has no effect
					for (String part : parts) {
						if(part.toLowerCase().startsWith(value.toLowerCase())) {
							allValues.add(part);
						}
					}
				}
			}
		}
		results.addAll(allValues);
		if(results.size() > 0) {
			List<String> resultList = results.subList(0, Math.min(results.size(), limit));//restrict length to 'limit'
			return new ArrayList<String>(resultList);
		} else {
			return results;
		}
	}
	
	public ArrayList<String> getSuggestionsForFieldChoice(String fieldName,
			String value, int limit) throws QueryNodeException,
			NoIndexAvailableException, IOException, ParseException {
		MaalrQuery maalrQuery = new MaalrQuery();
		maalrQuery.setQueryValue(fieldName, value);
		Query query = indexManager.buildQuery(maalrQuery);
		if(query == null) {
			return new ArrayList<String>();
		}
		ArrayList<String> results = new ArrayList<String>();
		Set<String> allValues = new TreeSet<String>();
		Set<String> fields = indexManager.getFieldNames(fieldName);
		for (String field : fields) {
			TopDocs docs = indexProvider.getSearcher().search(query, new DuplicateFilter(field), Integer.MAX_VALUE);
			ScoreDoc[] scoreDocs = docs.scoreDocs;
			for (int i = 0; i < scoreDocs.length; i++) {
				Document doc = indexProvider.getSearcher().doc(scoreDocs[i].doc);
				IndexableField[] indexableFields = doc.getFields(field);
				// FIXME: Don't split always - instead, implement MaalrFieldType.CSV!
				for (IndexableField indexedField : indexableFields) {
					String[] parts = indexedField.stringValue().split(", ");//TODO: FieldType.CSV has no effect
					for (String part : parts) {
						if(part.toLowerCase().startsWith(value.toLowerCase())) {
							allValues.add(part);
						}
					}
				}
			}
		}
		results.addAll(allValues);
		if(results.size() > 0) {
			List<String> resultList = results.subList(0, Math.min(results.size(), limit));//restrict length to 'limit'
			return new ArrayList<String>(resultList);
		} else {
			return results;
		}
	}

	private IndexCommandQueue queue = IndexCommandQueue.getInstance();

	public void reloadIndex() throws NoIndexAvailableException {
		try {
			queue.push(new IndexOperation() {
				
				@Override
				public void execute() throws NoIndexAvailableException {
					logger.info("Reloading index...");
					indexProvider.reloadIndex();
					logger.info("Index reloaded");
				}
			});
		} catch (Exception e) {
			throw new NoIndexAvailableException(e);
		}
	}

	public void addToIndex(final Iterator<LexEntry> iterator)
			throws NoDatabaseAvailableException, IndexException {
		try {
			queue.push(new IndexOperation() {
				
				@Override
				public void execute() throws Exception {
					int added = indexCreator.addToIndex(iterator);
				}
			});
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}

	public void dropIndex() throws IndexException {
		try {
			queue.push(new IndexOperation() {
				
				@Override
				public void execute() throws Exception {
					indexCreator.dropIndex();
				}
			});
		} catch (Exception e) {
			throw new IndexException(e);
		}
	}
	
	public void update(final LexEntry entry) throws IOException {
		try {
			queue.push(new IndexOperation() {
				
				@Override
				public void execute() throws Exception {
					long start = System.currentTimeMillis();
					indexCreator.update(entry);
					indexProvider.update(entry);
					long end = System.currentTimeMillis();
					logger.info("Index update for entry " + entry.getId()
							+ " completed after " + (end - start) + " ms.");
				}
			});
		} catch (Exception e) {
			throw new IOException(e);
		}
		
	}

	public void delete(final LexEntry entry) throws IOException {
		try {
			queue.push(new IndexOperation() {
				
				@Override
				public void execute() throws Exception {
					indexCreator.delete(entry);
					indexProvider.delete(entry);
				}
			});
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}

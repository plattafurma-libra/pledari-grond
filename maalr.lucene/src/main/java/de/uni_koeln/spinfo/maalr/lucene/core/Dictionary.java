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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.sandbox.queries.DuplicateFilter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.IndexedItem;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.MaalrFieldType;
import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.lucene.config.LuceneIndexer;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneConfiguration;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneHelper;

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

	private Analyzer analyzer = LuceneHelper.newAnalyzer();

	private Logger logger = LoggerFactory.getLogger(getClass());

	private final NumberFormat formatter;

	private DictionaryLoader indexProvider;

	private DictionaryCreator indexCreator;

	private LuceneConfiguration environment;
	
	private LemmaDescription description;
	
	private LuceneIndexer indexHelper;

	private HashMap<String, Type> sortTypes;
	
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
		indexHelper = new LuceneIndexer(Configuration.getInstance().getDictionaryConfig());
		List<IndexedItem> indexItems = Configuration.getInstance().getDictionaryConfig().getIndexConfiguration().getItems();
		sortTypes = new HashMap<String, Type>();
		for (IndexedItem item : indexItems) {
			sortTypes.put(item.getDest(), getType(item.getType()));
		}
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
//		logger.info("Received query: " + maalrQuery);
		long start = System.nanoTime();
		validateQuery(maalrQuery);
		int pageSize = maalrQuery.getPageSize();
		long s1 = System.nanoTime();
		Query query;
		try {
			query = indexHelper.buildQuery(maalrQuery);
		} catch (ParseException e4) {
			QueryResult dummy = new QueryResult();
			dummy.setEntries(new ArrayList<LemmaVersion>());
			return dummy;
		}
		TopDocs docs = null;
		// TODO: Make this configurable!
		Sort sort = new Sort();
		if(maalrQuery.getValue("language") != null && maalrQuery.getValue("language").equals(description.getLanguageName(false))) {
			ArrayList<String> items = description.getSortListLangB();
			SortField[] fields = new SortField[items.size()+1];
			fields[0] = SortField.FIELD_SCORE;
			for(int i = 0; i < items.size(); i++) {
				String item = items.get(i);
				fields[i+1] = new SortField(item, sortTypes.get(item));
			}
			sort.setSort(fields);	
			
		} else {
			ArrayList<String> items = description.getSortListLangA();
			SortField[] fields = new SortField[items.size()+1];
			fields[0] = SortField.FIELD_SCORE;
			for(int i = 0; i < items.size(); i++) {
				String item = items.get(i);
				fields[i+1] = new SortField(item, sortTypes.get(item));
			}
			sort.setSort(fields);
		}
		QueryResult result = null;
		int pageNr = maalrQuery.getPageNr();
		long e1 = System.nanoTime();
		long qTime = 0;
		long rTime = 0;
		try {
			long s2 = System.nanoTime();
			docs = indexProvider.getSearcher().search(query,
					pageSize * (pageNr + 1), sort);
			long e2 = System.nanoTime();
			qTime = (e2-s2);
			long s3 = System.nanoTime();
			result = toQueryResult(docs, pageSize * pageNr, maalrQuery);
			long e3 = System.nanoTime();
			rTime = (e3-s3);
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
			logger.debug("Prepare: " + (e1-s1)/1000000 + ", Query: " + (qTime/1000000) + ", Result: " + (rTime/1000000));
		}
		return result;
	}

	private void validateQuery(MaalrQuery maalrQuery) {
		// FIXME: This method should ensure that a query
		// is valid, and does not contain any "dangerous"
		// properties...
		if (maalrQuery.getPageNr() < 0) {
			maalrQuery.setPageNr(0);
		}
		if (maalrQuery.getPageSize() > 200) {
			maalrQuery.setPageSize(200);
		}
		if (maalrQuery.getPageSize() < 1) {
			maalrQuery.setPageSize(1);
		}
		// maalrQuery.setLanguage(QueryParser.escape(maalrQuery.getLanguage()));
//		maalrQuery.setMethod(QueryParser.escape(maalrQuery.getMethod()));
//		maalrQuery.setSearchPhrase(QueryParser.escape(maalrQuery.getSearchPhrase()));
	}
	
	private QueryResult toQueryResult(TopDocs docs, int startIndex,
			MaalrQuery query)
			throws NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		int length = query.getPageSize();
		final List<LemmaVersion> toReturn = new ArrayList<LemmaVersion>(length);
		final ScoreDoc[] scoreDocs = docs.scoreDocs;
		IndexSearcher searcher = indexProvider.getSearcher();
		
		for (int i = startIndex; i < scoreDocs.length
				&& i < startIndex + length; i++) {
			Document doc = searcher.doc(scoreDocs[i].doc);
			LemmaVersion e = indexHelper.getLemmaVersion(doc);
//			e.putMaalrValue(LemmaVersion.LEXENTRY_ID,
//					doc.get(LemmaVersion.LEXENTRY_ID));
//			e.putMaalrValue(LemmaVersion.ID, doc.get(LemmaVersion.ID));
//			e.putEntryValue(LemmaVersion.OVERLAY, doc.get(LemmaVersion.OVERLAY));
			toReturn.add(e);
		}
		return new QueryResult(toReturn, docs.totalHits, length);
	}

	public QueryResult getAllStartingWith(String language, String prefix,
			int page) throws NoIndexAvailableException, BrokenIndexException,
			InvalidQueryException {
		String field = null;
		String sortField = null;
		if(language.equals(description.getLanguageName(true))) {
			field = description.getDictFieldLangA();
			sortField = description.getSortListLangA().get(0);
		} else {
			field = description.getDictFieldLangB();
			sortField = description.getSortListLangB().get(0);
		}
		StandardQueryParser parser = new StandardQueryParser(analyzer);
		int pageSize = 120;
		try {
			String string = field + ":" + prefix + "*";
			Query query = parser.parse(string, field);
			TopDocs docs = indexProvider.getSearcher().search(query,
					new DuplicateFilter(field), Integer.MAX_VALUE,
					new Sort(new SortField(sortField, SortField.Type.STRING)));

			MaalrQuery maalrQuery = new MaalrQuery();
			maalrQuery.setQueryValue(field, prefix);
			maalrQuery.setPageSize(pageSize);
			maalrQuery.setQueryValue("language", language);
			logger.info("Query dictionary: " + maalrQuery);
			return toQueryResult(docs, page * pageSize, maalrQuery);
		} catch (QueryNodeException e) {
			throw new InvalidQueryException("Invalid query", e);
		} catch (IOException e) {
			throw new BrokenIndexException("Broken index!", e);
		} catch (InvalidTokenOffsetsException e) {
			throw new InvalidQueryException("Highlighting failed", e);
		}
	}

	public IndexStatistics getIndexStatistics() {
		final IndexStatistics statistics = new IndexStatistics();
		try {
			logger.info("Query for statistics...");
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
			logger.info("Received statistics: " + statistics);
			return statistics;
		} catch (Exception e) {
			return new IndexStatistics();
		}
			
	}

	public ArrayList<String> getSuggestionsForField(String fieldName,
			String value, int limit) throws QueryNodeException,
			NoIndexAvailableException, IOException, ParseException {
		MaalrQuery mq = new MaalrQuery();
		mq.setQueryValue(fieldName, value);
		mq.setPageSize(limit);
		Query query = indexHelper.buildQuery(mq);
		ArrayList<String> results = new ArrayList<String>();
		Set<String> allValues = new TreeSet<String>();
		ArrayList<String> fields = indexHelper.getFieldName(fieldName);
		for (String field : fields) {
			logger.info("Suggest-Query: " + query + ", field-name: " + fieldName + " for " + field);
			TopDocs docs = indexProvider.getSearcher().search(query, new DuplicateFilter(field), Integer.MAX_VALUE);
//			logger.info("Suggest results: " + docs.totalHits);
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
		logger.info(allValues.size()+" suggest results: "+ Arrays.toString(allValues.toArray()));
		}
		results.addAll(allValues);
		// if needed, oracles can be set to max length 'limit':
//		List<String> resultList = results.subList(0, Math.min(results.size(), limit));//restrict length to 'limit'
//		return new ArrayList<String>(resultList);
		return results;
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

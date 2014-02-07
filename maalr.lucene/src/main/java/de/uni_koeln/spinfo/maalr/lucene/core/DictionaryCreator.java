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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.DictionaryConfiguration;
import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.config.LuceneIndexer;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneConfiguration;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneHelper;

/**
 * Helper class used by {@link Dictionary}. It manages the lucene index
 * held in a {@link NIOFSDirectory}.
 *
 */
class DictionaryCreator {

	private LuceneConfiguration environment;

	private static final Logger logger = LoggerFactory
			.getLogger(DictionaryCreator.class);
	private NIOFSDirectory indexDirectory;
	private Analyzer analyzer;
	private final boolean tracing = logger.isTraceEnabled();
	private LuceneIndexer luceneIndexer;
	
	LuceneConfiguration getEnvironment() {
		return environment;
	}

	void setEnvironment(LuceneConfiguration environment) {
		this.environment = environment;
	}

	void initialize() throws IOException {
		resetIndexDirectory();
		logger.info("Init Indexer, using whiteSpaceAnalyzer");
//		analyzer = LuceneHelper.newAnalyzer();
		analyzer = LuceneHelper.newWhitespaceAnalyzer();
		DictionaryConfiguration configuration = Configuration.getInstance().getDictionaryConfig();
		luceneIndexer = new LuceneIndexer(configuration);
	}
	
	int addToIndex(final Iterator<LexEntry> iterator) throws NoDatabaseAvailableException, IndexException {
		logger.info("Indexing...");
		int counter = 0;
		try {
			Date begin = new Date();
			IndexWriter writer = initIndexWriter();
			counter = indexDocs(writer, iterator);
			writer.close();
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(environment.getLuceneTimestampFile()),
					"UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write("Created on " + new Date());
			bw.close();
			logger.info("Indexing prepared: "
					+ (new Date().getTime() - begin.getTime())
					+ " total milliseconds");
			return counter;
		} catch (UnsupportedEncodingException e) {
			throw new IndexException(e);
		} catch (FileNotFoundException e) {
			throw new IndexException(e);
		} catch (IOException e) {
			throw new IndexException(e);
		}
	}
	
	void resetIndexDirectory() throws IOException {
		if(indexDirectory != null) {
			indexDirectory.close();
		}
		indexDirectory = new NIOFSDirectory(environment.getLuceneIndexDir());
	}

	private IndexWriter initIndexWriter() throws IOException {
		logger.info("Init IndexWriter");
		IndexWriterConfig writerConfig = new IndexWriterConfig(
				LuceneHelper.CURRENT, analyzer);
		if (!indexAvailable()) {
			writerConfig.setOpenMode(OpenMode.CREATE);
		} else {
			writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		}
		writerConfig.setRAMBufferSizeMB(512.0);
		IndexWriter writer = new IndexWriter(indexDirectory, writerConfig);
		return writer;
	}

	private boolean indexAvailable() {
		if (!environment.getLuceneTimestampFile().exists()) {
			logger.warn("No timestamp file available, preparing new index...");
			deleteIndexDirectory();
			return false;
		}
		return true;
	}

	private void deleteIndexDirectory() {
		File[] files = environment.getLuceneIndexDir().listFiles();
		if (files != null) {
			logger.warn("Cleanup - Deleting " + files.length
					+ " possibly broken index files in "
					+ environment.getLuceneIndexDir().getAbsolutePath());
			for (File file : files) {
				boolean deleted = file.delete();
				if (!deleted) {
					logger.warn("Failed to delete during cleanup: "
							+ file.getAbsolutePath());
				}
			}
		}
	}

	private int indexDocs(final IndexWriter writer,
			final Iterator<LexEntry> iterator) throws IOException {
		int counter = 0;
		NumberFormat nf = NumberFormat.getNumberInstance();
		while(iterator.hasNext()) {
			LexEntry lexEntry = iterator.next();
			List<Document> docs = createDocument(lexEntry);
			if(tracing) {
				logger.trace("Indexing Documents: " + docs);
			}
			for (Document doc : docs) {
				writer.addDocument(doc);
			}
			counter++;
			if(counter % 10000 == 0) {
				logger.debug("Indexed " + nf.format(counter) + " documents.");
			}
		}
		logger.info("###########################################");
		logger.info("Indexing completed - " + nf.format(counter) + " entries have been indexed.");
		logger.info("###########################################");
		return counter;
	}

	private List<Document> createDocument(LexEntry lexEntry) {
		List<Document> docs = new ArrayList<Document>();
		Set<LemmaVersion> versions = new HashSet<LemmaVersion>();
		if(lexEntry.getCurrent() != null) {
			versions.add(lexEntry.getCurrent());
		}
		versions.addAll(lexEntry.getUnapprovedVersions());
		for (LemmaVersion version : versions) {
			Document doc = luceneIndexer.getDocument(lexEntry, version);
			docs.add(doc);
		}
		return docs;
	}

	Analyzer getAnalyzer() {
		return analyzer;
	}

	void dropIndex() throws IndexException {
		IndexWriter writer = null;
		try {
			writer = initIndexWriter();
			writer.deleteAll();
		} catch (IOException e) {
			throw new IndexException(e);
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	void update(LexEntry entry) throws IOException {
		IndexWriter writer = initIndexWriter();
		Term queryTerm = new Term(LexEntry.ID, entry.getId());
		writer.deleteDocuments(queryTerm);
		if(entry.getCurrent() != null) {
			List<Document> docs = createDocument(entry);
			for (Document document : docs) {
				writer.addDocument(document);
			}
		}
		writer.close();
	}

	void delete(LexEntry entry) throws IOException {
		IndexWriter writer = initIndexWriter();
		Term queryTerm = new Term(LexEntry.ID, entry.getId());
		writer.deleteDocuments(queryTerm);
		writer.commit();
		writer.close();
	}

	public long getLastUpdated() {
		File dir = indexDirectory.getDirectory();
		long lastModified = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if(file.lastModified() > lastModified) {
				lastModified = file.lastModified();
			}
		}
		return lastModified;
	}

}

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
package de.uni_koeln.spinfo.maalr.conjugator.searcher;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

public class SearcherTest {

	private static Searcher searcher;
	// Verzeichnis des Lucene-Indexes
	private static String indexDir;
	private static String q;

	@BeforeClass
	public static void initialize() {
		searcher = new Searcher();
		indexDir = Indexer.indexPath;
		q = "abandunass";
	}

	
	@Test
	public void testMongoSearch() throws UnknownHostException {
		searcher.mongoSearch(q);
	}


	@Test
	public void testLuceneSearch() throws IOException, ParseException {
		searcher.luceneSearch(indexDir, q);
	}

}

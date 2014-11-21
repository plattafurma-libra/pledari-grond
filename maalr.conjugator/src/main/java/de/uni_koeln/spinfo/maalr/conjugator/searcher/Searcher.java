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

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Searcher {

	public void mongoSearch(String q) throws UnknownHostException {

		MongoClient mongoClient = new MongoClient("localhost", 27017);

		DB db = mongoClient.getDB("verbs_rg");

		DBCollection collection = db.getCollection("verbs_fm");

		BasicDBObject query = new BasicDBObject("fullform", q);

		long start = System.currentTimeMillis();
		DBCursor cursor = collection.find(query);
		long end = System.currentTimeMillis();
		System.err.println("Took " + (end - start) + " ms to find in MONGODB "
				+ cursor.count() + " document(s) that matched query '" + q
				+ "':");

		try {
			while (cursor.hasNext()) {

				DBObject obj = cursor.next();
				// System.out.println(obj.get("_id"));
				System.out.println("Fullform: " + obj.get("fullform"));
				System.out.println("Infinitiv: " + obj.get("infinitiv"));
				System.out.println("Root: " + obj.get("root"));
				System.out.println("Tempus: " + obj.get("tempus"));
				System.out.println("Pronoun: " + obj.get("pronoun"));
				System.out.println("Person: " + obj.get("person"));
				System.out.println("Numerus: " + obj.get("numerus"));
				System.out.println("Genus: " + obj.get("genus"));

				System.out.println("+++++++++++++++++++++++++++++++++++");
			}
		} finally {
			cursor.close();
		}

	}

	public void luceneSearch(String indexDir, String q) throws IOException,
			ParseException {

		Directory dir = new SimpleFSDirectory(new File(indexDir));

		// Öffnet den Indexer (read-only-mode)
		DirectoryReader dirReader = DirectoryReader.open(dir);

		// Erzeuge einen IndexSearcher
		IndexSearcher is = new IndexSearcher(dirReader);

		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

		// Überführt den vom User eigegebenen "Suchtext" in eine Lucene Query
		QueryParser parser = new QueryParser(Version.LUCENE_46, "fullform",
				analyzer);
		Query query = parser.parse(q);
		System.out.println("QUERY: " + query);

		// IndexSearcher sucht nach der übergebenen Query und liefert die 10
		// besten Resultate
		long start = System.currentTimeMillis();
		TopDocs hits = is.search(query, 10);
		long end = System.currentTimeMillis();
		System.err.println("Took " + (end - start) + " ms to find in  LUCENE "
				+ hits.totalHits + " document(s) that matched query '" + q
				+ "':");
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			ScoreDoc scoreDoc = hits.scoreDocs[i];

			// Mittels der DocID wird das entsprechende Dokument ausgelesen
			Document doc = is.doc(scoreDoc.doc);
			// alternativ...
			// Document doc = is.getIndexReader().document(scoreDoc.doc);

			// Filename wird ausgegeben (key-value Prinzip)
			System.out.println("Fullform: " + doc.get("fullform"));
			System.out.println("Infinitiv: " + doc.get("infinitiv"));
			System.out.println("Root: " + doc.get("root"));
			System.out.println("Tempus: " + doc.get("tempus"));
			System.out.println("Pronoun: " + doc.get("pronoun"));
			System.out.println("Person: " + doc.get("person"));
			System.out.println("Numerus: " + doc.get("numerus"));
			System.out.println("Genus: " + doc.get("genus"));

			System.out.println("+++++++++++++++++++++++++++++++++++");
		}
		dirReader.close();
	}

}

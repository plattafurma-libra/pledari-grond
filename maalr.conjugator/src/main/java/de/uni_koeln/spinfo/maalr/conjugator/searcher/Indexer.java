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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Indexer {

	public static final String indexPath = "/Users/franciscomondaca/spinfo/local_projects/rg/konjugator/lucene/index2";

	private static final Version LUCENE_VERSION = Version.LUCENE_43;

	public DBCollection getMongoCollection() {
		MongoClient mongoClient = null;
		try {
			mongoClient = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		DB db = mongoClient.getDB("verbs_rg");
		DBCollection collection = db.getCollection("verbs_fm");

		return collection;

	}

	public void createIndex(DBCollection collection) throws IOException {

		StandardAnalyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);
		IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION,
				analyzer);

		IndexWriter writer = new IndexWriter(FSDirectory.open(new File(
				indexPath)), config);

		DBCursor cursor = collection.find();
		long start = System.currentTimeMillis();
		while (cursor.hasNext()) {

			DBObject dbObject = cursor.next();

			Document document = new Document();

			document.add(new StringField("_id", dbObject.get("_id").toString(),
					Field.Store.YES));
			addToDoc(dbObject, document, "fullform");
			addToDoc(dbObject, document, "infinitiv");
			addToDoc(dbObject, document, "root");
			addToDoc(dbObject, document, "tempus");
			addToDoc(dbObject, document, "pronoun");
			addToDoc(dbObject, document, "person");
			addToDoc(dbObject, document, "numerus");
			addToDoc(dbObject, document, "genus");

			writer.addDocument(document);
		}

		writer.close();
		long end = System.currentTimeMillis();
		System.out.println("Indexing took " + (end - start));

	}

	private void addToDoc(DBObject dbObject, Document document, String str) {
		Object object = dbObject.get(str);
		String content = "NOT DEFINED";
		if (object != null) {
			content = object.toString();
		}
		document.add(new TextField(str, content, Field.Store.YES));
	}

}

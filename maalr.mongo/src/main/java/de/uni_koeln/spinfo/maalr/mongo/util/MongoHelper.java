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
package de.uni_koeln.spinfo.maalr.mongo.util;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;

public class MongoHelper {

	private static Logger logger = LoggerFactory.getLogger(MongoHelper.class);

	private static MongoClient mongo;

	private static DB db;
	
	private static String DB_NAME;

	private static final Object lock = new Object();

	public static DB getDB(String dbName) throws UnknownHostException {
		synchronized(lock) {
			dbName = dbName != null ? dbName : Configuration.getInstance().getDbName();
			//if(db == null) {
			if(DB_NAME == null || !DB_NAME.equals(dbName)) {
				DB_NAME = dbName;
				logger.debug("Connecting to MongoDB... " + DB_NAME);
				mongo = new MongoClient(Configuration.getInstance().getMongoDBHost(), Configuration.getInstance().getMongoPort());
				db = mongo.getDB(DB_NAME);
			}
			return db;	
		}
	}
	
	public static boolean isRunning() {
		logger.info("Checking if MongoDB is running - might produce some stack traces...");
		try {
			CommandResult result = getDB(null).getStats();
			if(result != null) return true;
			return false;
		} catch (Exception e) {
			logger.info("Seems like MongoDB did not run - ignore exceptions above.");
			return false;
		}
	}

	public static void shutdown() {
		synchronized(lock ) {
			mongo.close();
			db = null;
			mongo = null;
		}
	}

}

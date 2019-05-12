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

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;

public class MongoHelper {

	private static Logger logger = LoggerFactory.getLogger(MongoHelper.class);

	private static MongoClient MONGO_CLIENT;

	private static MongoDatabase DATABASE;

	private static final Object lock = new Object();

	public static MongoDatabase getDB(String dbName) throws UnknownHostException {
		synchronized(lock) {
			
			dbName = dbName != null ? dbName : Configuration.getInstance().getDbName();
			
			if(MONGO_CLIENT == null) {
				MONGO_CLIENT = new MongoClient(Configuration.getInstance().getMongoDBHost(), Configuration.getInstance().getMongoPort());
			}
			
			DATABASE = MONGO_CLIENT.getDatabase(dbName);
			
			logger.info("Connecting to data base... " + DATABASE.getName());
			
			return DATABASE;	
		}
	}
	
	public static boolean isRunning() {
		return DATABASE != null && MONGO_CLIENT != null;
	}

	public static void shutdown() {
		synchronized(lock ) {
			MONGO_CLIENT.close();
			DATABASE = null;
			MONGO_CLIENT = null;
		}
	}

}

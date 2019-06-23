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

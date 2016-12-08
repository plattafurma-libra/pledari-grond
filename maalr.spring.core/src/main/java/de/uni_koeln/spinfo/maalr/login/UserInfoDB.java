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
package de.uni_koeln.spinfo.maalr.login;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;
import de.uni_koeln.spinfo.maalr.mongo.util.MongoHelper;

public class UserInfoDB {

	private DBCollection userCollection;
	private static final Logger logger = LoggerFactory.getLogger(UserInfoDB.class);
	
	UserInfoDB() {
		try {
			Configuration config = Configuration.getInstance();
			DB db = MongoHelper.getDB(config.getUserDb());
			userCollection = db.getCollection(config.getUserDbCollection());
			if(userCollection.count() == 0)
				createIndex();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createIndex() {
		userCollection.createIndex(new BasicDBObject(Constants.Users.CREATION_DATE, 1));
		userCollection.createIndex(new BasicDBObject(Constants.Users.LAST_MODIFICATION, 1));
		BasicDBObject login = new BasicDBObject(Constants.Users.LOGIN, 1);
		userCollection.ensureIndex(login,new BasicDBObject("unique", "true"));
		userCollection.createIndex(new BasicDBObject(Constants.Users.PASSWORD, 1));
	}

	boolean userExists(String login) {
		BasicDBObject obj = new BasicDBObject();
		obj.put(Constants.Users.LOGIN, login);
		DBCursor cursor = userCollection.find(obj);
		boolean hasNext = cursor.hasNext();
		cursor.close();
		return hasNext;
	}

	/**
	 * <strong>For unit tests only!</strong> This method drops the entire
	 * collection of entries and creates a new one.
	 */
	void deleteAllEntries() {
		userCollection.drop();
	}

	MaalrUserInfo insert(MaalrUserInfo user) throws InvalidUserException {
		if(userExists(user.getLogin())) throw new InvalidUserException("User already exists!");
		long now = System.currentTimeMillis();
		user.setCreationDate(now);
		user.setLastModificationDate(now);
		userCollection.insert(user);
		logger.info("Inserted new user " + user.getLogin());
		return user;
	}

	MaalrUserInfo getByLogin(String login) {
		BasicDBObject obj = new BasicDBObject();
		obj.put(Constants.Users.LOGIN, login);
		return findByDbObject(obj);
	}
	
	public MaalrUserInfo getByEmail(String email) {
		BasicDBObject obj = new BasicDBObject();
		obj.put(Constants.Users.EMAIL, email);
		return findByDbObject(obj);
	}

	private MaalrUserInfo findByDbObject(BasicDBObject obj) {
		DBCursor cursor = userCollection.find(obj);
		if(!cursor.hasNext()) return null;
		MaalrUserInfo toReturn = new MaalrUserInfo(cursor.next());
		cursor.close();
		return toReturn;
	}
	
	MaalrUserInfo getOrCreate(String login) throws InvalidUserException {
		if(userExists(login)) {
			return getByLogin(login);
		} else {
			Role role = "admin".equals(login) ? Role.ADMIN_5 : Role.GUEST_1;
			return insert(new MaalrUserInfo(login, role));
		}
	}

	void updateUser(MaalrUserInfo user) throws InvalidUserException {
		if(!userExists(user.getLogin())) {
			throw new InvalidUserException("User does not exist");
		}
		update(user);
	}

	private void update(MaalrUserInfo user) {
		// TODO: Stimmt das so?
		// DOTO: Google doch mal!
		// TODO: Na gut...
		userCollection.save(user);
	}
	
	public List<MaalrUserInfo> getAllUsers(int from, int length, String sortColumn, boolean sortAscending) {
		BasicDBObject query = new BasicDBObject();
		DBCursor cursor = userCollection.find(query);
		if(sortColumn != null) {
			BasicDBObject sort = new BasicDBObject();
			sort.put(sortColumn, sortAscending ? 1 : -1);
			cursor.sort(sort);
		}
		cursor = cursor.skip(from).limit(length);
		List<MaalrUserInfo> all = new ArrayList<MaalrUserInfo>();
		while(cursor.hasNext()) {
			DBObject o = cursor.next();
			MaalrUserInfo user = new MaalrUserInfo(o);
			all.add(user);
		}
		cursor.close();
		return all;
	}
	
	List<MaalrUserInfo> getAllUsers(Role role, String text, String sortColumn, boolean sortAscending, int from, int length) {
		BasicDBObject query = new BasicDBObject();
		Pattern pattern = Pattern.compile(".*" + text + ".*", Pattern.CASE_INSENSITIVE);
		if(role != null) {
			query.put(Constants.Users.ROLE, role.toString());
		}
		// The value for the variable 'text' is set in 'maalr.gwt > ListFilter.java'
		if(text != null && text.trim().length() > 0) {
			BasicDBList attributes = new BasicDBList();
//			DBObject firstName = new BasicDBObject();
//			firstName.put(Constants.Users.FIRSTNAME, pattern); 
//			attributes.add(firstName);
//			DBObject lastName = new BasicDBObject();
//			lastName.put(Constants.Users.LASTNAME, pattern);
//			attributes.add(lastName);
			DBObject login = new BasicDBObject();
			login.put(Constants.Users.LOGIN, pattern);
			attributes.add(login);
			query.append("$or", attributes);
		}
		DBCursor cursor = userCollection.find(query);
		if(sortColumn != null) {
			BasicDBObject sort = new BasicDBObject();
			sort.put(sortColumn, sortAscending ? 1 : -1);
			cursor.sort(sort);
		}
		cursor = cursor.skip(from).limit(length);
		List<MaalrUserInfo> all = new ArrayList<MaalrUserInfo>();
		while(cursor.hasNext()) {
			DBObject o = cursor.next();
			MaalrUserInfo user = new MaalrUserInfo(o);
			if(!all.contains(user)) 
				all.add(user);
		}
		cursor.close();
		return all;
	}

	int getNumberOfUsers() {
		return (int) userCollection.count();
	}

	public boolean deleteUser(LightUserInfo user) {
		BasicDBObject obj = new BasicDBObject();
		obj.put(Constants.Users.LOGIN, user.getLogin());
		userCollection.remove(obj);
		return true;
	}

//	public SocialUserDetails loadUserByUserId(String userId) {
//		BasicDBObject obj = new BasicDBObject();
//		obj.put(Constants.ID, userId);
//		DBCursor cursor = userCollection.find(obj);
//		if(!cursor.hasNext()) return null;
//		MaalrUserInfo toReturn = new MaalrUserInfo(cursor.next());
//		cursor.close();
//		return toReturn;
//	}

}

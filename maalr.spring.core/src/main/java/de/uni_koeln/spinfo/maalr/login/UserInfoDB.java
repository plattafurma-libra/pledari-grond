package de.uni_koeln.spinfo.maalr.login;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.result.DeleteResult;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;
import de.uni_koeln.spinfo.maalr.mongo.util.MongoHelper;

public class UserInfoDB {

	private MongoCollection<Document> userCollection;
	private static final Logger logger = LoggerFactory.getLogger(UserInfoDB.class);

	UserInfoDB() {
		try {
			Configuration config = Configuration.getInstance();
			MongoDatabase db = MongoHelper.getDB(config.getUserDb());
			userCollection = db.getCollection(config.getUserDbCollection());
			if (userCollection.countDocuments() == 0) {
				createIndex();
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	private void createIndex() {
		userCollection.createIndex(Indexes.ascending(Constants.Users.LOGIN), new IndexOptions().unique(true));
		userCollection.createIndex(Indexes.ascending(Constants.Users.CREATION_DATE));
		userCollection.createIndex(Indexes.ascending(Constants.Users.LAST_MODIFICATION));
		userCollection.createIndex(Indexes.ascending(Constants.Users.PASSWORD));

	}

	boolean userExists(String login) {
		BasicDBObject obj = new BasicDBObject();
		obj.put(Constants.Users.LOGIN, login);
		MongoCursor<Document> cursor = userCollection.find(obj).iterator();
		boolean hasNext = cursor.hasNext();
		cursor.close();
		return hasNext;
	}

	/**
	 * <strong>For unit tests only!</strong> This method drops the entire collection
	 * of entries and creates a new one.
	 */
	void deleteAllEntries() {
		userCollection.drop();
	}

	MaalrUserInfo insert(MaalrUserInfo user) throws InvalidUserException {
		if (userExists(user.getLogin())) {
			throw new InvalidUserException("User already exists!");
		}
		long now = System.currentTimeMillis();
		user.setCreationDate(now);
		user.setLastModificationDate(now);
		userCollection.insertOne(new Document(user));
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
		MongoCursor<Document> cursor = userCollection.find(obj).iterator();
		if (!cursor.hasNext())
			return null;
		MaalrUserInfo toReturn = new MaalrUserInfo(new BasicDBObject(cursor.next()));
		cursor.close();
		return toReturn;
	}

	MaalrUserInfo getOrCreate(String login) throws InvalidUserException {
		if (userExists(login)) {
			return getByLogin(login);
		} else {
			Role role = "admin".equals(login) ? Role.ADMIN_5 : Role.GUEST_1;
			return insert(new MaalrUserInfo(login, role));
		}
	}

	void updateUser(MaalrUserInfo user) throws InvalidUserException {
		if (!userExists(user.getLogin())) {
			throw new InvalidUserException("User does not exist");
		}
		update(user);
	}

	private void update(MaalrUserInfo user) {
		Document document = new Document(user);
		userCollection.insertOne(document);
		logger.info("user updated: {}", document.toJson());
	}

	public List<MaalrUserInfo> getAllUsers(int from, int length, String sortColumn, boolean sortAscending) {
		FindIterable<Document> find = userCollection.find();
		if (sortColumn != null) {
			BasicDBObject sort = new BasicDBObject();
			sort.put(sortColumn, sortAscending ? 1 : -1);
			find.sort(sort);
		}
		find = find.skip(from).limit(length);
		List<MaalrUserInfo> all = new ArrayList<MaalrUserInfo>();
		MongoCursor<Document> cursor = find.iterator();
		while (cursor.hasNext()) {
			DBObject o = new BasicDBObject(cursor.next());
			MaalrUserInfo user = new MaalrUserInfo(o);
			all.add(user);
		}
		cursor.close();
		return all;
	}

	List<MaalrUserInfo> getAllUsers(Role role, String text, String sortColumn, boolean sortAscending, int from,
			int length) {
		BasicDBObject query = new BasicDBObject();
		Pattern pattern = Pattern.compile(".*" + text + ".*", Pattern.CASE_INSENSITIVE);
		if (role != null) {
			query.put(Constants.Users.ROLE, role.toString());
		}
		// The value for the variable 'text' is set in 'maalr.gwt > ListFilter.java'
		if (text != null && text.trim().length() > 0) {
			BasicDBList attributes = new BasicDBList();
			DBObject login = new BasicDBObject();
			login.put(Constants.Users.LOGIN, pattern);
			attributes.add(login);
			query.append("$or", attributes);
		}
		FindIterable<Document> find = userCollection.find(query);
		if (sortColumn != null) {
			BasicDBObject sort = new BasicDBObject();
			sort.put(sortColumn, sortAscending ? 1 : -1);
			find.sort(sort);
		}
		find = find.skip(from).limit(length);
		List<MaalrUserInfo> all = new ArrayList<MaalrUserInfo>();
		MongoCursor<Document> cursor = find.iterator();
		while (cursor.hasNext()) {
			DBObject o = new BasicDBObject(cursor.next());
			MaalrUserInfo user = new MaalrUserInfo(o);
			if (!all.contains(user)) {
				all.add(user);
			}
		}
		cursor.close();
		return all;
	}

	int getNumberOfUsers() {
		return (int) userCollection.countDocuments();
	}

	public boolean deleteUser(LightUserInfo user) {
		BasicDBObject obj = new BasicDBObject();
		obj.put(Constants.Users.LOGIN, user.getLogin());
		DeleteResult deleteOne = userCollection.deleteOne(obj);
		return deleteOne.getDeletedCount() == 1;
	}

}

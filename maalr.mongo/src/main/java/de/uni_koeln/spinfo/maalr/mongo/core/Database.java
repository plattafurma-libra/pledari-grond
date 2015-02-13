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
package de.uni_koeln.spinfo.maalr.mongo.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntryList;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.stats.DatabaseStatistics;
import de.uni_koeln.spinfo.maalr.mongo.stats.StatEntry.Category;
import de.uni_koeln.spinfo.maalr.mongo.util.MongoHelper;
import de.uni_koeln.spinfo.maalr.mongo.util.Validator;

public class Database {

	private static final String QUERY_VERSION_CREATOR = LemmaVersion.CREATOR;
	private static final String QUERY_VERSION_ROLE = LemmaVersion.CREATOR_ROLE;
	private static final String QUERY_VERSION_VERIFICATION = LemmaVersion.VERIFICATION;
	private static final String QUERY_VERSION_IP = LemmaVersion.IP_ADDRESS;
	private static final String QUERY_VERSION_TIMESTAMP = LemmaVersion.TIMESTAMP;
	private static final String QUERY_VERSION_STATE = LemmaVersion.STATUS;
	private static final String QUERY_VERSION_VERIFIER = LemmaVersion.VERIFIER;

	private static Database instance;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private DBCollection entryCollection;

	// private DBCollection backupCollection;

	private LemmaDescription description = Configuration.getInstance()
			.getLemmaDescription();

	private final boolean debugging;

	Database() throws UnknownHostException {
		debugging = logger.isDebugEnabled();
		entryCollection = MongoHelper.getDB(null).getCollection("entries");
		// backupCollection = db.getCollection("backup");
		long entries = entryCollection.count();
		logger.info("Connected to entries-collection containing " + entries + " items.");
		createIndex();
	}

	private void createIndex() {
		// FIXME: Optimize indexes to efficiently support editor queries...
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_CREATOR, 1).append("background", true));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_IP, 1).append("background", true));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_ROLE, 1).append("background", true));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_STATE, 1).append("background", true));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_TIMESTAMP, 1));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_VERIFICATION, 1).append("background", true));
//		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS, 1)
//				.append("background", true));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_VERIFICATION, 1).append(
				QUERY_VERSION_TIMESTAMP, 1));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_VERIFIER, 1).append("background", true));
		entryCollection.ensureIndex(new BasicDBObject(LexEntry.VERSIONS + "."
				+ QUERY_VERSION_VERIFICATION, 1).append(QUERY_VERSION_STATE, 1)
				.append(QUERY_VERSION_TIMESTAMP, 1));
		logger.info("DB indices have been created.");
	}

	public static synchronized Database getInstance()
			throws NoDatabaseAvailableException {
		try {
			if (instance == null) {
				instance = new Database();
			}
			return instance;
		} catch (UnknownHostException e) {
			throw new NoDatabaseAvailableException("No Database Available!", e);
		}
	}

	private String toLogString(LemmaVersion lemma) {
		String first = description.toUnescapedString(lemma, UseCase.RESULT_LIST, true);
		String second = description.toUnescapedString(lemma, UseCase.RESULT_LIST, false);
		if (first.length() > 15) {
			first = first.substring(0, 15) + "...";
		}
		if (second.length() > 15) {
			second = second.substring(0, 15) + "...";
		}
		return first + " ⇔ " + second;
	}

	public void insert(final LexEntry entry) throws InvalidEntryException {
		Validator.validate(entry);
		DBObject object = Converter.convertLexEntry(entry);
		WriteResult result = entryCollection.insert(object);
		ObjectId id = (ObjectId) object.get("_id");
		entry.setId(id.toString());
		operationResult(result);
		logger.info("INSERTED: "
				+ toLogString(entry.getVersionHistory().get(0)) + ", entry "
				+ entry.getId());
	}

	public void insertBatch(final List<DBObject> entries)
			throws InvalidEntryException {
		if (entries == null)
			throw new IllegalArgumentException("Parameter must not be null!");
		if (entries.isEmpty())
			return;
		long s = System.nanoTime();
		WriteResult result = entryCollection.insert(entries);
		long e = System.nanoTime();
		logger.info("Batch insert of " + entries.size() + " entries completed in " + (e-s)/1000000 + " ms.");
		
		operationResult(result);
	}

	public Iterator<LexEntry> getEntries() {
		final DBCursor cursor = entryCollection.find();
		Iterator<LexEntry> entryIterator = new Iterator<LexEntry>() {

			@Override
			public boolean hasNext() {
				if (!cursor.hasNext()) {
					cursor.close();
					return false;
				}
				return cursor.hasNext();
			}

			@Override
			public LexEntry next() {
				return Converter.convertToLexEntry(cursor.next());
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub

			}

		};
		return entryIterator;
	}

	public BasicDBObject getById(final String id) {
		return (BasicDBObject) entryCollection.findOne(new ObjectId(id));
	}

	public void delete(LexEntry entry) throws InvalidEntryException {
		if (entry == null || entry.getId() == null) {
			throw new InvalidEntryException("Cannot delete entry without id!");
		}
		BasicDBObject object = new BasicDBObject();
		object.put("_id", new ObjectId(entry.getId()));
		WriteResult result = entryCollection.remove(object, WriteConcern.SAFE);
		operationResult(result);
		logger.info("DELETED: " + toLogString(entry.getCurrent()) + ", entry "
				+ entry.getId());
	}

	private void operationResult(final WriteResult result) {
		if (result.getError() != null) {
			throw new RuntimeException("Operation failed: " + result.getError());
		}
	}

	public void accept(LexEntry entry, LemmaVersion version)
			throws InvalidEntryException {
		Validator.validate(entry);
		version.setVerification(Verification.ACCEPTED);
		entry.setCurrent(version);
		DBObject object = Converter.convertLexEntry(entry);
		entryCollection.save(object);
		logger.info("ACCEPTED: " + toLogString(entry.getCurrent()) + ", entry "
				+ entry.getId());
	}

	public void acceptAfterUpdate(LexEntry entry, LemmaVersion suggestion,
			LemmaVersion modified) throws InvalidEntryException {
		Validator.validate(entry);
		suggestion.setVerification(Verification.OUTDATED);
		modified.setTimestamp(System.currentTimeMillis());
		modified.setUserId(suggestion.getUserId());
		modified.setIP(suggestion.getIP());
		modified.setVerification(Verification.ACCEPTED);
		modified.setCreatorRole(suggestion.getCreatorRole());
		modified.setStatus(Status.NEW_MODIFICATION);
		entry.addLemma(modified);
		entry.setCurrent(modified);
		DBObject object = Converter.convertLexEntry(entry);
		entryCollection.save(object);
		logger.info("ACCEPTED/UPDATED: " + toLogString(entry.getCurrent())
				+ ", entry " + entry.getId());
	}

	public void reject(LexEntry entry, LemmaVersion version)
			throws InvalidEntryException {
		Validator.validate(entry);
		if (version.equals(entry.getCurrent())) {
			throw new InvalidEntryException(
					"Please choose a new current version before rejecting!");
		}
		version.setVerification(Verification.REJECTED);
		DBObject object = Converter.convertLexEntry(entry);
		entryCollection.save(object);
		logger.info("REJECTED: " + toLogString(version) + ", entry "
				+ entry.getId());
	}

	public void rate(final LexEntry entry, boolean rateUp) {
		if (debugging)
			logger.debug("Rating " + entry + ", " + rateUp);
	}

	public void update(LexEntry entry, LemmaVersion update)
			throws InvalidEntryException {
		update.setStatus(Status.NEW_MODIFICATION);
		entry.addLemma(update);
		if (update.isApproved()) {
			LemmaVersion current = entry.getCurrent();
			if (current != null) {
				current.setVerification(Verification.OUTDATED);
			}
			entry.setCurrent(update);
		}
		DBObject object = Converter.convertLexEntry(entry);
		WriteResult result = entryCollection.save(object);
		if (update.isApproved()) {
			logger.info("UPDATED: " + toLogString(entry.getCurrent())
					+ ", entry " + entry.getId());
		} else {
			logger.info("SUGGESTED UPDATE: " + toLogString(update) + ", entry "
					+ entry.getId());
		}
		operationResult(result);
	}

	/**
	 * <strong>For unit tests only!</strong> This method drops the entire
	 * collection of entries and creates a new one.
	 */
	public void deleteAllEntries() {
		logger.warn("Dropping database!");
		entryCollection.drop();
		try {
			entryCollection = MongoHelper.getDB(null).getCollection("entries");
			createIndex();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <strong>For unit tests only!</strong>
	 * 
	 * @return
	 */
	public long getNumberOfEntries() {
		return entryCollection.count();
	}

	public LexEntryList queryForLexEntries(String login, Role role,
			Verification verification, String verifier, long startTime, long endTime,
			Status[] states, int limit, int offset, String orderField,
			boolean ascending) {
		 logger.info("Query Params: " + login + ", " + role + ", " +
		 verification + ", " + startTime + ", " + endTime + ", " + states +
		 ", " + limit + ", " + offset + ", " + orderField + ", " + ascending);
		DBCursor cursor = query(login, role, verification, verifier, startTime, endTime,
				states, limit, offset, orderField, ascending);
		List<LexEntry> results = new ArrayList<LexEntry>();
		while (cursor.hasNext()) {
			DBObject next = cursor.next();
			LexEntry entry = Converter.convertToLexEntry(next);
			results.add(entry);
		}
		int count = cursor.count();
		cursor.close();
		return new LexEntryList(results, count);
	}

	private DBCursor query(String loginOrIP, Role role,
			Verification verification, String verifier, long startTime, long endTime,
			Status[] states, int limit, int offset, String orderField,
			boolean ascending) {
		// TODO: Add querying for 'current' state
		BasicDBObject query = new BasicDBObject();
		BasicDBObject attributes = new BasicDBObject();
		if (loginOrIP != null && loginOrIP.trim().length() > 0) {
			BasicDBList or = new BasicDBList();
			DBObject creator = new BasicDBObject();
			creator.put(QUERY_VERSION_CREATOR, loginOrIP);
			or.add(creator);
			DBObject ip = new BasicDBObject();
			ip.put(QUERY_VERSION_IP, loginOrIP);
			or.add(ip);
			attributes.put("$or", or);
		}
		if(verifier != null && verifier.trim().length() > 0) {
			attributes.put(QUERY_VERSION_VERIFIER, verifier);
		}
		if (role != null) {
			attributes.put(QUERY_VERSION_ROLE, role.toString());
		}
		if (verification != null) {
			attributes.put(QUERY_VERSION_VERIFICATION, verification.toString());
		}
		if (states != null && states.length > 0) {
			BasicDBList or = new BasicDBList();
			for (Status s : states) {
				or.add(s.toString());
			}
			DBObject nestedOr = new BasicDBObject();
			nestedOr.put("$in", or);
			attributes.put(QUERY_VERSION_STATE, nestedOr);
		}
		if (startTime > 0) {
			attributes.put(QUERY_VERSION_TIMESTAMP, new BasicDBObject("$gt",
					startTime));
		}
		if (endTime > 0) {
			attributes.put(QUERY_VERSION_TIMESTAMP, new BasicDBObject("$lt",
					endTime));
		}
		if(startTime > 0 && endTime > 0) {
			DBObject obj = new BasicDBObject("$lt", endTime);
			obj.put("$gt", startTime);
			attributes.put(QUERY_VERSION_TIMESTAMP, obj);
		}
		DBCursor found;
		if (attributes.size() > 0) {
			BasicDBObject elemMatch = new BasicDBObject("$elemMatch",
					attributes);
			query.append(LexEntry.VERSIONS, elemMatch);
			// query.append("$and", attributes);
			// query.append("$elemMatch", attributes);
			found = entryCollection.find(query);
		} else {
			found = entryCollection.find();
		}
		if (orderField != null) {
			DBObject order = new BasicDBObject();
			order.put(LexEntry.VERSIONS + "." + orderField, ascending ? 1 : -1);
			found.sort(order);
		}
		// TODO: This is inefficient! However, it should be ok for
		// small queries, which is the expected usecase.
		if (offset > 0) {
			found = found.skip(offset);
		}
		if (limit > 0) {
			found = found.limit(limit);
		}
		return found;
	}

	public String getCollection() {
		return entryCollection.getName();
	}

	public boolean isEmpty() {
		return entryCollection.getCount() == 0;
	}

	public DatabaseStatistics getStatistics() {
		DatabaseStatistics stats = new DatabaseStatistics();
		stats.setNumberOfEntries((int) entryCollection.getCount());
		Map<Verification, Integer> count = new HashMap<Verification, Integer>();
		DBCursor cursor = entryCollection.find();
		int entryCount = 0, lemmaCount = 0;
		Verification[] values = Verification.values();
		for (Verification verification : values) {
			count.put(verification, 0);
		}
		count.put(null, 0);
		while (cursor.hasNext()) {
			LexEntry entry = Converter.convertToLexEntry(cursor.next());
			List<LemmaVersion> history = entry.getVersionHistory();
			entryCount++;
			lemmaCount += history.size();
			for (LemmaVersion lemma : history) {
				Integer old = count.get(lemma.getVerification());
				count.put(lemma.getVerification(), old + 1);
			}
		}
		stats.setNumberOfApproved(count.get(Verification.ACCEPTED));
		stats.setNumberOfSuggestions(count.get(Verification.UNVERIFIED));
		stats.setNumberOfDeleted(count.get(Verification.REJECTED));
		stats.setNumberOfOutdated(count.get(Verification.OUTDATED));
		stats.setNumberOfUndefined(count.get(null));
		stats.setNumberOfEntries(entryCount);
		stats.setNumberOfLemmata(lemmaCount);
		/*
		 * TODO: - Create a separate "System/Diagnostics"-Tab, containing a)
		 * general information about - RAM - Free Disk Space - CPU - Network b)
		 * DB-Statistics - general - entries - users
		 * 
		 * Check if this will work with sigar:
		 * 
		 * 
		 * 
		 * - Add green and red 'lamps' to each entry, each sub-statistic
		 * (entries, users, general) - Add one lamp to the navigation bar, to
		 * summarize the server state: should always be green.
		 */

		CommandResult dbStats = entryCollection.getDB().getStats();

		NumberFormat nf = NumberFormat.getInstance();
		stats.addDBProperty("Server", dbStats.getString("serverUsed"));

		Double serverStatus = dbStats.getDouble("ok");
		if (Double.compare(1D, serverStatus) == 0) {
			stats.addDBProperty("Server Status", "ok");
		} else {
			stats.addDBProperty("Server Status", "not ok: " + serverStatus,
					Category.ERROR);
		}

		long collections = dbStats.getLong("collections");
		if (collections == 0) {
			stats.addDBProperty("Number of Collections",
					nf.format(dbStats.getLong("collections")), Category.ERROR);
		} else {
			stats.addDBProperty("Number of Collections",
					nf.format(dbStats.getLong("collections")));
		}
		stats.addDBProperty("Number of Indexes",
				nf.format(dbStats.getLong("indexes")));
		stats.addDBProperty("Average Object Size",
				nf.format(dbStats.getDouble("avgObjSize") / 1024) + " KB");
		stats.addDBProperty("Data Size",
				nf.format(dbStats.getLong("dataSize") / (1024 * 1024)) + " MB");
		stats.addDBProperty("Storage Size",
				nf.format(dbStats.getLong("storageSize") / (1024 * 1024))
						+ " MB");
		stats.addDBProperty("Index Size",
				nf.format(dbStats.getLong("indexSize") / (1024 * 1024)) + " MB");
		stats.addDBProperty("File Size",
				nf.format(dbStats.getLong("fileSize") / (1024 * 1024)) + " MB");

		BasicDBObject query;
		BasicDBList attributes;
		query = new BasicDBObject();
		attributes = new BasicDBList();
		DBObject lemmata = new BasicDBObject();
		lemmata.put(QUERY_VERSION_TIMESTAMP, new BasicDBObject("$gt", -1));
		attributes.add(lemmata);
		query.append("$and", attributes);
		// stats.setNumberOfLemmata((int) entryCollection.count(query));

		return stats;
	}

	public void dropHistory(LexEntry entry) {
		Iterator<LemmaVersion> history = entry.getVersionHistory().iterator();
		while (history.hasNext()) {
			LemmaVersion version = history.next();
			if (version.getVerification() == Verification.OUTDATED
					|| version.getVerification() == Verification.REJECTED) {
				history.remove();
			}
		}
		DBObject object = Converter.convertLexEntry(entry);
		WriteResult result = entryCollection.save(object);
		operationResult(result);
		logger.info("HISTORY DROPPED: " + toLogString(entry.getCurrent())
				+ ", entry " + entry.getId());
	}

	public String export(boolean allVersions) throws JAXBException,
			IOException, NoSuchAlgorithmException {
		String fileName = null;
		if (allVersions) {
			fileName = "all_entries_" + DateFormat.getDateInstance().format(new Date());
		} else {
			fileName = "approved_entries_" + DateFormat.getDateInstance().format(new Date());
		}
		File file = new File(fileName + ".zip");
		FileOutputStream fos = new FileOutputStream(file);
		exportData(allVersions, false, fos, fileName);
		return file.getAbsolutePath();
	}

	public void exportData(boolean allVersions, boolean dropInternalKeys, OutputStream output, String fileName) throws JAXBException, IOException, NoSuchAlgorithmException {
		DBCursor cursor = entryCollection.find();
		JAXBContext context = null;
		if (allVersions) {
			context = JAXBContext.newInstance(LexEntry.class);
		} else {
			context = JAXBContext.newInstance(LemmaVersion.class);
		}
		ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(output));
		zout.putNextEntry(new ZipEntry(fileName + ".xml"));
		MD5OutputStream md5 = new MD5OutputStream(zout);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(md5, "UTF-8"));
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.write("\n<entries>\n");
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		int entryCounter = 0;
		int versionCounter = 0;
		while (cursor.hasNext()) {
			DBObject object = cursor.next();
			LexEntry entry = Converter.convertToLexEntry(object);
			entryCounter++;
			if (allVersions) {
				if (dropInternalKeys) {
					ArrayList<LemmaVersion> versions = entry.getVersionHistory();
					for (LemmaVersion version : versions) {
						version.setMaalrValues(new HashMap<String, String>());
					}
				}
				marshaller.marshal(entry, out);
				out.write("\n");
				versionCounter += entry.getVersionHistory().size();
			} else {
				LemmaVersion version = entry.getCurrent();
				if (version != null) {
					if (dropInternalKeys) {
						version.setMaalrValues(new HashMap<String, String>());
					}
					marshaller.marshal(version, out);
				}
				versionCounter++;
			}
		}
		out.write("\n</entries>\n");
		out.flush();
		zout.closeEntry();
		zout.putNextEntry(new ZipEntry("About.txt"));
		out = new BufferedWriter(new OutputStreamWriter(zout, "UTF-8"));
		out.write("MD5:\t" + md5.getHash() + "\n");
		out.write("Entries:\t" + entryCounter + "\n");
		out.write("Versions:\t" + versionCounter + "\n");
		out.flush();
		zout.closeEntry();
		zout.close();
	}

	public void importData(InputStream input) throws JAXBException, IOException, XMLStreamException, InvalidEntryException {
		logger.info("Importing...");
		XMLStreamReader xsr = getXMLStreamReader(new BufferedInputStream(input));
		xsr.nextTag(); // Advance to statements element
		Unmarshaller unmarshaller = JAXBContext.newInstance(LexEntry.class).createUnmarshaller();
		List<DBObject> toInsert = new ArrayList<DBObject>();
		int counter = 0;
		while (xsr.nextTag() == XMLStreamConstants.START_ELEMENT) {
			LexEntry entry = (LexEntry) unmarshaller.unmarshal(xsr);
			// System.out.println(entry.getMostRecent().getTimestamp());
			toInsert.add(Converter.convertLexEntry(entry));
			counter++;
			if (counter % 10000 == 0) {
				insertBatch(toInsert);
				toInsert.clear();
			}
		}
		insertBatch(toInsert);
		logger.info("Import done.");
	}
	
	

	public Iterator<LexEntry> getExportedData(InputStream input) throws JAXBException, IOException, XMLStreamException, InvalidEntryException {
		final XMLStreamReader xsr = getXMLStreamReader(new BufferedInputStream(input));
		xsr.nextTag();
		xsr.nextTag();
		final Unmarshaller unmarshaller = JAXBContext.newInstance(LexEntry.class).createUnmarshaller();
		Iterator<LexEntry> allEntries = new Iterator<LexEntry>() {
			
			
			private LexEntry next = (LexEntry) unmarshaller.unmarshal(xsr);

			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public LexEntry next() {
				LexEntry toReturn = next;
				try {
					xsr.nextTag();
					next = (LexEntry) unmarshaller.unmarshal(xsr);
				} catch (JAXBException e) {
					throw new RuntimeException("Failed to unmarshal entry", e);
				} catch (IllegalStateException e) {
					next = null;
				} catch (XMLStreamException e) {
					throw new RuntimeException("Failed to unmarshal entry", e);
				}
				return toReturn;
			}

			@Override
			public void remove() {
				// TODO Auto-generated method stub
				
			}
		};
		return allEntries;
	}

	private XMLStreamReader getXMLStreamReader(InputStream input) throws IOException, FactoryConfigurationError, XMLStreamException, UnsupportedEncodingException {
		ZipInputStream in = new ZipInputStream(input);
		getNextEntry(in);
		XMLInputFactory xif = XMLInputFactory.newInstance();
		XMLStreamReader xsr = xif.createXMLStreamReader(new BufferedReader(new InputStreamReader(in, "UTF-8")));
		return xsr;
	}

	private void getNextEntry(ZipInputStream in) throws IOException {
		ZipEntry ze = in.getNextEntry();
		while (!ze.getName().endsWith(".xml")) {
			ze = in.getNextEntry();
		}
	}

}

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;

/**
 * Converts {@link LexEntry} and {@link LemmaVersion} objects to/from
 * {@link DBObject} objects. FIXME: Separate Maalr entries and user entries, to
 * allow non-string values for Maalr entries. Required for search...
 * 
 * @return
 */
public class Converter {

	public static BasicDBObject convertLexEntry(LexEntry entry) {
		BasicDBObject object = new BasicDBObject();
		List<LemmaVersion> vHistory = entry.getVersionHistory();
		BasicDBList versions = new BasicDBList();
		for (LemmaVersion lemmaVersion : vHistory) {
			BasicDBObject obj = convertLemmaVersion(lemmaVersion);
			versions.add(obj);
		}
		object.put(LexEntry.VERSIONS, versions);
		if (entry.getId() != null) {
			object.put(LexEntry.ID, new ObjectId(entry.getId()));
		}
		object.put(LexEntry.CURRENT, entry.getCurrentId());
		object.put(LexEntry.INTERNAL_ID, entry.getNextInternalId());
		object.put(LexEntry.CHANGE_STAMP, entry.getChangeStamp());
		return object;
	}

	private static BasicDBObject convertLemmaVersion(LemmaVersion lemmaVersion) {
		BasicDBObject obj = new BasicDBObject();
		Map<String, String> toStore = new HashMap<String, String>(lemmaVersion.getEntryValues());
		toStore.keySet().removeAll(LemmaVersion.MAALR_KEYS);
		toStore.putAll(lemmaVersion.getMaalrValues());
		obj.putAll(toStore);
		obj.put(LemmaVersion.TIMESTAMP, lemmaVersion.getTimestamp());
		return obj;
	}

	public static LexEntry convertToLexEntry(DBObject obj) {
		ArrayList<Document> versions = (ArrayList<Document>) obj.get(LexEntry.VERSIONS);
		ArrayList<LemmaVersion> list = new ArrayList<LemmaVersion>();
		for (Document doc : versions) {
			Map<String, String> entryValues = new HashMap(doc);
			entryValues.keySet().removeAll(LemmaVersion.MAALR_KEYS);
			Map<String, String> maalrValues = new HashMap(doc);
			maalrValues.keySet().retainAll(LemmaVersion.MAALR_KEYS);
			Long timeStamp = (Long) doc.remove(LemmaVersion.TIMESTAMP);
			LemmaVersion lemmaVersion = new LemmaVersion();
			lemmaVersion.getEntryValues().putAll(entryValues);
			lemmaVersion.getMaalrValues().putAll(maalrValues);
			lemmaVersion.setTimestamp(timeStamp);
			list.add(lemmaVersion);
		}
		ObjectId objId = (ObjectId) obj.get(LexEntry.ID);
		LexEntry entry = new LexEntry();
		if (objId != null) {
			entry.setId(objId.toString());
		}
		entry.setNextInternalId((Integer) obj.get(LexEntry.INTERNAL_ID));
		entry.setLemmaVersions(list);
		entry.setCurrentId((Integer) obj.get(LexEntry.CURRENT));
		entry.setChangeStamp((String) obj.get(LexEntry.CHANGE_STAMP));
		return entry;
	}
}
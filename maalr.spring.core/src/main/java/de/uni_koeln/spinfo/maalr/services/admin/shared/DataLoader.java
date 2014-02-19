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
package de.uni_koeln.spinfo.maalr.services.admin.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.login.LoginManager;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;

@Service
public class DataLoader {
	
	
	@Autowired
	private Index index;
	
	@Autowired
	private LoginManager loginManager;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public void createFromSQLDump(File file, int maxEntries) throws IOException, NoDatabaseAvailableException, InvalidEntryException, IndexException {
		
		if(!file.exists()) {
			logger.info("No data to import - file " + file + " does not exist.");
			return;
		}
		
		BufferedReader br;
		ZipFile zipFile = null;
		if(file.getName().endsWith(".zip")) {
			logger.info("Trying to read data from zip file=" + file.getName());
			zipFile = new ZipFile(file);
			String entryName = file.getName().replaceAll(".zip", "");
//			ZipEntry entry = zipFile.getEntry(entryName+".tsv");
			ZipEntry entry = zipFile.getEntry(entryName);
			if(entry == null) {
				logger.info("No file named " + entryName + " found in zip file - skipping import");
				zipFile.close();
				return;
			}
			 br = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), "UTF-8"));
		} else {
			logger.info("Trying to read data from file " + file);
			 br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
		}
		
		String line = br.readLine();
		String[] keys = line.split("\t",-1);
		Database db = Database.getInstance();
		List<DBObject> entries = new ArrayList<DBObject>();
		int counter = 0;
		String userId = loginManager.getCurrentUserId();
		while((line = br.readLine()) != null) {
			String[] values = line.split("\t",-1);
			if(values.length != keys.length) {
				logger.warn("Ignoring entry: Attribute mismatch (" + values.length + " entries found, " + keys.length + " entries expected) in line " + line);
				continue;
			}
			LemmaVersion version = new LemmaVersion();
			for(int i = 0; i < keys.length; i++) {
				String value = values[i].trim();
				String key = keys[i].trim();
				if(value.length() == 0) continue;
				if(key.length() == 0) continue;
				version.setValue(key, value);
			}
			LexEntry entry = new LexEntry(version);
			entry.setCurrent(version);
			entry.getCurrent().setStatus(Status.NEW_ENTRY);
			entry.getCurrent().setVerification(Verification.ACCEPTED);
			long timestamp = System.currentTimeMillis();
			String embeddedTimeStamp = version.getEntryValue(LemmaVersion.TIMESTAMP);
			if(embeddedTimeStamp != null) {
				timestamp = Long.parseLong(embeddedTimeStamp);
				version.removeEntryValue(LemmaVersion.TIMESTAMP);
			}
			entry.getCurrent().setUserId(userId);
			entry.getCurrent().setTimestamp(timestamp);
			entry.getCurrent().setCreatorRole(Role.ADMIN_5);
			entries.add(Converter.convertLexEntry(entry));
			if(entries.size() == 10000) {
				db.insertBatch(entries);
				entries.clear();
			}
			counter++;
			if (counter == maxEntries) {
				logger.warn("Skipping db creation, as max entries is "
						+ maxEntries);
				break;
			}
		}
		db.insertBatch(entries);
		entries.clear();
		//loginManager.login("admin", "admin");
		Iterator<LexEntry> iterator = db.getEntries();
		index.dropIndex();
		index.addToIndex(iterator);
		logger.info("Index has been created, swapping to RAM...");
		index.reloadIndex();
		logger.info("RAM-Index updated.");
		br.close();
		if(zipFile != null) {
			zipFile.close();
		}
		//loginManager.logout();
		logger.info("Dataloader initialized.");
	}

}

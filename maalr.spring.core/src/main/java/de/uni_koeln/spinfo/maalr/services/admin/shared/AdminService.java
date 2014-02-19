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

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.zip.ZipException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.DatabaseException;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.statistics.IStatisticsService;
import de.uni_koeln.spinfo.maalr.common.shared.statistics.SystemSummary;
import de.uni_koeln.spinfo.maalr.configuration.Environment;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.DatabaseIOException;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.stats.BackupInfos;
import de.uni_koeln.spinfo.maalr.mongo.stats.DatabaseStatistics;
import de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics;
import de.uni_koeln.spinfo.maalr.mongo.util.BackUpHelper;

@Service
@Secured(Constants.Roles.ADMIN_5)
public class AdminService {
	
	@Autowired
	private Environment environment;
	
	@Autowired(required=false)
	@Qualifier("maalr.system.stats")
	private IStatisticsService systemStats;
	
//	@Autowired
//	private DataBaseCreator dbCreator;
	
	@Autowired
	private Index index;
	
	@Autowired
	private DataLoader dbCreator;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void importDatabase() throws NoDatabaseAvailableException, IndexException, InvalidEntryException, DatabaseIOException, ZipException, IOException {
		dbCreator.createFromSQLDump(environment.getLexFile(), -1);
			rebuildIndex();
	}

	public void importDatabase(int max) throws Exception {
			dbCreator.createFromSQLDump(environment.getLexFile(), max);
			rebuildIndex();
	}

	public String dropDatabase() throws DatabaseException {
		Database.getInstance().deleteAllEntries();
		boolean empty = Database.getInstance().isEmpty();
		if(empty) {
			return "The database has been dropped and is empty";
		} else {
			return "The database has been dropped but is still not empty, which is weird.";
		}
	}

	public String reloadDatabase() throws DatabaseException, IndexException {
		dropDatabase();
		try {
			dbCreator.createFromSQLDump(environment.getLexFile(), -1);
		} catch (ZipException e) {
			throw new DatabaseIOException(e);
		} catch (IOException e) {
			throw new DatabaseIOException(e);
		}
		return "The database has been reloaded.";
	}
	
	public String rebuildIndex() throws NoDatabaseAvailableException, IndexException {
		logger.info("Rebuilding index...");
		Database db = Database.getInstance();
		Iterator<LexEntry> iterator = db.getEntries();
		index.dropIndex();
		index.addToIndex(iterator);
		logger.info("Index has been created, swapping to RAM...");
		index.reloadIndex();
		logger.info("RAM-Index updated!");
		return "The index has been rebuilt";
	}

	public DatabaseStatistics getDatabaseStats() throws NoDatabaseAvailableException {
		return Database.getInstance().getStatistics();
	}
	
	public IndexStatistics getIndexStats() throws NoIndexAvailableException {
		IndexStatistics statistics = index.getIndexStatistics();
		DictionaryStatistics.initialize(statistics.getUnverifiedEntries(), statistics.getApprovedEntries(), statistics.getLastUpdated(), statistics.getOverlayCount());
		return statistics;
	}

	public SystemSummary getSystemSummary() {
		if(systemStats == null) return null;
		return systemStats.getCurrent();
	}

	public void importDatabase(HttpServletRequest request) throws IOException, InvalidEntryException, NoDatabaseAvailableException, JAXBException, XMLStreamException {
		DefaultMultipartHttpServletRequest dmhsRequest = (DefaultMultipartHttpServletRequest) request;
		MultipartFile multipartFile = (MultipartFile) dmhsRequest.getFile("file");
		InputStream in = multipartFile.getInputStream();
		Database.getInstance().importData(in);
	}

	public void exportData(boolean allVersions, boolean dropKeys,
			ServletOutputStream out, String fileName) throws NoDatabaseAvailableException, NoSuchAlgorithmException, JAXBException, IOException {
		Database.getInstance().exportData(allVersions, dropKeys, out, fileName);
	}
	
	public BackupInfos getBackupInfos() {
		return BackUpHelper.getInstance().getBackupInfos();
	}
}

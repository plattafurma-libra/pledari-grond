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
package de.uni_koeln.spinfo.maalr.webapp.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.zip.ZipException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.uni_koeln.spinfo.maalr.common.shared.DatabaseException;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.statistics.SystemSummary;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.DatabaseIOException;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.stats.BackupInfos;
import de.uni_koeln.spinfo.maalr.mongo.stats.DatabaseStatistics;
import de.uni_koeln.spinfo.maalr.services.admin.shared.AdminService;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendService;

@Controller("backendService")
public class AdminController implements BackendService {
	
	@Autowired
	private AdminService service;
	
	@Override
	public void importDatabase(int maxItems) throws Exception {
		service.importDatabase(maxItems);
	}

	@Override
	public String dropDatabase() throws DatabaseException {
		return service.dropDatabase();
	}

	@Override
	public String reloadDatabase() throws DatabaseException, IndexException {
		return service.reloadDatabase();
	}

	@Override
	public String rebuildIndex() throws NoDatabaseAvailableException,
			IndexException {
		return service.rebuildIndex();
	}

	@Override
	public DatabaseStatistics getDatabaseStats() throws NoDatabaseAvailableException {
		return service.getDatabaseStats();
	}

	@Override
	public IndexStatistics getIndexStats() throws NoIndexAvailableException {
		return service.getIndexStats();
	}

	@Override
	public SystemSummary getSystemSummary() {
		return service.getSystemSummary();
	}
	
	@Override
	public BackupInfos getBackupInfos() {
		return service.getBackupInfos();
	}	
	
	@ModelAttribute("query")
	public MaalrQuery getQuery() {
		return new MaalrQuery();
	}
	
	@RequestMapping(value = "/admin/rebuildIndex", method = RequestMethod.GET)
	public ModelAndView rebuildIndexMV() throws NoDatabaseAvailableException, IndexException {
		service.rebuildIndex();
		return new ModelAndView("admin/admin");
	}

	
	@RequestMapping(value = "/admin/importDatabase", method = RequestMethod.GET)
	public ModelAndView importDatabase() throws NoDatabaseAvailableException, InvalidEntryException, DatabaseIOException, ZipException, IndexException, IOException {
		service.importDatabase();
		return new ModelAndView("admin/admin");
	}

	@RequestMapping(value = "/admin/importDB", method = { RequestMethod.POST })
	public ModelAndView importDatabase(HttpServletRequest request) throws IOException, InvalidEntryException, NoDatabaseAvailableException, JAXBException, XMLStreamException {
		service.importDatabase(request);
		return new ModelAndView("admin/admin");
	}
	
	@RequestMapping("/admin/export")
	public void export(@RequestParam(value = "all", defaultValue = "true") boolean allVersions,
			@RequestParam(value = "dropKeys", defaultValue = "false") boolean dropKeys,
			HttpServletResponse response) throws IOException, NoDatabaseAvailableException, JAXBException, NoSuchAlgorithmException {
		response.setContentType("application/zip");
		StringBuilder fileName = new StringBuilder();
		fileName.append("maalr_db_dump_");
		if(allVersions) {
			fileName.append("all_versions_");
		} else {
			fileName.append("current_versions_");
		}
		if(dropKeys) {
			fileName.append("anonymized_");
		}
		fileName.append(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date()));
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".zip");
		ServletOutputStream out = response.getOutputStream();
		service.exportData(allVersions, dropKeys, out, fileName.toString());
	}

}

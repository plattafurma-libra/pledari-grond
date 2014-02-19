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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.GenerationFailedException;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntryList;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditor;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.services.editor.server.EditorServiceImpl;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;

@Controller("editorService")
public class EditorController implements EditorService {
	
	@Autowired
	EditorServiceImpl service;

	@Override
	public LexEntryList getLexEntries(EditorQuery query) throws Exception {
		return service.getLexEntries(query);
	}

	@Override
	public LexEntry accept(LexEntry entry, LemmaVersion version)
			throws Exception {
		return service.accept(entry, version);
	}

	@Override
	public LexEntry reject(LexEntry entry, LemmaVersion rejected)
			throws Exception {
		return service.reject(entry, rejected);
	}

	@Override
	public LexEntry drop(LexEntry entry) throws Exception {
		return service.drop(entry);
	}

	@Override
	public LexEntry acceptAfterUpdate(LexEntry entry, LemmaVersion suggested,
			LemmaVersion modified) throws Exception {
		return service.acceptAfterUpdate(entry, suggested, modified);
	}
	
	

	@Override
	public LexEntry update(LexEntry entry, LemmaVersion updated) throws Exception {
		return service.update(entry, updated);
	}

	@Override
	public LexEntry dropOutdatedHistory(LexEntry entry) throws Exception {
		return service.dropOutdatedHistory(entry);
	}

	@Override
	public QueryResult search(MaalrQuery maalrQuery) throws Exception {
		return service.search(maalrQuery);
	}

	@Override
	public LexEntry getLexEntry(String entryId) throws Exception {
		return service.getLexEntry(entryId);
	}

	@Override
	public LexEntry insert(LexEntry entry) throws Exception {
		return service.insert(entry);
	}


	@RequestMapping("/editor/export")
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

	@Override
	public List<LexEntry> updateOrder(boolean firstLang, List<LemmaVersion> ordered) throws Exception {
		return service.updateOrder(firstLang, ordered);
	}

	@Override
	public ArrayList<LemmaVersion> getOrder(String lemma, boolean firstLanguage)
			throws Exception {
		return service.getOrder(firstLanguage, lemma);
	}

	@Override
	public String export(Set<String> fields, EditorQuery query) throws NoDatabaseAvailableException, IOException {
		File dir = new File("exports");
		dir.mkdirs();
		final File tmp = new File(dir, "export_" + UUID.randomUUID() + ".tsv.zip");
		Timer timer = new Timer();
		service.export(fields, query, tmp);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(tmp.exists()) {
					System.out.println("Deleting file " + tmp);
					tmp.delete();
				}
			}
		}, 60000*30);
		return tmp.getName();
	}
	
	@Override
	public String export(Set<String> fields, MaalrQuery query)
			throws Exception {
		File dir = new File("exports");
		dir.mkdirs();
		final File tmp = new File(dir, "export_" + UUID.randomUUID() + ".tsv.zip");
		Timer timer = new Timer();
		service.export(fields, query, tmp);
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(tmp.exists()) {
					System.out.println("Deleting file " + tmp);
					tmp.delete();
				}
			}
		}, 60000*30);
		return tmp.getName();
	}
	
	@RequestMapping("/editor/download/{fileName}.html")
	public void export(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
		File dir = new File("exports");
		File file = new File(dir, fileName);
		ServletOutputStream out = response.getOutputStream();
		if(!file.exists()) {
			OutputStreamWriter writer = new OutputStreamWriter(out);
			writer.write("This link has expired. Please re-export the data and try again.");
			writer.flush();
			return;
		}
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		IOUtils.copy(in, out);
		in.close();
		out.close();
		file.delete();
	}
	
	@Override
	public OverlayEditor getOverlayEditor(String overlayId) throws IOException {
		return service.getOverlayEditor(overlayId);
	}

	@Override
	public HashMap<String, String> getOverlayEditorPreset(String overlayId,
			String presetId, String base) throws GenerationFailedException {
		return service.getOverlayEditorPreset(overlayId, presetId, base);
	}

	@Override
	public ArrayList<String> getOverlayTypes(boolean firstLanguage) {
		return service.getOverlayTypes(firstLanguage);
	}
	
	

	
}

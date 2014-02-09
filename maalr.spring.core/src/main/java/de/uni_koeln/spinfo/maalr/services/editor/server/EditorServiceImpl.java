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
package de.uni_koeln.spinfo.maalr.services.editor.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.xml.bind.JAXBException;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.server.IOverlayGenerator;
import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.GenerationFailedException;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntryList;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditor;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayPresetChooser;
import de.uni_koeln.spinfo.maalr.common.shared.Overlays;
import de.uni_koeln.spinfo.maalr.configuration.Environment;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.mongo.SpringBackend;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;

@Service
@Secured(Constants.Roles.TRUSTED_IN_4)
public class EditorServiceImpl {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private SpringBackend db;
	
	@Autowired
	private UserInfoBackend userInfos;

	@Autowired
	private Index index;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	public LexEntryList getLexEntries(EditorQuery query) throws Exception {
		LexEntryList result = Database.getInstance().queryForLexEntries(query.getUserOrIp(), query.getRole(), query.getVerification(), query.getVerifier(), query.getStartTime(), query.getEndTime(), query.getState(), query.getPageSize(), query.getCurrent(), query.getSortColumn(), query.isSortAscending());
		for (LexEntry lexEntry : result.entries()) {
			addUserInfos(lexEntry);
		}
		return result;
	}
	
	private void addUserInfos(LexEntry lexEntry) {
		List<LemmaVersion> lemmata = lexEntry.getVersionHistory();
		for (LemmaVersion lemma : lemmata) {
			addUserInfo(lemma);
		}
	}

	private void addUserInfo(LemmaVersion lemma) {
		String userId = lemma.getUserId();
		MaalrUserInfo userInfo = userInfos.getByLogin(userId);
		if(userInfo != null) {
			lemma.setUserInfo(userInfo.toLightUser());
		}
	}

	public LexEntry accept(LexEntry entry, LemmaVersion version) throws Exception {
		db.accept(entry, version);
		return entry;
	}
	
	public LexEntry insert(LexEntry entry) throws Exception {
		db.insert(entry);
		return entry;
	}
	
	public LexEntry acceptAfterUpdate(LexEntry entry, LemmaVersion suggested, LemmaVersion modified) throws Exception {
		db.acceptAfterUpdate(entry, suggested, modified);
		return entry;
	}
	
	public LexEntry reject(LexEntry entry, LemmaVersion version) throws Exception {
		db.reject(entry, version);
		return entry;
	}

	public LexEntry drop(LexEntry entry) throws Exception {
		db.delete(entry);
		return entry;
	}

	public LexEntry dropOutdatedHistory(LexEntry entry) throws Exception {
		db.dropOutdatedHistory(entry);
		return entry;
	}

	public QueryResult search(MaalrQuery maalrQuery) throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = index.query(maalrQuery);
		return result;
	}

	public LexEntry getLexEntry(String entryId) throws NoDatabaseAvailableException {
		return Converter.convertToLexEntry(Database.getInstance().getById(entryId));
	}

	public void exportData(boolean allVersions, boolean dropKeys,
			ServletOutputStream out, String fileName) throws NoDatabaseAvailableException, NoSuchAlgorithmException, JAXBException, IOException {
		Database.getInstance().exportData(allVersions, dropKeys, out, fileName);
	}

	public LexEntry update(LexEntry entry, LemmaVersion updated) throws Exception {
		db.update(entry, updated);
		return entry;
	}
	
	public List<LexEntry> updateOrder(boolean firstLang, List<LemmaVersion> ordered) throws Exception {
		return db.updateOrder(firstLang, ordered);
	}

	public ArrayList<LemmaVersion> getOrder(boolean firstLanguage, String lemma) throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		MaalrQuery query = new MaalrQuery();
		query.setPageSize(100);
		query.setQueryValue("searchPhrase", lemma);
		return new ArrayList<LemmaVersion>(index.queryExact(query).getEntries());
	}

	public void export(Set<String> fields, EditorQuery query, File dest) throws NoDatabaseAvailableException, IOException {
		query.setCurrent(0);
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(dest));
		zout.putNextEntry(new ZipEntry("exported.tsv"));
		OutputStream out = new BufferedOutputStream(zout);
		OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		for (String field : fields) {
			writer.write(field);
			writer.write("\t");
		}
		writer.write("\n");
		while(true) {
			LexEntryList result = Database.getInstance().queryForLexEntries(query.getUserOrIp(), query.getRole(), query.getVerification(), query.getVerifier(), query.getStartTime(), query.getEndTime(), query.getState(), query.getPageSize(), query.getCurrent(), query.getSortColumn(), query.isSortAscending());
			if(result == null ||result.getEntries() == null || result.getEntries().size() == 0) break;
			for (LexEntry lexEntry : result.entries()) {
				addUserInfos(lexEntry);
				LemmaVersion version = lexEntry.getCurrent();
				write(writer, version, fields);
				writer.write("\n");
			}
			query.setCurrent(query.getCurrent() + query.getPageSize());
		}
		writer.flush();
		zout.closeEntry();
		writer.close();
	}
	
	public void export(Set<String> fields, MaalrQuery query, File dest) throws IOException, InvalidQueryException, NoIndexAvailableException, BrokenIndexException, InvalidTokenOffsetsException {
		query.setPageNr(0);
		query.setPageSize(50);
		ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(dest));
		zout.putNextEntry(new ZipEntry("exported.tsv"));
		OutputStream out = new BufferedOutputStream(zout);
		OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
		for (String field : fields) {
			writer.write(field);
			writer.write("\t");
		}
		writer.write("\n");
		while(true) {
			QueryResult result = index.query(query);
			if(result == null ||result.getEntries() == null || result.getEntries().size() == 0) break;
			List<LemmaVersion> entries = result.getEntries();
			for (LemmaVersion version : entries) {
				write(writer, version, fields);
				writer.write("\n");
			}
			query.setPageNr(query.getPageNr()+1);
		}
		writer.flush();
		zout.closeEntry();
		writer.close();
		
	}

	private void write(OutputStreamWriter writer, LemmaVersion version,
			Set<String> fields) throws IOException {
		for (String field : fields) {
			String value = version.getEntryValue(field);
			if(value != null) writer.write(value.trim());
			writer.write("\t");
		}
	}

	public OverlayEditor getOverlayEditor(String overlayType) throws IOException {
		return Overlays.getEditor(overlayType);
	}

	public HashMap<String, String> getOverlayEditorPreset(String overlayType,
			String presetId, String base) throws GenerationFailedException {
		try {
			OverlayEditor editor = Overlays.getEditor(overlayType);
			logger.info("Overlay-Editor for type " + overlayType + ": " + editor);
			if(editor != null) {
				OverlayPresetChooser chooser = editor.getPresetChooser();
				String presetBuilderClass = chooser.getPresetBuilderClass();
				logger.info("PReset-Builder: " + presetBuilderClass);
				if(presetBuilderClass != null) {
					Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(presetBuilderClass);
					IOverlayGenerator generator = (IOverlayGenerator) clazz.newInstance();
					logger.info("Generator created!");
					return generator.buildPreset(presetId, base);
				}
			}
		} catch (ClassNotFoundException e) {
			logger.error("Failed to find overlay generator class", e);
		} catch (InstantiationException e) {
			logger.error("Failed to instantiate overlay generator class", e);
		} catch (IllegalAccessException e) {
			logger.error("Failed to instantiate overlay generator class", e);
		}
		logger.info("Returning null!");
		return null;
	}

	public ArrayList<String> getOverlayTypes(boolean firstLanguage) {
		return Overlays.getOverlayTypes(firstLanguage);
	}

	
	
}

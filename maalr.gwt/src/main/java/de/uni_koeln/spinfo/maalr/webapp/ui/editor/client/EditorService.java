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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.GenerationFailedException;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntryList;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditor;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;

@RemoteServiceRelativePath("rpc/editor")
public interface EditorService extends RemoteService {
	
	public LexEntryList getLexEntries(EditorQuery query) throws Exception;
	
	public LexEntry accept(LexEntry entry, LemmaVersion version) throws Exception;

	public LexEntry reject(LexEntry entry, LemmaVersion rejected) throws Exception;
	
	public LexEntry drop(LexEntry entry) throws Exception;
	
	public LexEntry acceptAfterUpdate(LexEntry entry, LemmaVersion suggested, LemmaVersion modified) throws Exception;

	LexEntry dropOutdatedHistory(LexEntry entry) throws Exception;

	QueryResult search(MaalrQuery maalrQuery) throws Exception;

	LexEntry getLexEntry(String entryId) throws Exception;

	LexEntry insert(LexEntry entry) throws Exception;

	LexEntry update(LexEntry entry, LemmaVersion fromEditor) throws Exception;

	List<LexEntry> updateOrder(boolean firstLang, List<LemmaVersion> ordered) throws Exception;

	ArrayList<LemmaVersion> getOrder(String lemma, boolean firstLanguage) throws Exception;

	String export(Set<String> fields, EditorQuery query) throws Exception;

	String export(Set<String> selected, MaalrQuery query) throws Exception;
	
	OverlayEditor getOverlayEditor(String overlayId) throws IOException;
	
	HashMap<String, String> getOverlayEditorPreset(String overlayId, String presetId, String base) throws GenerationFailedException;
	
	ArrayList<String> getOverlayTypes(boolean firstLanguage);
	

}

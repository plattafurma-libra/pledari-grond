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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntryList;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditor;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;

public interface EditorServiceAsync {

	
	void getLexEntries(EditorQuery query,
			AsyncCallback<LexEntryList> callback);

	void accept(LexEntry entry, LemmaVersion version,
			AsyncCallback<LexEntry> callback);

	void reject(LexEntry entry, LemmaVersion rejected,
			AsyncCallback<LexEntry> asyncCallback);
	
	void insert(LexEntry entry, AsyncCallback<LexEntry> callback);
	
	void drop(LexEntry entry, AsyncCallback<LexEntry> asyncCallback);
	
	void acceptAfterUpdate(LexEntry entry, LemmaVersion suggested, LemmaVersion modified, AsyncCallback<LexEntry> callback);

	void dropOutdatedHistory(LexEntry entry, AsyncCallback<LexEntry> asyncCallback);
	
	void search(MaalrQuery maalrQuery, AsyncCallback<QueryResult> callback);

	void getLexEntry(String entryId, AsyncCallback<LexEntry> asyncCallback);

	void update(LexEntry entry, LemmaVersion fromEditor,
			AsyncCallback<LexEntry> callback);
	
	void updateOrder(boolean firstLang, List<LemmaVersion> ordered,
			AsyncCallback<List<LexEntry>> callback);

	void getOrder(String lemma, boolean firstLanguage,
			AsyncCallback<ArrayList<LemmaVersion>> asyncCallback);

	void export(Set<String> fields, EditorQuery query, AsyncCallback<String> asyncCallback);

	void export(Set<String> selected, MaalrQuery query, AsyncCallback<String> asyncCallback);
	
	void getOverlayEditor(String overlayId, AsyncCallback<OverlayEditor> editor);
	
	void getOverlayEditorPreset(String overlayId, String presetId, String base, AsyncCallback<HashMap<String, String>> preset);

	void getOverlayTypes(boolean firstLanguage,
			AsyncCallback<ArrayList<String>> asyncCallback);
	
}

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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell;


import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorMessages;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.AdvancedEditor;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.filter.ListFilter;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.AllEntriesList;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LexEntryCellWrapper;


public class VerifiedEntryOptionsCell extends AbstractCell<LexEntryCellWrapper> {
	
	private static EntryListUiRenderer renderer = GWT.create(EntryListUiRenderer.class);
	
	private static EditorServiceAsync service = GWT.create(EditorService.class);
	
	
	interface EntryListUiRenderer extends UiRenderer {

		/*
		 * Responsible for rendering the cell as defined in the 'ui.xml'
		 * Remember that the parameter names must be identical here and in the ui.xml-file!
		 * See https://developers.google.com/web-toolkit/doc/latest/DevGuideUiBinder#Rendering_HTML_for_Cells
		 */
		void render(SafeHtmlBuilder sb, LexEntryCellWrapper wrapper);

		void onBrowserEvent(VerifiedEntryOptionsCell cell, NativeEvent event, Element element, LexEntryCellWrapper wrapper);
	}

	private AllEntriesList list;

	private ListFilter filter;

	private EditorConstants constants;

	private EditorMessages messages;

	public VerifiedEntryOptionsCell(AllEntriesList list, ListFilter filter, final EditorConstants constants,  final EditorMessages messages) {
		super("click");
		this.list = list;
		this.filter = filter;
		this.constants = constants;
		this.messages = messages;
	}


	@UiHandler({"accept"})
	void onAccept(ClickEvent event, Element parent, final LexEntryCellWrapper wrapper) {
		AdvancedEditor.openEditor(wrapper.getLexEntry(), wrapper.getLemmaVersion(), new AsyncCallback<LexEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO: Add translation for 'Throwable caught'
				Dialog.showError(constants.operationFailed(), caught);
			}

			@Override
			public void onSuccess(LexEntry result) {
				EditorQuery query = filter.getQuery();
				filter.setQuery(query, true);

			}
		}, constants, messages);
	}
	
	@UiHandler({"reject"})
	void onReject(ClickEvent event, Element parent, final LexEntryCellWrapper wrapper) {
		final AsyncCallback<LexEntry> callback = new AsyncCallback<LexEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO: Add translation for 'Throwable caught'
				Dialog.showError(constants.operationFailed(), caught);
			}

			@Override
			public void onSuccess(LexEntry result) {
				EditorQuery query = filter.getQuery();
				filter.setQuery(query, true);
			}
		};
		Command ok = new Command() {
			
			@Override
			public void execute() {
				service.drop(wrapper.getLexEntry(), callback);
			}
		};
		Dialog.confirm(constants.confirmDeletion(), constants.deleteEntryQuestion(), constants.confirmDeleteButton(), constants.cancelDeleteButton(), ok , null, true);
	}

	@Override
	public void render(Context context, LexEntryCellWrapper wrapper, SafeHtmlBuilder sb) {
		if(wrapper == null) return;
		renderer.render(sb, wrapper);
	}
	
	@Override
	public void onBrowserEvent(Context context,
			Element parent, LexEntryCellWrapper user, NativeEvent event,
			ValueUpdater<LexEntryCellWrapper> valueUpdater) {
		user.setRow(context.getIndex());
		renderer.onBrowserEvent(this, event, parent, user);
	}
	
}

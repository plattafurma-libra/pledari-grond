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

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorMessages;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.AdvancedEditor;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LemmaVersionCellWrapper;


public class EntryEditCell extends AbstractCell<LemmaVersionCellWrapper> {
	
	private static EntryListUiRenderer renderer = GWT.create(EntryListUiRenderer.class);
	
	private EditorServiceAsync service = GWT.create(EditorService.class);

	private Command modifyCommand;

	private AsyncCallback<LemmaVersion> deleteCommand;

	private EditorConstants constants;

	private EditorMessages messages;
	

	
	interface EntryListUiRenderer extends UiRenderer {

		/*
		 * Responsible for rendering the cell as defined in the 'ui.xml'
		 * Remember that the parameter names must be identical here and in the ui.xml-file!
		 * See https://developers.google.com/web-toolkit/doc/latest/DevGuideUiBinder#Rendering_HTML_for_Cells
		 */
		void render(SafeHtmlBuilder sb, LemmaVersionCellWrapper wrapper);

		void onBrowserEvent(EntryEditCell cell, NativeEvent event, Element element, LemmaVersionCellWrapper wrapper);
	}

	public EntryEditCell(final EditorConstants constants, final EditorMessages messages) {
		super("click");
		this.constants = constants;
		this.messages = messages;
	}

	@Override
	public void render(Context context, LemmaVersionCellWrapper wrapper, SafeHtmlBuilder sb) {
		if(wrapper == null) return;
		renderer.render(sb, wrapper);
	}
	
	@Override
	public void onBrowserEvent(Context context,
			Element parent, LemmaVersionCellWrapper user, NativeEvent event,
			ValueUpdater<LemmaVersionCellWrapper> valueUpdater) {
		renderer.onBrowserEvent(this, event, parent, user);
	}
	

	@UiHandler({"edit"})
	void onEdit(ClickEvent event, Element parent, final LemmaVersionCellWrapper wrapper) {
		String entryId = wrapper.getLemmaVersion().getLexEntryId();
		service.getLexEntry(entryId, new AsyncCallback<LexEntry>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(LexEntry result) {
				AdvancedEditor.openEditor(result, wrapper.getLemmaVersion(), new AsyncCallback<LexEntry>() {
					
					@Override
					public void onSuccess(LexEntry result) {
						modifyCommand.execute();
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Dialog.showError(constants.failedToEditEntry(), caught);
					}
				}, constants, messages);
			}
		});
		event.getNativeEvent().preventDefault();
		event.getNativeEvent().stopPropagation();
	}
	
	@UiHandler({"delete"})
	void onDelete(ClickEvent event, Element parent, final LemmaVersionCellWrapper wrapper) {
		service.getLexEntry(wrapper.getLemmaVersion().getLexEntryId(), new AsyncCallback<LexEntry>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(final LexEntry result) {
				Command ok = new Command() {
					
					@Override
					public void execute() {
						service.drop(result, new AsyncCallback<LexEntry>() {

							@Override
							public void onFailure(Throwable caught) {
								Dialog.showError(constants.failedToDeleteEntry(), caught);
							}

							@Override
							public void onSuccess(LexEntry result) {
								deleteCommand.onSuccess(wrapper.getLemmaVersion());
							}
							
						});
					}
				};
				Dialog.confirm(constants.confirmDeletion(), constants.deleteEntryQuestion(), constants.confirmDeleteButton(), constants.cancelDeleteButton(), ok , null, false);
			}
		
		});
		event.getNativeEvent().preventDefault();
		event.getNativeEvent().stopPropagation();
	}

	public void setModifyCommand(Command command) {
		this.modifyCommand = command;
	}
	
	public void setDeleteCallback(AsyncCallback<LemmaVersion> command) {
		this.deleteCommand = command;
	}

	
	
}

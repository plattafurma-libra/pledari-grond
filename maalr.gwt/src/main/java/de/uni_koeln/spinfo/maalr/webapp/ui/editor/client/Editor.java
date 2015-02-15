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
import java.util.Map;

import com.github.gwtbootstrap.datetimepicker.client.ui.resources.DatetimepickerResourceInjector;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import de.uni_koeln.spinfo.maalr.common.shared.ClientOptions;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonService;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Navigation;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.HasHistorySupport;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.LexEditor;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.SuggestionEditor;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.filter.ListFilter;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.DataCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.DateCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.EntryFilterCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.EntryFilterUserCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.ShortDataCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.SuggestedEntryOptionsCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.TypeCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.UserCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.VerifiedEntryOptionsCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.VerifierCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.column.LexEntryColumn;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.ICellWrapper;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LexEntryCellWrapper;

public class Editor implements EntryPoint {
	
	private EditorConstants constants = GWT.create(EditorConstants.class);
	
	private EditorMessages messages = GWT.create(EditorMessages.class);

	private SimplePanel panel;
	
	private Map<String, Composite> modules = new HashMap<String, Composite>();
	
	
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		DatetimepickerResourceInjector.configure();
		final RootPanel panel = RootPanel.get("navigation");
		panel.clear();
		final Navigation navigation = new Navigation();
		CommonServiceAsync service = GWT.create(CommonService.class);
		service.getClientOptions(new AsyncCallback<ClientOptions>() {
			

			@Override
			public void onSuccess(ClientOptions result) {
				navigation.setAppName(messages.getAppName(result.getShortAppName(), constants.editorBackend()));
				panel.add(navigation);
				try {
					AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(LemmaDescription result) {
							initializeMainPanel();
							initHistory();
							initModules(navigation);
							if(History.getToken().isEmpty()) {
								History.newItem(Modules.ANCHOR_SUGGESTION_EDITOR);
							}
							History.fireCurrentHistoryState();
						}
					});
				} catch (Exception e) {
					Window.alert("Error: " + e.getMessage());
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	
	}

	
	private void initializeMainPanel() {
		//RootLayoutPanel rootPanel = RootLayoutPanel.get();
		RootPanel rootPanel = RootPanel.get("main-content");
		rootPanel.getElement().getStyle().setTop(5, Unit.EM);
		rootPanel.getElement().getStyle().setBottom(2, Unit.EM);
		panel = new SimplePanel();
		rootPanel.add(panel);
	}

	private void initModules(Navigation navigation) {
		ListFilter userFilter = new ListFilter(constants.suggestionFilter());
		userFilter.setVerification(Verification.UNVERIFIED);
		userFilter.setVerificationVisible(false);
		userFilter.setRoleVisible(false);
		userFilter.setStateVisible(false);
		userFilter.setVerifierVisible(false);
		final SuggestionEditor suggestEditor = new SuggestionEditor(constants, userFilter, constants.suggestedLemma());
		suggestEditor.setNewButtonVisible(false);
		suggestEditor.setRejectAllButtonVisible(true);
		setSuggestionColumns(suggestEditor);
		suggestEditor.setVerifierColumnVisible(false);
		//suggestEditor.setDefaultColumns();
		AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(LemmaDescription description) {
				ArrayList<String> fields = new ArrayList<String>(description.getFields(UseCase.FIELDS_FOR_SIMPLE_EDITOR, true));
				fields.addAll(description.getFields(UseCase.FIELDS_FOR_SIMPLE_EDITOR, false));
				fields.add(LemmaVersion.COMMENT);
				suggestEditor.setColumns(fields);
			}
		});
		registerModule(suggestEditor, Modules.ANCHOR_SUGGESTION_EDITOR);
		ListFilter historyFilter = new ListFilter(constants.historyFilter());
		historyFilter.setVerification(Verification.ACCEPTED);
		historyFilter.setLemmaState(null);
		historyFilter.setUserRole(null);
		historyFilter.setStateVisible(false);
		historyFilter.setVerifierVisible(false);
		historyFilter.setVerificationVisible(false);
		historyFilter.setRoleVisible(false);
		final SuggestionEditor historyEditor = new SuggestionEditor(constants, historyFilter, constants.verifiedLemma());
		historyEditor.setRejectAllButtonVisible(false);
		setHistoryColumns(historyEditor);
		AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(LemmaDescription description) {
				ArrayList<String> fields = new ArrayList<String>(description.getEditorFields(true));
				fields.addAll(description.getEditorFields(false));
				fields.add(LemmaVersion.COMMENT);
				historyEditor.setColumns(fields);
			}
		});
		registerModule(historyEditor, Modules.ANCHOR_HISTORY_EDITOR);
		final LexEditor lexEditor = new LexEditor(constants);
		AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(LemmaDescription description) {
				ArrayList<String> fields = new ArrayList<String>(description.getEditorFields(true));
				fields.addAll(description.getEditorFields(false));
				fields.add(LemmaVersion.COMMENT);
				lexEditor.setColumns(fields);
			}
		});
		String contextPath = DictionaryConstants.getDictionary().get(DictionaryConstants.PATH);
		String locale = DictionaryConstants.getDictionary().get(DictionaryConstants.LOCALE);
		registerModule(lexEditor, Modules.ANCHOR_LEX_EDITOR);
		navigation.addLinkLeft(constants.suggestionEditor(), "#" + Modules.ANCHOR_SUGGESTION_EDITOR);
		navigation.addLinkLeft(constants.verificationHistory(), "#" + Modules.ANCHOR_HISTORY_EDITOR);
		navigation.addLinkLeft(constants.lexiconEditor(), "#" + Modules.ANCHOR_LEX_EDITOR);
		navigation.addLinkRight(constants.logout(), contextPath + "/j_spring_security_logout", true);
		navigation.addLinkRight(constants.ss(), GWT.getHostPageBaseURL() + "editor.html?locale=" + locale, false);
		navigation.addLinkRight(constants.en(), GWT.getHostPageBaseURL() + "editor.html?locale=en", false);
	}
	
	private void setSuggestionColumns(SuggestionEditor editor) {
		
		Column<LexEntry, ICellWrapper> user = new LexEntryColumn(new UserCell());
		editor.addDefaultColumn(constants.user(), user, "120px");
		Column<LexEntry, LexEntryCellWrapper> filter = new Column<LexEntry, LexEntryCellWrapper>(new EntryFilterUserCell(editor.getEntryList().getFilter())) {

			@Override
			public LexEntryCellWrapper getValue(LexEntry object) {
				return new LexEntryCellWrapper(object, object.getMostRecent());
			}
		};
		editor.addDefaultColumn(constants.filter(), filter, "90px");
		LexEntryColumn entryColumn = new LexEntryColumn(new ShortDataCell());
		editor.addDefaultColumn(constants.entry(), entryColumn, "250px");
		Column<LexEntry, LexEntryCellWrapper> options = new Column<LexEntry, LexEntryCellWrapper>(new SuggestedEntryOptionsCell(editor.getEntryList(), editor.getListFilter(), constants, messages)) {

			@Override
			public LexEntryCellWrapper getValue(LexEntry object) {
				return new LexEntryCellWrapper(object, object.getMostRecent());
			}
		};
		editor.addDefaultColumn(constants.options(), options, "80px");
		LexEntryColumn state = new LexEntryColumn(new TypeCell());
		state.setDataStoreName(LemmaVersion.STATUS);
		editor.addDefaultColumn(constants.state(), state, "100px");
		// Creation date
		LexEntryColumn creation = new LexEntryColumn(new DateCell());
		creation.setDataStoreName(LemmaVersion.TIMESTAMP);
		editor.addDefaultColumn(constants.created(), creation, "100px");
		editor.makeDefaultsSortable();
	}

	private void setHistoryColumns(SuggestionEditor historyEditor) {
		
		Column<LexEntry, ICellWrapper> user = new LexEntryColumn(new UserCell());
		historyEditor.addDefaultColumn(constants.user(), user, "125px");
		LexEntryColumn verifier = new LexEntryColumn(new VerifierCell());
		historyEditor.addDefaultColumn(constants.verifier(), verifier, "100px");
		Column<LexEntry, LexEntryCellWrapper> filter = new Column<LexEntry, LexEntryCellWrapper>(new EntryFilterCell(historyEditor.getEntryList().getFilter())) {

			@Override
			public LexEntryCellWrapper getValue(LexEntry object) {
				return new LexEntryCellWrapper(object, object.getMostRecent());
			}
		};
		historyEditor.addDefaultColumn(constants.filter(), filter, "90px");
		LexEntryColumn entryColumn = new LexEntryColumn(new DataCell());
		historyEditor.addDefaultColumn(constants.entry(), entryColumn, "250px");
		Column<LexEntry, LexEntryCellWrapper> options = new Column<LexEntry, LexEntryCellWrapper>(new VerifiedEntryOptionsCell(historyEditor.getEntryList(), historyEditor.getListFilter(), constants, messages)) {

			@Override
			public LexEntryCellWrapper getValue(LexEntry object) {
				return new LexEntryCellWrapper(object, object.getMostRecent());
			}
		};
		historyEditor.addDefaultColumn(constants.options(), options, "80px");
		LexEntryColumn state = new LexEntryColumn(new TypeCell());
		state.setDataStoreName(LemmaVersion.STATUS);
		historyEditor.addDefaultColumn(constants.state(), state, "100px");
		// Creation date
		LexEntryColumn creation = new LexEntryColumn(new DateCell());
		creation.setDataStoreName(LemmaVersion.TIMESTAMP);
		historyEditor.addDefaultColumn(constants.created(), creation, "100px");
		historyEditor.makeDefaultsSortable();
	}


	private void initHistory() {
		 History.addValueChangeHandler(new ValueChangeHandler<String>() {
			 
			public void onValueChange(ValueChangeEvent<String> event) {
		        final String historyToken = event.getValue();
		        if(historyToken.lastIndexOf('?') > 0) {
		        	String module = historyToken.substring(0,historyToken.lastIndexOf('?'));
		        	String state = historyToken.substring(historyToken.lastIndexOf('?')+1);
		        	Composite composite = modules.get(module);
		        	showModule(composite);
		        	if(composite instanceof HasHistorySupport) {
		        		((HasHistorySupport)composite).setHistoryToken(state);
		        	}
		        } else {
		        	showModule(modules.get(historyToken));
		        }
		      }

			private void showModule(Composite module) {
				panel.setWidget(module);
			}
		    });
	}
	
	private void registerModule(Composite composite, String key) {
		modules.put(key, composite);
	}


}

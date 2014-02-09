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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchService;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.ConfigurableSearchArea;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CustomPager;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.IResultDisplay;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.SearchHelper;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.LazyLoadEvent;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.LazyLoadHandler;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorMessages;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.Modules;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.EntryVersionsList;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.dataprovider.HistoryDataProvider;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LemmaVersionCellWrapper;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.query.QueryList;

public class LexEditor extends Composite implements HasHistorySupport {
	
	private static LexEditorUiBinder uiBinder = GWT.create(LexEditorUiBinder.class);
	
	interface LexEditorUiBinder extends UiBinder<Widget, LexEditor> {
	}
	
	private ConfigurableSearchArea queryBox;
	
	@UiField
	Column editArea;
	
	@UiField
	Label resultSummary;
	
	@UiField
	EntryVersionsList historyList;
	
	@UiField
	ButtonGroup deleteOldGroup;
	
	@UiField
	Button deleteHistory;
	
	@UiField
	Button fullHistory;
	
	@UiField
	Button newEntry;
	
	@UiField(provided=true)
	QueryList entryList;
	
	@UiField
	Well historyArea;
	
	@UiField
	CustomPager pager;
	
	@UiField
	SimplePanel searchBoxParent;
	
	@UiField
	Button export;
	
	@UiField
	DropdownButton columns;
	
	private EditorServiceAsync service;
	
	private MaalrQuery query;

	private HistoryDataProvider historyDataProvider;

	private ListDataProvider<LemmaVersion> provider;
	
	private SearchServiceAsync searchService = GWT.create(SearchService.class);

	private EditorConstants constants;
	
	private EditorMessages messages = GWT.create(EditorMessages.class);
	
	public LexEditor(EditorConstants constants) {
		this.constants = constants;
		initialize();
	}
	
	private void initialize() {
		Logger logger = Logger.getLogger(getClass());
		logger.info("Initializing...");
		final SingleSelectionModel<LemmaVersion> selectionModel = new SingleSelectionModel<LemmaVersion>();
		provider = new ListDataProvider<LemmaVersion>();
		entryList = new QueryList(selectionModel, constants, messages);
		entryList.addSelectionChangedHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Logger.getLogger(getClass()).info("Selection changed: " + event + ", " + selectionModel.getSelectedObject());
			}
		});
		service = GWT.create(EditorService.class);
		//entryDetails = new LemmaEditorWidget(AsyncLemmaDescriptionLoader.getDescription(), UseCase.FIELDS_FOR_ADVANCED_EDITOR, false, 1, true, null);
		//antlr = new AntlrEditorWidget(entryDetails);
		logger.info("Init Widget..." + getClass().toString());
		initWidget(uiBinder.createAndBindUi(LexEditor.this));
		export.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				export();
			}
		});
		queryBox = new ConfigurableSearchArea(new IResultDisplay() {
			
			@Override
			public void updateResult(MaalrQuery maalrQuery, QueryResult result) {
				historyArea.setVisible(true);
				historyDataProvider.getList().clear();
				provider.getList().clear();
				provider.getList().addAll(result.getEntries());
				selectionModel.clear();
				resultSummary.setText(messages.displayResults(result.getEntries().size(), result.getMaxEntries()));
				entryList.setQuery(maalrQuery);
				entryList.redraw();
				if(result.getEntries().size() < result.getMaxEntries()) {
					pager.createPageLinks(maalrQuery, result);
					pager.setVisible(true);
				} else {
					pager.setVisible(false);
				}
			}
		}, true, true, Modules.ANCHOR_LEX_EDITOR + "?");
		pager.setHistoryPrefix(Modules.ANCHOR_LEX_EDITOR + "?");
		searchBoxParent.add(queryBox);
		entryList.setDataProvider(provider);
		entryList.setLemmaDescription(AsyncLemmaDescriptionLoader.getDescription());
		entryList.setModifyCommand(new Command() {

			@Override
			public void execute() {
				((SingleSelectionModel)entryList.getSelectionModel()).clear();
				entryList.redraw();
			}
			
		});
		entryList.setDeleteCommand(new AsyncCallback<LemmaVersion>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(LemmaVersion result) {
				boolean removed = provider.getList().remove(result);
				entryList.redraw();
			}
		});
		provider.addDataDisplay(entryList.getTable());
		entryList.getTable().setHeight("1000px");
		historyDataProvider = new HistoryDataProvider(historyList.getTable());
		historyList.setDataProvider(historyDataProvider);
		historyList.getTable().setHeight("400px");
		historyList.setLemmaDescription(AsyncLemmaDescriptionLoader.getDescription());
		deleteOldGroup.getElement().getStyle().setFloat(Float.RIGHT);
		entryList.addLoadHandler(new LazyLoadHandler() {
			
			@Override
			public void onLoad(LazyLoadEvent event) {
				if(query != null) {
					query.setPageNr(query.getPageNr()+1);
					service.search(query, new AsyncCallback<QueryResult>() {
						
						@Override
						public void onSuccess(QueryResult result) {
							if(provider.getList().size() < result.getMaxEntries()) {
								provider.getList().addAll(result.getEntries());
								entryList.getTable().setVisibleRange(0, provider.getList().size());
								resultSummary.setText(messages.displayResults(provider.getList().size(), result.getMaxEntries()));
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
						}
					});
				}
			}
		});
		entryList.addSelectionChangedHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				LemmaVersion selected = selectionModel.getSelectedObject();
				if(selected != null) {
					service.getLexEntry(selected.getLexEntryId(), new AsyncCallback<LexEntry>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(LexEntry result) {
							historyArea.setVisible(true);
							historyDataProvider.setEntry(result, fullHistory.isToggled());
						}
					});
				} else {
					historyDataProvider.setEntry(null, fullHistory.isToggled());
				}
			}
		});
		fullHistory.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				historyDataProvider.setEntry(historyDataProvider.getEntry(), !fullHistory.isToggled());
			}
		});
		deleteHistory.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final LexEntry entry = historyDataProvider.getEntry();
				if(entry.getCurrent() == null) {
					Dialog.showInfo(constants.operationUnavailable(), constants.requireApprovedVers());
				} else {
					service.dropOutdatedHistory(entry, new AsyncCallback<LexEntry>() {

						@Override
						public void onFailure(Throwable caught) {
							Dialog.showError(constants.failedToDropEntryHistory(), caught);
						}

						@Override
						public void onSuccess(LexEntry result) {
							historyDataProvider.setEntry(result, !fullHistory.isToggled());
							entryList.redraw();
						}
					});
				}
			}
		});
		newEntry.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AdvancedEditor.openEditor(new AsyncCallback<LexEntry>() {

					@Override
					public void onFailure(Throwable caught) {
						Dialog.showError(constants.failedToCreateEntry(), caught);
					}

					@Override
					public void onSuccess(LexEntry result) {
						//Window.alert("Entry has been saved.");
					}
				}, constants, messages);
				//entryDetails.setData(new LemmaVersion());
			}
		});
		logger.info("Widget initialized.");
	}

	@Override
	public void setHistoryToken(String state) {
		MaalrQuery query = MaalrQuery.parse(state);
		SearchHelper.setLastQuery(null);
		queryBox.setQuery(query);
		doSearch(query);
	}
	
	private void doSearch(final MaalrQuery maalrQuery) {
		if(SearchHelper.getLastQuery() != null && SearchHelper.getLastQuery().equals(maalrQuery)) {
			return;
		}
		if(maalrQuery.getValues().size() == 0) {
			entryList.setVisible(true);
			return;
		}
		historyDataProvider.getList().clear();
		searchService.search(maalrQuery, new AsyncCallback<QueryResult>() {

			@Override
			public void onSuccess(QueryResult result) {
				SearchHelper.setLastQuery(maalrQuery);
				historyArea.setVisible(true);
				provider.getList().clear();
				provider.getList().addAll(result.getEntries());
				resultSummary.setText(messages.displayResults(provider.getList().size(), result.getMaxEntries()));
				entryList.setQuery(maalrQuery);
				entryList.redraw();
				if(result.getEntries().size() < result.getMaxEntries()) {
					pager.createPageLinks(maalrQuery, result);
					pager.setVisible(true);
				} else {
					pager.setVisible(false);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				entryList.setVisible(false);
				Dialog.showError(constants.errorOccurred());
			}
		});
	}
	
	public void setColumns(final List<String> fields) {
		LocalizedStrings.afterLoad(new AsyncCallback<Map<String,String>>() {

			@Override
			public void onFailure(Throwable caught) {
				setColumns(fields, new HashMap<String, String>());
			}

			private void setColumns(List<String> fields,
					Map<String, String> hashMap) {
				for (final String field : fields) {
					final String title = hashMap.get(field) == null ? field : hashMap.get(field);
					final CheckBox box = new CheckBox(title);
					box.getElement().getStyle().setMargin(5, Unit.PX);
					final Cell<LemmaVersionCellWrapper> cell = new AbstractCell<LemmaVersionCellWrapper>() {

						@Override
						public void render(
								com.google.gwt.cell.client.Cell.Context context,
								LemmaVersionCellWrapper wrapper, SafeHtmlBuilder sb) {
							String value = wrapper.getLemmaVersion().getEntryValue(field);
							if(value == null) return;
							value = value.trim();
							if(value.length() > 105) {
								value = value.substring(0, 50) + "..." + value.substring(value.length()-50, value.length());
							}
							sb.appendEscaped(value);
							
						}
					};
					box.addClickHandler(new ClickHandler() {
						
						com.google.gwt.user.cellview.client.Column<LemmaVersion, LemmaVersionCellWrapper> column = new com.google.gwt.user.cellview.client.Column<LemmaVersion, LemmaVersionCellWrapper>(cell) {

							@Override
							public LemmaVersionCellWrapper getValue(LemmaVersion object) {
								return new LemmaVersionCellWrapper(object);
							}
						};
						
						@Override
						public void onClick(ClickEvent event) {
							if(box.getValue()) {
								entryList.addColumn(title, column);
							} else {
								entryList.removeColumn(column);
							}
							//event.preventDefault();
							event.stopPropagation();
						}
					});
					columns.add(box);
				}
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				setColumns(fields, result);
			}
		});
	}
	

	private void export() {
		AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(final LemmaDescription description) {
				final ArrayList<String> fields = new ArrayList<String>(description.getEditorLangA());
				fields.addAll(description.getEditorLangB());
				fields.add(LemmaVersion.COMMENT);
				LocalizedStrings.afterLoad(new AsyncCallback<Map<String,String>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Map<String, String> translation) {
						showExportDialog(fields, translation);
					}
				});
				
			}

			private void showExportDialog(final ArrayList<String> fields, Map<String, String> translation) {
				final Modal dialog = new Modal();
				final FlowPanel panel = new FlowPanel();
				final Set<String> selected = new HashSet<String>();
				final List<CheckBox> boxes = new ArrayList<CheckBox>();
				for (final String field : fields) {
					String label = translation.get(field);
					if(label == null) label = field;
					final CheckBox box = new CheckBox(label);
					box.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							if(box.getValue()) {
								selected.add(field);
							} else {
								selected.remove(field);
							}
						};
					});
					boxes.add(box);
					panel.add(box);
				}
				Button exportAll = new Button(constants.selectAll());
				exportAll.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						for (CheckBox box : boxes) {
							box.setValue(true);
						}
						selected.addAll(fields);
					}
				});
				Button exportNone = new Button(constants.deselectAll());
				exportNone.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						for (CheckBox box : boxes) {
							box.setValue(false);
						}
						selected.clear();
					}
				});
				ButtonGroup group = new ButtonGroup();
				group.add(exportAll);
				group.add(exportNone);
				panel.add(group);
				dialog.add(panel);
				Button cancel = new Button(constants.cancel());
				cancel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						dialog.hide();
					}
				});
				final Button ok = new Button(constants.export());
				ok.setType(ButtonType.PRIMARY);
				dialog.setTitle(constants.selectedFieldsExport());
				ok.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						panel.clear();
						dialog.setTitle(constants.exporting());
						ok.setVisible(false);
						Label busyLabel = new Label(constants.exportingData());
						panel.add(busyLabel);
						service.export(selected, queryBox.getQuery(), new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(String result) {
								dialog.setTitle(constants.exportCompleted());
								panel.clear();
								result = GWT.getHostPageBaseURL() + "download/" + result + ".html";
								Label label = new Label(constants.exportDownload());
								panel.add(label);
								Anchor anchor = new Anchor(new SafeHtmlBuilder().appendEscaped(constants.download()).toSafeHtml(), result);
								anchor.addClickHandler(new ClickHandler() {
									
									@Override
									public void onClick(ClickEvent event) {
										dialog.hide();
									}
								});
								panel.add(anchor);
							}
						});
					}
				});
				ModalFooter footer = new ModalFooter(cancel, ok);
				dialog.add(footer);
				dialog.setAnimation(true);
				dialog.setBackdrop(BackdropType.STATIC);
				dialog.show();
			}
		});
	}
	
}

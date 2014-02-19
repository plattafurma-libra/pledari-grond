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
import com.github.gwtbootstrap.client.ui.Divider;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.PagingDataGrid;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorMessages;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.filter.ListFilter;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.AllEntriesList;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.EntryVersionsList;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.dataprovider.HistoryDataProvider;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.dataprovider.SimplePagingDataProvider;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.ICellWrapper;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LexEntryCellWrapper;

public class SuggestionEditor extends Composite {

	private static EntryEditorUiBinder uiBinder = GWT
			.create(EntryEditorUiBinder.class);
	
	private EditorConstants constants;
	
	private EditorMessages messages = GWT.create(EditorMessages.class);

	interface EntryEditorUiBinder extends UiBinder<Widget, SuggestionEditor> {
	}
	
	@UiField
	AllEntriesList entryList;
	
	@UiField
	EntryVersionsList historyList;
	
	@UiField(provided=true)
	ListFilter listFilter;
	
	private EditorServiceAsync service;
	
	@UiField
	Button deleteHistory;
	
	@UiField
	Button rejectAll;
	
	@UiField
	ButtonGroup toggleSelectGroup;
	
	@UiField
	Button toggleMultiSelect;
	
	@UiField
	ButtonGroup deleteEntriesGroup;
	
	@UiField
	ButtonGroup deleteOldGroup;
	
	@UiField
	Button selectAll;
	
	@UiField
	Button deselectAll;
	
	@UiField
	Button fullHistory;
	
	@UiField
	Column editArea;
	
	@UiField
	Label resultSummary;
	
	@UiField
	Label listLegend;
	
	@UiField
	HorizontalPanel pagination;
	
	@UiField
	DropdownButton columns;
	
	@UiField
	Button export;
	
	@UiField
	Button newEntry;

	private SimplePagingDataProvider dataProvider;

	private HistoryDataProvider historyDataProvider;

	public SuggestionEditor(EditorConstants constants, ListFilter filter, String listHeader) {
		this.constants = constants;
		service = GWT.create(EditorService.class);
		initialize(filter, listHeader);
	}
	

	public void setColumns(final List<String> fields) {
		LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

			@Override
			public void onFailure(Throwable caught) {
				setColumns(fields, new TranslationMap());
			}

			private void setColumns(List<String> fields,
					TranslationMap map) {
				for (final String field : fields) {
					final String title = map.get(field) == null ? field : map.get(field);
					final CheckBox box = new CheckBox(title);
					box.getElement().getStyle().setMargin(5, Unit.PX);
					final Cell<LexEntryCellWrapper> cell = new AbstractCell<LexEntryCellWrapper>() {

						@Override
						public void render(
								com.google.gwt.cell.client.Cell.Context context,
								LexEntryCellWrapper wrapper, SafeHtmlBuilder sb) {
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

						com.google.gwt.user.cellview.client.Column<LexEntry, LexEntryCellWrapper> column = new com.google.gwt.user.cellview.client.Column<LexEntry, LexEntryCellWrapper>(cell) {

							@Override
							public LexEntryCellWrapper getValue(LexEntry object) {
								return new LexEntryCellWrapper(object, object.getCurrent());
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
			public void onSuccess(TranslationMap result) {
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
				final ArrayList<String> fields = new ArrayList<String>(description.getEditorFields(true));
				fields.addAll(description.getEditorFields(false));
				fields.add(LemmaVersion.COMMENT);
				LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(TranslationMap translation) {
						showExportDialog(fields, translation);
					}
				});
				
			}

			private void showExportDialog(final ArrayList<String> fields, TranslationMap translation) {
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
						EditorQuery query = listFilter.getQuery();
						query.setCurrent(0);
						service.export(selected, query, new AsyncCallback<String>() {

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

	private void initialize(ListFilter filter, String listHeader) {
		this.listFilter = filter;
		initWidget(uiBinder.createAndBindUi(SuggestionEditor.this));
		export.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				export();
			}

		});
		columns.setRightDropdown(true);
		deleteEntriesGroup.getElement().getStyle().setFloat(Float.RIGHT);
		deleteOldGroup.getElement().getStyle().setFloat(Float.RIGHT);
		dataProvider = new SimplePagingDataProvider(entryList.getTable(), pagination, listFilter, resultSummary);
		entryList.setDataProvider(dataProvider);
		listLegend.setText(listHeader);
		// Enable sorting columns 
		historyDataProvider = new HistoryDataProvider(historyList.getTable());
		historyList.setDataProvider(historyDataProvider);
		entryList.addSelectionChangedHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				historyDataProvider.setEntry(entryList.getSelectedEntry(), fullHistory.isToggled());
			}
		});
		entryList.setFilterOptions(listFilter);
		entryList.setLemmaDescription(AsyncLemmaDescriptionLoader.getDescription());
		historyList.setLemmaDescription(AsyncLemmaDescriptionLoader.getDescription());
		listFilter.setDataProvider(dataProvider);
		fullHistory.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				historyDataProvider.setEntry(entryList.getSelectedEntry(), !fullHistory.isToggled());
			}
		});
		deleteHistory.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final LexEntry entry = entryList.getSelectedEntry();
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
							dataProvider.replace(entry, result);
							historyDataProvider.setEntry(result, true);
							entryList.redraw();
						}
						
					});
				}
			}
		});
		selectAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				entryList.setMultiSelect(true);
				if(!toggleMultiSelect.isToggled()) {
					toggleMultiSelect.setStyleName("active", true);
				}
				MultiSelectionModel<LexEntry> selectionModel = (MultiSelectionModel<LexEntry>) entryList.getSelectionModel();
				List<LexEntry> entries = dataProvider.getData();
				for (LexEntry entry : entries) {
					selectionModel.setSelected(entry, true);
				}
			}
		});
		deselectAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MultiSelectionModel<LexEntry> selectionModel = (MultiSelectionModel<LexEntry>) entryList.getSelectionModel();
				selectionModel.clear();
			}
		});
		toggleMultiSelect.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				boolean isMulti = !toggleMultiSelect.isToggled();
				entryList.setMultiSelect(isMulti);
			}
		});
		rejectAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MultiSelectionModel<LexEntry> selected = (MultiSelectionModel<LexEntry>) entryList.getSelectionModel();
				final Set<LexEntry> entries = selected.getSelectedSet();
				Command ok = new Command() {

					@Override
					public void execute() {
						for (final LexEntry entry : entries) {
							LemmaVersion lemma = entry.getMostRecent();
							if(lemma.getVerification() == Verification.UNVERIFIED) {
								if(lemma.getStatus() == Status.NEW_ENTRY) {
									service.drop(entry, new AsyncCallback<LexEntry>() {

										@Override
										public void onFailure(Throwable caught) {
											Dialog.showError(constants.failedToRejectLemma(), caught);
										}

										@Override
										public void onSuccess(LexEntry result) {
											dataProvider.dropLastQuery();
											dataProvider.refreshQuery();
										}
									});
									
								} else {
									service.reject(entry, lemma, new AsyncCallback<LexEntry>() {

										@Override
										public void onFailure(Throwable caught) {
											Dialog.showError(constants.failedToRejectLemma(), caught);
										}

										@Override
										public void onSuccess(LexEntry result) {
											dataProvider.dropLastQuery();
											dataProvider.refreshQuery();
										}
									});
								}
							}
						}
					}
					
				};
				if(entries.size() > 1) {
					Dialog.confirm(constants.rejectChanges(), messages.rejectChanges(entries.size()), constants.confirmDeleteButton(), constants.cancelDeleteButton(), ok, null, true);
				} else {
					if(entries.size() == 0) {
						Dialog.showInfo(constants.noEntriesSelected(), constants.pleaseSelectOne());
					} else {
						ok.execute();
					}
				}
			}
		});
		listFilter.setPageSize(dataProvider.getPageSize());
		listFilter.setQuery(listFilter.getQuery(), true);
		//listFilter.execQuery(true);
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
	}

	public void setVerifierColumnVisible(boolean visible) {
		entryList.setVerifierColumnVisible(visible);
	}


	public void addDefaultColumn(final String title, final com.google.gwt.user.cellview.client.Column<LexEntry, ? extends ICellWrapper> column, String width) {
		entryList.addColumn(title, column);
		entryList.setColumnWidth(column, width);
		final CheckBox box = new CheckBox(title);
		box.getElement().getStyle().setMargin(5, Unit.PX);
		columns.add(box);
		box.addClickHandler(new ClickHandler() {
			
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
		box.setValue(true);
	}


	public AllEntriesList getEntryList() {
		return entryList;
	}


	public void makeDefaultsSortable() {
		PagingDataGrid<LexEntry> table = entryList.getTable();
		int columnCount = table.getColumnCount();
		for(int i = 0; i < columnCount; i++) {
			if(table.getColumn(i).getDataStoreName() != null) {
				table.getColumn(i).setSortable(true);
			}
		}
		columns.add(new Divider());
	}


	public void setNewButtonVisible(boolean visible) {
		newEntry.setVisible(visible);
	}
	
	public void setRejectAllButtonVisible(boolean visible) {
		toggleSelectGroup.setVisible(visible);
		deleteEntriesGroup.setVisible(visible);
	}


	public ListFilter getListFilter() {
		return listFilter;
	}
	
}

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
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Controls;
import com.github.gwtbootstrap.client.ui.Fieldset;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.HelpInline;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditor;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditorColumn;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditorItem;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayEditorRow;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayOption;
import de.uni_koeln.spinfo.maalr.common.shared.OverlayPresetChooser;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.LemmaEditorWidget;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorMessages;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorServiceAsync;

public class AdvancedEditor {
	

	public static PopupPanel openEditor(AsyncCallback<LexEntry> afterSave,
			EditorConstants constants, EditorMessages messages) {
		return openEditor(null, new LemmaVersion(), afterSave, constants, messages);
	}

	private static EditorServiceAsync service = GWT.create(EditorService.class);
	private static TextBox email;
	private static TextArea comment;
	private static ListBox langAOverlay;
	private static ListBox langBOverlay;

	public static PopupPanel openEditor(final LexEntry entry, final LemmaVersion lv, final AsyncCallback<LexEntry> afterClose, final EditorConstants constants, final EditorMessages messages) {
		
		final PopupPanel popup = new PopupPanel(false, false);
		final AsyncCallback<LexEntry> callback = new AsyncCallback<LexEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				afterClose.onFailure(caught);
			}

			@Override
			public void onSuccess(LexEntry result) {
				popup.hide();
				afterClose.onSuccess(result);
			}
		};
		popup.setStyleName("editor-modal");
		popup.setGlassEnabled(true);
		popup.setGlassStyleName("editor-modal-glass");
		final VerticalPanel content = new VerticalPanel();
		popup.add(content);
		popup.setPopupPosition(50, 50);
		popup.setPixelSize(Window.getClientWidth() - 100, Window.getClientHeight() - 100);
		final LemmaEditorWidget editorWidget = new LemmaEditorWidget(AsyncLemmaDescriptionLoader.getDescription(), UseCase.FIELDS_FOR_ADVANCED_EDITOR, true, 2, true, null);
		editorWidget.setData(lv);
		content.add(editorWidget);
		final boolean withComment = true;
		final boolean withEmail = true;
		Fieldset overlayPanel = new Fieldset();
		overlayPanel.getElement().getStyle().setMarginLeft(10, Unit.PX);
		overlayPanel.getElement().getStyle().setMarginRight(10, Unit.PX);
		overlayPanel.setStyleName("form-horizontal");
		content.add(overlayPanel);
		final ControlGroup left = new ControlGroup();
		final ControlGroup right = new ControlGroup();
		overlayPanel.add(left);
		overlayPanel.add(right);

		LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(final TranslationMap localizedStrings) {

				service.getOverlayTypes(true, new AsyncCallback<ArrayList<String>>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(ArrayList<String> result) {
								if (result.size() == 0) {
									return;
								}
								FlowPanel group = new FlowPanel();
								langAOverlay = new ListBox(false);
								langAOverlay.addItem(constants.none(), (String) null);
								for (final String type : result) {
									langAOverlay.addItem(type, type);
								}
								group.add(langAOverlay);
								final Button edit = new Button(constants.edit());
								langAOverlay.addChangeHandler(new ChangeHandler() {

									@Override
									public void onChange(ChangeEvent event) {
										edit.setEnabled(langAOverlay.getSelectedIndex() != 0);
									}
								});
								
								edit.getElement().getStyle().setMarginLeft(10, Unit.PX);
								edit.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										showOverlayEditor(lv, langAOverlay.getValue(), localizedStrings, constants, messages);
									}
								});
								group.add(edit);
								// left.add(new ControlLabel(messages.controlLabelOverlay(AsyncLemmaDescriptionLoader.getDescription().getLanguageName(true))));
								left.add(new ControlLabel(messages.controlLabelOverlay()));
								Controls controls = new Controls();
								controls.add(group);
								left.add(controls);
								edit.setEnabled(false);
								String overlay = lv.getEntryValue(LemmaVersion.OVERLAY_LANG1);
								for (int i = 0; i < result.size(); i++) {
									if (result.get(i).equals(overlay)) {
										langAOverlay.setItemSelected(i + 1, true);
										edit.setEnabled(true);
										break;
									}
								}
							}
						});
				service.getOverlayTypes(false, new AsyncCallback<ArrayList<String>>() {
					
							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(ArrayList<String> result) {
								if (result.size() == 0) {
									return;
								}
								FlowPanel group = new FlowPanel();
								langBOverlay = new ListBox(false);
								langBOverlay.addItem(constants.none(), (String) null);
								for (final String type : result) {
									langBOverlay.addItem(type, type);
								}
								group.add(langBOverlay);
								final Button edit = new Button(constants.edit());
								langBOverlay.addChangeHandler(new ChangeHandler() {
									
									@Override
									public void onChange(ChangeEvent event) {
										edit.setEnabled(langBOverlay.getSelectedIndex() != 0);
									}
								});
								edit.getElement().getStyle().setMarginLeft(10, Unit.PX);
								edit.addClickHandler(new ClickHandler() {

									@Override
									public void onClick(ClickEvent event) {
										showOverlayEditor(lv, langBOverlay.getValue(), localizedStrings, constants, messages);
									}
								});
								group.add(edit);
//								right.add(new ControlLabel(messages.controlLabelOverlay(AsyncLemmaDescriptionLoader.getDescription().getLanguageName(false))));
								right.add(new ControlLabel(messages.controlLabelOverlay()));
								Controls controls = new Controls();
								controls.add(group);
								right.add(controls);
								edit.setEnabled(false);
								String overlay = lv.getEntryValue(LemmaVersion.OVERLAY_LANG2);
								for (int i = 0; i < result.size(); i++) {
									if (result.get(i).equals(overlay)) {
										langBOverlay.setItemSelected(i + 1, true);
										edit.setEnabled(true);
										break;
									}
								}
							}
						});

				Fieldset set = null;
				if (withComment) {
					set = new Fieldset();
					ControlGroup commentGroup = new ControlGroup();
					commentGroup.add(new ControlLabel(localizedStrings.get("dialog.comment.header")));
					comment = new TextArea();
					comment.getElement().getStyle().setWidth(100, Unit.PCT);
					comment.getElement().getStyle().setProperty("resize", "none");
					comment.setVisibleLines(8);
					commentGroup.add(comment);
					set.add(commentGroup);
					comment.setText(lv.getEntryValue(LemmaVersion.COMMENT));
				}
				if (withEmail) {
					if (set == null) {
						set = new Fieldset();
					}
					ControlGroup group = new ControlGroup();
					group.add(new ControlLabel(localizedStrings.get("dialog.email.header")));
					HorizontalPanel hp = new HorizontalPanel();
					group.add(hp);
					email = new TextBox();
					email.getElement().getStyle().setWidth(350, Unit.PX);
					email.setText(lv.getEntryValue(LemmaVersion.EMAIL));
					hp.add(email);

					Button sendButton = new Button(constants.reply());
					sendButton.getElement().getStyle().setMarginLeft(10, Unit.PX);
					sendButton.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onSuccess(LemmaDescription ld) {
									String firstLanguageId = ld.getLanguage(true).getId();
									String secondLanguageId = ld.getLanguage(false).getId();
									String first = localizedStrings.get(firstLanguageId);
									String second = localizedStrings.get(secondLanguageId);
									String lemma1st = ld.toUnescapedString(lv, UseCase.RESULT_LIST, true);
									String lemma2nd = ld.toUnescapedString(lv, UseCase.RESULT_LIST, false);
									String german = first + "%20%3D%20" + lemma1st;
									String surmiran = second + "%20%3D%20" + lemma2nd;
									String remartg = localizedStrings.get("mail.comment") + "%0A" + URL.encode(comment.getText());
									
									String subject = "?subject=" + URL.encode(localizedStrings.get("mail.subject"));
									String body = "&body=" + german + "%0A"+ surmiran + "%0A%0A" + remartg;
									String cc = "";
									if(localizedStrings.get("mail.cc") != null) {
										cc = "&cc=" + localizedStrings.get("mail.cc");	
									}
									Window.Location.assign("mailto:" + email.getText() + subject + cc + body);
								}
							});
							
						}
					});
					hp.add(sendButton);
					set.add(group);

				}
				if (set != null) {
					set.getElement().getStyle().setPaddingLeft(15, Unit.PC);
					set.getElement().getStyle().setPaddingRight(15, Unit.PC);
					HTML html = new HTML(new SafeHtmlBuilder().appendHtmlConstant("<hr/>").toSafeHtml());
					html.getElement().getStyle().setMarginLeft(20, Unit.PX);
					html.getElement().getStyle().setMarginRight(20, Unit.PX);
					content.add(html);
					content.add(set);
				}
			}
		});

		Button cancel = new Button(constants.cancel());
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
		Button reset = new Button(constants.reset());
		reset.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editorWidget.setData(lv);
			}
		});
		Button ok = new Button(constants.ok());
		ok.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (entry == null) {
					LemmaVersion fromEditor = new LemmaVersion();
					fromEditor.getEntryValues().putAll(lv.getEntryValues());
					fromEditor.getMaalrValues().putAll(lv.getMaalrValues());
					transferSpecialValues(lv, fromEditor);
					editorWidget.updateFromEditor(fromEditor);
					if(editorWidget.isValid(true, true)) {
						LexEntry entry = new LexEntry(fromEditor);
						service.insert(entry, callback);
					}
				} else {
					LemmaVersion fromEditor = new LemmaVersion();
					fromEditor.getEntryValues().putAll(lv.getEntryValues());
					fromEditor.getMaalrValues().putAll(lv.getMaalrValues());
					transferSpecialValues(lv, fromEditor);
					editorWidget.updateFromEditor(fromEditor);
					if(editorWidget.isValid(true, true)) {
						service.acceptAfterUpdate(entry, lv, fromEditor, callback);
					}
				}
				editorWidget.updateFromEditor(lv);
			}

			private void transferSpecialValues(final LemmaVersion lv, LemmaVersion fromEditor) {
				
				if (langAOverlay != null) {
					int index = langAOverlay.getSelectedIndex();
					String value = index <= 0 ? null : langAOverlay.getValue(index);
					fromEditor.setValue(LemmaVersion.OVERLAY_LANG1, value);
					lv.setValue(LemmaVersion.OVERLAY_LANG1, value);
				}
				if (langBOverlay != null) {
					int index = langBOverlay.getSelectedIndex();
					String value = index <= 0 ? null : langBOverlay.getValue(index);
					fromEditor.setValue(LemmaVersion.OVERLAY_LANG2, value);
					lv.setValue(LemmaVersion.OVERLAY_LANG2, value);
				}
				if (email != null) {
					fromEditor.setValue(LemmaVersion.EMAIL, email.getText());
					lv.setValue(LemmaVersion.EMAIL, email.getText());
				}
				if (comment != null) {
					fromEditor.setValue(LemmaVersion.COMMENT, comment.getText());
					lv.setValue(LemmaVersion.COMMENT, comment.getText());
				}
			}
		});
		ok.setType(ButtonType.PRIMARY);
		content.add(new ModalFooter(cancel, reset, ok));
		popup.show();
		final HandlerRegistration handler = Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				popup.setSize(Window.getClientWidth() - 100 + "px", Window.getClientHeight() - 100 + "px");
			}
		});
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				handler.removeHandler();
			}
		});
		return popup;
	}

	/**
	 * The verb-generator-dialog. Opens when you hit the edit button on the
	 * tudestg/rumantsch overlay for editing a verb.
	 */
	private static void showOverlayEditor(final LemmaVersion lv, final String type, final TranslationMap localizedStrings, final EditorConstants constants, final EditorMessages messages) {
		
		service.getOverlayEditor(type, new AsyncCallback<OverlayEditor>() {

			@Override
			public void onFailure(Throwable caught) {
				Dialog.showError(messages.failedToGetOverlay(type), caught);
			}

			private Map<String, String> presetValues = new HashMap<String, String>();

			@Override
			public void onSuccess(OverlayEditor result) {
				
//				List<OverlayEditorRow> rows2 = result.getRows();
//				for (OverlayEditorRow overlayEditorRow : rows2) {
//					List<OverlayEditorColumn> columns = overlayEditorRow.getColumns();
//					for (OverlayEditorColumn overlayEditorColumn : columns) {
//						List<OverlayEditorItem> items = overlayEditorColumn.getItems();
//						for (OverlayEditorItem overlayEditorItem : items) {
//							String id = overlayEditorItem.getId();
//							SimpleWebLogger.log(AdvancedEditor.class, "id=" + id);
//						}
//						
//					}
//				}
				
				final PopupPanel modal = new PopupPanel();
				modal.setStyleName("editor-modal");
				modal.setPopupPosition(50, 50);
				modal.setPixelSize(Window.getClientWidth() - 100, Window.getClientHeight() - 100);
				List<OverlayEditorRow> rows = result.getRows();
				VerticalPanel content = new VerticalPanel();
				FluidContainer container = new FluidContainer();
				content.add(container);
				modal.add(content);
				final Map<String, TextBox> fields = new HashMap<String, TextBox>();
				OverlayPresetChooser presetChooser = result.getPresetChooser();
				if (presetChooser != null) {
					FluidRow row = new FluidRow();
					container.add(row);
					Column column = new Column(12);
					row.add(column);
					String base = presetChooser.getBase();
					VerticalPanel vp = new VerticalPanel();
					vp.setWidth("100%");
					Legend legend = new Legend(constants.generator());
					vp.add(legend);
					HorizontalPanel panel = new HorizontalPanel();
					vp.add(panel);
					column.add(vp);
					final TextBox field = new TextBox();
					if (base != null) {
						Fieldset set = new Fieldset();
						set.setStyleName("form-horizontal");
						ControlGroup group = new ControlGroup();
						group.add(new ControlLabel(getLabel(base, localizedStrings)));
						Controls controls = new Controls();
						field.setWidth("180px");
						field.setValue(lv.getEntryValue(base));
						presetValues.put(base, lv.getEntryValue(base));
						controls.add(field);
						group.add(controls);
						set.add(group);
						panel.add(set);
					}
					final String id = presetChooser.getId();
					final List<OverlayOption> options = presetChooser.getOptions();
					final ListBox list = new ListBox(false);
					final ControlGroup group = new ControlGroup();
					final HelpInline listTypeFeedback = new HelpInline();
					if (options != null) {
						Fieldset set = new Fieldset();
						set.setStyleName("form-horizontal");
						group.add(new ControlLabel(getLabel(id, localizedStrings)));
						final Controls controls = new Controls();
						list.addItem(constants.none(), (String) null);
						int selected = -1;
						for (int i = 0; i < options.size(); i++) {
							OverlayOption option = options.get(i);
							list.addItem(option.getValue());
							if (option.getValue().equals(lv.getEntryValue(id))) {
								selected = i;
							}
						}
						if (selected != -1) {
							list.setSelectedIndex(selected + 1);
						}
						controls.add(list);
						group.add(controls);
						set.add(group);
						panel.add(set);
					}
					Button apply = new Button(constants.generate()); 
					apply.getElement().getStyle().setMarginLeft(10, Unit.PX);
					panel.add(apply);
					panel.add(listTypeFeedback);
					apply.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							final String base = field.getValue().trim().length() == 0 ? null : field.getValue();
							int index = list.getSelectedIndex();

							final String option = (index <= 0 ? null : list.getValue(index));
							service.getOverlayEditorPreset(type, option, base, new AsyncCallback<HashMap<String, String>>() {

										@Override
										public void onFailure(Throwable caught) {
											// FIXME: There seems to be a bug in gwt-bootstrap: Under some
											// circumstances(multiple modal dialogs), the clickhandler will not be called.
											// Dialog.showError("Failed to get preset",caught);
											if (group != null) {
												listTypeFeedback.setText(messages.selectType(options.size()));
												group.setType(ControlGroupType.ERROR);
											}
										}

										@Override
										public void onSuccess(HashMap<String, String> result) {
											listTypeFeedback.setText("");
											group.setType(ControlGroupType.NONE);
											presetValues.put(base, lv.getEntryValue(base));
											presetValues.put(id, option);
											Set<Entry<String, String>> entries = result.entrySet();
											for (Entry<String, String> entry : entries) {
												TextBox box = fields.get(entry.getKey());
												if (box != null) {
													box.setValue(entry.getValue());
												} else {
													Logger.getLogger(getClass()).warn("No TextBox found for key " + entry.getKey());
												}
											}
										}
									});
						}
					});

				}
				for (OverlayEditorRow row : rows) {
					FluidRow uiRow = new FluidRow();
					container.add(uiRow);
					List<OverlayEditorColumn> columns = row.getColumns();
					int size = (int) Math.max(12D / columns.size(), 1);
					int rest = Math.max(12 - columns.size() * size, 0);
					for (int i = 0; i < columns.size(); i++) {
						OverlayEditorColumn column = columns.get(i);
						Column uiColumn = new Column(size + (i == columns.size() - 1 ? rest : 0));
						uiRow.add(uiColumn);
						uiColumn.add(new Legend(getLabel(column.getId(), localizedStrings)));
						List<OverlayEditorItem> items = column.getItems();
						if (items == null)
							continue;
						for (OverlayEditorItem item : items) {
							Fieldset set = new Fieldset();
							set.setStyleName("form-horizontal");
							ControlGroup group = new ControlGroup();
							group.add(new ControlLabel(getLabel(item.getId(), localizedStrings)));
							Controls controls = new Controls();
							TextBox field = new TextBox();
							field.setWidth("180px");
							field.setValue(lv.getEntryValue(item.getId()));
							fields.put(item.getId(), field);
							controls.add(field);
							group.add(controls);
							set.add(group);
							uiColumn.add(set);
						}
					}
				}
				Button cancel = new Button(constants.cancel());
				cancel.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						modal.hide();
					}
				});
				Button reset = new Button(constants.reset());
				reset.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Set<Entry<String, TextBox>> entries = fields.entrySet();
						for (Entry<String, TextBox> entry : entries) {
							entry.getValue().setValue(
									lv.getEntryValue(entry.getKey()));
						}
						// FIXME: Reset generator fields
					}
				});
				Button ok = new Button(constants.ok());
				ok.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						Set<Entry<String, TextBox>> entries = fields.entrySet();
						for (Entry<String, TextBox> entry : entries) {
							lv.setValue(entry.getKey(), entry.getValue()
									.getValue());
						}
						Set<Entry<String, String>> chooserValues = presetValues.entrySet();
						for (Entry<String, String> chooserValue : chooserValues) {
							lv.setValue(chooserValue.getKey(),
									chooserValue.getValue());
						}
						modal.hide();
					}
				});
				ok.setType(ButtonType.PRIMARY);
				content.add(new ModalFooter(cancel, reset, ok));
				modal.show();
			}

			private String getLabel(String key,
					TranslationMap localizedStrings) {
				return localizedStrings.get(key);
			}
		});
	}
	

}

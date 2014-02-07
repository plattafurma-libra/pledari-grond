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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.entry;

import java.util.Map;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.services.user.shared.LexService;
import de.uni_koeln.spinfo.maalr.services.user.shared.LexServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.LemmaEditorWidget;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;


public class LemmaEditor {
	
	private static LexServiceAsync lexService = GWT.create(LexService.class);
	
	private static Map<String, String> translation = null;
	
	public static void openEditor() {
		LocalizedStrings.afterLoad(new AsyncCallback<Map<String,String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				translation = result;
				internalOpenEditor(new LemmaVersion(), translation.get("suggest.title"), translation.get("suggest.subtext"), false);
			}
		});
	}
	

	public static void openEditor(final LemmaVersion toModify) {
		LocalizedStrings.afterLoad(new AsyncCallback<Map<String,String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				translation = result;
				LemmaEditorWidget editor = internalOpenEditor(toModify, translation.get("modify.title"), translation.get("modify.subtext"), true);
				editor.setData(toModify);
			}
		});
	}

	private static LemmaEditorWidget internalOpenEditor(final LemmaVersion lemma, String title, String subTitle, final boolean modify) {
		final LemmaEditorWidget editor = new LemmaEditorWidget(AsyncLemmaDescriptionLoader.getDescription(), UseCase.FIELDS_FOR_SIMPLE_EDITOR, false, 1, false, null);
		final Modal popup = new Modal(true);
		popup.setBackdrop(BackdropType.STATIC);
		popup.setCloseVisible(true);
		final Button reset = new Button(translation.get("button.clear"));
		final Button cancel = new Button(translation.get("button.cancel"));
		final Button ok = new Button(translation.get("button.ok"));
		String description = null;
		if(modify) {
//			popup.setTitle(translation.get("header.modify"));
			description = translation.get("description.modify");
		} else {
//			popup.setTitle(translation.get("header.suggest"));
			description = translation.get("description.suggest");
		}
		final PopupEditor popupEditor = new PopupEditor(title, subTitle, description, editor, translation, true, true);
		ModalFooter footer = new ModalFooter(cancel, reset, ok);
		reset.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				editor.clearData();
				popupEditor.reset();
			}
		});
		cancel.addClickHandler( new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
		ok.setType(ButtonType.PRIMARY);
		ok.addClickHandler(new ClickHandler() {
			
			boolean clicked = false;
			
			@Override
			public void onClick(ClickEvent event) {
				if(clicked) return;
				if(!popupEditor.isValid()) {
					return;
				}
				clicked = true;
				popupEditor.updateFromEditor(lemma);
				cancel.setEnabled(false);
				reset.setEnabled(false);
				ok.setText(translation.get("dialog.saving"));
				ok.setType(ButtonType.INVERSE);
				AsyncCallback<String> callback = new AsyncCallback<String>() {
					
					@Override
					public void onFailure(Throwable caught) {
						cancel.setEnabled(true);
						reset.setEnabled(true);
						ok.setText(translation.get("button.ok"));
						ok.setType(ButtonType.DANGER);
						clicked = false;
						Dialog.showError(translation.get("dialog.failure"), translation.get(caught.getMessage())); 
//						Dialog.showError(translation.get("dialog.failure"), caught); // exception from SpringBackend
					}

					@Override
					public void onSuccess(String result) {
						ok.setText(translation.get("dialog.success"));
						ok.setType(ButtonType.SUCCESS);
						Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
							
							@Override
							public boolean execute() {
								popup.hide();
								return false;
							}
						}, 800);
						
					}
					
				};
				if(modify) {
					lexService.suggestModification(lemma, callback);
				} else {
					lexService.suggestNewEntry(lemma, callback);
				}
			}
		});
		popup.add(popupEditor);
		popup.add(footer);
		int customWidth = 850;
		popup.setWidth(customWidth + "px");
		popup.show();
		double customMargin = -1*(customWidth/2);
		popup.getElement().getStyle().setMarginLeft(customMargin, Unit.PX);
		popup.getElement().getStyle().setMarginRight(customMargin, Unit.PX);
		return editor;
	}




}

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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.order;

import java.util.ArrayList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorServiceAsync;

public class OrderPopup {
	
	private static EditorServiceAsync service = GWT.create(EditorService.class);
	
	public static void showPopup(final String lemma, final boolean firstLanguage, final EditorConstants constants) {
		service.getOrder(lemma, firstLanguage, new AsyncCallback<ArrayList<LemmaVersion>>() {

			@Override
			public void onFailure(Throwable caught) {
				Dialog.showError("Failed to get ordered entries", caught);
			}

			@Override
			public void onSuccess(ArrayList<LemmaVersion> result) {
				final Modal modal = new Modal();
				modal.setAnimation(true);
				modal.setBackdrop(BackdropType.STATIC);
				modal.setCloseVisible(true);
				modal.setTitle(constants.orderTranslations()+ "'" + lemma + "'");
//				modal.setTitle("Order translations of '" + lemma + "'");
				final OrderWidget orderWidget = new OrderWidget();
//				orderWidget.setSubtext("Translations of '" + lemma + "'");
				orderWidget.setOrderedItems(result);
				modal.add(orderWidget);
				Button ok = new Button(constants.ok());
//				Button ok = new Button("OK");
				ok.setType(ButtonType.PRIMARY);
				ok.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						ArrayList<LemmaVersion> ordered = orderWidget.getOrderedItems();
						service.updateOrder(firstLanguage, ordered, new AsyncCallback<List<LexEntry>>() {

							@Override
							public void onFailure(Throwable caught) {
								Dialog.showError("Failed to update order", caught);
							}

							@Override
							public void onSuccess(List<LexEntry> result) {
								modal.hide();
							}
						});
					}
				});
				Button cancel = new Button(constants.cancel());
//				Button cancel = new Button("Cancel");
				cancel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						modal.hide();
					}
				});
				ModalFooter footer = new ModalFooter(cancel, ok);
				modal.add(footer);
				modal.setAnimation(true);
				modal.setBackdrop(BackdropType.STATIC);
				int customWidth = 850;
				modal.setWidth(customWidth + "px");
				modal.show();
				double customMargin = -1*(customWidth/2);
				modal.getElement().getStyle().setMarginLeft(customMargin, Unit.PX);
				modal.getElement().getStyle().setMarginRight(customMargin, Unit.PX);
				modal.getElement().getStyle().setMarginTop(10, Unit.PX);
			}
		});
	}

}

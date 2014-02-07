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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.Overlay;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchService;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchServiceAsync;

public class OverlayPopup {
	
	private static Map<String, Overlay> cache = new HashMap<String, Overlay>();
	
	private static SearchServiceAsync service = GWT.create(SearchService.class);

	public static void show(final LemmaVersion lemmaVersion, String overlayField, final String closeButton) {
		final String overlayType = lemmaVersion.getEntryValue(overlayField);
		Overlay overlay = cache.get(overlayType);
		if(overlay == null) {
			service.getOverlay(overlayType, new AsyncCallback<Overlay>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Overlay result) {
					if(result == null) return;
					cache.put(overlayType, result);
					createOverlay(result, lemmaVersion, closeButton);
				}
			});
		} else {
			createOverlay(overlay, lemmaVersion, closeButton);
		}
	}
	/*
	 * Overlay displayed for verb conjugations
	 */
	private static void createOverlay(Overlay overlay, LemmaVersion lemmaVersion, String closeButton) {
		String form = overlay.getForm();
		HashSet<String> values = new HashSet<String>();
		RegExp regExp = RegExp.compile("(\\$\\{(.*)\\})", "g");
	    for (MatchResult matcher = regExp.exec(form); matcher != null; matcher = regExp.exec(form)) {
	       values.add(matcher.getGroup(2));
	    }
		for (String key : values) {
			String value = lemmaVersion.getEntryValue(key);
			if(value == null || value.trim().length() == 0) {
				form = form.replaceAll("\\$\\{"+key+"\\}", "");
			} else {
				SafeHtmlBuilder builder = new SafeHtmlBuilder();
				builder.appendEscaped(value);
				form = form.replaceAll("\\$\\{"+key+"\\}", builder.toSafeHtml().asString());
			}
		}
		SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant(form);
		final Modal popup = new Modal(true);
		popup.add(new HTML(builder.toSafeHtml()));
		popup.setBackdrop(BackdropType.NORMAL);
		popup.setCloseVisible(true);
		popup.setWidth(1000);
		
		final Button close = new Button(closeButton);
		close.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
			}
		});
		ModalFooter footer = new ModalFooter(close);
		popup.add(footer);
		popup.show();
	}


}

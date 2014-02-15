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

import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LemmaVersionCellWrapper;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.order.OrderPopup;


public class EntryOrderCell extends AbstractCell<LemmaVersionCellWrapper> {
	
	private static EntryListUiRenderer renderer = GWT.create(EntryListUiRenderer.class);
	
	private String lemmaAKey;

	private String lemmaBKey;
	
	interface EntryListUiRenderer extends UiRenderer {

		/*
		 * Responsible for rendering the cell as defined in the 'ui.xml'
		 * Remember that the parameter names must be identical here and in the ui.xml-file!
		 * See https://developers.google.com/web-toolkit/doc/latest/DevGuideUiBinder#Rendering_HTML_for_Cells
		 */
		void render(SafeHtmlBuilder sb, LemmaVersionCellWrapper wrapper);

		void onBrowserEvent(EntryOrderCell cell, NativeEvent event, Element element, LemmaVersionCellWrapper wrapper);
	}

	private EditorConstants constants;

	public EntryOrderCell(EditorConstants constants) {
		super("click");
		lemmaAKey = AsyncLemmaDescriptionLoader.getDescription().getResultList(true).get(0).getKey();
		lemmaBKey = AsyncLemmaDescriptionLoader.getDescription().getResultList(false).get(0).getKey();
		this.constants = constants;
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
	

	@UiHandler({"orderA"})
	void onOrderA(ClickEvent event, Element parent, final LemmaVersionCellWrapper wrapper) {
		String lemma = wrapper.getLemmaVersion().getEntryValue(lemmaAKey);
		OrderPopup.showPopup(lemma, true, constants);
		event.getNativeEvent().preventDefault();
		event.getNativeEvent().stopPropagation();
	}
	
	@UiHandler({"orderB"})
	void onOrderB(ClickEvent event, Element parent, final LemmaVersionCellWrapper wrapper) {
		String lemma = wrapper.getLemmaVersion().getEntryValue(lemmaBKey);
		OrderPopup.showPopup(lemma, false, constants);
		event.getNativeEvent().preventDefault();
		event.getNativeEvent().stopPropagation();
	}

	
	
}

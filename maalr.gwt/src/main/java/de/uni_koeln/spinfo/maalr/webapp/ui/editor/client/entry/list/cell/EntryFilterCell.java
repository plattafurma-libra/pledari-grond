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

import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.filter.ListFilter;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LexEntryCellWrapper;


public class EntryFilterCell extends AbstractCell<LexEntryCellWrapper> {
	
	private static EntryListUiRenderer renderer = GWT.create(EntryListUiRenderer.class);
	
	interface EntryListUiRenderer extends UiRenderer {

		/*
		 * Responsible for rendering the cell as defined in the 'ui.xml'
		 * Remember that the parameter names must be identical here and in the ui.xml-file!
		 * See https://developers.google.com/web-toolkit/doc/latest/DevGuideUiBinder#Rendering_HTML_for_Cells
		 */
		void render(SafeHtmlBuilder sb, LexEntryCellWrapper wrapper);

		void onBrowserEvent(EntryFilterCell cell, NativeEvent event, Element element, LexEntryCellWrapper wrapper);
	}


	private ListFilter filter;
	

	public EntryFilterCell(ListFilter filter) {
		super("click");
		this.filter = filter;
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
		renderer.onBrowserEvent(this, event, parent, user);
	}
	

	@UiHandler({"filterUser"})
	void onToggleFilter(ClickEvent event, Element parent, LexEntryCellWrapper wrapper) {
	  filter.toggleUser(wrapper.getLexEntry().getMostRecent());
	}
	
	@UiHandler({"filterVerifier"})
	void onVerifierFilter(ClickEvent event, Element parent, LexEntryCellWrapper wrapper) {
	  filter.toggleVerifier(wrapper.getLexEntry().getMostRecent());
	}

}

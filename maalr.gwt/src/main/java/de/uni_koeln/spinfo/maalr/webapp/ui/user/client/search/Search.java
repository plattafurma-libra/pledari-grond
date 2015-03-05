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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.NavPills;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.constants.Device;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.ConfigurableSearchArea;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.IResultDisplay;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.PagerEvent;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.PagerHandler;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.SearchEvent;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.SearchHandler;
import de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search.celltable.ResultCellTable;

public class Search extends Composite implements HasHandlers, IResultDisplay {

	private static SearchInterface uiBinder = GWT.create(SearchInterface.class);

	private HandlerManager handlerManager = new HandlerManager(this);

	interface SearchInterface extends UiBinder<Widget, Search> {
	}

	@UiField
	Well resultColumn;

	@UiField
	Well well;
	
	ResultCellTable resultCellTable;

	private ConfigurableSearchArea searchForm;

	private Dictionary localeDictionary;

	public void updateUI(MaalrQuery maalrQuery) {
		searchForm.setQuery(maalrQuery);
	}

	public Search() {
		initWidget(uiBinder.createAndBindUi(this));
		searchForm = new ConfigurableSearchArea(this, false, true, null);
		localeDictionary = DictionaryConstants.getLocaleDictionary();
		well.add(searchForm);
		// TODO: Disabled in sutsilvan edition 
		// well.add(getLink(DictionaryConstants.DICT_LINKS));
		// well.add(getLink(DictionaryConstants.GLOSSAR_LINKS));
	}

	private Widget getLink(final List<String> links) {
		final Anchor anchor = new Anchor(new SafeHtml() {

			private static final long serialVersionUID = -8025097762092729852L;

			@Override
			public String asString() {
				return "<span>" + localeDictionary.get(links.get(0)) + "</span>";
			}
		});

		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				new ExternalLinkDialog(links);

				event.getNativeEvent().preventDefault();
				event.getNativeEvent().stopPropagation();
			}
		});
		anchor.getElement().setId("dictionary_links");
		return anchor;
	}

	public void addSearchHandler(SearchHandler searchHandler) {
		handlerManager.addHandler(SearchEvent.TYPE, searchHandler);
	}

	public void addPagerHandler(PagerHandler pagerHandler) {
		handlerManager.addHandler(PagerEvent.TYPE, pagerHandler);
	}

	public void setResultCellTable(ResultCellTable resultCellTable) {
		this.resultCellTable = resultCellTable;
		resultColumn.add(resultCellTable);
		resultColumn.setVisible(false);
		String className = DOM.getElementById("content").getClassName();
		DOM.getElementById("content").setClassName(
				className + " search-centered");
		setMargin(75);
		
	}

	@Override
	public void updateResult(MaalrQuery query, QueryResult result) {
		if (query.getQueryMap().isEmpty()) {
			resultColumn.setVisible(false);
			String className = DOM.getElementById("content").getClassName();
			DOM.getElementById("content").setClassName(
					className + " search-centered");
			setMargin(0);
		} else {
			this.resultCellTable.setResults(query, result);
			DOM.getElementById("content").removeClassName("search-centered");
			resultColumn.setVisible(true);
			setMargin(75);
		}

	}

	private void setMargin(int margin) {
//		if(Window.getClientWidth() < 767) 
//			this.getElement().getStyle().setMarginTop(0, Unit.PX);
//		else
			this.getElement().getStyle().setMarginTop(margin, Unit.PX);
	}

	public void setFocus(boolean selectAll) {
		searchForm.setFocus(selectAll);
	}

}

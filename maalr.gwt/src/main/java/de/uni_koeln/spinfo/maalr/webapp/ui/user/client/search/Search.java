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

import com.github.gwtbootstrap.client.ui.Well;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.lucene.query.LightQueryResult;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQueryFormatter;
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

	public void updateUI(MaalrQuery maalrQuery) {
		searchForm.setQuery(maalrQuery);
	}

	public Search() {
		initWidget(uiBinder.createAndBindUi(this));
		searchForm = new ConfigurableSearchArea(this, false, true, null);
		well.add(searchForm);
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
		DOM.getElementById("content").setClassName(className + " search-centered");
	}

	@Override
	public void updateResult(MaalrQuery query, LightQueryResult result) {
		if(MaalrQueryFormatter.getQueryLabel(query) == null) {
			resultColumn.setVisible(false);
			String className = DOM.getElementById("content").getClassName();
			DOM.getElementById("content").setClassName(className + " search-centered");
		} else {
			this.resultCellTable.setResults(query, result);
			DOM.getElementById("content").removeClassName("search-centered");
			resultColumn.setVisible(true);
		}

	}

	public void setFocus(boolean selectAll) {
		searchForm.setFocus(selectAll);
	}

}

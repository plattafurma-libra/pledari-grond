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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.client;

import java.util.Map;

import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Pagination;
import com.github.gwtbootstrap.client.ui.resources.Bootstrap;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;

import de.uni_koeln.spinfo.maalr.lucene.query.LightQueryResult;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;

public class CustomPager extends SimplePanel {

	private Pagination pagination;

	private int left = 0;

	private int right = 0;

	private String historyPrefix;

	public CustomPager() {
		pagination = new Pagination();
		pagination.setStyle(Bootstrap.Pagination.CENTERED);
		this.add(pagination);
	}

	public void createPageLinks(final MaalrQuery maalrQuery, LightQueryResult result) {
		if (pagination.getWidgetCount() > 0)
			pagination.clear();

		final int current = maalrQuery.getPageNr();
		final int maxPageNumber = getMaxPageNumber(maalrQuery, result);

		if (!(result.getMaxEntries() <= maalrQuery.getPageSize())) {

			left = Math.max(0,current-2);
			right = Math.min(maxPageNumber, current+3);
//			left = (current - 2) < 0 ? 0 : current - 2;
//			right = (left + 5) > maxPageNumber ? maxPageNumber : left
//					+ (left == 0 ? 5 : 4);
//			left = (right - 5) < 0 ? 0 : (right - 5);
			LocalizedStrings.afterLoad(new AsyncCallback<Map<String,String>>() {
				
				@Override
				public void onSuccess(Map<String, String> result) {
					String first = result.get("maalr.query.results_first_page");
					String last = result.get("maalr.query.results_last_page");
					addFirstPageLink(maalrQuery, current, first);
					addPrevPageLink(maalrQuery, current);
					addNumberedLinks(maalrQuery, current, left, right);
					addNextPageLink(maalrQuery, current, maxPageNumber);
					addLastPageLink(maalrQuery, current, maxPageNumber, last);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
			
		}
	}

	private int getMaxPageNumber(MaalrQuery query, LightQueryResult result) {
		return result.getMaxEntries() / query.getPageSize()
				+ (result.getMaxEntries() % query.getPageSize() != 0 ? 1 : 0);
	}

	private void addNumberedLinks(MaalrQuery query, int current, int left,
			int right) {
		for (int i = left; i < right; i++) {
			pagination.add(createLink(query, i, (i + 1) + "", (i == current)));
		}
	}

	private void addNextPageLink(MaalrQuery query, int current, int maxPageNumber) {
		NavLink link = createLink(query, (current + 1), null, false);
		if (current == maxPageNumber - 1)
			link.setDisabled(true);
		//link.setIcon(IconType.CHEVRON_RIGHT);
		link.getAnchor().getElement().setInnerHTML("<i class=\"icon-chevron-right\"></i>&nbsp;");
		pagination.add(link);
	}

	private void addPrevPageLink(MaalrQuery query, int current) {
		NavLink link = createLink(query, (current - 1), null, false);
		if (current == 0)
			link.setDisabled(true);
		link.getAnchor().getElement().setInnerHTML("<i class=\"icon-chevron-left\"></i>&nbsp;");
		pagination.add(link);
	}

	private void addLastPageLink(final MaalrQuery query, int current,
			final int maxPageNumber, String label) {
		NavLink link = createLink(query, maxPageNumber - 1, label, false);
		if (current == maxPageNumber - 1)
			link.setDisabled(true);
		pagination.add(link);
	}

	private void addFirstPageLink(final MaalrQuery query, int current, String label) {
		NavLink link = createLink(query, 0, label, false);
		if (current == 0)
			link.setDisabled(true);
		pagination.add(link);
	}

	private void addClickHandler(final MaalrQuery query, final int pageNumber,
			NavLink link) {
		link.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				query.setPageNr(pageNumber);
				String url = query.toURL();
				if(historyPrefix != null) {
					url = historyPrefix + url;
				}
				History.newItem(url);
				event.getNativeEvent().preventDefault();
			}
		});
	}

	private NavLink createLink(final MaalrQuery query, final int pageNumber,
			String displayedName, boolean isCurrent) {
		NavLink link = displayedName != null ? new NavLink(displayedName)
				: new NavLink();
		addClickHandler(query, pageNumber, link);
		if (isCurrent) {
			link.setActive(true);
		}
		return link;
	}

	public void setHistoryPrefix(String prefix) {
		this.historyPrefix = prefix;
	}


}

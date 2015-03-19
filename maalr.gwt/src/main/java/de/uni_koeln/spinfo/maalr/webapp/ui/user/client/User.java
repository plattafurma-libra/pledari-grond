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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;

import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchService;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.HiJax;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.SearchHelper;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.PagerEvent;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.PagerHandler;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.SearchEvent;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.SearchHandler;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;
import de.uni_koeln.spinfo.maalr.webapp.ui.user.client.entry.LemmaEditor;
import de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search.Search;
import de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search.celltable.ResultCellTable;

public class User implements EntryPoint {

	private Logger logger = Logger.getLogger(getClass());

	private SearchServiceAsync service;

	private ResultCellTable resultCellTable;

	private Search search;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		this.service = GWT.create(SearchService.class);
		search = new Search();
		resultCellTable = new ResultCellTable();
		AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(LemmaDescription result) {
				HiJax.hijackAnchor("propose_navi", new Command() {

					@Override
					public void execute() {
						openEditor();
					}
				});
				try {
					initializeMainPanel();
				} catch (Exception e) {
					logger.error("Error!", e);
					Window.alert("Please check log file: " + e);
				}
			}
		});
		Element element = DOM.getElementById("languages-widget");
		if(element != null) {
			updateLanguageLinks(element);
		}
	}

	private void updateLanguageLinks(Element element) {
		String attribute = null;
		try {
			attribute = element.getAttribute("class");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(attribute != null && attribute.contains("lang_select")) {
			final Anchor anchor = Anchor.wrap(element);
			final String langParam = anchor.getHref().substring(anchor.getHref().lastIndexOf('?'));
			HiJax.hijackAnchor(anchor, new Command() {
				
				@Override
				public void execute() {
					String url = Window.Location.getPath() + langParam + "#"+History.getToken();
					Window.Location.assign(url);
				}
			});
		} else {
			for(int i = 0; i < element.getChildCount(); i++) {
				Node child = element.getChild(i);
				Element e = child.cast();
				updateLanguageLinks(e);
			}
		}
	}

	private void initializeMainPanel() {
		
		Element noScriptDiv = DOM.getElementById("nojs_searchcontainer");
		if(noScriptDiv != null)
			noScriptDiv.removeFromParent();
	
		search.setResultCellTable(resultCellTable);
		search.addSearchHandler(new SearchHandler() {

			@Override
			public void onSearch(final SearchEvent event) {

				AsyncLemmaDescriptionLoader
						.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
							}

							@Override
							public void onSuccess(LemmaDescription result) {
								// TODO Auto-generated method stub
							}
						});
			}

		});
		search.addPagerHandler(new PagerHandler() {

			@Override
			public void onSizeChanged(PagerEvent event) {
				resultCellTable.doPageSizeChanged(event.getSize());
			}

		});
		
		// Insert search widget into div#content 
		RootPanel contentPanel = RootPanel.get("content");
		if(contentPanel != null) {
			DictLinksDropDown dictLinksDropDown = new DictLinksDropDown();
			RootPanel.get("navi_head").add(dictLinksDropDown);
			contentPanel.add(search);
		}


		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				MaalrQuery maalrQuery = MaalrQuery.parse(historyToken);
				search.updateUI(maalrQuery);
				doSearch(maalrQuery);
			}
		});

		if (History.getToken() != null) {
			//Logger.getLogger(getClass()).info("History.getToken(): " + History.getToken());
			History.fireCurrentHistoryState();
		}
		
		Element anchorToOtherDicts = DOM.getElementById("links_ulteriurs");
		Anchor wrapper = Anchor.wrap(anchorToOtherDicts);
		wrapper.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new ExternalLinkDialog(DictionaryConstants.DICT_LINKS_EXTERNAL, 
						DictionaryConstants.getExtLinksDictionary());
				event.getNativeEvent().preventDefault();
				event.getNativeEvent().stopPropagation();
			}
		});
		
		search.setFocus(true);
	}

	private void doSearch(final MaalrQuery maalrQuery) {
		if(maalrQuery.getValues().size() == 0) {
			return;
		}
		if(SearchHelper.getLastQuery() != null && SearchHelper.getLastQuery().equals(maalrQuery)) {
			return;
		}
		service.search(maalrQuery, new AsyncCallback<QueryResult>() {

			@Override
			public void onSuccess(QueryResult result) {
				SearchHelper.setLastQuery(maalrQuery);
				search.updateResult(maalrQuery, result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Dialog.showError("An error occurred while processing your request.");
				logger.error("Failed to send request", caught);
			}
		});
	}

	private void openEditor() {
		/**
		 * Instead of executing the command directly, the call is wrapped into
		 * an async callback. This results in a "code split": GWT will lazy-load
		 * the javascript required to show the editor. Thus, the initial page
		 * download is smaller and faster. See
		 * https://developers.google.com/web-
		 * toolkit/doc/latest/DevGuideCodeSplitting?
		 */
		GWT.runAsync(new RunAsyncCallback() {

			public void onFailure(Throwable caught) {
				Window.alert("Code download failed");
			}

			public void onSuccess() {
				LemmaEditor.openEditor();
			}

		});
	}

}

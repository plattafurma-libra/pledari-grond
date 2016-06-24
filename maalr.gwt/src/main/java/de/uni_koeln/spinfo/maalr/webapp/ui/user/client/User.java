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

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.Dictionary;
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

	
	private static final String ID_GRID_WRAPPER = "gridWrapper";

	private static final String CLASS_FEED_MARGE_MORE = "feedMargeMore";

	private UserConstants constants = GWT.create(UserConstants.class);
	
	private Logger logger = Logger.getLogger(getClass());

	private SearchServiceAsync service;

	private ResultCellTable resultCellTable;

	private Search search;
	
	private static final String SEARCH_PANEL = "search_panel";
	private static final String EXT_LINKS_CONTAINER = "ext_links_container";
	private static final String FEED_CONTAINER = "feed_container";
	private static final String EXTERNAL_LINKS = "links_ulteriurs";
	private static final String GLOSSAR = "link_glossaris";
	private static final String LANGUAGES_WIDGET = "languages-widget";
	private static final String NOJS_SEARCHCONTAINER = "nojs_searchcontainer";
	private static final String CONTENT = "content";
	private static final String PROPOSE_NAVI = "propose_navi";
	
	private static final int DISPLAY_SIZE_1024 = 1024;

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
						HiJax.hijackAnchor(PROPOSE_NAVI, new Command() {

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
		Element element = DOM.getElementById(LANGUAGES_WIDGET);
		if (element != null) {
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

		Element noScriptDiv = DOM.getElementById(NOJS_SEARCHCONTAINER);
		if (noScriptDiv != null)
			noScriptDiv.removeFromParent();

		search.setResultCellTable(resultCellTable);
		search.addSearchHandler(new SearchHandler() {

			@Override
			public void onSearch(final SearchEvent event) {

				AsyncLemmaDescriptionLoader
						.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(LemmaDescription result) {
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

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				MaalrQuery maalrQuery = MaalrQuery.parse(historyToken);
				search.updateUI(maalrQuery);
				doSearch(maalrQuery);
			}
		});

		if (History.getToken() != null) {
			History.fireCurrentHistoryState();
		}

		RootPanel contentPanel = RootPanel.get(CONTENT);
		if (contentPanel != null) {
			contentPanel.add(search);
		}
		
		// ADD EXTERNAL LINKS 
		addExternalLinks();
		search.setFocus(true);
	}

	private void addExternalLinks() {
		
		final RootPanel extLinksPanel = RootPanel.get(EXT_LINKS_CONTAINER);
		final RootPanel searchPanel = RootPanel.get(SEARCH_PANEL);
		final RootPanel resultWrapper = RootPanel.get("resultWrapper");
		final Element feedContainer = DOM.getElementById(FEED_CONTAINER);
		
		if(extLinksPanel != null && searchPanel != null) {
			Anchor extDictLinks = createAnchor(constants.dictionaries(), EXTERNAL_LINKS);
			Anchor glossary = createAnchor(constants.glossar(), GLOSSAR);

			if (Window.getClientWidth() <= 768) {
				searchPanel.add(extDictLinks);
				searchPanel.add(glossary);
				glossary.getElement().getStyle().setVisibility(Visibility.HIDDEN);
				resultWrapper.getElement().getParentNode().appendChild(feedContainer);
				feedContainer.setClassName(CLASS_FEED_MARGE_MORE);
				feedContainer.getStyle().setVisibility(Visibility.VISIBLE);
			} else {
				feedContainer.getStyle().setVisibility(Visibility.VISIBLE);
				feedContainer.removeClassName(CLASS_FEED_MARGE_MORE);
				extLinksPanel.add(extDictLinks);
				extLinksPanel.add(glossary);
			}

			addResizeHandler(extLinksPanel, searchPanel, resultWrapper);
			
			hiJackExternalLinks(EXTERNAL_LINKS, DictionaryConstants.DICT_LINKS_EXTERNAL, DictionaryConstants.getExtLinksDictionary());
			hiJackExternalLinks(GLOSSAR, DictionaryConstants.GLOSSAR_LINKS, DictionaryConstants.getLinksDictionary());
		}
	}

	private Anchor createAnchor(final String title, final String id) {
		Anchor anchor = new Anchor(title);
		anchor.getElement().setId(id);
		return anchor;
	}

	private void addResizeHandler(final RootPanel sidePanel, final RootPanel searchPanel, final RootPanel resultWrapper) {
		
		Window.addResizeHandler(new ResizeHandler() {
			
			@Override
			public void onResize(ResizeEvent event) {
				
				Element extDictLinks = DOM.getElementById(EXTERNAL_LINKS);
				Element glossary = DOM.getElementById(GLOSSAR);
				Element feedContainer = DOM.getElementById(FEED_CONTAINER);

				if(extDictLinks != null) {
					extDictLinks.removeFromParent();
				}
				if(glossary != null) {
					glossary.removeFromParent();
				}
				if(feedContainer != null) {
					feedContainer.removeFromParent();
				}
				
				if (event.getWidth() >= DISPLAY_SIZE_1024) {
					appendTo(sidePanel, extDictLinks);
					appendTo(sidePanel, glossary);
					glossary.getStyle().setVisibility(Visibility.VISIBLE);
					DOM.getElementById(ID_GRID_WRAPPER).appendChild(feedContainer);
					feedContainer.removeClassName(CLASS_FEED_MARGE_MORE);
				} else {
					appendTo(searchPanel, extDictLinks);
					appendTo(searchPanel, glossary);
					resultWrapper.getElement().getParentNode().appendChild(feedContainer);
					feedContainer.addClassName(CLASS_FEED_MARGE_MORE);
					feedContainer.getStyle().setVisibility(Visibility.VISIBLE);
					glossary.getStyle().setVisibility(Visibility.HIDDEN);
				}
			}

			private void appendTo(final RootPanel panel, Element element) {
				panel.getElement().appendChild(element);
			}

		});
	}

	private void hiJackExternalLinks(final String linkId, final List<String> dictLinksList, final Dictionary dictionary) {
		Element anchor = DOM.getElementById(linkId);
		if(anchor != null) {
			Anchor wrapper = Anchor.wrap(anchor);
			wrapper.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					new ExternalLinkDialog(dictLinksList, dictionary);
					event.getNativeEvent().preventDefault();
					event.getNativeEvent().stopPropagation();
				}
			});
		}
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
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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search.celltable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonCell;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQueryFormatter;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.AsyncLemmaDescriptionLoader;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CustomPager;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Highlighter;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.i18n.LocalizedStrings;
import de.uni_koeln.spinfo.maalr.webapp.ui.user.client.entry.LemmaEditor;
import de.uni_koeln.spinfo.maalr.webapp.ui.user.client.entry.OverlayPopup;

/**
 * Composite that displays query results.
 * 
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 */

public class ResultCellTable extends Composite {

	interface RCTInterface extends UiBinder<Widget, ResultCellTable> {}

	private static RCTInterface uiBinder = GWT.create(RCTInterface.class);

	@UiField(provided = true)
	CellTable<LemmaVersion> cellTable;

	@UiField(provided = true)
	CustomPager pager;

	@UiField
	com.github.gwtbootstrap.client.ui.Column resultLabelCell;

	HTML label;

	private Button suggest;

	private ListDataProvider<LemmaVersion> dataProvider;

	private LemmaDescription description;

	private Column<LemmaVersion, SafeHtml> columnA;

	private Column<LemmaVersion, SafeHtml> columnB;

	private Column<LemmaVersion, String> optionsColumn;

	private int hoveredRow;

	private MaalrQuery maalrQuery;

	private Column<LemmaVersion, SafeHtml> popupColumn;
	
	private static final ProvidesKey<LemmaVersion> KEY_PROVIDER = new ProvidesKey<LemmaVersion>() {
		public Object getKey(LemmaVersion item) {
			return item;
		}
	};

	public ResultCellTable() {
		AsyncLemmaDescriptionLoader.afterLemmaDescriptionLoaded(new AsyncCallback<LemmaDescription>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(LemmaDescription result) {
				description = result;
			}
		});
		cellTable = new CellTable<LemmaVersion>(KEY_PROVIDER);
		cellTable.addStyleName("resultlist");
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		RowHoverEvent.Handler handler = new RowHoverEvent.Handler() {

			@Override
			public void onRowHover(RowHoverEvent event) {
				int newIndex = event.getHoveringRow().getSectionRowIndex();
				if (hoveredRow != newIndex && !event.isUnHover()) {
					hoveredRow = newIndex;
				}
			}
		};
		cellTable.addRowHoverHandler(handler);
		dataProvider = new ListDataProvider<LemmaVersion>();
		dataProvider.addDataDisplay(cellTable);
		pager = new CustomPager();
		initWidget(uiBinder.createAndBindUi(this));
		label = new HTML();
		resultLabelCell.add(label);
	}

	private void addOptionsColumn(TranslationMap translationMap) {
		final ButtonCell cell = new ButtonCell(IconType.EDIT,
				ButtonType.DEFAULT, ButtonSize.MINI) {

			@Override
			public Set<String> getConsumedEvents() {
				Set<String> consumed = new HashSet<String>();
				consumed.add(BrowserEvents.CLICK);
				return consumed;
			}

			@Override
			public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
				if (event.getType().equals(BrowserEvents.CLICK)) {
					LemmaVersion selected = dataProvider.getList().get(hoveredRow);
					onButtonClicked(selected);
				}
			}

			private void onButtonClicked(LemmaVersion selected) {
				final LemmaVersion lemma = new LemmaVersion();
				lemma.setEntryValues(selected.getEntryValues());
				lemma.setMaalrValues(selected.getMaalrValues());
				openModifyEditor(lemma);
			}
		};
		final String modify = translationMap.get("maalr.query.result_modify");
		optionsColumn = new Column<LemmaVersion, String>(cell) {

			@Override
			public String getValue(LemmaVersion object) {
				return modify;
			}

		};
		cellTable.addColumn(optionsColumn);

	}

	private void openModifyEditor(final LemmaVersion lemma) {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				LemmaEditor.openEditor(lemma);
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Code download failed");
			}
		});
	}

	private void openEditor() {
		GWT.runAsync(new RunAsyncCallback() {

			@Override
			public void onSuccess() {
				LemmaEditor.openEditor();
			}

			@Override
			public void onFailure(Throwable reason) {
				Window.alert("Code download failed");
			}
		});
	}

	private void addOverlayColumn(final String overlayField, final TranslationMap translationMap) {
		final SafeHtmlCell overlayCell = new SafeHtmlCell() {
			final String closeButton = translationMap.get("maalr.verbOverlayPopup.closeButton");

			@Override
			public Set<String> getConsumedEvents() {
				Set<String> consumed = new HashSet<String>();
				consumed.add(BrowserEvents.CLICK);
				return consumed;
			}

			@Override
			public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, SafeHtml value, NativeEvent event, ValueUpdater<SafeHtml> valueUpdater) {
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
				if (event.getType().equals(BrowserEvents.CLICK)) {
					LemmaVersion selected = dataProvider.getList().get(hoveredRow);
					onButtonClicked(selected);
				}
			}

			private void onButtonClicked(LemmaVersion selected) {
				// Window.alert("Selected: " + selected.getEntryValues());
				OverlayPopup.show(selected, overlayField, closeButton);
				// final LemmaVersion lemma = new LemmaVersion();
				// lemma.setEntryValues(selected.getEntryValues());
				// lemma.setMaalrValues(selected.getMaalrValues());
				// openModifyEditor(lemma);
			}

		};
		popupColumn = new Column<LemmaVersion, SafeHtml>(overlayCell) {

			private final SafeHtml nothing = new SafeHtmlBuilder().toSafeHtml();

			@Override
			public SafeHtml getValue(LemmaVersion object) {
				String overlay = object.getEntryValue(overlayField);
				if (overlay != null) {
					SafeHtmlBuilder builder = new SafeHtmlBuilder();
					builder.appendHtmlConstant("<span class=\"maalr_overlay maalr_overlay_" + overlay + "\">" + overlay + "</span>");
					return builder.toSafeHtml();
				} else {
					return nothing;
				}
			}
		};
		cellTable.addColumn(popupColumn);
	}

	private void addColumnA(String langA, final boolean b) {

		final SafeHtmlCell leftCell = new SafeHtmlCell();
		columnA = new Column<LemmaVersion, SafeHtml>(leftCell) {

			@Override
			public SafeHtml getValue(LemmaVersion object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String toDisplay = description.toString(object, UseCase.RESULT_LIST, b);
				String redirect = b ? object.getEntryValue("redirect_a") : object.getEntryValue("redirect_b");
				if (maalrQuery.isHighlight() && redirect == null) {
					toDisplay = Highlighter.highlight(toDisplay, maalrQuery.getValue("searchPhrase"));
				}
				if (redirect != null) {
					MaalrQuery mq = new MaalrQuery();
					String[] redir = redirect.split("=");
					mq.setQueryValue(redir[0], redir[1]);
					String text = redir[1];
					if (maalrQuery.isHighlight()) {
						text = Highlighter.highlight(text, maalrQuery.getValue("searchPhrase"));
					}
					String url = "<a href=\"" + Window.Location.getPath() + "#" + mq.toURL() + "\">" + text + "</a>";
					toDisplay = toDisplay.replace(redir[1], url);
				}
				if (!object.isApproved()) {
					toDisplay = "<span class=\"unverified\">" + toDisplay + "</span>";
				}
				sb.appendHtmlConstant(toDisplay);
				return sb.toSafeHtml();
			}
		};

		cellTable.addColumn(columnA, new SafeHtmlBuilder().appendHtmlConstant("<span class=\"maalr_result_title\">" + langA + "</span>").toSafeHtml());
	}

	private void addColumnB(String langB, final boolean b) {
		columnB = new Column<LemmaVersion, SafeHtml>(new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(LemmaVersion object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String toDisplay = description.toString(object, UseCase.RESULT_LIST, b);
				String redirect = b ? object.getEntryValue("redirect_a") : object.getEntryValue("redirect_b");
				if (maalrQuery.isHighlight() && redirect == null) {
					toDisplay = Highlighter.highlight(toDisplay, maalrQuery.getValue("searchPhrase"));
				}
				if (redirect != null) {
					MaalrQuery mq = new MaalrQuery();
					String[] redir = redirect.split("=");
					mq.setQueryValue(redir[0], redir[1]);
					String text = redir[1];
					if (maalrQuery.isHighlight()) {
						text = Highlighter.highlight(text, maalrQuery.getValue("searchPhrase"));
					}
					String url = "<a href=\"" + Window.Location.getPath() + "#" + mq.toURL() + "\">" + text + "</a>";
					toDisplay = toDisplay.replace(redir[1], url);
					// toDisplay = "<a href=\"" + mq.toURL() + "\">" + redir[1]
					// + "</a>";
				}
				if (!object.isApproved()) {
					toDisplay = "<span class=\"unverified\">" + toDisplay + "</span>";
				}
				// if (!b)
				// sb.appendHtmlConstant(refereTo(toDisplay));
				// else
				sb.appendHtmlConstant(toDisplay);
				return sb.toSafeHtml();
			}

		};
		cellTable.addColumn(columnB, new SafeHtmlBuilder().appendHtmlConstant("<span class=\"maalr_result_title\">" + langB + "</span>").toSafeHtml());
	}

	public void setResults(final MaalrQuery query, QueryResult result) {
		if (result.getMaxEntries() > 0) {
			this.maalrQuery = query;
			setSuggestVisible(false);
			removeColumns();
			addColumns(query);
			List<LemmaVersion> dataList = dataProvider.getList();
			dataList.clear();
			dataList.addAll(result.getEntries());
			dataProvider.refresh();
			cellTable.setVisible(true);
			label.setVisible(true);
			pager.setVisible(true);
			cellTable.redraw();
			pager.createPageLinks(query, result);
			addResultLabel(query, result);
		} else {
			cellTable.setVisible(false);
			pager.setVisible(false);
			LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(TranslationMap result) {
					String info = result.get("maalr.query.nothing_found");
					if (info != null) {
						if(MaalrQueryFormatter.getQueryLabel(query) == null){
							label.setHTML(""); //return nothing on empty searchphrases
							setSuggestVisible(false);
							return;
						}
						else{
							info = info.replaceAll("\\{0\\}", MaalrQueryFormatter.getQueryLabel(query));
							info = info.replaceAll("\\{1\\}", result.get("suggest.button"));
							label.setHTML(info);
						}
					}
					if (suggest == null) {
						initSuggestButton(result);
						resultLabelCell.add(suggest);
					} else {
						setSuggestVisible(true);
					}
				}
			});
		}
	}

	private void setSuggestVisible(boolean visible) {
		if (suggest != null)
			suggest.setVisible(visible);
	}
	
	private void initSuggestButton(TranslationMap result) {
		suggest = new Button(result.get("suggest.button"), IconType.INFO_SIGN);
		suggest.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				openEditor();
			}
		});
	}

	private void removeColumns() {
		while (cellTable.getColumnCount() > 0) {
			cellTable.removeColumn(0);
		}
	}

	private void addColumns(final MaalrQuery maalrQuery) {
		LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(TranslationMap translationMap) {
				String value = maalrQuery.getValue("language");
				boolean defaultOrder = true;
				if (description.getLanguageName(false).equals(value)) {
					defaultOrder = false;
				}
				addOverlayColumn(defaultOrder ? LemmaVersion.OVERLAY_LANG1 : LemmaVersion.OVERLAY_LANG2, translationMap);
				addColumnA(translationMap.get(description.getLanguageName(defaultOrder)), defaultOrder);
				addOverlayColumn(defaultOrder ? LemmaVersion.OVERLAY_LANG2 : LemmaVersion.OVERLAY_LANG1, translationMap);
				addColumnB(translationMap.get(description.getLanguageName(!defaultOrder)), !defaultOrder);
				// TODO: Commented to disable modify option for surmiran
				// addOptionsColumn(translationMap);
			}
		});

	}

	private void addResultLabel(MaalrQuery maalrQuery,
			final QueryResult result) {
		int a = ((maalrQuery.getPageNr() + 1) * maalrQuery.getPageSize()) - (maalrQuery.getPageSize() - 1);
		int b = ((maalrQuery.getPageNr() + 1) * maalrQuery.getPageSize());
		if (((maalrQuery.getPageNr() + 1) * maalrQuery.getPageSize()) > result.getMaxEntries()) {
			b = result.getMaxEntries();
		}
		final String formatted = MaalrQueryFormatter.getQueryLabel(maalrQuery);
		final int first = a;
		final int last = b;
		if (formatted != null) {
			LocalizedStrings.afterLoad(new AsyncCallback<TranslationMap>() {

				@Override
				public void onSuccess(TranslationMap translation) {
					String toShow = translation.get("maalr.query.results");
					toShow = toShow.replaceAll("\\{0\\}", "");
					toShow = toShow.replaceAll("\\{1\\}", first + "");
					toShow = toShow.replaceAll("\\{2\\}", last + "");
					toShow = toShow.replaceAll("\\{3\\}", result.getMaxEntries() + "");
					String text = "<span style=\"font-size: large;font-weight: bold; text-align: left;\">" + toShow + "</span>";
					String wrappedSearchPhraseForCss = "<span id=\"searchphrase\">" + formatted + "</span>";
					label.setHTML(wrappedSearchPhraseForCss + text);
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}
	}

	public void doPageSizeChanged(int size) {
		cellTable.setPageSize(size);
		cellTable.redraw();
	}
	
}

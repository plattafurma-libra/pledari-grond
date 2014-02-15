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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.dataprovider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;

import de.uni_koeln.spinfo.maalr.common.shared.EditorQuery;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntryList;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.PagingDataGrid;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorMessages;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorService;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.filter.ListFilter;

public class SimplePagingDataProvider extends AbstractDataProvider<LexEntry> {
	
	private ListDataProvider<LexEntry> delegate = new ListDataProvider<LexEntry>();
	private PagingDataGrid<LexEntry> table;
	private EditorServiceAsync service;
	private AsyncCallback<LexEntryList> callback;

	private EditorQuery searchOptions = new EditorQuery();
	private EditorQuery lastQuery;
	
	private EditorMessages messages = GWT.create(EditorMessages.class);
	private EditorConstants constants = GWT.create(EditorConstants.class);
	
	public EditorQuery getQuery() {
		return searchOptions;
	}

	public SimplePagingDataProvider(final PagingDataGrid<LexEntry> table, final HorizontalPanel pagination, final ListFilter filterOptions, final Label resultSummary) {
		this.table = table;
		this.service = GWT.create(EditorService.class);
		pagination.getElement().getStyle().setMarginBottom(10, Unit.PX);
		final HorizontalPanel sizePanel = new HorizontalPanel();
		table.setIncrementSize(10);
		searchOptions.setPageSize(10);
		searchOptions.setVerification(Verification.UNVERIFIED);
		
		// Callback responsible for updating the list
		callback = new AsyncCallback<LexEntryList>() {

			@Override
			public void onFailure(Throwable caught) {
				pagination.clear();
				Dialog.showError(constants.failedToUpdateEntryList(), caught);
			}

			@Override
			public void onSuccess(LexEntryList result) {
				if(result.getOverallCount() == 0) {
					resultSummary.setText(constants.noEntriesMached());
				} else {
					resultSummary.setText(messages.displayingEntries((searchOptions.getCurrent() + 1), (searchOptions.getCurrent() + result.entries().size()), result.getOverallCount()));
				}
				setList(result.entries());
				updatePagination(result.getOverallCount());
				MultiSelectionModel<?> selectionModel = (MultiSelectionModel<?>) table.getSelectionModel();
				selectionModel.clear();
				table.redraw();
				lastQuery = searchOptions.getCopy();
			}

			private void updatePagination(final int overall) {
				int currentPage = searchOptions.getCurrent()/searchOptions.getPageSize();
				int start = Math.max(0, currentPage-3);
				final int end = Math.min(currentPage+4, (overall+searchOptions.getPageSize()-1)/searchOptions.getPageSize());
				pagination.clear();
				pagination.add(sizePanel);
				pagination.setCellHorizontalAlignment(sizePanel, HorizontalAlignmentConstant.startOf(Direction.LTR));
				ButtonGroup pagingButtons = new ButtonGroup();
				pagingButtons.getElement().getStyle().setFloat(Float.RIGHT);
				pagination.setCellHorizontalAlignment(pagingButtons, HorizontalAlignmentConstant.endOf(Direction.LTR));
				Button first = new Button(constants.first(), IconType.FAST_BACKWARD);
				first.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						searchOptions.setCurrent(0);
						doUpdate();
					}
				});
				pagingButtons.add(first);
				for(int i = start; i < end; i++) {
					final int page = i;
					Button button = new Button((i+1)+"");
					button.setToggle(true);
					if(i == currentPage) {
						button.setStyleName("active", true);
					} else {
						button.addClickHandler(new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								searchOptions.setCurrent(page*searchOptions.getPageSize());
								doUpdate();
							}
						});
					}
					pagingButtons.add(button);
				}
				Button last = new Button(constants.last(), IconType.FAST_FORWARD);
				last.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						int lastPage = (overall+searchOptions.getPageSize()-1)/searchOptions.getPageSize()-1;
						searchOptions.setCurrent(lastPage*searchOptions.getPageSize());
						doUpdate();
					}
				});
				pagingButtons.add(last);
				pagination.add(pagingButtons);
			}

		};
		addDataDisplay(table);
		// Enable sorting columns 
		table.addColumnSortHandler(new Handler() {
			
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				// Update sort properties and start a new query
				searchOptions.setOrder(column.getDataStoreName(), event.getColumnSortList().get(0).isAscending());
				searchOptions.setCurrent(0);
				refreshQuery();
			}
		});
	}

	public void setQuery(EditorQuery query) {
		this.searchOptions = query;
	}
	
	public void dropLastQuery() {
		lastQuery = null;
	}

	public void refreshQuery() {
		table.setVisibleRange(0, searchOptions.getPageSize());
		doUpdate();
	}
	
	/**
	 * Request additional users from the user service, by passing all
	 * related parameters (filters, order, current index etc) to the service.
	 * The asynchronous reply is handled by {@link SimplePagingDataProvider#callback}.
	 */
	private void doUpdate() {
		if(lastQuery != null && lastQuery.equals(searchOptions)) return;
		service.getLexEntries(searchOptions, callback);
	}

	public int replace(LexEntry entry, LexEntry result) {
		// FIXME: Reimplement this!
		List<LexEntry> loaded = getList();
		for(int i = 0; i < loaded.size(); i++) {
			if(loaded.get(i) == entry) {
				loaded.set(i, result);
				table.setRowData(loaded);
				return i;
			}
		}
		return -1;
	}

	public void set(int index, LexEntry result) {
		List<LexEntry> list = getList();
		list.set(index, result);
		table.setRowData(index, Arrays.asList(result));
		table.redrawRow(index+1);
	}
	
	public void remove(int row, LexEntry result) {
		List<LexEntry> loaded = getList();
		loaded.remove(row);
		table.setRowData(loaded);
		table.redraw();
	}
	
	public void remove(LexEntry result) {
		List<LexEntry> loaded = getList();
		loaded.remove(result);
		table.setRowData(loaded);
		table.redraw();
	}
	
	public List<LexEntry> getData() {
		return new ArrayList<LexEntry>(getList());
	}

	@Override
	protected void onRangeChanged(HasData<LexEntry> display) {
	}

	public void addDataDisplay(HasData<LexEntry> display) {
		delegate.addDataDisplay(display);
	}

	public Set<HasData<LexEntry>> getDataDisplays() {
		return delegate.getDataDisplays();
	}

	public Object getKey(LexEntry item) {
		return delegate.getKey(item);
	}

	public ProvidesKey<LexEntry> getKeyProvider() {
		return delegate.getKeyProvider();
	}

	public Range[] getRanges() {
		return delegate.getRanges();
	}

	public void flush() {
		delegate.flush();
	}

	public List<LexEntry> getList() {
		return delegate.getList();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public void removeDataDisplay(HasData<LexEntry> display) {
		delegate.removeDataDisplay(display);
	}

	public void refresh() {
		delegate.refresh();
	}

	public void setList(List<LexEntry> listToWrap) {
		delegate.setList(listToWrap);
	}

	public String toString() {
		return delegate.toString();
	}

	public int getPageSize() {
		return searchOptions.getPageSize();
	}

}

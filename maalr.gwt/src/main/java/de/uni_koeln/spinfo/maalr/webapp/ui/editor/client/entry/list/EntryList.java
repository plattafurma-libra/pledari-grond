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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;

import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.PagingDataGrid;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.events.LazyLoadHandler;

public abstract class EntryList<T> extends Composite {

	private static EntryListUiBinder uiBinder = GWT
			.create(EntryListUiBinder.class);

	@UiField(provided = true)
	protected PagingDataGrid<T> table;

	protected AbstractDataProvider<T> dataProvider;

	private AbstractSelectionModel<T> selectionModel;

	private Set<String> usedColumns = new HashSet<String>();

	public AbstractSelectionModel<T> getSelectionModel() {
		return selectionModel;
	}

	protected LemmaDescription lemmaDescription;

	protected Column<T, String> state;

	interface EntryListUiBinder extends UiBinder<Widget, EntryList<?>> {
	}

	private boolean multiSelect = false;

	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public EntryList(final AbstractSelectionModel<T> selectionModel) {
		// The table is not provided by GWT, such that is
		// must be initialized before the widget is initialized:
		table = new PagingDataGrid<T>(20);
		initWidget(uiBinder.createAndBindUi(this));
		this.selectionModel = selectionModel;

		DefaultSelectionEventManager<T> selectionManager = DefaultSelectionEventManager
				.createCustomManager(new DefaultSelectionEventManager.CheckboxEventTranslator<T>() {
					@Override
					public SelectAction translateSelectionEvent(
							CellPreviewEvent<T> event) {
						SelectAction action = super
								.translateSelectionEvent(event);
						if (action.equals(SelectAction.IGNORE)) {
							if (multiSelect) {
								return SelectAction.TOGGLE;
							} else {
								if (selectionModel instanceof MultiSelectionModel) {
									((MultiSelectionModel<?>) selectionModel)
											.clear();
								}
								return SelectAction.SELECT;
							}
						}
						return action;
					}
				});
		table.setSelectionModel(selectionModel, selectionManager);
	}

	private int hoveredRow;

	public int getHoveredRow() {
		return hoveredRow;
	}

	/**
	 * Returns either the String which should be displayed, or "--" if it is
	 * <code>null</code>.
	 * 
	 * @param toDisplay
	 * @return
	 */
	String getDisplayString(String toDisplay) {
		if (toDisplay == null || toDisplay.trim().length() == 0) {
			return "--";
		}
		return toDisplay.trim();
	}

	/**
	 * Add a selection changed handler to the selection model used by this
	 * class. It will be informed whenever a different row has been selected.
	 * 
	 * @param handler
	 */
	public void addSelectionChangedHandler(Handler handler) {
		selectionModel.addSelectionChangeHandler(handler);
	}

	/**
	 * Returns the used {@link PagingDataGrid}.
	 */
	public PagingDataGrid<T> getTable() {
		return table;
	}

	public void setLemmaDescription(final LemmaDescription lemmaDescription) {
		this.lemmaDescription = lemmaDescription;
		addColumns();
		// Enable navigation through arrow keys
		// table.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		// table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
	}

	protected abstract void addColumns();

	public void setDataProvider(AbstractDataProvider<T> dataProvider) {
		this.dataProvider = dataProvider;
	}

	public void redraw() {
		table.redraw();
	}

	public void addLoadHandler(LazyLoadHandler handler) {
		table.addLoadHandler(handler);
	}

	public void addColumn(String name, Column<T, ?> column) {
		if (!usedColumns.contains(name)) {
			table.addColumn(column, name);
			usedColumns.add(name);
		}
		table.setColumnWidth(column, 200, Unit.PX);
	}

	public void removeColumn(Column<T, ?> column) {
		table.setColumnWidth(column, 0, Unit.PX);
	}

}

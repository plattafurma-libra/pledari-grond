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

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.view.client.OrderedMultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;

import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.filter.ListFilter;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.column.LexEntryColumn;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.ICellWrapper;

public class AllEntriesList extends EntryList<LexEntry> {
	
	private static final ProvidesKey<LexEntry> keyProvider = new ProvidesKey<LexEntry>() {

		@Override
		public Object getKey(LexEntry item) {
			return item.getId();
		}
	};
	private ListFilter filter;
	private Column<LexEntry, Boolean> selectColumn;
	private LexEntryColumn verifier;
	
	public AllEntriesList() {
		super(new OrderedMultiSelectionModel<LexEntry>(keyProvider));
	}
	
	public LexEntry getSelectedEntry() {
		OrderedMultiSelectionModel<LexEntry> selectionModel = (OrderedMultiSelectionModel<LexEntry>) getSelectionModel();
		if(selectionModel.getSelectedSet().size() != 1) {
			if(selectionModel.getSelectedList().size() > 1) {
				return selectionModel.getSelectedList().get(selectionModel.getSelectedList().size()-1);
			}
			return null;
		}
		return selectionModel.getSelectedSet().iterator().next();
	}
	
	@Override
	protected void addColumns() {
		
		CheckboxCell selectCell = new CheckboxCell(false, false);
		selectColumn = new Column<LexEntry, Boolean>(selectCell) {

			@Override
			public Boolean getValue(LexEntry object) {
				return getSelectionModel().isSelected(object);
			}
			
		};
		table.addColumn(selectColumn);
		table.setColumnWidth(selectColumn, "0px");
		RowStyles<LexEntry> wrapper = new RowStyles<LexEntry>() {

			@Override
			public String getStyleNames(LexEntry row, int rowIndex) {
				return "user-row";
			}
		};
		table.setRowStyles(wrapper);
	}

	public void setFilterOptions(ListFilter filterOptions) {
		this.filter = filterOptions;
	}
	
	@Override
	public void setMultiSelect(boolean multiSelect) {
		if(multiSelect) {
			table.setColumnWidth(selectColumn, "30px");
		} else {
			table.setColumnWidth(selectColumn, "0px");
		}
		super.setMultiSelect(multiSelect);
	}

	public void setVerifierColumnVisible(boolean visible) {
		if(visible) {
			table.setColumnWidth(verifier, 120, Unit.PX);
		} else {
			table.setColumnWidth(verifier, 0, Unit.PX);
		}
	}

	public void setColumnWidth(
			Column<LexEntry, ? extends ICellWrapper> column,
			String width) {
		table.setColumnWidth(column, width);
	}

	public ListFilter getFilter() {
		return this.filter;
	}

}

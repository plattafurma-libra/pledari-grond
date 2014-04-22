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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.query;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorMessages;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.EntryList;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.EntryEditCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.EntryOrderCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.column.LemmaVersionColumnHighlightable;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.wrapper.LemmaVersionCellWrapper;

public class QueryList extends EntryList<LemmaVersion> {

	private EntryEditCell editCell;
	
	private LemmaVersionColumnHighlightable entryColumn;
	
	private EditorConstants constants;
	
	private EditorMessages messages;

	public QueryList(AbstractSelectionModel<LemmaVersion> selectionModel, final EditorConstants constants, final EditorMessages messages) {
		super(selectionModel);
		this.constants = constants;
		this.messages = messages;
	}

	public void setQuery(MaalrQuery query) {
		entryColumn.setQuery(query);
	}
	
	@Override
	protected void addColumns() {
		entryColumn = new LemmaVersionColumnHighlightable(new SafeHtmlCell());
		table.addColumn(entryColumn, constants.entry());
		table.setColumnWidth(entryColumn, "250px");
		editCell = new EntryEditCell(constants, messages);
		Column<LemmaVersion, LemmaVersionCellWrapper> column = new Column<LemmaVersion, LemmaVersionCellWrapper>(editCell) {

			@Override
			public LemmaVersionCellWrapper getValue(LemmaVersion object) {
				return new LemmaVersionCellWrapper(object);
			}
		};
		table.addColumn(column);
		table.setColumnWidth(column, "80px");
		EntryOrderCell orderCell = new EntryOrderCell(constants);
		Column<LemmaVersion, LemmaVersionCellWrapper> column2 = new Column<LemmaVersion, LemmaVersionCellWrapper>(orderCell) {

			@Override
			public LemmaVersionCellWrapper getValue(LemmaVersion object) {
				return new LemmaVersionCellWrapper(object);
			}
		};
		table.addColumn(column2);
		table.setColumnWidth(column2, "120px");
		RowStyles<LemmaVersion> wrapper = new RowStyles<LemmaVersion>() {

			@Override
			public String getStyleNames(LemmaVersion row, int rowIndex) {
				return "user-row";
			}
		};
		table.setRowStyles(wrapper);
	}

	public void setModifyCommand(Command command) {
		editCell.setModifyCommand(command);
	}
	
	public void setDeleteCommand(AsyncCallback<LemmaVersion> command) {
		editCell.setDeleteCallback(command);
	}

	public void clearSelection() {
		AbstractSelectionModel<LemmaVersion> model = super.getSelectionModel();
		if(model instanceof SingleSelectionModel) {
			((SingleSelectionModel<?>) model).clear();
		}
	}

}

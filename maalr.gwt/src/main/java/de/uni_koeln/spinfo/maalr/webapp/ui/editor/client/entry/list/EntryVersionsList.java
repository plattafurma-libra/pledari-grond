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

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.SingleSelectionModel;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.DataCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.DateCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.TypeCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.cell.UserCell;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.list.column.LemmaVersionColumn;

public class EntryVersionsList extends EntryList<LemmaVersion> {
	
	private EditorConstants constants = GWT.create(EditorConstants.class);
	
	public EntryVersionsList() {
		super(new SingleSelectionModel<LemmaVersion>());
	}
	
	@Override
	protected void addColumns() {
		LemmaVersionColumn verification = new LemmaVersionColumn(new TypeCell());
		table.addColumn(verification, constants.state());
		table.setColumnWidth(verification, "100px");
		LemmaVersionColumn user = new LemmaVersionColumn(new UserCell());
		table.addColumn(user, constants.user());
		table.setColumnWidth(user, "120px");
		LemmaVersionColumn langA = new LemmaVersionColumn(new DataCell());
		table.addColumn(langA, constants.entry());
		LemmaVersionColumn creation = new LemmaVersionColumn(new DateCell());
		table.addColumn(creation, constants.changed());
		table.setColumnWidth(creation, "100px");
	}

	public LemmaVersion getSelectedEntry() {
		SingleSelectionModel<LemmaVersion> selectionModel = (SingleSelectionModel<LemmaVersion>) getSelectionModel();
		return selectionModel.getSelectedObject();
	}

}

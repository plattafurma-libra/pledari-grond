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
import java.util.List;

import com.google.gwt.view.client.ListDataProvider;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.PagingDataGrid;

public class HistoryDataProvider extends ListDataProvider<LemmaVersion> {

	private PagingDataGrid<LemmaVersion> table;
	
	private LexEntry entry;

	public HistoryDataProvider(PagingDataGrid<LemmaVersion> table) {
		this.table = table;
		addDataDisplay(table);
	}

	public void setEntry(LexEntry entry, boolean showAll) {
		this.entry = entry;
		if(entry == null) {
			super.getList().clear();
			table.setRowCount(0, true);
		} else {
			if(showAll) {
				super.getList().clear();
				super.getList().addAll(entry.getVersionHistory());
			} else {
				List<LemmaVersion> filtered = new ArrayList<LemmaVersion>();
				List<LemmaVersion> all = entry.getVersionHistory();
				LemmaVersion current = entry.getCurrent();
				for (LemmaVersion lv : all) {
					if(lv.getVerification() != Verification.REJECTED) {
						filtered.add(lv);
					}
					if(lv == current) {
						break;
					}
				}
				super.getList().clear();
				super.getList().addAll(filtered);
			}
			table.setRowCount(getList().size(), true);
			table.getSelectionModel().setSelected(getList().get(0), true);
		}
		super.refresh();
	}

	public LexEntry getEntry() {
		return entry;
	}

}

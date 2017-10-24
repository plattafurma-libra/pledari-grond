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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.list;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserService;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.PagingDataGrid;

/**
 * A DataProvider which is able to asynchronously populate a {@link PagingDataGrid} of
 * {@link LightUserInfo}-objects. In addition, it can filter the displayed items
 * through {@link UserListDataProvider#setRole(Role)} and {@link UserListDataProvider#setText(String)}.
 * @author sschwieb
 *
 */
@Deprecated
public class UserListDataProvider extends AsyncDataProvider<LightUserInfo> {

	/**
	 * The list of all currently loaded users, as displayed in the
	 * table.
	 */
	private List<LightUserInfo> loaded = new ArrayList<LightUserInfo>();
	
	/**
	 * The user service used to query for users
	 */
	private UserServiceAsync service;
	
	/**
	 * The table which will display the data provided by this
	 * class.
	 */
	private PagingDataGrid<LightUserInfo> table;
	
	/**
	 * Used for collecting query results
	 */
	private AsyncCallback<List<LightUserInfo>> callback;
	
	/**
	 * The number of items to fetch in a single query
	 */
	private int pageSize = 20;
	
	/**
	 * The current offset
	 */
	private int current = 0;

	/**
	 * The current role used for filtering results
	 */
	private Role role;
	
	/**
	 * The current text (first name, last name, email, login) used for filtering results
	 */
	private String text;
	
	/**
	 * The current sort column
	 */
	private String sortColumn;
	
	/**
	 * Whether or not sort ascending
	 */
	private boolean sortAscending;
	
	public UserListDataProvider(final PagingDataGrid<LightUserInfo> table) {
		this.service = GWT.create(UserService.class);
		this.table = table;
		// Callback responsible for updating the list
		callback = new AsyncCallback<List<LightUserInfo>>() {

			@Override
			public void onFailure(Throwable caught) {
				Dialog.showError("Failed to update user list", caught);
			}

			@Override
			public void onSuccess(List<LightUserInfo> result) {
				update(result);
				current += result.size();
				// Check if results are found but the scrollbar
				// is not yet visible. In this case, continue
				// fetching data
				if(result.size() > 0) {
					ScrollPanel panel = table.getScrollPanel();
					if(panel.getMaximumVerticalScrollPosition() == 0) {
						doUpdate();
					}
				}
			}

		};
		addDataDisplay(table);
		// Enable sorting columns 
		table.addColumnSortHandler(new Handler() {
			
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				// Update sort properties and start a new query
				sortAscending = event.getColumnSortList().get(0).isAscending();
				sortColumn = column.getDataStoreName();
				reset();
			}
		});
	}

	@Override
	protected void onRangeChanged(HasData<LightUserInfo> display) {
		updateRowCount(loaded.size()+pageSize, false);
		doUpdate();
	}
	
	/**
	 * Reset the data provider. The list of loaded elements is cleared,
	 * and the current index is set to zero. Filter properties, such as
	 * role or text, are not modified.
	 */
	public void reset() {
		loaded.clear();
		pageSize = 20;
		current = 0;
		table.setVisibleRangeAndClearData(new Range(0, pageSize), false);
		doUpdate();
	}
	
	/**
	 * Request additional users from the user service, by passing all
	 * related parameters (filters, order, current index etc) to the service.
	 * The asynchronous reply is handled by {@link UserListDataProvider#callback}.
	 */
	private void doUpdate() {
		service.getAllUsers(role, text, sortColumn, sortAscending, current, 20, callback);
	}
		
	private void update(List<LightUserInfo> result) {
		if(!result.isEmpty()) {
			loaded.clear();
		}
		loaded.addAll(result);
		pageSize = loaded.size();
		updateRowData(0, loaded);
		if(result.size() == 0) {
			updateRowCount(loaded.size(), true);	
		} else {
			updateRowCount(pageSize, false);
		}
	}

	/**
	 * Define the role which must match all users. If set to <code>null</code>,
	 * any role will match and the filter will be disabled.
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * Define the text which must match all users in one of the defined
	 * query fields (first name, last name, login, or email).
	 * If set to <code>null</code>, any text will match and the 
	 * filter will be disabled.
	 * @param state
	 */
	public void setText(String text) {
		this.text = text;
	}

}

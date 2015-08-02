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

import java.util.Date;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserService;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.PagingDataGrid;

/**
 * 
 * This {@link Composite} displays a list of {@link LightUserInfo} elements,
 * which is fetched through a {@link UserService}. The list can be filtered and sorted,
 * and one or more {@link SelectionHandler}s can be added to the list.
 *  
 * @author sschwieb
 *
 */
public class UserList extends Composite {

	private static UserListUiBinder uiBinder = GWT.create(UserListUiBinder.class);

	@UiField(provided = true)
	PagingDataGrid<LightUserInfo> table;

	private UserListDataProvider dataProvider;

	private SingleSelectionModel<LightUserInfo> selectionModel;

	interface UserListUiBinder extends UiBinder<Widget, UserList> {
	}

	public UserList() {
		// The table is not provided by GWT, such that is
		// must be initialized before the widget is initialized:
		initCellTable();
		initWidget(uiBinder.createAndBindUi(this));
		// Only single rows can be selected:
		selectionModel = new SingleSelectionModel<LightUserInfo>();
		table.setSelectionModel(selectionModel);
	}

	/**
	 * Returns either the String which should be displayed,
	 * or "-" if it is <code>null</code>.
	 * @param toDisplay
	 * @return
	 */
	private String getDisplayString(String toDisplay) {
		if (toDisplay == null || toDisplay.trim().length() == 0) {
			return "-";
		}
		return toDisplay.trim();
	}

	/**
	 * Initializes the table, by adding and configuring columns,
	 * and by assigning the data provider.
	 */
	private void initCellTable() {
		table = new PagingDataGrid<LightUserInfo>();
		// Login Column
		UserColumn user = new UserColumn();
		user.setDataStoreName(LightUserInfo.SORT_LOGIN);
		table.addColumn(user, "Login");

		// First name column
		Column<LightUserInfo, String> firstName = new Column<LightUserInfo, String>(new TextCell()) {

			@Override
			public String getValue(LightUserInfo object) {
				return getDisplayString(object.getFirstName());
			}
		};
		
		// Unique identifier required for sorting
		firstName.setDataStoreName(LightUserInfo.SORT_FIRST_NAME);
		table.addColumn(firstName, "First Name");
		// Last name column
		Column<LightUserInfo, String> lastName = new Column<LightUserInfo, String>(new TextCell()) {

			@Override
			public String getValue(LightUserInfo object) {
				return getDisplayString(object.getLastName());
			}
		};
		// Unique identifier required for sorting
		lastName.setDataStoreName(LightUserInfo.SORT_LAST_NAME);
		table.addColumn(lastName, "Last Name");
		// Email column
		Column<LightUserInfo, String> email = new Column<LightUserInfo, String>(new TextCell()) {

			@Override
			public String getValue(LightUserInfo user) {
				if(user.getEmail() != null)
					return getDisplayString(user.getEmail());
				return "-";
			}

		};
		// Unique identifier required for sorting
		email.setDataStoreName(LightUserInfo.SORT_EMAIL);
		table.addColumn(email, "Email");
		// Role column
		Column<LightUserInfo, String> role = new Column<LightUserInfo, String>(new TextCell()) {

			@Override
			public String getValue(LightUserInfo object) {
				return object.getRole().getRoleName();
			}
		};
		// Unique identifier required for sorting
		role.setDataStoreName(LightUserInfo.SORT_ROLE);
		table.addColumn(role, "Role");
		// Creation date
		Column<LightUserInfo, Date> creation = new Column<LightUserInfo, Date>(new DateCell()) {

			@Override
			public Date getValue(LightUserInfo object) {
				return new Date(object.getCreationDate());
			}
		};
		// Unique identifier required for sorting
		creation.setDataStoreName(LightUserInfo.SORT_CREATED);
		table.addColumn(creation, "Created");
		// Last modified date
		Column<LightUserInfo, Date> modified = new Column<LightUserInfo, Date>(new DateCell()) {

			@Override
			public Date getValue(LightUserInfo object) {
				return new Date(object.getLastModificationDate());
			}
		};
		// Unique identifier required for sorting
		modified.setDataStoreName(LightUserInfo.SORT_MODIFIED);
		table.addColumn(modified, "Last Change");
		
		// Enable navigation through arrow keys
		table.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);
		// Assing data provider
		dataProvider = new UserListDataProvider(table);
		// Make columns sortable
		int columnCount = table.getColumnCount();
		for(int i = 0; i < columnCount; i++) {
			table.getColumn(i).setSortable(true);
		}
		table.setRowCount(20);

	}

	/**
	 * Add a selection changed handler to the selection model
	 * used by this class. It will be informed whenever a 
	 * different row has been selected.
	 * @param handler
	 */
	public void addSelectionChangedHandler(Handler handler) {
		selectionModel.addSelectionChangeHandler(handler);
	}

	/**
	 * Returns the selected user, as defined in
	 * {@link SingleSelectionModel#getSelectedObject()}.
	 * @return
	 */
	public LightUserInfo getSelectedUser() {
		return selectionModel.getSelectedObject();
	}

	/**
	 * Returns the used {@link PagingDataGrid}.
	 */
	public PagingDataGrid<LightUserInfo> getTable() {
		return table;
	}

	/**
	 * Set a query filter: The role of each returned user
	 * must match the given role. If it is <code>null</code>,
	 * any user will match this filter. Note that this method
	 * does not immediately update the list - this must be 
	 * triggered by calling {@link UserList#reset()}.
	 */
	public void setRole(Role role) {
		dataProvider.setRole(role);
	}

	/**
	 * Set a query filter: Either login, email, first name or last name must
	 * contain the given string. If it is <code>null</code>, any user will match
	 * this filter.Note that <code>text</code> can be a regular expression. Note
	 * that this method does not immediately update the list - this must be
	 * triggered by calling {@link UserList#reset()}.
	 */
	public void setText(String text) {
		dataProvider.setText(text);
	}
	
	/**
	 * Reset the data provider, such that it will start
	 * a new query based on the currently defined filters
	 * and sort order.
	 */
	public void reset() {
		selectionModel.clear();
		dataProvider.reset();
	}
	

}

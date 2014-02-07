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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.filter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.list.UserList;

/**
 * Filter-Composite with provides filter options to query for
 * users by text (email, login, firstname, lastname) and/or
 * role. The layout of this {@link Composite} is defined in
 * the corresponding ui.xml-file.
 * 
 * @author sschwieb
 *
 */
public class ListFilter extends Composite {

	private static UserDetailsUiBinder uiBinder = GWT
			.create(UserDetailsUiBinder.class);
	
	@UiField
	TextBox name;
	
	@UiField
	ListBox role;
	
	@UiField
	Button apply;
	
	@UiField
	Button reset;
	
	/**
	 * The list of roles, which corresponds to the
	 * list provided by {@link ListFilter#role};
	 * @see ListFilter#getRole()
	 */
	private final Role[] roles;

	private UserList userList;

	interface UserDetailsUiBinder extends UiBinder<Widget, ListFilter> {
	}
	
	/**
	 * Timer to fetch users while filter options are
	 * modified.
	 */
	Timer delayedSearchTimer = new Timer() {

		@Override
		public void run() {
			userList.setText(name.getText());
			userList.setRole(getRole());
			userList.reset();
		}
		
	};

	public ListFilter() {
		initWidget(uiBinder.createAndBindUi(this));
		roles = Role.values();
		role.addItem("Any", (String) null);
		for (Role r : roles) {
			role.addItem(r.getRoleName(), r.toString());
		}
		initHandlers();
	}

	/**
	 * Initializes handlers for input fields
	 * and buttons.
	 */
	private void initHandlers() {
		name.addKeyUpHandler(new KeyUpHandler() {
			
			String current = "";
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if(current.equals(name.getText())) return;
				current = name.getText();
				// Schedule the timer with a delay of 300 milliseconds.
				// If the text is modified within the delay time,
				// the timer is cancelled and re-scheduled.
				delayedSearchTimer.cancel();
				delayedSearchTimer.schedule(300);
			}
		});
		role.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}
		});
		
		apply.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// Immediately schedule update
				delayedSearchTimer.schedule(1);
			}
		
		});
		
		reset.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// Reset input fields and schedule update
				name.setText("");
				role.setSelectedIndex(0);
				delayedSearchTimer.schedule(1);
			}
		});
	}
	
	/**
	 * Connect the filter with a userList.
	 * The list must not be <code>null</code>, and
	 * this method must be called before the {@link ListFilter}
	 * can be used.
	 * @param userList
	 */
	public void setUserList(UserList userList) {
		this.userList = userList;
	}

	/**
	 * Returns the selected role, or <code>null</code>
	 * if the 'Any'-option is selected.
	 */
	private Role getRole() {
		int selected = role.getSelectedIndex();
		if(selected <= 0) {
			return null;
		}
		return roles[selected-1];
	}

}

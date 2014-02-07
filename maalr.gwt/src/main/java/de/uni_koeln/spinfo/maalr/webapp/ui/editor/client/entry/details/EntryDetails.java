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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.entry.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.webapp.ui.editor.client.EditorConstants;
//import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.UserService;
//import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.UserServiceAsync;

public class EntryDetails extends Composite {

	private static EntryDetailsUiBinder uiBinder = GWT
			.create(EntryDetailsUiBinder.class);
	private EditorConstants constants = GWT.create(EditorConstants.class);

	@UiField
	TextBox firstName;

	@UiField
	TextBox lastName;

	@UiField
	TextBox email;

	@UiField
	TextBox login;

	@UiField
	ListBox role;

	@UiField
	Button save;

	@UiField
	Button cancel;

	@UiField
	DateBox creationDate;

	@UiField
	DateBox modifiedDate;

	@UiField
	Anchor edits;

	@UiField
	Anchor mailto;

	private Role[] roles;

	private LexEntry unmodified;

	private LexEntry workingCopy;

	// private UserServiceAsync service;

	private AbstractHasData<?> table;

	interface EntryDetailsUiBinder extends UiBinder<Widget, EntryDetails> {
	}

	public EntryDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		// service = GWT.create(UserService.class);
		roles = Role.values();
		for (Role r : roles) {
			role.addItem(r.getRoleName(), r.toString());
		}
		initListeners();
		creationDate.setEnabled(false);
		modifiedDate.setEnabled(false);
	}

	private void initListeners() {
		ChangeHandler handler = new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (workingCopy == null)
					return;
				// workingCopy.setEmail(email.getText());
				// workingCopy.setFirstName(firstName.getText());
				// workingCopy.setLastName(lastName.getText());
				// String roleId = role.getValue(role.getSelectedIndex());
				// Role r = Role.valueOf(roleId);
				// workingCopy.setRole(r);
			}
		};

		email.addChangeHandler(handler);
		firstName.addChangeHandler(handler);
		lastName.addChangeHandler(handler);
		role.addChangeHandler(handler);
		save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (unmodified == null)
					return;
				save(unmodified, unmodified);
			}
		});
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (unmodified == null)
					return;
				createWorkingCopy(unmodified);
			}
		});
		// mailto.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// Window.open("mailto:" + email.getText(), "_blank", "");
		// }
		// });
		// edits.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// Window.alert("This feature is not yet implemented.");
		// }
		// });
	}

	public void setEntry(LexEntry user) {
		checkUnmodifiedChanges(user);
	}

	private void createWorkingCopy(LexEntry user) {
		// this.unmodified = user;
		// this.workingCopy = user.getCopy();
		// firstName.setText(workingCopy.getFirstName());
		// lastName.setText(workingCopy.getLastName());
		// email.setText(workingCopy.getEmail());
		// login.setText(workingCopy.getLogin());
		// creationDate.setValue(new Date(workingCopy.getCreationDate()));
		// modifiedDate.setValue(new
		// Date(workingCopy.getLastModificationDate()));
		// role.setSelectedIndex(getRoleIndex(workingCopy.getRole()));
		// if(user.getLogin().equals("admin")) {
		// role.setEnabled(false);
		// } else {
		// role.setEnabled(true);
		// }
	}

	private int getRoleIndex(Role role) {
		for (int i = 0; i < roles.length; i++) {
			if (roles[i] == role)
				return i;
		}
		return -1;
	}

	private void checkUnmodifiedChanges(LexEntry nextUser) {
		if (unmodified != null && workingCopy != null) {
			if (!workingCopy.equals(unmodified)) {
				boolean doSave = Window
						.confirm(constants.saveChangesQuestion());
				if (doSave) {
					save(unmodified, nextUser);
					return;
				}
			}
		}
		createWorkingCopy(nextUser);
	}

	private void save(final LexEntry currentUser, final LexEntry nextUser) {
		// TODO ???
		final LexEntry toSave = workingCopy;
	}

	public void setDataSource(AbstractHasData<?> table) {
		this.table = table;
	}

	public void setLemmaDescription(LemmaDescription lemmaDescription) {
		// TODO ???
	}

}

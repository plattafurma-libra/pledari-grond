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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.details;

import java.util.Date;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserService;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.list.UserList;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;

public class UserDetails extends Composite {

	private static UserDetailsUiBinder uiBinder = GWT
			.create(UserDetailsUiBinder.class);
	
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
	Button delete;
	
	@UiField
	DateBox creationDate;
	
	@UiField
	DateBox modifiedDate;
	
	@UiField
	Anchor edits;
	
	@UiField
	Anchor mailto;

	private Role[] roles;

	private LightUserInfo unmodified;

	private LightUserInfo workingCopy;

	private UserServiceAsync service;

	private UserList userList;

	interface UserDetailsUiBinder extends UiBinder<Widget, UserDetails> {
	}

	public UserDetails() {
		initWidget(uiBinder.createAndBindUi(this));
		service = GWT.create(UserService.class);
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
				if(workingCopy == null) return;
				workingCopy.setEmail(email.getText());
				workingCopy.setFirstName(firstName.getText());
				workingCopy.setLastName(lastName.getText());
				String roleId = role.getValue(role.getSelectedIndex());
				Role r = Role.valueOf(roleId);
				workingCopy.setRole(r);
			}
		};
		
		email.addChangeHandler(handler);
		firstName.addChangeHandler(handler);
		lastName.addChangeHandler(handler);
		role.addChangeHandler(handler);
		save.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(unmodified == null) return;
				save(unmodified, unmodified);
			}
		});
		cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(unmodified == null) return;
				createWorkingCopy(unmodified);
			}
		});
		delete.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(unmodified == null) return;
				Command command = new Command() {
					
					@Override
					public void execute() {
						service.deleteUser(unmodified, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(Boolean result) {
								userList.reset();
							}
						});
					}
				};
				Dialog.confirm("Confirm deletion", "Do you really want to delete user \"" + unmodified.getEmail() + "\"? This cannot be undone!", "OK", "Cancel", command , null, false);
			}
		});
		mailto.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.open("mailto:" + email.getText(), "_blank", "");
			}
		});
		edits.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("This feature is not yet implemented.");
			}
		});
	}
	
	public void setUser(LightUserInfo user) {
		checkUnmodifiedChanges(user);
	}

	private void createWorkingCopy(LightUserInfo user) {
		this.unmodified = user;
		this.workingCopy = user.getCopy();
		firstName.setText(workingCopy.getFirstName());
		lastName.setText(workingCopy.getLastName());
		email.setText(workingCopy.getEmail());
		login.setText(workingCopy.getLogin());
		creationDate.setValue(new Date(workingCopy.getCreationDate()));
		modifiedDate.setValue(new Date(workingCopy.getLastModificationDate()));
		role.setSelectedIndex(getRoleIndex(workingCopy.getRole()));
		if(user.getLogin().equals("admin")) {
			role.setEnabled(false);
		} else {
			role.setEnabled(true);
		}
	}

	private int getRoleIndex(Role role) {
		for(int i = 0; i < roles.length; i++) {
			if(roles[i] == role) return i;
		}
		return -1;
	}

	private void checkUnmodifiedChanges(LightUserInfo nextUser) {
		if(unmodified != null && workingCopy != null) {
			if(!workingCopy.equals(unmodified)) {
				boolean doSave = Window.confirm("Save changes?");
				if(doSave) {
					save(unmodified, nextUser);
					return;
				}
			}
		}
		createWorkingCopy(nextUser);
	}

	private void save(final LightUserInfo currentUser, final LightUserInfo nextUser) {
		final LightUserInfo toSave = workingCopy;
		Command ok = new Command() {
			
			@Override
			public void execute() {
				service.adminUpdate(toSave, new AsyncCallback<Void>() {
					
					@Override
					public void onSuccess(Void result) {
						currentUser.setEmail(toSave.getEmail());
						currentUser.setFirstName(toSave.getFirstName());
						currentUser.setLastName(toSave.getLastName());
						currentUser.setRole(toSave.getRole());
						userList.getTable().redraw();
						createWorkingCopy(nextUser);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Dialog.showError("Failed to save user", caught);
					}
				});
			}
		};
		Dialog.confirm("Please confirm", "Do you really want to update user " + toSave.getFirstName() + "?", "OK", "Cancel", ok, null, true);
	}
	
	public void setDataSource(UserList userList) {
		this.userList = userList;
	}

}

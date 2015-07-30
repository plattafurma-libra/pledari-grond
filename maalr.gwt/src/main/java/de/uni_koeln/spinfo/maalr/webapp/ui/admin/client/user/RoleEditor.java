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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Controls;
import com.github.gwtbootstrap.client.ui.Fieldset;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.FormType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserService;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.common.help.HelpBox;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.details.UserDetails;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.filter.ListFilter;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.list.UserList;

public class RoleEditor extends Composite {

	private static RoleEditorUiBinder uiBinder = GWT
			.create(RoleEditorUiBinder.class);
	

	interface RoleEditorUiBinder extends UiBinder<Widget, RoleEditor> {
	}
	
	@UiField
	UserList userList;
	
	@UiField
	UserDetails userDetails;
	
	@UiField
	Button createUser;
	
	@UiField
	ListFilter filterOptions;
	
	@UiField
	HelpBox helpBox;

	private UserServiceAsync service;

	public RoleEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		service = GWT.create(UserService.class);
		userDetails.setDataSource(userList);
		userList.addSelectionChangedHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				userDetails.setVisible(userList.getSelectedUser() != null);
				userDetails.setUser(userList.getSelectedUser());
			}
		});
		filterOptions.setUserList(userList);
		helpBox.setHelpText("Use the fields in 'Filter' to query for users. Sort the list of users by selecting a column header. Select a user in the list and modify first name, last name, or assigned role in the 'Account Details' area.");
		userDetails.setVisible(userList.getSelectedUser() != null);
		createUser.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final LightUserInfo newUser = new LightUserInfo();
				
				final Modal modal = new Modal(true);	
				modal.setTitle("Create New User");
				
				final UserForm form = new UserForm();
				
				Button create = new Button("CREATE", new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
//						service.insertNewUser(newUser, new AsyncCallback<LightUserInfo>() {
//							
//							@Override
//							public void onSuccess(LightUserInfo result) {
//								userList.reset();
//							}
//							
//							@Override
//							public void onFailure(Throwable caught) {
//							}
//						});
						modal.hide();
					}
				});
				create.setType(ButtonType.DEFAULT);
				create.setBlock(true);
				
				Button cancel = new Button("CANCEL", new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						modal.hide();
					}
					
				});
				cancel.setType(ButtonType.INVERSE);
				cancel.setBlock(true);
				
				
				ModalFooter modalFooter = new ModalFooter();
				modalFooter.add(create);
				modalFooter.add(cancel);
				modal.add(form);
				modal.add(modalFooter);
				modal.show();
			}
		});
	}
	
	
	
	
}

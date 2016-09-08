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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.SimplePager;
import com.github.gwtbootstrap.client.ui.SimplePager.TextLocation;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.Handler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserService;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;

/**
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 */
public class RoleEditorNew extends Composite {
	
	private static RoleEditorNewUiBinder uiBinder = GWT.create(RoleEditorNewUiBinder.class);
	
	interface RoleEditorNewUiBinder extends UiBinder<Widget, RoleEditorNew> {
	}

	@UiField(provided = true) CellTable<LightUserInfo> cellTable;
	@UiField Button createUser;
	@UiField(provided=true) SimplePager pager;

	private UserServiceAsync service;
	private AsyncCallback<List<LightUserInfo>> callback;
	private AsyncDataProvider<LightUserInfo> provider;
	protected boolean sortAscending;
	protected String sortColumn;
	
	public RoleEditorNew() {
		// Create RPC service
		this.service = GWT.create(UserService.class);
		
		// Initialize data provider 
		initDataProvider();
		
		// Create table 
		cellTable = new CellTable<LightUserInfo>();
		cellTable.setPageSize(10);
		getAndSetRowsCount();
		
		// Create table columns
		Column<LightUserInfo, String> loginColumn = createLoginColumn();
		createRoleColumn();
		createUpdateRoleColumn();
		createDateColumn();
	    createDeleteUserColumn();
	    
		// Connect table with data provider
		provider.addDataDisplay(cellTable);
		
		// Add sort handler
		// FIXME: Still not working...
		cellTable.addColumnSortHandler(new Handler() {
			
			@Override
			public void onColumnSort(ColumnSortEvent event) {
				Column<?, ?> column = event.getColumn();
				// Update sort properties and start a new query
				sortAscending = event.getColumnSortList().get(0).isAscending();
				sortColumn = column.getDataStoreName();
				reset();
			}

			private void reset() {
				cellTable.setVisibleRangeAndClearData(new Range(0, 10), false);
			}
		});		
		
		// Set column to be sorted when showing the table for first time
		cellTable.getColumnSortList().push(loginColumn);
		
		// Add pagination
		pager = new SimplePager(TextLocation.CENTER);
		pager.setDisplay(cellTable);
		pager.setPageSize(10);
		
		// Create remaining widgets not manually provided
		initWidget(uiBinder.createAndBindUi(this));
		createUserButton();
	}

	private Column<LightUserInfo, String> createUpdateRoleColumn() {
		SelectionCell roleCell = new SelectionCell(Arrays.asList(Constants.Roles.ALL_ROLES));
	    Column<LightUserInfo, String> roleUpdateColumn = new Column<LightUserInfo, String>(roleCell) {
	      @Override
	      public String getValue(LightUserInfo object) {
	        return object.getRole().getRoleId();
	      }
	    };
	    cellTable.addColumn(roleUpdateColumn, "Update Role");
	    
	    roleUpdateColumn.setFieldUpdater(new FieldUpdater<LightUserInfo, String>() {
	    	@Override
	    	public void update(int index, LightUserInfo object, String value) {
	    	  
	    	Role[] values = Role.values();
	  		for (Role role : values) {
	  			if(role.getRoleId().equals(value)) {
	  				 object.setRole(role);
	  				 service.updateRole(object, new AsyncCallback<Void>() {
						
						@Override
						public void onSuccess(Void result) {
							cellTable.redraw();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							Logger.getLogger(getClass()).info("UerService.updateRole().Exception :: " + caught.getMessage());
						}
	  				 });
	  			}
	  		}
	      }
	    });
	    return roleUpdateColumn;
	}

	private Column<LightUserInfo, String> createLoginColumn() {
		Column<LightUserInfo, String> loginColumn = new Column<LightUserInfo, String>(new TextCell()) {
			@Override
			public String getValue(LightUserInfo object) {
				return object.getLogin();
			}
		};
		loginColumn.setDataStoreName(LightUserInfo.SORT_LOGIN);
		loginColumn.setSortable(false);
		cellTable.setColumnWidth(loginColumn, 40, Unit.PC);
		cellTable.addColumn(loginColumn, "Login");
		return loginColumn;
	}

	private Column<LightUserInfo, String> createRoleColumn() {
		Column<LightUserInfo, String> roleColumn = new Column<LightUserInfo, String>(new TextCell()) {
			@Override
			public String getValue(LightUserInfo object) {
				return object.getRole().getRoleName();
			}
		};
		roleColumn.setDataStoreName(LightUserInfo.SORT_ROLE);
		roleColumn.setSortable(false);
		cellTable.setColumnWidth(roleColumn, 20, Unit.PC);
		cellTable.addColumn(roleColumn, "Role");
		return roleColumn;
	}

	private Column<LightUserInfo, String> createDateColumn() {
		Column<LightUserInfo, String> createdColumn = new Column<LightUserInfo, String>(new TextCell()) {
			@Override
			public String getValue(LightUserInfo object) {
				DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd '('HH:mm:ss')'");
				return format.format(new Date(object.getCreationDate()));
			}
		};
		cellTable.setColumnWidth(createdColumn, 30, Unit.PC);
		cellTable.addColumn(createdColumn, "Created");
		return createdColumn;
	}

	private Column<LightUserInfo, String> createDeleteUserColumn() {
		Column<LightUserInfo, String> deleteColumn = new Column<LightUserInfo, String>(new ButtonCell()) {
			@Override
			public String getValue(LightUserInfo object) {
				return "remove";
			}
		};
		deleteColumn.setFieldUpdater(new FieldUpdater<LightUserInfo, String>() {
			  public void update(int index, final LightUserInfo object, String value) {
				  deleteUserDialog(object);
			  }
		});
		cellTable.setColumnWidth(deleteColumn, 10, Unit.PC);
		cellTable.addColumn(deleteColumn, "");
		return deleteColumn;
	}

	private void initDataProvider() {
		provider = new AsyncDataProvider<LightUserInfo>() {

			@Override
			protected void onRangeChanged(HasData<LightUserInfo> display) {
				
				final int start = display.getVisibleRange().getStart();
				int length = display.getVisibleRange().getLength();
				
//				Logger.getLogger(getClass()).info("start :: " + start);
//				Logger.getLogger(getClass()).info("length :: " + length);
//				Logger.getLogger(getClass()).info("sortAscending :: " + sortAscending);
//				Logger.getLogger(getClass()).info("sortColumn :: " + sortColumn);
				
				callback = new AsyncCallback<List<LightUserInfo>>() {
					@Override
					public void onFailure(Throwable caught) {
						Logger.getLogger(getClass()).info("UserService.getAllUsers().Exception :: " + caught.getMessage());
					}

					@Override
					public void onSuccess(List<LightUserInfo> result) {
						updateRowData(start, result);
//						Logger.getLogger(getClass()).info("CellTable updated!");
					}
				};
				service.getAllUsers(start, length, sortColumn, sortAscending, callback);
			}
		};
	}

	private void getAndSetRowsCount() {
		service.getNumberOfUsers(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				Logger.getLogger(getClass()).info("UserService.getNumberOfUsers().Exception :: " + caught.getMessage());
			}

			@Override
			public void onSuccess(Integer result) {
//				Logger.getLogger(getClass()).info("UserService.getNumberOfUsers() :: " + result);
				cellTable.setRowCount(result, true);	
			}
		});
	}

	private void createUserButton() {
		createUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				final Modal modal = new Modal(true);
				modal.setTitle("Create New User");

				Button create = new Button("CREATE");
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

				UserForm form = new UserForm(new LightUserInfo(), modal, create, callback);

				ModalFooter modalFooter = new ModalFooter();
				modalFooter.add(create);
				modalFooter.add(cancel);
				modal.add(form);
				modal.add(modalFooter);
				modal.show();
			}
		});
	}
	
	private void deleteUserDialog(final LightUserInfo object) {
		if(object.getLogin().equals("admin")) {
			  Window.alert("Admin user can not be removed!");
			  return;
		  }
		
//		  Logger.getLogger(getClass()).info("Removing user :: " + object);
		  
		  final Modal dialogBox = new Modal();
		  dialogBox.setTitle("Confirm deletion");
		  Paragraph label = new Paragraph("Are you sure you want to delete user '" + object.getLogin() + "'?");
		  dialogBox.add(label);
		  ModalFooter modalFooter = new ModalFooter();
		  dialogBox.add(modalFooter);
		  Button yes = new Button("yes");
		  Button no = new Button("no");
		  modalFooter.add(yes);
		  modalFooter.add(no);
		  yes.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				 service.deleteUser(object, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Logger.getLogger(getClass()).info("UserService.deleteUser().Exception :: " + caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							if(result)
								Logger.getLogger(getClass()).info("User deleted :: " + result);
							getAndSetRowsCount();
							service.getAllUsers(0, Integer.MAX_VALUE, null, false, callback);
							dialogBox.hide();
						}
					});
			}
		});
		  
		no.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogBox.show();
	}

}

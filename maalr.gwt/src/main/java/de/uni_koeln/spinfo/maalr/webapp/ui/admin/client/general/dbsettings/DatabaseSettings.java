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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.dbsettings;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Controls;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.base.ProgressBarBase.Style;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.mongo.stats.DatabaseStatistics;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendService;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;


public class DatabaseSettings extends Composite {

	private static DatabaseSettingsUiBinder uiBinder = GWT.create(DatabaseSettingsUiBinder.class);
	
	@UiField
	Button drop;
	
	@UiField
	Button reload;
	
	@UiField
	Button exportAll;
	
	@UiField
	Button importAll;
	
	@UiField
	Button updateStats;
	
	@UiField
	Paragraph databaseInfos;
	
	@UiField
	ProgressBar busyIndicator;
	
	@UiField
	ButtonGroup leftGroup;
	
	@UiField
	ButtonGroup rightGroup;
	
	private BackendServiceAsync service;

	private NumberFormat nf;
	
	interface DatabaseSettingsUiBinder extends UiBinder<Widget, DatabaseSettings> {
	}
	
	public DatabaseSettings() {
		service = GWT.create(BackendService.class);
		initWidget(uiBinder.createAndBindUi(this));
		busyIndicator.setType(Style.ANIMATED);
		busyIndicator.setPercent(100);
		leftGroup.getElement().getStyle().setFloat(Float.LEFT);
		rightGroup.getElement().getStyle().setFloat(Float.RIGHT);
		nf = NumberFormat.getDecimalFormat();
		initHandlers();
		updateDatabaseStatistics();
	}
	
	private void updateDatabaseStatistics() {
		service.getDatabaseStats(new AsyncCallback<DatabaseStatistics>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(DatabaseStatistics result) {
				databaseInfos.clear();
				databaseInfos.add(new Paragraph("Entries: " + nf.format(result.getNumberOfEntries())));
				databaseInfos.add(new Paragraph("Versions: " + nf.format(result.getNumberOfLemmata())));
				databaseInfos.add(new Paragraph("Approved: " + nf.format(result.getNumberOfApproved())));
				databaseInfos.add(new Paragraph("Suggested: " + nf.format(result.getNumberOfSuggestions())));
				databaseInfos.add(new Paragraph("Deleted: " + nf.format(result.getNumberOfDeleted())));
				databaseInfos.add(new Paragraph("Outdated: " + nf.format(result.getNumberOfOutdated())));
				databaseInfos.add(new Paragraph("Other: " + nf.format(result.getNumberOfUndefined())));
			}
		});
	}

	/**
	 * Initializes handlers for input fields and buttons.
	 */
	private void initHandlers() {
		final AsyncCallback<String> resultCallback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				busyIndicator.setVisible(false);
				Dialog.showError("Operation failed", caught);
			}

			@Override
			public void onSuccess(String result) {
				updateDatabaseStatistics();
				busyIndicator.setVisible(false);
			}
		};
		
		exportAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final Modal modal = new Modal();
				modal.setAnimation(true);
				modal.setTitle("Download Data...");
				VerticalPanel panel = new VerticalPanel();
				ControlGroup group = new ControlGroup();
				Controls all = new Controls();
				final CheckBox allButton = new CheckBox("Export complete version history");
				all.add(allButton);
				final CheckBox allData = new CheckBox("Anonymize data");
				all.add(allData);
				group.add(all);
				panel.add(group);
				panel.add(new Label("For a full backup, export the version history and do not anonymize the entries. " +
						"In any case, the downloaded file will contain a MD5-Checksum which can be used to verify " +
						"the integrity of the data."));
				ModalFooter footer = new ModalFooter();
				Button export = new Button("Download");
				export.setType(ButtonType.PRIMARY);
				footer.add(export);
				export.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						Window.open("../admin/export?all=" + allButton.getValue() + "&dropKeys=" + allData.getValue(), "_blank", null);
						modal.hide();
					}
				});
				Button cancel = new Button("Cancel");
				cancel.addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						modal.hide();
					}
				});
				footer.add(cancel);
				modal.add(panel);
				modal.add(footer);
				modal.show();
			}
		});
		importAll.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ImportDialog dialog = new ImportDialog();
				dialog.show();
			}
		});
		
		drop.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Command ok = new Command() {
					
					@Override
					public void execute() {
						busyIndicator.setVisible(true);
						service.dropDatabase(resultCallback);	
					}
				};
				Dialog.confirm("Deleting all entries", "This operation will drop the entire database and cannot be undone. Do you really want to continue?", "OK", "Cancel", ok, null, false);
			}
		});
		reload.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Command ok = new Command() {
					
					@Override
					public void execute() {
						busyIndicator.setVisible(true);
						service.reloadDatabase(resultCallback);
					}
				};
				Dialog.confirm("Reload database", "This operation will reload the entire database. Do you really want to continue?", "OK", "Cancel", ok, null, false);
			}
		});
		updateStats.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				updateDatabaseStatistics();
			}

			
		});
	}
	

}

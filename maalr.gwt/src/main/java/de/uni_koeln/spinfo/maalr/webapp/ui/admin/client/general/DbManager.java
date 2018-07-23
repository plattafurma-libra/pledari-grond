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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Container;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.common.help.HelpBox;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.backupsettings.BackupSettings;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.dbsettings.DatabaseSettings;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.indexsettings.IndexSettings;

public class DbManager extends Composite {

	private static RoleEditorUiBinder uiBinder = GWT.create(RoleEditorUiBinder.class);

	interface RoleEditorUiBinder extends UiBinder<Widget, DbManager> {
	}

	@UiField
	DatabaseSettings dbSettings;

	@UiField
	IndexSettings indexSettings;

	@UiField
	BackupSettings backupSettings;

	@UiField
	HelpBox helpBox;

	private BackendServiceAsync service;

	@UiField
	Column settingsColumn;

	@UiField
	Container container;

	public DbManager(String contextPath) {
		try {
			initWidget(uiBinder.createAndBindUi(this));
			helpBox.setHelpText("Backup or import a dictionary. Note that you cannot undo or cancel any operation.");
			service = GWT.create(BackendService.class);
			backupSettings.setContext(contextPath);
		} catch (Exception e) {
			Window.alert("Failed to init db mgr: " + e);
		}
	}

}

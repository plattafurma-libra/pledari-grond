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

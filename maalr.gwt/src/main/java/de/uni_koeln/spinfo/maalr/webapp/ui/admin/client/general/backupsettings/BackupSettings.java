package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.backupsettings;

import java.util.List;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.base.DivWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.mongo.stats.BackupInfos;
import de.uni_koeln.spinfo.maalr.mongo.stats.FileInfo;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendService;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendServiceAsync;

public class BackupSettings extends Composite {

	private static BackupSettingsUiBinder uiBinder = GWT.create(BackupSettingsUiBinder.class);

	interface BackupSettingsUiBinder extends UiBinder<Widget, BackupSettings> {
	}
	
	@UiField
	Accordion backupInfo;
	
	@UiField
	DivWidget generalInfo;
	
	AccordionGroup noBackupYet;
		
	private BackupInfos backupInfos; 

	private BackendServiceAsync service;
	

	public BackupSettings() {
		service = GWT.create(BackendService.class);
		initWidget(uiBinder.createAndBindUi(this));
		initBackupInfos();
	}


	private void initBackupInfos() {
		service.getBackupInfos(new AsyncCallback<BackupInfos>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(BackupInfos result) {
				backupInfos = result;
				displayGeneralInfos();
				dispalyBackupInfos();
			}

			private void displayGeneralInfos() {
				Label label = new Label("General Settings");
				label.addStyleName("label-info");
				generalInfo.add(label);
				generalInfo.add(new Paragraph());
				generalInfo.add(new Paragraph("Count: " + backupInfos.getBackupNums()));
				generalInfo.add(new Paragraph("Location: " + backupInfos.getBackupLocation()));
				generalInfo.add(new Paragraph("Time: " + backupInfos.getTriggerTime()));
			}

			private void dispalyBackupInfos() {
				List<FileInfo> infos = backupInfos.getInfos();
				if (infos.size() != 0) {
					if(noBackupYet != null) { 
						backupInfo.remove(noBackupYet);
					}
					for (int i = 0; i < infos.size(); i++) {

						final FileInfo file = infos.get(i);
						AccordionGroup accordionGroup = new AccordionGroup();
						setAccGroupSytle(accordionGroup);

						accordionGroup.setHeading(file.getCreationDate());

						accordionGroup.add(new Paragraph("<b>File</b>: " + file.getName()));
						accordionGroup.add(new Paragraph("<b>Last mod.</b>: " + file.getLastModified()));
						accordionGroup.add(new Paragraph("<b>Size</b>: ~ " + file.getSize() + " MB"));
						Paragraph paragraph = new Paragraph("Download ");
						paragraph.add(new Anchor("link", "/dowanload/backup/" + file.getName()));
						accordionGroup.add(paragraph);

						backupInfo.add(accordionGroup);
					}
				} else {
					noBackupYet = noBackupYet == null ? new AccordionGroup() : noBackupYet;
					setAccGroupSytle(noBackupYet);
					noBackupYet.setHeading("no backups created yet...");
					Paragraph paragraph = new Paragraph();
					paragraph.setText("Check out the maalr configurations or properties setting. The next back is scheduled for (DATE + TIME).");
					noBackupYet.add(paragraph);
					backupInfo.add(noBackupYet);
				}
			}
			
			private void setAccGroupSytle(Widget w) {
				w.getElement().getStyle().setBorderColor("#999999");
			}
		});
	}

}

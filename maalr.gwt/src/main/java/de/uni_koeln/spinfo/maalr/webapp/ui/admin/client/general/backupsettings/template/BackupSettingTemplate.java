package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.backupsettings.template;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface BackupSettingTemplate extends SafeHtmlTemplates {

	@Template("<b>File name</b>: {0}")
	public SafeHtml fileName(String fileName);

	@Template("<b>Created at</b>: {0}")
	public SafeHtml createdAt(String createdAt);

	@Template("<b>Size</b>: {0} MB")
	public SafeHtml size(String size);

	@Template("{0}/downloads/backup/{1}")
	public SafeHtml link(String context, String fileName);



}
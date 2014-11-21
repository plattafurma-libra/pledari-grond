package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client;

import com.google.gwt.i18n.client.Messages;

public interface EditorMessages extends Messages {
	
	public String getAppName(String shortAppName, String editorBackend);

	public String displayResults(int size, int maxEntries);

	public String rejectChanges(int size);

//	public String controlLabelOverlay(String languageName);
	public String controlLabelOverlay();

	public String failedToGetOverlay(String type);

	public String selectType(int size);

	public String displayingEntries(int from, int to, int of);

}

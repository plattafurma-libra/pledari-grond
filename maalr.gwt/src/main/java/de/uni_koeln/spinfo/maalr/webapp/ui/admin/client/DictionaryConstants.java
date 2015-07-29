package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client;

import com.google.gwt.i18n.client.Dictionary;

public class DictionaryConstants {

	protected static final String CONTEXT = "context";

	protected static final String PATH = "path";

	static Dictionary getDictionary() {
		return Dictionary.getDictionary(CONTEXT);
	}

}

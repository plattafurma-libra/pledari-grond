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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;

/**
 * <p>Utility class for dynamic i18n within the
 * {@code de.uni_koeln.spinfo.maalr.webapp.ui.user.User} modules.
 * </p>
 * <p>E.g. in {@link LinkDialog}</p>
 * 
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 * 
 */

public class DictionaryConstants {

	public static final String FALLBACK_PROPERTIES = "properties_rm";
	public static final String PROPERTIES = "properties_";
	public static final String DICT_LINK_LABEL = "dictionary_link_label";
	public static final String KEY_LOCALE = "pl";

	public static final String LINKS = "links_";
	public static final String LINK_SURSILVAN = "link_sursilvan";
	public static final String LINK_PUTER = "link_puter";
	public static final String LINK_VALLADER = "link_vallader";
	public static final String LINK_PLEDARI = "link_pledari";

	public static final String SURSILVAN = "sursilvan";
	public static final String PUTER = "puter";
	public static final String VALLADER = "vallader";
	public static final String PLEDARI = "pledari";

	public static Dictionary getLocaleDictionary() {
		String locale = Window.Location.getParameter(KEY_LOCALE);
		if (locale == null)
			return Dictionary.getDictionary(FALLBACK_PROPERTIES);
		else
			return Dictionary.getDictionary(PROPERTIES + locale);
	}

	public static Dictionary getLinksDictionary() {
		return Dictionary.getDictionary(LINKS);
	}

}

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
package de.uni_koeln.spinfo.maalr.webapp.ui.user.client.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;

/**
 * <p>Utility class for dynamic i18n within the
 * {@code de.uni_koeln.spinfo.maalr.webapp.ui.user.User} modules.
 * </p>
 * <p>E.g. in {@link ExternalLinkDialog}</p>
 * 
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 * 
 */

public class DictionaryConstants {

	protected static final String LOCALE_PARAM = "pl";
	protected static final String LOCALE_FALLBACK = "ss";
	protected static final String LINKS = "links";
	public static final String EXT_LINKS = "ext_links";
	
	public static final List<String> DICT_LINKS;
	public static final List<String> GLOSSAR_LINKS;
	public static final List<String> DICT_LINKS_EXTERNAL;
	
	static {
		
		DICT_LINKS_EXTERNAL = new ArrayList<String>();
		DICT_LINKS_EXTERNAL.add("dict_ulteriurs");
		DICT_LINKS_EXTERNAL.add("pledari");
		
		DICT_LINKS = new ArrayList<String>();
		DICT_LINKS.add("dict_label");
		DICT_LINKS.add("sursilvan");
		DICT_LINKS.add("puter");
		DICT_LINKS.add("vallader");
		DICT_LINKS.add("pledari");
		
		GLOSSAR_LINKS = new ArrayList<String>();
		GLOSSAR_LINKS.add("glossar_label");
		GLOSSAR_LINKS.add("gourmet");
		GLOSSAR_LINKS.add("flora");
		GLOSSAR_LINKS.add("avionary");
		GLOSSAR_LINKS.add("ballape");
		
	}

	public static Dictionary getLocaleDictionary() {
		String locale = Window.Location.getParameter(LOCALE_PARAM);
		if (locale == null)
			return Dictionary.getDictionary(LOCALE_FALLBACK);
		else
			return Dictionary.getDictionary(locale);
	}

	public static Dictionary getLinksDictionary() {
		return Dictionary.getDictionary(LINKS);
	}
	
	public static Dictionary getExtLinksDictionary() {
		return Dictionary.getDictionary(EXT_LINKS);
	}

}

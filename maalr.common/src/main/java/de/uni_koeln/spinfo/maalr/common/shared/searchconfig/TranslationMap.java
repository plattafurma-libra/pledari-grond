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
package de.uni_koeln.spinfo.maalr.common.shared.searchconfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Helper class which stores localized strings as key-value-pairs. A localized
 * string can be requested via {@link TranslationMap#get(String)}. If it is
 * not found, a string denoting the source configuration file and the missing key
 * is returned.
 * @author sschwieb
 *
 */
public class TranslationMap implements Serializable {
	
	private static final long serialVersionUID = -1608628909237116292L;

	private Map<String, String> translations = new HashMap<String, String>();
	
	private String sourceFileName;

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public void put(String key, String value) {
		translations.put(key, value);
	}

	public String get(String key) {
		String value = translations.get(key);
		if(value == null) {
			return "??" + sourceFileName + ":" + key + "??";
		}
		return value;
	}

	public Set<Entry<String, String>> entrySet() {
		return translations.entrySet();
	}

	public String getSourceFileName() {
		return sourceFileName;
	}
	
	

}

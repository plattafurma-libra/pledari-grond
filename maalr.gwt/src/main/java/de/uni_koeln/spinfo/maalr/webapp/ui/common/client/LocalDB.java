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
package de.uni_koeln.spinfo.maalr.webapp.ui.common.client;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.storage.client.Storage;

import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;

public class LocalDB {
	
	private static final long UPDATE_INTERVAL = 1000 * 60 * 60 * 4;

	public static HashMap<String, String> getEditorTranslation(String locale) {
		if(!Storage.isLocalStorageSupported()) return null;
		Storage storage = Storage.getLocalStorageIfSupported();
		String dataKey = "et_data_"+locale;
		String timeKey = "et_time_"+locale;
		String timeString = storage.getItem(timeKey);
		if(timeString == null) {
			return null;
		}
		long time = Long.parseLong(timeString);
		if(time < System.currentTimeMillis() - UPDATE_INTERVAL) {
			Logger.getLogger(LocalDB.class).info("Editor translations outdated.");
			return null;
		}
		String data = storage.getItem(dataKey);
		if(data == null) {
			return null;
		}
		HashMap<String, String> toReturn = parseData(data);
		Logger.getLogger(LocalDB.class).info("Parsed editor translations for locale " + locale);
		return toReturn;
	}
	
	private static HashMap<String, String> parseData(String data) {
		HashMap<String, String> toReturn = new HashMap<String, String>();
		String[] pairs = data.split("\n",-1);
		for (String keyValue : pairs) {
			String[] kv = keyValue.split("\t");
			toReturn.put(kv[0], kv[1]);
		}
		return toReturn;
	}

	public static void setEditorTranslation(String locale, HashMap<String, String> map) {
		if(!Storage.isLocalStorageSupported()) return;
		Storage storage = Storage.getLocalStorageIfSupported();
		String dataKey = "et_data_"+locale;
		String timeKey = "et_time_"+locale;
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, String>> entries = map.entrySet();
		for (Entry<String, String> entry : entries) {
			sb.append(entry.getKey());
			sb.append("\t");
			sb.append(entry.getValue());
			sb.append("\n");
		}
		storage.setItem(dataKey, sb.toString().trim());
		storage.setItem(timeKey, System.currentTimeMillis()+"");
		Logger.getLogger(LocalDB.class).info("Stored translations for locale " + locale);
	}

}

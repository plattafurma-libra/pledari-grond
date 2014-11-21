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
package de.uni_koeln.spinfo.maalr.lucene.query;

import java.util.ArrayList;

import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;

public class MaalrQueryFormatter {
	
	private static UiConfiguration config;

	public static String getQueryLabel(MaalrQuery query) {
		if(config == null) {
			return null;
		}
		ArrayList<String> fields = config.getMainFields();
		StringBuilder builder = new StringBuilder();
		for (String field : fields) {
			String value = query.getValue(field);
			if(value != null) {
				if(builder.length() > 0) {
					builder.append(", ");
				}
				builder.append(value);
			}
		}
		if(builder.length() == 0) {
			return null;
		}
		return builder.toString();
	}

	public static void setUiConfiguration(UiConfiguration configuration) {
		config = configuration;
	}
	

}

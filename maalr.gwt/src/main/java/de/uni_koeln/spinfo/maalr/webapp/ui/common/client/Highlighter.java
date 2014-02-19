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

public class Highlighter {
	
	public static String highlight(String toDisplay, String searchPhrase) {
		if(searchPhrase == null) return toDisplay;
		searchPhrase = searchPhrase.trim();
		if(searchPhrase.length() == 0) return toDisplay;
		String lcSearchPhrase = searchPhrase.toLowerCase();
		String lcDisplay = toDisplay.toLowerCase();
		int index = -1;
		while((index = lcDisplay.indexOf(lcSearchPhrase, index+1)) != -1) {
			String prefix = toDisplay.substring(0, index);
			String suffix = toDisplay.substring(index+searchPhrase.length());
			String replacement = "<b>" + toDisplay.substring(index, index + searchPhrase.length()) + "</b>";
			toDisplay = prefix + replacement + suffix;
			lcDisplay = toDisplay.toLowerCase();
			index += replacement.length();
		}
		return toDisplay;
	}

}

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
package de.uni_koeln.spinfo.maalr.webapp.ui.editor.client;

import com.google.gwt.i18n.client.Dictionary;

/**
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 * 
 */

class DictionaryConstants {

	protected static final String CONTEXT = "context";
	protected static final String PATH = "path";
	protected static final String LOCALE = "locale";
	
	static Dictionary getDictionary() {
		return Dictionary.getDictionary(CONTEXT);
	}


}

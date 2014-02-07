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

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_koeln.spinfo.maalr.common.shared.ClientOptions;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;

public interface CommonServiceAsync {

	void getLemmaDescription(AsyncCallback<LemmaDescription> callback);

	void getSuggestionsForField(String internalName, String query, int limit,
			AsyncCallback<ArrayList<String>> asyncCallback);

	void getCurrentUser(AsyncCallback<LightUserInfo> callback);
	
	void getClientOptions(AsyncCallback<ClientOptions> callback);
	
	void getEditorTranslation(String locale,
			AsyncCallback<HashMap<String, String>> asyncCallback);


}

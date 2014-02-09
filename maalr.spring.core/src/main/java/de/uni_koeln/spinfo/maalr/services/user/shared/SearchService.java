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
package de.uni_koeln.spinfo.maalr.services.user.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.uni_koeln.spinfo.maalr.common.shared.Overlay;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;

@RemoteServiceRelativePath("rpc/lookup")
public interface SearchService extends RemoteService {

	QueryResult search(MaalrQuery maalrQuery);

	ArrayList<UiConfiguration> getUserConfigurations(String locale,
			boolean editor);

	Overlay getOverlay(String overlayType);

	List<String> getSuggestions(String id, String query, int limit);
	
}

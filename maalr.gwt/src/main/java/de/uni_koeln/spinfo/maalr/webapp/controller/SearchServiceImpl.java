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
package de.uni_koeln.spinfo.maalr.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Overlay;
import de.uni_koeln.spinfo.maalr.common.shared.Overlays;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.services.user.shared.SearchService;

@Service("searchService")
public class SearchServiceImpl implements SearchService {
	
	@Autowired
	private Index index;
	
	private Configuration configuration = Configuration.getInstance();

	@Override
	public QueryResult search(MaalrQuery maalrQuery) {
		try {
			QueryResult qr = index.query(maalrQuery, true);
			return qr;
		} catch (InvalidQueryException e) {
			e.printStackTrace();
		} catch (NoIndexAvailableException e) {
			e.printStackTrace();
		} catch (BrokenIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<UiConfiguration> getUserConfigurations(String locale, boolean editor) {
		UiConfiguration defaultConfig = editor ? configuration.getEditorDefaultSearchUiConfig() : configuration.getUserDefaultSearchUiConfig();
		UiConfiguration extendedConfig = editor ? configuration.getEditorExtendedSearchUiConfig() : configuration.getUserExtendedSearchUiConfig();
		return new ArrayList<UiConfiguration>(Arrays.asList(Localizer.localize(defaultConfig, locale), Localizer.localize(extendedConfig, locale)));
	}



	@Override
	public Overlay getOverlay(String overlayType) {
		try {
			return Overlays.get(overlayType);
		} catch (IOException e) {
			Overlay dummy = new Overlay();
			dummy.setForm("This service is currently not available.");
			return dummy;
		}
	}

	@Override
	public List<String> getSuggestions(String id, String query, int limit) {
		System.out.println("Suggestions for field " + id);
		try {
			return index.getSuggestionsForField(id, query, limit);
		} catch (NoIndexAvailableException e) {
			e.printStackTrace();
		} catch (QueryNodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}

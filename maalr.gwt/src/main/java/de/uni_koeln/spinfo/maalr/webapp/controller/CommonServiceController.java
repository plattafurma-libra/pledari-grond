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

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.ClientOptions;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.TranslationMap;
import de.uni_koeln.spinfo.maalr.login.custom.PGAutenticationProvider;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonService;

@Service("commonService")
public class CommonServiceController implements CommonService {

	@Autowired private PGAutenticationProvider authProvider;
	@Autowired private Index index;

	private final LemmaDescription lemmaDescription;

	private ClientOptions clientOptions;
	
	public CommonServiceController() {
		lemmaDescription = Configuration.getInstance().getLemmaDescription();
		clientOptions = Configuration.getInstance().getClientOptions();
	}

	@Override
	public LemmaDescription getLemmaDescription() {
		return lemmaDescription;
	}

	@Override
	public ArrayList<String> getSuggestionsForField(String fieldName,
			String query, int limit) {
		try {
			return index.getSuggestionsForField(fieldName, query, limit);
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
	
	@Override
	public LightUserInfo getCurrentUser() {
		LightUserInfo user = authProvider.getCurrentUser();
		return user;
	}

	@Override
	public ClientOptions getClientOptions() {
		return clientOptions;
	}

	@Override
	public TranslationMap getEditorTranslation(String locale) {
		return Localizer.getEditorTranslations(locale);
	}

}

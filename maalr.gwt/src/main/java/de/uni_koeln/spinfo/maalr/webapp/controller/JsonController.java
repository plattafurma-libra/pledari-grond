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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueFormat;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;

@Controller("jsonService")
public class JsonController {

	@Autowired
	private Index index;
	
	private Configuration configuration = Configuration.getInstance();
	
	private LemmaDescription ld = configuration.getLemmaDescription();
	
	@RequestMapping(value="/json", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
	public void queryJSON(@RequestParam("callback") String callback, MaalrQuery query, @RequestParam String locale, HttpServletResponse response) throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = index.query(query, true);
		List<LemmaVersion> entries = result.getEntries();
		List<Map<String, String>> toReturn = new ArrayList<Map<String, String>>(entries.size());
		for (LemmaVersion entry : entries) {
			String first = ld.toString(entry, UseCase.RESULT_LIST, true);
			String second = ld.toString(entry, UseCase.RESULT_LIST, false);
			Map<String, String> map = new HashMap<String, String>();
			map.put("a", first);
			map.put("b", second);
			if(map.size() > 0) toReturn.add(map);
		}
		JsonResult json = new JsonResult(toReturn);
		if(toReturn.size() == 0) {
			String message = Localizer.getTranslation(locale, "maalr.query.nothing_found.embedded");
			json.setNothingFoundMessage(message.replace("{0}", query.getValue("searchPhrase")));
		}
		ObjectMapper om = new ObjectMapper();
		String string = om.writeValueAsString(json);
		response.setContentType("text/javascript; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(callback);
		out.print('(');
		out.print(string);
		out.print(')');
	}

}

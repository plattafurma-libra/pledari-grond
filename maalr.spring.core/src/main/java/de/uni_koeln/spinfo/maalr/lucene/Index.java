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
package de.uni_koeln.spinfo.maalr.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Constants.Roles;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.configuration.Environment;
import de.uni_koeln.spinfo.maalr.lucene.core.Dictionary;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.IndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;

@Service(value = "searchIndex")
public class Index {

	@Autowired
	private Environment environment;
	private Dictionary dictionary;
	
	@PostConstruct
	public void initialize() throws IOException {
		dictionary = new Dictionary(environment.getLuceneConfig());
	}

	/**
	 * Queries the index and returns the result in form of a
	 * {@link QueryResult}. 
	 */
	public QueryResult query(MaalrQuery maalrQuery, boolean removeInternalData) throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = dictionary.query(maalrQuery);
		if(removeInternalData) return clean(result);
		return result;
	}
	
	public QueryResult queryExact(MaalrQuery maalrQuery, boolean removeInternalData) throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		String exactModifier = Configuration.getInstance().getDictionaryConfig().getExactModifier();
		String[] keyValue = exactModifier.split("=");
		maalrQuery.getQueryMap().put(keyValue[0], keyValue[1]);
		QueryResult result = dictionary.query(maalrQuery);
		if(removeInternalData) return clean(result);
		return result;
	}
	
	public QueryResult getAllStartingWith(String language, String prefix, int page) throws NoIndexAvailableException, BrokenIndexException, InvalidQueryException {
		QueryResult result = dictionary.getAllStartingWith(language, prefix, page);
		return clean(result);
	}
	
	private QueryResult clean(QueryResult result) {
		List<LemmaVersion> entries = result.getEntries();
		for (LemmaVersion lemma : entries) {
			clean(lemma);
		}
		return result;
	}

	private LemmaVersion clean(LemmaVersion lemma) {
		lemma.getMaalrValues().keySet().retainAll(LemmaVersion.PUBLIC_MAALR_KEYS);
		lemma.getEntryValues().remove(LemmaVersion.COMMENT);
		lemma.getEntryValues().remove(LemmaVersion.EMAIL);
		return lemma;
	}
	
	
	
	//@Secured({Roles.TRUSTED_IN_4, Roles.ADMIN_5})
	public IndexStatistics getIndexStatistics() {
		return dictionary.getIndexStatistics();
	}

	@Secured(Roles.ADMIN_5)
	public void reloadIndex() throws NoIndexAvailableException {
		dictionary.reloadIndex();
	}

	@Secured(Roles.ADMIN_5)
	public void dropIndex() throws IndexException {
		dictionary.dropIndex();
	}

	@Secured({Roles.TRUSTED_IN_4, Roles.ADMIN_5})
	public void addToIndex(Iterator<LexEntry> iterator) throws NoDatabaseAvailableException, IndexException {
		dictionary.addToIndex(iterator);
	}

	public ArrayList<String> getSuggestionsForField(String fieldName, String query, int limit) throws NoIndexAvailableException, QueryNodeException, IOException, ParseException {
		return dictionary.getSuggestionsForField(fieldName, query, limit);
	}

	public void update(LexEntry entry) throws IOException {
		dictionary.update(entry);
	}

	public void delete(LexEntry entry) throws IOException {
		dictionary.delete(entry);
	}

	public void updateAll(List<LexEntry> modified) throws IOException {
		for (LexEntry entry : modified) {
			dictionary.update(entry);
		}
	}

}

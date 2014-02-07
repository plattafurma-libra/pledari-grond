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

import java.io.Serializable;
import java.util.ArrayList;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;

/**
 * Holds the data for displaying the query results.
 * 
 * @author matana <saeko.bjagai@gmail.com>
 * 
 */

public class LightQueryResult implements Serializable {

	private static final long serialVersionUID = -7065588378705406351L;

	private ArrayList<LemmaVersion> entries;

	private String language;

	private int maxEntries;

	private int pageSize;

	private String searchPhrase;

	public LightQueryResult() {
	}

	public ArrayList<LemmaVersion> getEntries() {
		return entries;
	}

	public String getLanguage() {
		return language;
	}

	public int getMaxEntries() {
		return maxEntries;
	}

	public int getPageSize() {
		return pageSize;
	}

	public String getSearchPhrase() {
		return searchPhrase;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}

	public void setEntries(ArrayList<LemmaVersion> entries) {
		this.entries = entries;
	}

}

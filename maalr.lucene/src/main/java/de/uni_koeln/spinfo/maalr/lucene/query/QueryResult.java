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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;

@XmlRootElement
public class QueryResult implements Serializable {

	private static final long serialVersionUID = 6228488470783259240L;

	private List<LemmaVersion> entries;
	
	@XmlTransient
	private int maxEntries;

	@XmlTransient
	private int pageSize;
	
	public QueryResult() {
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public QueryResult(List<LemmaVersion> entries, int maxEntries, int pageSize) {
		this.entries = entries;
		this.maxEntries = maxEntries;
		this.pageSize = pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	@XmlElement(name="entry")
	public List<LemmaVersion> getEntries() {
		return entries;
	}

	public void setEntries(List<LemmaVersion> entries) {
		this.entries = entries;
	}

	public int getMaxEntries() {
		return maxEntries;
	}

	public void setMaxEntries(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	@Override
	public String toString() {
		return "QueryResult [entries=" + entries + ", maxEntries=" + maxEntries
				+ ", pageSize=" + pageSize + "]";
	}
	
}

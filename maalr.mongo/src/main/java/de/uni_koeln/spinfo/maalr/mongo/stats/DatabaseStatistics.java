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
package de.uni_koeln.spinfo.maalr.mongo.stats;

import java.io.Serializable;
import java.util.ArrayList;

import de.uni_koeln.spinfo.maalr.mongo.stats.StatEntry.Category;

public class DatabaseStatistics implements Serializable {

	private static final long serialVersionUID = 8517174780704841339L;

	private long numberOfLemmata;
	
	private long numberOfEntries;
	
	private long numberOfSuggestions;

	private long numberOfApproved;

	private long numberOfDeleted;
	
	private long numberOfUndefined;
	
	private ArrayList<StatEntry> dbStats = new ArrayList<StatEntry>();

	private Integer numberOfOutdated;

	
	public long getNumberOfUndefined() {
		return numberOfUndefined;
	}

	public long getNumberOfApproved() {
		return numberOfApproved;
	}

	public void setNumberOfApproved(long numberOfApproved) {
		this.numberOfApproved = numberOfApproved;
	}

	public long getNumberOfDeleted() {
		return numberOfDeleted;
	}

	public void setNumberOfDeleted(long numberOfDeleted) {
		this.numberOfDeleted = numberOfDeleted;
	}

	public void setNumberOfUndefined(long numberOfUndefined) {
		this.numberOfUndefined = numberOfUndefined;
	}

	public long getNumberOfLemmata() {
		return numberOfLemmata;
	}

	public void setNumberOfLemmata(long numberOfLemmata) {
		this.numberOfLemmata = numberOfLemmata;
	}

	public long getNumberOfEntries() {
		return numberOfEntries;
	}

	public void setNumberOfEntries(long numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	public long getNumberOfSuggestions() {
		return numberOfSuggestions;
	}

	public void setNumberOfSuggestions(long numberOfSuggestions) {
		this.numberOfSuggestions = numberOfSuggestions;
	}

	public void addDBProperty(String key, String value) {
		dbStats.add(new StatEntry(key, value, Category.NORMAL));
	}
	
	public void addDBProperty(String key, String value, Category category) {
		dbStats.add(new StatEntry(key, value, category));
	}

	public void setNumberOfOutdated(Integer numberOfOutdated) {
		this.numberOfOutdated = numberOfOutdated;
	}

	public Integer getNumberOfOutdated() {
		return numberOfOutdated;
	}
	

}

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
package de.uni_koeln.spinfo.maalr.common.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LexEntryList implements Serializable {
	
	private static final long serialVersionUID = 1515880841822457782L;
	
	private int overallCount;

	private ArrayList<LexEntry> entries;
	
	public LexEntryList(List<LexEntry> entries, int overall) {
		this.entries = new ArrayList<LexEntry>(entries);
		this.overallCount = overall;
	}
	
	public LexEntryList() {
		
	}

	public int getOverallCount() {
		return overallCount;
	}

	public ArrayList<LexEntry> getEntries() {
		return entries;
	}

	public List<LexEntry> entries() {
		return entries;
	}
	

}

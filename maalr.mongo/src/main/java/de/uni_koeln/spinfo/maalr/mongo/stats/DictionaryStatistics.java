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

import java.util.HashMap;

import de.uni_koeln.spinfo.maalr.mongo.operations.Statistics;

public class DictionaryStatistics {
	
	private static final Statistics statistics = new Statistics();
	
	public static Statistics getStatistics() {
		return new Statistics(statistics);
	}
	
	public static void newSuggestion() {
		statistics.suggestionCounter++;
	}

	public static void userSuggestionAccepted(boolean wasApproved) {
		if(!wasApproved) {
			statistics.suggestionCounter--;
			statistics.entryCounter++;
			statistics.lastChange = System.currentTimeMillis();
		}
	}

	public static void entryDeleted(boolean wasApproved) {
		if(wasApproved) {
			statistics.entryCounter--;
			statistics.lastChange = System.currentTimeMillis();
		}
	}

	public static void newEntry() {
		statistics.entryCounter++;
		statistics.lastChange = System.currentTimeMillis();
	}

	public static void userSuggestionRejected() {
		statistics.suggestionCounter--;
	}

	public static void newEntryModificationSuggestion() {
		statistics.suggestionCounter++;
	}

	public static void newEntryModification() {
		statistics.lastChange = System.currentTimeMillis();
	}

	public static void initialize(int suggestions, int entries, long lastChange, HashMap<String, Integer> overlayCount) {
		statistics.entryCounter = entries;
		statistics.suggestionCounter = suggestions;
		statistics.lastChange = lastChange;
		statistics.overlayCount = new HashMap<String, Integer>(overlayCount);
	}

}

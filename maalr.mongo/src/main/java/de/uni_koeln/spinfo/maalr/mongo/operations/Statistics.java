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
package de.uni_koeln.spinfo.maalr.mongo.operations;

import java.util.HashMap;

public class Statistics {
	
	public int suggestionCounter;
	public int entryCounter;
	public long lastChange;
	public HashMap<String, Integer> overlayCount;

	public Statistics(Statistics toClone) {
		this.suggestionCounter = toClone.suggestionCounter;
		this.entryCounter = toClone.entryCounter;
		this.lastChange = toClone.lastChange;
		this.overlayCount = toClone.overlayCount;
	}
	
	public Statistics() {}
	
}

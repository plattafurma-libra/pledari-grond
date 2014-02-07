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
package de.uni_koeln.spinfo.maalr.lucene.stats;

import java.io.Serializable;
import java.util.HashMap;

public class IndexStatistics implements Serializable {

	private static final long serialVersionUID = -8108629687068738531L;

	private int numberOfEntries;
	
	private int approvedEntries;
	
	private int unverifiedEntries;
	
	private int unknown;

	private long lastUpdated;

	private HashMap<String, Integer> overlayCount;
	
	public HashMap<String, Integer> getOverlayCount() {
		return overlayCount;
	}

	public int getUnknown() {
		return unknown;
	}

	public void setUnknown(int unknown) {
		this.unknown = unknown;
	}

	public int getApprovedEntries() {
		return approvedEntries;
	}

	public void setApprovedEntries(int approvedEntries) {
		this.approvedEntries = approvedEntries;
	}

	public int getUnverifiedEntries() {
		return unverifiedEntries;
	}

	public void setUnverifiedEntries(int unverifiedEntries) {
		this.unverifiedEntries = unverifiedEntries;
	}

	public int getNumberOfEntries() {
		return numberOfEntries;
	}

	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "IndexStatistics [numberOfEntries=" + numberOfEntries
				+ ", approvedEntries=" + approvedEntries
				+ ", unverifiedEntries=" + unverifiedEntries + ", unknown="
				+ unknown + ", lastUpdated=" + lastUpdated + "]";
	}

	public void setOverlayCount(HashMap<String, Integer> byCategory) {
		this.overlayCount = byCategory;
	}
	
	
	

}

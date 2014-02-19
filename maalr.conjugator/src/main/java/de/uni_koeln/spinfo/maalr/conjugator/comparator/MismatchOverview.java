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
package de.uni_koeln.spinfo.maalr.conjugator.comparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class MismatchOverview {

	HashMap<String, ArrayList<Integer>> overview;
	ArrayList<HashMap<String, String>> diffsList;
	HashMap<String, String> diff;
	HashMap<String, String> misMap;
	String verb;

	ArrayList<String> misStrings;
	ArrayList<Integer> misInts;

	public MismatchOverview(String verb, ArrayList<Integer> misInts,
			ArrayList<HashMap<String, String>> diffList) {
		this.verb = verb;
		this.misInts = misInts;
		this.diffsList = diffList;
	}

	public HashMap<String, ArrayList<Integer>> getOverview() {
		return overview;
	}

	public void setOverview(HashMap<String, ArrayList<Integer>> overview) {
		this.overview = overview;
	}

	public HashMap<String, String> getDiff() {
		return diff;
	}

	public void setDiff(HashMap<String, String> diff) {
		this.diff = diff;
	}

	public HashMap<String, String> getMismap() {
		return misMap;
	}

	public void setMismap(HashMap<String, String> mismap) {
		this.misMap = mismap;
	}

	public ArrayList<String> getMisStrings() {
		return misStrings;
	}

	public void setMisStrings(ArrayList<String> misStrings) {
		this.misStrings = misStrings;
	}

	public ArrayList<Integer> getMisInts() {
		return misInts;
	}

	public void setMisInts(ArrayList<Integer> misInts) {
		this.misInts = misInts;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public ArrayList<HashMap<String, String>> getDiffList() {
		return diffsList;
	}

	public void setDiffList(ArrayList<HashMap<String, String>> diffList) {
		this.diffsList = diffList;
	}

	static class MismatchOverviewComparator implements
			Comparator<MismatchOverview> {
		public int compare(MismatchOverview o1, MismatchOverview o2) {
			return o1.getVerb().compareToIgnoreCase(o2.getVerb());
		}
	}

}

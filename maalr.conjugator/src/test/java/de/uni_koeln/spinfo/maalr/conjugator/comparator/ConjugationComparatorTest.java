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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

//@Ignore("Requires an installed and configured mysql-db.")
public class ConjugationComparatorTest {

	private static ConjugationComparator comparator;

	private static ArrayList<MismatchOverview> mismatchList;

	@BeforeClass
	public static void initialize() throws Exception {
		comparator = new ConjugationComparator();
		// mismatchList = comparator.getMismatchOverviewList();
		// More specific test to be executed
		// assertFalse(mismatchList.isEmpty());

	}

	@Test
	public void testGetOneMismatchList() throws Exception {
		ArrayList<MismatchOverview> list_1 = comparator
				.getOneMismatchList(mismatchList);
		// More specific test to be executed
		assertTrue(!list_1.isEmpty());
		assertFalse(list_1.size() == mismatchList.size());
		File f = comparator.outputOneMismatch(list_1, "outputOneMismatch.txt");
		assertTrue(f.length() != 0);

	}

	@Test
	public void testOutputSpecMismatches() throws Exception {
		// testing with 3 missmatches
		Map<String, List<Integer>> map = comparator.outputSpecMismatches(
				mismatchList, 3);
		// More specific test to be executed
		assertTrue(!map.isEmpty());

	}

	@Test
	public void testOutputMismatches() throws Exception {
		// Search for verbs with 1 mismatches, avoid those ones with 0
		Map<String, List<Integer>> map = comparator.outputMismatches(
				mismatchList, 1, 0);
		// More specific test to be executed
		assertTrue(!map.isEmpty());
		File f = comparator.printMap(map,
				ConjugationComparator.mismatches_path, "mismatches_overview");
		assertTrue(f.length() != 0);
	}

	@Test
	public void testOutputMismatchesB() throws Exception {
		// Search for verbs with 1 mismatches, avoid those ones with 0
		Map<String, Map<String, Map<String, String>>> map = comparator
				.outputMismatchesB(mismatchList, 1, 0);
		// More specific test to be executed
		assertTrue(!map.isEmpty());
		File f = comparator.printMismatchesB(map, "printMismatchesB.txt");
		assertTrue(f.length() != 0);

	}

	@Ignore
	@Test
	public void testPrintSet() {
		fail("testPrintSet() has not been implemented");
	}

}

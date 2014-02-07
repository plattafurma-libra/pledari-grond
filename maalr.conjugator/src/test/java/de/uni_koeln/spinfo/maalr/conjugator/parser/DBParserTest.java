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
package de.uni_koeln.spinfo.maalr.conjugator.parser;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.uni_koeln.spinfo.maalr.conjugator.generator.Pronouns;

public class DBParserTest {

	private static DBParser parser;
	private static Map<String, Map<String, String>> bigMap;

	@BeforeClass
	public static void initialize() throws Exception {
		parser = new DBParser();
//		FileInputStream fstream = new FileInputStream(
//				DBParser.path_to_unparsed_verbs_filemaker);
//		DataInputStream in = new DataInputStream(fstream);

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(DBParser.path_to_unparsed_verbs_filemaker_final), "UTF-8"));
		bigMap = parser.parseDB(br);
		assertTrue(bigMap.containsKey("abandunar"));
		assertTrue(bigMap.containsKey("torclar"));

	}
	
	
	@Test public void testGetConjugation(){
		
		
		Map<String, String> conjugation = bigMap.get("chastrar");
		
		for (Entry<String, String> entry : conjugation.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println(key + "\t" + value + "\n");
		}

		
		
	}

	@Test
	public void testCreateSQL() {
		
		parser.createSQLTable();

	}

	@Test
	public void testCreateSQL_RP() {
	
		parser.createSQLTable_RP();

	}

	@Ignore
	@Test
	public void testMapToMongo() throws Exception {
		parser.mapToMongo(bigMap);
	}

	@Test
	public void testMapToSQL() throws Exception {
		parser.mapToSQL(bigMap);
	}

	@Test
	public void testMapToSQL_RP() throws Exception {
		parser.mapToSQL_RP(bigMap);
	}

	@Test
	public void testGetInfinitivesSet() throws Exception {

		Set<String> inf = parser.getInfinitives("conjugation");
		// Set<String> inf_rp = parser.getInfinitives("conjugation_rp");

		// Set<String> missing = new TreeSet<>();
		//
		// for (String s : inf) {
		// if (!inf_rp.contains(s)) {
		// missing.add(s);
		// }
		//
		// }

		parser.printSet(inf, "s_conjugation");

	}

	@Test
	public void testPronouns() {

		boolean reflexive = false;
		boolean vocal = false;

		String string = "estripar";

		reflexive = true;

		parser.isVocal(string);

		if (parser.isVocal(string)) {

			vocal = true;
		}

		if (reflexive == true) {
			if (vocal == true) {
				string = Pronouns.pron_r_v_1ps + string;
			} else if (vocal == false) {
				string = Pronouns.pron_r_1ps + string;
			}
		} else {
			string = "not reflexive";
		}

		System.out.println(string);

	}

	@Test
	public void testGetInfinitivesList() throws Exception {

		List<String> inf = parser.getAllInfinitives("conjugation");
		List<String> inf_rp = parser.getAllInfinitives("conjugation_rp");

		List<String> missing = new LinkedList<>();

		for (String s : inf_rp) {
			if (!inf.contains(s)) {
				missing.add(s);
			}

		}
		parser.printList(inf, "list_conjugation");
		parser.printList(inf_rp, "list_conjugation_rp");
		parser.printList(missing, "list_missing");

	}

	@Test
	public void testCountInfintives() {

		int count = 0;
		;

		LinkedList<String> listFromMap = new LinkedList<>();

		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {

			String verb = entry.getKey();
			listFromMap.add(verb);

		}

		System.out.println(count);
	}

	@Ignore
	@Test
	public void testPrintAllInfinitives() throws Exception {
		parser.printAllInfinitives(bigMap, "testPrintAllInfinitives");

	}

	@Ignore
	@Test
	public void testPrintReflexives() throws Exception {
		parser.printReflexives(bigMap, "testPrintReflexives");
	}

	@Test
	public void testAvoidES() {
		String s = parser.avoidNullPointer("");
		// assertEquals(s, "test");
		System.out.println(s);

	}

	@Test
	public void testAvoidEmptyString() {
		String s = parser.avoidEmptyString("	");
		// assertEquals(s, "test");
		System.out.println(s);

	}

	@Test
	public void checkWeirdSymbols() throws IOException {
		FileInputStream fstream = new FileInputStream("data/genfiles/fail.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String s;

		while ((s = br.readLine()) != null) {

			// System.out.println(s);

			int i = s.charAt(7);
			System.out.println("wierdy :" + i);

			int codepoint = Character.codePointAt(s, 7);

			System.out.println("wierdy :" + codepoint);

		}

		br.close();
	}

	@Test
	public void testCountInfinitivesB() throws Exception {
		int i = parser.countInfinitives(bigMap);
		System.out.println(i);

	}

	@Test
	public void testCountReflexives() {
		int i = parser.countReflexives(bigMap);
		System.out.println(i);
	}

	@Test
	public void testGetPPInfo() throws FileNotFoundException, Exception {

		parser.getPPInfo(bigMap, "pp_info_rfl");

	}

	@Test
	public void testGetEntriesRGDE() throws Exception {

		parser.getEntriesRGDE("entries_RG-DE.txt");

	}

	@Test
	public void testGetEntriesDERG() throws Exception {

		parser.getEntriesDERG("entries_DE-RG.txt");

	}

	@Test
	public void testGetUniqueEntriesRGDE() throws Exception {

		Set<String> types = parser.getUniqueEntriesRGDE();

		parser.printSet(types, "unique_entries_RG-DE.txt");

	}

	@Test
	public void testGetUniqueEntriesDERG() throws Exception {

		Set<String> types = parser.getUniqueEntriesDERG();

		parser.printSet(types, "unique_entries_DE-RG.txt");

	}

}

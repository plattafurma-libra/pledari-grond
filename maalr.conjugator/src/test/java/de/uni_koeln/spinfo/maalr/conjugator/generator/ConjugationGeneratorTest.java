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
package de.uni_koeln.spinfo.maalr.conjugator.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConjugationGeneratorTest {

	private static ConjugationGenerator conjugationGenerator;
	private static String query;
	private static String root;

	@BeforeClass
	public static void initialize() {
		conjugationGenerator = new ConjugationGenerator();
		query = "sa caminar";
		root = conjugationGenerator.getRoot(query);

	}

	@Test
	public void testGetRoot() {
		String root = conjugationGenerator.getRoot(query);
		assertEquals(root, "affid");
	}

	@Test
	public void testCheckReflexiveness() {
		conjugationGenerator.checkReflexiveness(query);
		String reflexiveness = conjugationGenerator.getIsReflexive();
		assertEquals(reflexiveness, "true");

	}

	@Test
	public void testGenerateConjugation() {
		// CONJ. CLASS 1
		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(query, 8);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
	}

	@Test
	public void testGenerateAll() {
		ArrayList<HashMap<String, String>> conjugations = conjugationGenerator
				.generateAll(query);
		HashMap<String, String> c1 = conjugations.get(0);
		assertTrue(c1.get("participperfectfp").equals("affidadas"));
	}

	@Test
	public void testFirstConjugation() {
		HashMap<String, String> conjugation = conjugationGenerator
				.firstConjugation(root);
		assertTrue(conjugation.get("participperfectfp").equals("affidadas"));
	}

	@Test
	public void testSecondAndThirdConjugationConjugation() {
		HashMap<String, String> conjugation = conjugationGenerator
				.secondAndThirdConjugation(root);
		assertTrue(conjugation.get("preschentsing2").equals("affidas"));
	}

	@Test
	public void testFourthConjugation() {
		HashMap<String, String> conjugation = conjugationGenerator
				.fourthConjugation(root);
		assertFalse(conjugation.get("participperfectfp").equals("affidadas"));
	}

	@Test
	public void testFifthConjugation() {
		HashMap<String, String> conjugation = conjugationGenerator
				.fifthConjugation(root);
		assertFalse(conjugation.get("participperfectfp").equals("affidadas"));
	}

	@Test
	public void testSixthConjugation() {
		HashMap<String, String> conjugation = conjugationGenerator
				.sixthConjugation(root);
		assertTrue(conjugation.get("participperfectfp").equals("affidadas"));
	}

	@Test
	public void testSeventhConjugation() {
		HashMap<String, String> conjugation = conjugationGenerator
				.seventhConjugation(root);
		assertTrue(conjugation.get("participperfectfp").equals("affidadas"));
	}

	@Test
	public void testSetImperativ() {
		String root = "affidar";
		ConjugationStructure cs = new ConjugationStructure();
		cs.setConjugationClass("art-1");

		conjugationGenerator.setImperativ(root, cs);

		HashMap<String, String> map = cs.getValues();

		conjugationGenerator.printConjugation(map);

	}

	@Test
	public void testSetFutur() {

		String root = conjugationGenerator.getRoot("SA PRESTAR");

		ConjugationStructure cs = new ConjugationStructure();

		cs.setInfinitiv(conjugationGenerator.getInfinitiv());

		cs.setReflexive(conjugationGenerator.getIsReflexive());

		conjugationGenerator.setFutur(root, cs);

		HashMap<String, String> map = cs.getValues();

		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(map);
		conjugationGenerator.printConjugation(treeMap);

	}

}

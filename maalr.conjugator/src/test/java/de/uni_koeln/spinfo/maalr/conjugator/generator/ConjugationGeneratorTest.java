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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class ConjugationGeneratorTest {

	private static ConjugationGenerator conjugationGenerator;
	private static String query;
	private static String root;
	private static String outputPath = "output/";

	@Before
	public void setUp() {
		conjugationGenerator = new ConjugationGenerator();

	}
	
	@Test
	public void testGenerateOne() throws IOException {
		
		String q = "quietar";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 1);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
		conjugationGenerator.printMapReadable(tm, outputPath, "VerbenAuf-AR");
	}
	
	@Test
	public void testGenerateTwo() throws IOException {
		
		String q = "spargnear";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 2);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
		conjugationGenerator.printMapReadable(tm, outputPath, "VerbenAuf-EAR");
	}
	
	@Test
	public void testGenerateThree() throws IOException {
		
		String q = "parer";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 3);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
		conjugationGenerator.printMapReadable(tm, outputPath, "VerbenAuf-ER");
	}
	
	@Test
	public void testGenerateFour() throws IOException {
		
		String q = "sortir";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 4);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
		conjugationGenerator.printMapReadable(tm, outputPath, "VerbenAuf-IR");
	}
	
	@Test
	public void testGenerateFive() throws IOException {
		
		String q = "cumbinar";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 5);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
		conjugationGenerator.printMapReadable(tm, outputPath, "VerbenAuf-AR(-esch)");
	}
	
	@Test
	public void testGenerateSix() throws IOException {
		
		String q = "inditgear";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 6);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
		conjugationGenerator.printMapReadable(tm, outputPath, "VerbenAuf-EAR(-esch)");
	}
	
	@Test
	public void testGenerateSeven() throws IOException {
		
		String q = "amplanir";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 7);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
		conjugationGenerator.printMapReadable(tm, outputPath, "VerbenAuf-IR(-esch)");
	}
		
	
	@Test
	public void testGetRoot() {
		query = "gidar";
		String root = conjugationGenerator.getRoot(query);
		System.out.println(root);
		assertEquals(root, "gid");
	}

	@Test
	public void testCheckReflexiveness() {
		conjugationGenerator.checkReflexiveness("sacrer");
		String reflexiveness = conjugationGenerator.getIsReflexive();
		assertEquals(reflexiveness, "true");

	}

	@Test
	public void testGenerateConjugation() {
		// CONJ. CLASS 1
		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(query, 7);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
	}

	@Test
	public void testGenerateAll() {
		query = "gidar";
		ArrayList<HashMap<String, String>> conjugations = conjugationGenerator
				.generateAll(query);
		HashMap<String, String> c1 = conjugations.get(0);
		assertTrue(c1.get("participperfectfp").equals("gidadas"));
	}

	@Test
	public void testFirstConjugation() {
		query = "gidar";
		HashMap<String, String> conjugation = conjugationGenerator
				.firstConjugation(conjugationGenerator.getRoot(query));
		System.out.println("Partizip Perfekt f pl :\"" + conjugation.get("participperfectfp") + "\"");
		assertTrue(conjugation.get("participperfectfp").equals("gidadas"));
		
	}
	
	@Test
	public void testFirstConjugationOutput() {
		query = "gidar";
		HashMap<String, String> conjugation = conjugationGenerator
				.firstConjugation(conjugationGenerator.getRoot(query));
		//System.out.println("Partizip Perfekt f pl :\"" + conjugation.get("participperfectfp") + "\"");
		assertTrue(conjugation.get("participperfectfp").equals("gidadas"));
		
		
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		//treeMap.putAll(conjugationGenerator.getV);
		conjugationGenerator.printConjugation(treeMap);
		
	}
	
	

	@Test
	public void testSecondConjugationConjugation() {
		query = "spargnear";
		HashMap<String, String> conjugation = conjugationGenerator
				.secondConjugation(conjugationGenerator.getRoot(query));
		assertTrue(conjugation.get("preschentsing2").equals("spargnas"));
	}
	
	@Test
	public void testThirdConjugationConjugation() {
		query = "repeter";
		HashMap<String, String> conjugation = conjugationGenerator
				.thirdConjugation(conjugationGenerator.getRoot(query));
		assertTrue(conjugation.get("preschentsing2").equals("repetas"));
	}

	@Test
	public void testFourthConjugation() {
		query = "partir";
		HashMap<String, String> conjugation = conjugationGenerator
				.fourthConjugation(conjugationGenerator.getRoot(query));
		System.out.println("Partizip Perfekt f pl :\"" + conjugation.get("participperfectfp") + "\"");
		assertTrue(conjugation.get("participperfectfp").equals("partidas"));
	}

	@Test
	public void testFifthConjugation() {
		query = "cumbinar";
		HashMap<String, String> conjugation = conjugationGenerator
				.fifthConjugation(conjugationGenerator.getRoot(query));
		assertTrue(conjugation.get("imperativ1").equals("cumbinescha!"));
	}

	@Test
	public void testSixthConjugation() {
		query = "inditgear";
		HashMap<String, String> conjugation = conjugationGenerator
				.sixthConjugation(conjugationGenerator.getRoot(query));
		assertTrue(conjugation.get("conjunctivplural3").equals("inditgesch[i]an"));
	}

	@Test
	public void testSeventhConjugation() {
		query = "amplanir";
		HashMap<String, String> conjugation = conjugationGenerator
				.seventhConjugation(conjugationGenerator.getRoot(query));
		assertTrue(conjugation.get("conjunctivsing3").equals("amplaneschi"));
	}

	@Test
	public void testSetImperativ() {
		String root = "gid";
		ConjugationStructure cs = new ConjugationStructure();
		cs.setConjugationClass("art-1");

		conjugationGenerator.setImperativ(root, cs);

		HashMap<String, String> map = cs.getValues();

		conjugationGenerator.printConjugation(map);

	}

	@Test
	public void testSetFutur() {

		String root = conjugationGenerator.getRoot("inditgear");

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

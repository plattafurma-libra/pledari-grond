/*******************************************************************************
 * Copyright 2013-2016 Sprachliche Informationsverarbeitung, University of Cologne
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class ConjugationGeneratorTest {

	private static ConjugationGenerator conjGen;
	private static String query;
	private static String outPath = "output/";
	private String conjType;
	private HashMap<String, String> conjugation;

	@Before
	public void setUp() {
		conjGen = new ConjugationGenerator();
		new File(outPath).mkdir();
	}


	@Test
	public void testGenerateConjugation() throws FileNotFoundException,
			IOException {

		conjugation = conjGen.generateConjugation("mussar", 9);

		conjGen.printConjToFile(conjugation, outPath, "ART-9_mussar");

	}

	@Test
	public void testEndsWithDoubleConsonant() {
		System.out.println(conjGen.endsWithDoubleConsonant("muss"));
		String s = "muss";
		s = s.substring(0, s.length() - 1);
		System.out.println(s);

	}

	@Test
	public void testGenerateOne() throws IOException {

		String q = "gidar";

		conjugation = conjGen.generateConjugation(q, 1);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenAuf-AR");
	}

	@Test
	public void testGenerateTwo() throws IOException {

		// String q = "angraztgear";
		String q = "spargnear";

		conjugation = conjGen.generateConjugation(q, 2);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenAuf-EAR");
	}

	@Test
	public void testGenerateThree() throws IOException {

		String q = "repeter";

		conjugation = conjGen.generateConjugation(q, 3);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenAuf-ER");
	}

	@Test
	public void testGenerateFour() throws IOException {

		String q = "partir";

		conjugation = conjGen.generateConjugation(q, 4);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenAuf-IR");
	}

	@Test
	public void testGenerateFive() throws IOException {

		String q = "cumbinar";

		conjugation = conjGen.generateConjugation(q, 5);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenAuf-AR(-esch)");
	}

	@Test
	public void testGenerateSix() throws IOException {

		String q = "inditgear";

		conjugation = conjGen.generateConjugation(q, 6);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenAuf-EAR(-esch)");
	}

	@Test
	public void testGenerateSeven() throws IOException {

		// String q = "capir";
		String q = "amplanir";

		conjugation = conjGen.generateConjugation(q, 7);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenAuf-IR(-esch)");
	}

	@Test
	public void testGenerateEight() throws IOException {

//		String q = "ampruvar";
//		String q = "sgular";
//		String q = "andrizar";
//		String q = "clamar";
		String q = "dasdar";

		conjugation = conjGen.generateConjugation(q, 8);
		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);
		conjGen.printConjugation(tm);
		conjGen.printConjToFile(tm, outPath, "VerbenMitVokalwechsel");
	}
	
	@Test
	public void testGetRoot() {
		query = "gidar";
		String root = conjGen.getRoot(query);
		System.out.println("---");
		System.out.println("ROOT: " + root);
		System.out.println("---");
		System.out.println();
		assertEquals(root, "gid");
	}

	@Test
	public void testGenerateAll() {
		query = "gidar";
		ArrayList<HashMap<String, String>> conjugations = conjGen
				.generateAll(query);
		HashMap<String, String> c1 = conjugations.get(0);
		assertTrue(c1.get("participperfectfp").equals("gidadas"));
	}

	@Test
	public void testFirstConjugation() {
		query = "gidar";
		conjType = "art-1";
		conjugation = conjGen.conjugate(conjGen.getRoot(query), conjType);
		System.out.println("Partizip Perfekt f pl :\""
				+ conjugation.get("participperfectfp") + "\"");
		assertTrue(conjugation.get("participperfectfp").equals("gidadas"));

	}

	@Test
	public void testFirstConjugationOutput() {
		query = "gidar";
		conjType = "art-1";
		conjugation = conjGen.conjugate(conjGen.getRoot(query), conjType);
		// System.out.println("Partizip Perfekt f pl :\"" +
		// conjugation.get("participperfectfp") + "\"");
		assertTrue(conjugation.get("participperfectfp").equals("gidadas"));

		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		// treeMap.putAll(conjugationGenerator.getV);
		conjGen.printConjugation(treeMap);
	}

	@Test
	public void testSecondConjugation() {
		query = "spargnear";
		conjType = "art-2";
		HashMap<String, String> conjugation = conjGen.conjugate(
				conjGen.getRoot(query), conjType);
		assertTrue(conjugation.get("preschentsing2").equals("spargnas"));
	}

	@Test
	public void testThirdConjugation() {
		query = "repeter";
		conjType = "art-3";
		HashMap<String, String> conjugation = conjGen.conjugate(
				conjGen.getRoot(query), conjType);
		assertTrue(conjugation.get("preschentsing2").equals("repetas"));
	}

	@Test
	public void testFourthConjugation() {
		query = "partir";
		conjType = "art-4";
		HashMap<String, String> conjugation = conjGen.conjugate(
				conjGen.getRoot(query), conjType);
		System.out.println("Partizip Perfekt f pl :\""
				+ conjugation.get("participperfectfp") + "\"");
		assertTrue(conjugation.get("participperfectfp").equals("partidas"));
	}

	@Test
	public void testFifthConjugation() {
		query = "cumbinar";
		conjType = "art-5";
		HashMap<String, String> conjugation = conjGen.conjugate(
				conjGen.getRoot(query), conjType);
		assertTrue(conjugation.get("imperativ1").equals("cumbinescha!"));
	}

	@Test
	public void testSixthConjugation() {
		query = "inditgear";
		conjType = "art-6";
		HashMap<String, String> conjugation = conjGen.conjugate(
				conjGen.getRoot(query), conjType);
		assertTrue(conjugation.get("conjunctivplural3").equals(
				"inditgesch(i)an"));
	}

	@Test
	public void testSeventhConjugation() {
		query = "amplanir";
		conjType = "art-7";
		HashMap<String, String> conjugation = conjGen.conjugate(
				conjGen.getRoot(query), conjType);
		assertTrue(conjugation.get("conjunctivsing3").equals("amplaneschi"));
	}

	@Test
	public void testEightConjugation() {
		query = "ampruvar";
		conjType = "art-8";
		HashMap<String, String> conjugation = conjGen.conjugate(
				conjGen.getRoot(query), conjType);
		System.out.println(conjugation.get("conjunctivsing3"));
		assertTrue(conjugation.get("conjunctivsing3").equals("ampruvi"));
	}

	@Test
	public void testSetImperativ() {
		String root = "gid";
		ConjugationStructure cs = new ConjugationStructure ();
		cs.setConjugationClass("art-1");
		conjGen.setImperativ(root, cs);
		HashMap<String, String> map = cs.getValues();
		conjGen.printConjugation(map);
	}

	@Test
	public void testSetFutur() {
		String root = conjGen.getRoot("inditgear");
		ConjugationStructure cs = new ConjugationStructure();
		cs.setInfinitiv(conjGen.getInfinitiv());
		conjGen.setFutur(root, cs);
		HashMap<String, String> map = cs.getValues();
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(map);
		conjGen.printConjugation(treeMap);
	}
}

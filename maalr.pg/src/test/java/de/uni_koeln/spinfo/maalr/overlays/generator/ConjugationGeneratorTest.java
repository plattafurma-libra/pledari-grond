package de.uni_koeln.spinfo.maalr.overlays.generator;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConjugationGeneratorTest {
	
	private static ConjugationGenerator conjugationGenerator;

	@BeforeClass
	public static void initialize() {
		conjugationGenerator = new ConjugationGenerator();

	}

	@Test
	public void testGenerateConjugation() {
		String q = "pabrachar";

		HashMap<String, String> conjugation = conjugationGenerator
				.generateConjugation(q, 6);

		TreeMap<String, String> tm = new TreeMap<>();
		tm.putAll(conjugation);

		conjugationGenerator.printConjugation(tm);
	}

	

	@Test
	public void testGetRoot() {
		
		String query = "abrachair";
		
		String q = conjugationGenerator.getRoot(query);
		
		System.out.println(q);
		
	}

}

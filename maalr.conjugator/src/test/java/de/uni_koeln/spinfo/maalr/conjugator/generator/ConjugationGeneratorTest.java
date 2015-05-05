package de.uni_koeln.spinfo.maalr.conjugator.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConjugationGeneratorTest {

	private static ConjugationGenerator generator;
	private HashMap<String, String> conjugation;

	@BeforeClass
	public static void initialize() {
		generator = new ConjugationGenerator();

	}

	@Test
	public void testGenerateConjugation() throws FileNotFoundException,
			IOException {

		conjugation = generator.generateConjugation("mussar", 9);

		generator.printConjugation(conjugation, "ART-9_mussar");

	}

	@Test
	public void testEndsWithDoubleConsonant() {

		System.out.println(generator.endsWithDoubleConsonant("muss"));
		String s = "muss";
		s = s.substring(0, s.length() - 1);
		System.out.println(s);

	}

}

package de.uni_koeln.spinfo.maalr.conjugator.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class ConjugationGeneratorTest {

	private static ConjugationGenerator generator;
	private static String query;
	private HashMap<String, String> conjugation;

	@BeforeClass
	public static void initialize() {
		generator = new ConjugationGenerator();
		//query = "s'infurmar";

	}

	@Test
	public void testGenerateConjugation() throws FileNotFoundException,
			IOException {

		conjugation = generator.generateConjugation("bugnier", 9);

		generator.printConjugation(conjugation, "ART-9_bugnier");

	}

	@Test
	public void changeVocalInRoot() {

		String s = generator.changeVocalInRoot("confirm");

		System.out.println(s);

	}

}

package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class MapVerbsTest {
	private static MapVerbs mapper;

	@BeforeClass
	public static void initialize() {
		mapper = new MapVerbs();

	}

	@Test
	public void testAddConjugations() throws IOException {

		List<String> list = mapper.addConjugations("data_sm.tab");

		VerbsIO.printList(list, "data_sm_irr");

	}

	@Test
	public void test() {

		String s = "esser–sein";

		if (s.contains("–")) {

			System.out.println(s);
		}
	}

	@Test
	public void formatIrregulars() throws IOException {

		List<HashMap<String, String>> list_irregulars = mapper
				.parseIrregulars("irregulars.txt");

		VerbsIO.printList(list_irregulars, "irreg_format_map");
	}

}

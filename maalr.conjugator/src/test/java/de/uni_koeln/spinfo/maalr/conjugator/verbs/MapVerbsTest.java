package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.IOException;
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

		List<String> list = mapper.addConjugations("data_sm_edit.tab");

		VerbsIO.printList(list, "data_sm_edit");

	}

	
}

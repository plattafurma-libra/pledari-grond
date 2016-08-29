package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class MapVerbsTest {
	private static MapVerbs mapper;

	@BeforeClass
	public static void initialize() {
		mapper = new MapVerbs();
	}

	/*
	 * Add conjugations to existing data.tab
	 */
	@Test
	public void testAddConjugations() throws IOException {

		List<String> list = mapper.addConjugations("data_st_noVerbs.tab");
		VerbsIO.printList(list, "data_st_out");

		Set<String> set = new LinkedHashSet<String>(list);
		VerbsIO.printSet(set, "data_st_set_out");

	}

	@Test
	public void testRead() {
		String s ="55996	quälen		tr	torturar		tr																																									";
		String[] array = s.split("\\t");
		if (array[3].equals("tr")) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}

	@Test
	public void testRegex() {

		String s = "";

		if (mapper.isEmpty(s)) {
			System.out.println("empty");
		}

	}

	@Test
	public void formatIrregulars() throws IOException {

		List<HashMap<String, String>> list_irregulars = mapper
				.parseIrregulars("irregulars_st.txt");

		// VerbsIO.printList(list_irregulars, "irreg_format_map");
	}


	@Test
	public void showDups() throws IOException {

//		List<HashMap<String, String>> list_irregulars = mapper
//				.parseIrregulars("irregulars_st.txt");
		
		List<String> toGenerate = new ArrayList<>();
		List<String> regulars = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "regulars_st.txt"),
				Charset.forName("UTF8"));
		List<String> esch = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg_esch_st.txt"),
				Charset.forName("UTF8"));
		toGenerate.addAll(regulars);
		toGenerate.addAll(esch);
		Set<String> set = new HashSet<>();
		for (String s : toGenerate) {
			String[] splitted = s.split("\t");
			set.add(splitted[0]);
		}
//		for (HashMap<String, String> m : list_irregulars) {
//			String v = m.get("verb");
//			if (set.contains(v)) {
//				System.out.println("DUP: " + v);
//			}
//		}
	}
}

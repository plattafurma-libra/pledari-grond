package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
		s ="528	anfangen		tr/int	antschever		tr/int	antschavagn; partizip perfect: antschiet																																								";
		array = s.split("\\t");
		if (array[3].equals("tr/int")) {
			System.out.println("true: "+array[3]);
		} else {
			System.out.println("false");
		}
		System.out.println(Arrays.asList(array));
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}
		System.out.println("RFlex: "+array[7]);
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
		 VerbsIO.printList(list_irregulars, "irreg_format_map");
	}


	@Test
	public void showDups() throws IOException {

		List<HashMap<String, String>> list_irregulars = mapper
				.parseIrregulars("irregulars_st.txt");
		
		List<String> toGenerate = new ArrayList<>();
		List<String> regulars = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "regulars_st.txt"),
				Charset.forName("UTF8"));
		List<String> vw = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "vw_st.txt"),
				Charset.forName("UTF8"));
		List<String> esch = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg_esch_st.txt"),
				Charset.forName("UTF8"));
		toGenerate.addAll(regulars);
		toGenerate.addAll(vw);
		toGenerate.addAll(esch);
		Set<String> set = new HashSet<>();
		for (String s : toGenerate) {
			String[] splitted = s.split("\t");
			set.add(splitted[0]);
		}
		for (HashMap<String, String> m : list_irregulars) {
			String v = m.get("verb");
			if (set.contains(v)) {
				System.out.println("DUP: " + v);
			}
		}
	}
}

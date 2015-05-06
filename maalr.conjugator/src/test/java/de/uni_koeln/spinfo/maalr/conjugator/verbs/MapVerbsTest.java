package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

	@Test
	public void testAddConjugations() throws IOException {

		List<String> list = mapper.addConjugations("data_sm.tab");

		VerbsIO.printList(list, "data_sm");

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

		//VerbsIO.printList(list_irregulars, "irreg_format_map");
	}

	@Test
	public void addReflexiva() throws IOException {
		
		List<Reflex> reflex = mapper.cleanReflexives();
		
		for (Reflex r : reflex){
			
			System.out.println(r.getPrefix()+r.getVerb());
			
		}
	}

	@Test
	public void showDups() throws IOException {

		List<HashMap<String, String>> list_irregulars = mapper
				.parseIrregulars("irregulars.txt");

		List<String> toGenerate = new ArrayList<>();
		List<String> regulars = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg.txt"),
				Charset.forName("UTF8"));

		List<String> vw = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "vw.txt"),
				Charset.forName("UTF8"));

		List<String> esch = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg_esch.txt"),
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

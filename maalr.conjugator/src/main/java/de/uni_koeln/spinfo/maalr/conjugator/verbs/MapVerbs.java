package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationGenerator;
import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationStructure;

public class MapVerbs {

	private List<String> regVerbs;
	private List<String> regEschVerbs;
	private List<String> vwVerbs;
	ConjugationGenerator generator = new ConjugationGenerator();

	public List<String> addConjugations(String fileName) throws IOException {
		List<String> list = new ArrayList<>();

		List<String> found = new ArrayList<>();

		vwVerbs = Files.readAllLines(Paths.get(VerbsIO.input_dir + "vw.txt"),
				Charset.forName("UTF8"));
		regEschVerbs = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg_esch.txt"),
				Charset.forName("UTF8"));
		regVerbs = Files.readAllLines(Paths.get(VerbsIO.input_dir + "reg.txt"),
				Charset.forName("UTF8"));

		FileInputStream fis = new FileInputStream(VerbsIO.input_dir + fileName);
		InputStreamReader isr = new InputStreamReader(fis, "UTF8");
		LineNumberReader reader = new LineNumberReader(isr);
		ConjugationStructure structure = new ConjugationStructure();

		String currentLine;
		try {

			while ((currentLine = reader.readLine()) != null) {

				// Add required conjugation fields @ first line
				if (reader.getLineNumber() == 1) {

					StringBuffer fl = new StringBuffer();
					fl.append(currentLine);

					for (String s : structure.msi) {
						fl.append("\t");
						fl.append(s);

					}
					fl.append("\t");
					fl.append("maalr_overlay_lang2");

					list.add(fl.toString());

				} else {

					String[] array = currentLine.split("\t");
					StringBuffer ol = new StringBuffer();
					String verb = array[4];
					String DGenus = array[2];
					String RGenus = array[5];
					String RGrammatik = array[3];

					HashMap<String, String> map = null;

					if (regVerbs.contains(verb)) {
						if (isVerb(structure, DGenus, RGenus, RGrammatik, list,
								currentLine) == true) {
							continue;

						} else {

							found.add(verb + "\t" + "reg");

							generator.processQuery(verb);
							String endung = generator.getEnding();

							switch (endung) {

							case "ar":

								map = generator.generateConjugation(verb, 1);
								break;

							case "er":

								map = generator.generateConjugation(verb, 2);
								break;

							case "ier":

								map = generator.generateConjugation(verb, 3);
								break;

							case "eir":

								String verbFromList = regVerbs.get(regVerbs
										.indexOf(verb));
								String[] vA = verbFromList.split("\t");

								if (vA != null) {
									map = generator
											.generateConjugation(verb, 6);
									break;

								}

								else {

									map = generator
											.generateConjugation(verb, 4);

									break;

								}

							}
						}

					}

					if (vwVerbs.contains(verb)) {

						if (isVerb(structure, DGenus, RGenus, RGrammatik, list,
								currentLine) == true) {
							continue;
						} else {
							found.add(verb + "\t" + "vw");
							map = generator.generateConjugation(verb, 9);
						}

					}

					if (regEschVerbs.contains(verb)) {

						if (isVerb(structure, DGenus, RGenus, RGrammatik, list,
								currentLine) == true) {
							continue;

						} else {

							found.add(verb + "\t" + "esch");
							generator.processQuery(verb);
							String endung = generator.getEnding();

							switch (endung) {

							case "ar":

								map = generator.generateConjugation(verb, 8);

								break;

							case "eir":

								map = generator.generateConjugation(verb, 7);

								break;

							}
						}
					}

					// Verb was found and conjugated
					if (map != null) {

						ol.append(currentLine);
						for (String s : structure.msi) {

							ol.append("\t");
							ol.append(map.get(s));

						}
						ol.append("\t");
						ol.append("V");
						list.add(ol.toString());

					}
					// Verb was not found
					else {
						StringBuffer buffer = new StringBuffer();
						buffer.append(currentLine);
						buffer.append("\t");
						for (String s : structure.msi) {

							buffer.append("\t");
						}

						list.add(buffer.toString());

					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		VerbsIO.printList(found, "found");

		reader.close();
		return list;
	}

	private boolean isVerb(ConjugationStructure structure, String DGenus,
			String RGenus, String RGrammatik, List<String> list,
			String currentLine) {

		if (isEmpty(DGenus) == false || isEmpty(RGenus) == false
				|| RGrammatik.equals("Adjektiv")) {

			StringBuffer buffer = new StringBuffer();
			buffer.append(currentLine);
			buffer.append("\t");
			for (String s : structure.msi) {

				buffer.append("\t");
			}

			list.add(buffer.toString());
			return true;
		} else {
			return false;
		}

	}

	public boolean isEmpty(String s) {
		Pattern patt = Pattern.compile("^$");
		Matcher m = patt.matcher(s);
		return m.matches();
	}
}
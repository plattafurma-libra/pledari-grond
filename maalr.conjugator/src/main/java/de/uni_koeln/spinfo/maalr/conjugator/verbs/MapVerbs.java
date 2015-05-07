package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationGenerator;
import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationStructure;

public class MapVerbs {

	ConjugationGenerator generator = new ConjugationGenerator();

	public List<String> addConjugations(String fileName) throws IOException {

		List<String> list = new ArrayList<>();
		List<String> found = new ArrayList<>();
		Set<String> reg_fifth = new HashSet<String>();
		Set<String> reg_second = new HashSet<String>();

		List<String> raw_vwVerbs = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "vw.txt"),
				Charset.forName("UTF8"));

		List<String> raw_regEschVerbs = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg_esch.txt"),
				Charset.forName("UTF8"));

		List<String> raw_regVerbs = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg.txt"),
				Charset.forName("UTF8"));

		// Get only the infinitive forms
		List<Verb> vwVerbs = cleanVW(raw_vwVerbs);
		List<Verb> regEschVerbs = cleanRegEsch(raw_regEschVerbs);
		List<Verb> regVerbs = new ArrayList<>();

		for (String s : raw_regVerbs) {
			String[] array = s.split("\t");

			if (array.length == 2) {
				Verb v = new Verb();
				v.setForm(array[0]);

				// Not necessary, but still
				if (array[1].equals("[é]")) {

					// ART 6
					v.setStress(true);
				} else {
					// ART 4
					v.setStress(false);
				}

				if (array[1].equals("2")) {
					reg_second.add(array[0]);
				} else if (array[1].equals("5")) {
					reg_fifth.add(array[0]);
				}

				regVerbs.add(v);
			} else if (array.length == 1) {
				Verb v = new Verb();
				v.setForm(s);
				v.setStress(false);

				regVerbs.add(v);
			}

		}

		LineNumberReader reader = VerbsIO.getReader(fileName);
		ConjugationStructure structure = new ConjugationStructure();

		String currentLine;

		List<HashMap<String, String>> list_irregulars = parseIrregulars("irregulars.txt");

		List<Reflex> reflexives = cleanReflexives();

		boolean irregular;

		try {

			while ((currentLine = reader.readLine()) != null) {
				irregular = false;
				// Add required conjugation fields @ first line
				if (reader.getLineNumber() == 1) {

					StringBuffer fl = new StringBuffer();
					fl.append(currentLine);
					for (String s : structure.msi) {
						System.out.println(s);
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

					// PROCESS IRREGULARS
					for (HashMap<String, String> m : list_irregulars) {

						String v = m.get("verb");

						if (verb.equals(v)) {

							if (notVerb(structure, DGenus, RGenus, RGrammatik,
									list, currentLine) == true) {
								continue;

							} else {

								irregular = true;

								ol.append(currentLine);

								for (String s : structure.msi) {

									ol.append("\t");
									ol.append(m.get(s));

								}
								ol.append("\t");
								ol.append("V");
								list.add(ol.toString());

								break;
							}

						}
					}

					// PROCESS REGULARS
					for (Verb vs : regVerbs) {

						if (vs.getForm().equals(verb)) {

							if (notVerb(structure, DGenus, RGenus, RGrammatik,
									list, currentLine) == true) {
								continue;

							} else {

								found.add(verb + "\t" + "reg");

								generator.processQuery(verb);
								String ending = generator.getEnding();

								map = generateRegulars(verb, vs, map, ending,
										reg_fifth, reg_second);

							}

						}

					}

					// PROCESS VW
					for (Verb vs : vwVerbs) {

						if (vs.getForm().equals(verb)) {

							if (notVerb(structure, DGenus, RGenus, RGrammatik,
									list, currentLine) == true) {
								continue;
							} else {
								found.add(verb + "\t" + "vw");
								map = generator.generateConjugation(verb, 9);

							}

						}

					}

					// PROCESS REG_ESCH
					for (Verb vs : regEschVerbs) {

						if (vs.getForm().equals(verb)) {

							if (notVerb(structure, DGenus, RGenus, RGrammatik,
									list, currentLine) == true) {
								continue;

							} else {

								found.add(verb + "\t" + "esch");
								generator.processQuery(verb);
								String ending = generator.getEnding();
								map = generateRegularsEsch(map, ending, verb);

							}

						}

					}

					// VERB WAS FOUND AND CONJUGATED
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
					// VERB WAS NOT FOUND
					else if (irregular == false) {
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

	private HashMap<String, String> generateRegularsEsch(
			HashMap<String, String> map, String ending, String verb) {

		switch (ending) {

		case "ar":

			map = generator.generateConjugation(verb, 8);

			break;

		case "eir":

			map = generator.generateConjugation(verb, 7);

			break;

		}

		return map;
	}

	private HashMap<String, String> generateRegulars(String verb, Verb vs,
			HashMap<String, String> map, String ending, Set<String> reg_fifth,
			Set<String> reg_second) {

		switch (ending) {

		case "ar":

			map = generator.generateConjugation(verb, 1);
			break;

		case "er":

			if (reg_fifth.contains(verb)) {

				map = generator.generateConjugation(verb, 5);
			} else if (reg_second.contains(verb)) {

				map = generator.generateConjugation(verb, 2);
			}

			break;

		case "ier":

			map = generator.generateConjugation(verb, 3);
			break;

		case "eir":

			if (vs.isStress()) {

				map = generator.generateConjugation(verb, 6);
				break;
			} else {
				map = generator.generateConjugation(verb, 4);

				break;

			}

		}

		return map;
	}

	public List<HashMap<String, String>> parseIrregulars(String fileName)
			throws IOException {

		List<String> irreg = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + fileName),
				Charset.forName("UTF8"));

		List<String> indent = new ArrayList<>();
		for (String s : irreg) {
			if (!s.equals("")) {

				indent.add(s);
			}

		}
		ConjugationGenerator cg = new ConjugationGenerator();
		List<HashMap<String, String>> lcs = new ArrayList<>();
		int j = 0;
		for (int i = 0; i < indent.size(); i++) {

			if (indent.get(i).contains("–") && indent.get(i).length() > 2
					|| indent.get(i).contains("-")
					&& indent.get(i).length() > 2) {
				j++;
				String st = indent.get(i);
				// System.out.println(st);
				String[] lemma = null;
				if (indent.get(i).contains("–")) {
					lemma = st.split("–");
				} else if (indent.get(i).contains("-")) {
					lemma = st.split("-");
				}

				ConjugationStructure cs = new ConjugationStructure();
				cs.setVerb(lemma[0]);

				cg.checkReflexiveness(lemma[0]);

				cs.setReflexive(cg.getIsReflexive());

				// Preschaint
				String pres = indent.get(++i);
				String[] presArray = pres.split("\t");
				cs.setPreschentsing1(presArray[0]);
				cs.setPreschentsing2(presArray[1]);
				cs.setPreschentsing3(presArray[2]);
				cs.setPreschentplural1(presArray[3]);
				cs.setPreschentplural2(presArray[4]);
				cs.setPreschentplural3(presArray[5]);

				// Imperfect
				String imp = indent.get(++i);
				String[] impArray = imp.split("\t");
				cs.setImperfectsing1(impArray[0]);
				cs.setImperfectsing2(impArray[1]);
				cs.setImperfectsing3(impArray[2]);
				cs.setImperfectplural1(impArray[3]);
				cs.setImperfectplural2(impArray[4]);
				cs.setImperfectplural3(impArray[5]);

				// Futur
				String fut = indent.get(++i);
				String[] futArray = fut.split("\t");
				cs.setFutursing1(futArray[0]);
				cs.setFutursing2(futArray[1]);
				cs.setFutursing3(futArray[2]);
				cs.setFuturplural1(futArray[3]);
				cs.setFuturplural2(futArray[4]);
				cs.setFuturplural3(futArray[5]);

				// Conjunctiv
				String conj = indent.get(++i);
				String[] conjArray = conj.split("\t");
				cs.setConjunctivsing1(conjArray[0]);
				cs.setConjunctivsing2(conjArray[1]);
				cs.setConjunctivsing3(conjArray[2]);
				cs.setConjunctivplural1(conjArray[3]);
				cs.setConjunctivplural2(conjArray[4]);
				cs.setConjunctivplural3(conjArray[5]);

				// Cundizional
				String cund = indent.get(++i);
				String[] cundArray = cund.split("\t");
				cs.setCundizionalsing1(cundArray[0]);
				cs.setCundizionalsing2(cundArray[1]);
				cs.setCundizionalsing3(cundArray[2]);
				cs.setCundizionalplural1(cundArray[3]);
				cs.setCundizionalplural2(cundArray[4]);
				cs.setCundizionalplural3(cundArray[5]);

				// Partizip
				String part = indent.get(++i);
				String[] partArray = part.split("\t");
				cs.setParticipperfectms(partArray[0]);
				cs.setParticipperfectmp(partArray[1]);
				cs.setParticipperfectfs(partArray[2]);
				cs.setParticipperfectfp(partArray[3]);

				// Gerundium
				String ger = indent.get(++i);
				cs.setGerundium(ger);

				// Imperativ
				String imperat = indent.get(++i);
				String[] imperatArray = imperat.split("\t");
				cs.setImperativ1(imperatArray[0]);
				cs.setImperativ2(imperatArray[1]);

				lcs.add(cg.addPronouns(cs.getValues()));
			}

		}

		VerbsIO.printList(lcs, "lcs");
		System.out.println(j);

		return lcs;

	}

	public List<Reflex> cleanReflexives() throws IOException {
		List<Reflex> cleaned_ref = new ArrayList<>();
		List<String> reflexives = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reflexives.txt"),
				Charset.forName("UTF8"));

		for (String s : reflexives) {

			if (s.startsWith("s'")) {

				s = s.substring(2, s.length());
				Reflex r = new Reflex();
				r.setPrefix("s'");
				s = s.replaceAll("//s+", "");
				r.setVerb(s);
				cleaned_ref.add(r);
			} else {

				s = s.substring(3, s.length());
				Reflex r = new Reflex();
				r.setPrefix("sa ");
				s = s.replaceAll("//s+", "");
				r.setVerb(s);
				cleaned_ref.add(r);
			}

		}

		return cleaned_ref;

	}

	private List<Verb> cleanVW(List<String> toClean) {
		List<Verb> cleaned = new ArrayList<>();

		for (String s : toClean) {
			String[] array = s.split("\t");
			if (array.length == 2) {
				Verb v = new Verb();
				v.setForm(array[0]);

				if (array[1].equals("[é]")) {

					// ART 6
					v.setStress(true);
				} else {
					// ART 4
					v.setStress(false);
				}

				cleaned.add(v);
			} else if (array.length == 1) {
				Verb v = new Verb();
				v.setForm(s);
				v.setStress(false);

				cleaned.add(v);
			}

		}

		return cleaned;

	}

	private List<Verb> cleanRegEsch(List<String> toClean) {
		List<Verb> cleaned = new ArrayList<>();

		for (String s : toClean) {

			String[] array = s.split("\t");

			if (array.length == 2) {
				Verb v = new Verb();
				v.setForm(array[0]);
				v.setStress(true);

				cleaned.add(v);
			} else if (array.length == 1) {
				Verb v = new Verb();
				v.setForm(s);
				v.setStress(false);

				cleaned.add(v);
			}

		}

		return cleaned;

	}

	private List<Verb> cleanReg(List<String> toClean) {
		List<Verb> cleaned = new ArrayList<>();

		for (String s : toClean) {

			String[] array = s.split("\t");

			if (array.length == 2) {
				Verb v = new Verb();
				v.setForm(array[0]);
				v.setStress(true);

				cleaned.add(v);
			} else if (array.length == 1) {
				Verb v = new Verb();
				v.setForm(s);
				v.setStress(false);

				cleaned.add(v);
			}

		}

		return cleaned;

	}

	private boolean notVerb(ConjugationStructure structure, String DGenus,
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
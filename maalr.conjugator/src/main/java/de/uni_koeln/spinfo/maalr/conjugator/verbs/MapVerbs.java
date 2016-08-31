package de.uni_koeln.spinfo.maalr.conjugator.verbs;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationGenerator;
import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationStructure;

public class MapVerbs {

	ConjugationGenerator generator = new ConjugationGenerator();

	public List<String> addConjugations(String fileName) throws IOException {

		System.out.println("processing infile: " + fileName);
		
		List<String> list = new ArrayList<>();
		List<String> found = new ArrayList<>();

		List<String> raw_vwVerbs = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "vw_st.txt"),
				Charset.forName("UTF8"));

		List<String> raw_regEschVerbs = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "reg_esch_st.txt"),
				Charset.forName("UTF8"));

		List<String> raw_regVerbs = Files.readAllLines(
				Paths.get(VerbsIO.input_dir + "regulars_st.txt"),
				Charset.forName("UTF8"));

		// Get only the infinitive forms
		List<Verb> vwVerbs = clean(raw_vwVerbs);
		List<Verb> regEschVerbs = clean(raw_regEschVerbs);
		List<Verb> regVerbs = clean(raw_regVerbs);

		List<HashMap<String, String>> list_irregulars = parseIrregulars("irregulars_st.txt");

		ConjugationStructure structure = new ConjugationStructure();
		boolean processed;
		LineNumberReader reader = VerbsIO.getReader(fileName);
		String currentLine;
		try {
			while ((currentLine = reader.readLine()) != null) {
				processed = false;

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
					String verb, DGenus = "", RGenus = "", DGrammatik = "", RFlex = "";
					//skip grammar check for short lines (avoid IndexOutOfBounds)
					if (array.length < 8) {
						verb = array[4];
					} else {
						verb = array[4];
						DGenus = array[2];
						RGenus = array[5];
						DGrammatik = array[3];
						RFlex = array[7];
					}

					StringBuffer ol = new StringBuffer();
					HashMap<String, String> map = null;

					// PROCESS IRREGULARS
					for (HashMap<String, String> m : list_irregulars) {
						String v = m.get("verb");
						if (verb.equals(v)) {
							if (!isVerb(structure, DGenus, RGenus, DGrammatik,
									list, currentLine)) {
								System.out.println("IRR\tno verb entry: "
										+ currentLine);
								break;
							} else {
								processed = true;
								found.add(verb + "\t" + "irr");

								// remove RFlex (containing conjugation hints)
								currentLine = removeRFlex(currentLine, RFlex);

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
							if (!isVerb(structure, DGenus, RGenus, DGrammatik,
									list, currentLine)) {
								System.out.println("REG\tno verb entry: "
										+ currentLine);
								break;
							} else {
								processed = true;
								found.add(verb + "\t" + "reg");
								generator.processQuery(verb);
								String ending = generator.getEnding();
								map = generateRegulars(verb, map, ending);
							}
						}
					}

					// PROCESS VW
					for (Verb vs : vwVerbs) {
						if (vs.getForm().equals(verb)) {
							if (!isVerb(structure, DGenus, RGenus, DGrammatik,
									list, currentLine)) {
								System.out.println("VW\tno verb entry: "
										+ currentLine);
								break;
							} else {
								processed = true;
								found.add(verb + "\t" + "vw");
								map = generator.generateConjugation(verb, 8);
							}
						}
					}

					// PROCESS REG_ESCH
					for (Verb vs : regEschVerbs) {
						if (vs.getForm().equals(verb)) {
							if (!isVerb(structure, DGenus, RGenus, DGrammatik,
									list, currentLine)) {
								System.out.println("REG_ESCH\tno verb entry: "
										+ currentLine);
								break;
							} else {
								processed = true;
								found.add(verb + "\t" + "esch");
								generator.processQuery(verb);
								String ending = generator.getEnding();
								map = generateRegularsEsch(map, ending, verb);
							}
						}
					}

					// VERB WAS FOUND AND CONJUGATED
					if (map != null) {

						// remove RFlex (containing conjugation hints)
						currentLine = removeRFlex(currentLine, RFlex);

						ol.append(currentLine);
						for (String s : structure.msi) {
							ol.append("\t");
							ol.append(map.get(s));
						}
						ol.append("\t");
						ol.append("V");
						list.add(ol.toString());
					}
					// IT's NOT a VERB
					else if (processed == false) {
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
		Set<String> foundset = new LinkedHashSet<>(found);
		VerbsIO.printSet(foundset, "found_set");
		System.out.println("### done. " + reader.getLineNumber()
				+ " lines read, " + list.size() + " lines to write.");
		reader.close();

		return list;
	}

	/*
	 * remove field contents in 'RFlex' (containing conjugation hints)
	 */
	private String removeRFlex(String currentLine, String RFlex) {
		return currentLine.replace(RFlex, "");
	}

	private HashMap<String, String> generateRegularsEsch(
			HashMap<String, String> map, String ending, String verb) {
		switch (ending) {
		case "ar":
			map = generator.generateConjugation(verb, 5);
			break;
		case "ear":
			map = generator.generateConjugation(verb, 6);
			break;
		case "ir":
			map = generator.generateConjugation(verb, 7);
			break;
		}
		return map;
	}

	private HashMap<String, String> generateRegulars(String verb,
			HashMap<String, String> map, String ending) {

		switch (ending) {
		case "ar":
			map = generator.generateConjugation(verb, 1);
			break;
		case "ear":
			map = generator.generateConjugation(verb, 2);
			break;
		case "er":
			map = generator.generateConjugation(verb, 3);
			break;
		case "ir":
			map = generator.generateConjugation(verb, 4);
			break;
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
			// line not empty and no comment:
			if (!s.equals("") && !s.startsWith("#")) {
				indent.add(s);
			}
		}
		System.out.println("indent: " + indent);

		ConjugationGenerator cg = new ConjugationGenerator();
		List<HashMap<String, String>> lcs = new ArrayList<>();
		for (int i = 0; i < indent.size(); i++) {

			if (indent.get(i).contains("–") && indent.get(i).length() > 2
					|| indent.get(i).contains("-")
					&& indent.get(i).length() > 2) {
				String st = indent.get(i);
				String[] lemma = null;
				if (indent.get(i).contains("–")) {
					lemma = st.split("–");
				} else if (indent.get(i).contains("-")) {
					lemma = st.split("-");
				}

				ConjugationStructure cs = new ConjugationStructure();
				cs.setVerb(lemma[0]);

				// Preschaint
				String pres = indent.get(++i);
				String[] presArray = pres.split("\t");
				cs.setPreschentsing1(presArray[0]);
				cs.setPreschentsing2(presArray[1]);
				cs.setPreschentsing3(presArray[2]);
				cs.setPreschentplural1(presArray[3]);
				cs.setPreschentplural2(presArray[4]);
				cs.setPreschentplural3(presArray[5]);
//				System.out.println("pres\t"+Arrays.asList(presArray));

				// Imperfect
				String imp = indent.get(++i);
				String[] impArray = imp.split("\t");
				cs.setImperfectsing1(impArray[0]);
				cs.setImperfectsing2(impArray[1]);
				cs.setImperfectsing3(impArray[2]);
				cs.setImperfectplural1(impArray[3]);
				cs.setImperfectplural2(impArray[4]);
				cs.setImperfectplural3(impArray[5]);
//				System.out.println("imp\t"+Arrays.asList(impArray));

				// Futur
				String fut = indent.get(++i);
				String[] futArray = fut.split("\t");
				cs.setFutursing1(futArray[0]);
				cs.setFutursing2(futArray[1]);
				cs.setFutursing3(futArray[2]);
				cs.setFuturplural1(futArray[3]);
				cs.setFuturplural2(futArray[4]);
				cs.setFuturplural3(futArray[5]);
//				System.out.println("fut\t"+Arrays.asList(futArray));

				// Conjunctiv
				String conj = indent.get(++i);
				String[] conjArray = conj.split("\t");
				cs.setConjunctivsing1(conjArray[0]);
				cs.setConjunctivsing2(conjArray[1]);
				cs.setConjunctivsing3(conjArray[2]);
				cs.setConjunctivplural1(conjArray[3]);
				cs.setConjunctivplural2(conjArray[4]);
				cs.setConjunctivplural3(conjArray[5]);
//				System.out.println("conj\t"+Arrays.asList(conjArray));

				// Cundizional
				String cund = indent.get(++i);
				String[] cundArray = cund.split("\t");
				cs.setCundizionalsing1(cundArray[0]);
				cs.setCundizionalsing2(cundArray[1]);
				cs.setCundizionalsing3(cundArray[2]);
				cs.setCundizionalplural1(cundArray[3]);
				cs.setCundizionalplural2(cundArray[4]);
				cs.setCundizionalplural3(cundArray[5]);
//				System.out.println("cund\t"+Arrays.asList(cundArray));

				// Partizip
				String part = indent.get(++i);
				String[] partArray = part.split("\t");
				cs.setParticipperfectms(partArray[0]);
				cs.setParticipperfectfs(partArray[1]);
				cs.setParticipperfectmp(partArray[2]);
				cs.setParticipperfectfp(partArray[3]);
//				System.out.println("part\t"+Arrays.asList(partArray));

				// Gerundium
				String ger = indent.get(++i);
				cs.setGerundium(ger);
//				System.out.println("ger\t"+ger);

				// Imperativ
				String imperat = indent.get(++i);
				String[] imperatArray = imperat.split("\t");
				cs.setImperativ1(imperatArray[0]);
				cs.setImperativ2(imperatArray[1]);
//				System.out.println("imperat\t"+Arrays.asList(imperatArray));

				lcs.add(cg.addPronouns(cs.getValues()));
				System.out.println(cs.getValues());
			}
		}
		VerbsIO.printList(lcs, "lcs");
		System.out.println("irr. verbs processed: " + lcs.size());
		return lcs;
	}

	private List<Verb> clean(List<String> toClean) {
		List<Verb> cleaned = new ArrayList<>();
		for (String s : toClean) {
			//skip comments and empty lines
			if (s.startsWith("#") || s.isEmpty()) {
				continue;
			}
			String[] array = s.split("\t");
			if (array.length == 2) {
				Verb v = new Verb();
				v.setForm(array[0]);
				cleaned.add(v);
			} else if (array.length == 1) {
				Verb v = new Verb();
				v.setForm(s.trim());
				cleaned.add(v);
			}
		}
		return cleaned;
	}

	private boolean isVerb(ConjugationStructure structure, String DGenus,
			String RGenus, String DGrammatik, List<String> list,
			String currentLine) {

		if (isEmpty(DGenus) == false || isEmpty(RGenus) == false
				|| DGrammatik.equals("adj")) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isEmpty(String s) {
		Pattern patt = Pattern.compile("^$");
		Matcher m = patt.matcher(s);
		return m.matches();
	}
}
package de.uni_koeln.spinfo.maalr.conjugator.generator;

/*******************************************************************************
 * Copyright 2013-2016 Sprachliche Informationsverarbeitung, University of Cologne
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConjugationGenerator {

	// No reflexives in Sutsilvan! No reflexive pronouns.
	//
	// (Endung - Klasse)
	//
	// -ar (gidar) - 1
	//
	// -ear (angraztgear) - 2
	//
	// -er (repeter) - 3
	//
	// -ir (partir) - 4
	//
	// -ar_esch (cumbinar) - 5
	//
	// -ear_esch (inditgear) - 6
	//
	// -ir_esch (capir) - 7
	//
	// - vokalwechsel (ampruvar) - 8

	private ConjugationStructure cs;

	private String infinitiv;

	private String verb;

	private String root;

	private String modRoot;

	private String ending;

	private ArrayList<HashMap<String, String>> conjugationList;

	private HashMap<String, String> conjugation;

	private String lastTwo;
	private String lastThree;

	private char vocalInRoot;

	public String processQuery(String query) {

		String l2 = query.substring(query.length() - 2);
		String l3 = query.substring(query.length() - 3);

		switch (l2) {
		case "ar":
		case "er":
		case "ir":
			if (!l3.equals("ear")) {
				setLastTwo(l2);
				setEnding(getLastTwo());
				query = query.substring(0, query.length() - 2);
				return query;
			}
			break;
		}

		if (l3.equals("ear")) {
			setLastThree(l3);
			setEnding(getLastThree());
			query = query.substring(0, query.length() - 3);
			return query;
		}
		return query;
	}

	public HashMap<String, String> generateConjugation(String infinitiv,
			int conjugationCLass) {

		setInfinitiv(infinitiv);
		root = getRoot(infinitiv);

		if (conjugationCLass == 8) {
			modRoot = changeVocalInRoot(root, conjugationCLass);
			if (modRoot == null) {
				throw new RuntimeException(
						"For this conjugation you need to enter a verb with a vowel in its root!");
			}
		}

		if (conjugationCLass < 1 || conjugationCLass > 8) {
			throw new RuntimeException(conjugationCLass
					+ " is not a valid conjugation class." + "\n"
					+ "The range of conjugations is from 1 to 8.");
		} else if (getEnding() == null) {
			throw new RuntimeException(infinitiv + "  is not a valid verb."
					+ "\n" + "Please type a verb in its infinitive form.");
		}

		switch (conjugationCLass) {
		case 1:
			conjugation = conjugate(root, "art-1");
			break;
		case 2:
			conjugation = conjugate(root, "art-2");
			break;
		case 3:
			conjugation = conjugate(root, "art-3");
			break;
		case 4:
			conjugation = conjugate(root, "art-4");
			break;
		case 5:
			conjugation = conjugate(root, "art-5");
			break;
		case 6:
			conjugation = conjugate(root, "art-6");
			break;
		case 7:
			conjugation = conjugate(root, "art-7");
			break;
		case 8:
			conjugation = conjugate(root, "art-8");
			break;
		}
		return addPronouns(conjugation);
	}

	public String getRoot(String query) {
		if (query != null) {
			query = removeWhitespaces(query);
			if (query.equals("ir") || query.equals("er")) {
				setVerb(query);
				setEnding(query);
				return query;
			}
			HashSet<String> avoid;
			switch (query.length()) {
			case 0:
			case 1:
			case 2:
				throw new RuntimeException("'" + query + "'"
						+ " is not a valid verb." + "\n"
						+ "Please type a verb in its infinitive form.");
			default:
				avoid = new HashSet<String>();
				avoid.add("near");
				avoid.add("sa");
				if (avoid.contains(query)) {
					throw new RuntimeException("'" + query + "'"
							+ " is not a valid verb." + "\n"
							+ "Please type a verb in its infinitive form.");
				}
				query = processQuery(query);
			}
		}
		return query;
	}

	public String removeWhitespaces(String query) {
		return query.toLowerCase().replaceAll("^\\s+|\\s+$", "");
	}

	public String changeVocalInRoot(String root, int conjugationClass) {
		StringBuilder builder = null;
		List<String> a_turnsTo_o = Arrays.asList("clam", "dumand", "sadumand",
				"racumand");
		for (int i = root.length() - 1; i >= 0; i--) {
			char ch = root.charAt(i);
			if (isVocal(ch)) {
				setVocalInRoot(ch);
				builder = new StringBuilder(root);

				switch (ch) {
				case 'a':
					if (a_turnsTo_o.contains(root)) {
						builder.setCharAt(i, 'o');
					} else {
						builder.setCharAt(i, 'e');
					}
					break;
				case 'i':
					builder.setCharAt(i, 'e');
					break;
				case 'u':
					builder.setCharAt(i, 'o');
					break;
				}
				break;
			}
		}
		if (builder == null) {
			return null;
		}
		return builder.toString();
	}

	private boolean isVocal(char ch) {

		switch (ch) {
		case 'a':
		case 'e':
		case 'i':
		case 'o':
		case 'u':
		case 'ä':
		case 'ö':
		case 'ü':
			return true;
		default:
			return false;
		}
	}

	public ArrayList<HashMap<String, String>> generateAll(String infinitiv) {

		setInfinitiv(infinitiv);
		String root = getRoot(infinitiv);

		conjugationList = new ArrayList<HashMap<String, String>>();
		conjugationList.add(conjugate(root, "art-1"));
		conjugationList.add(conjugate(root, "art-2"));
		conjugationList.add(conjugate(root, "art-3"));
		conjugationList.add(conjugate(root, "art-4"));
		conjugationList.add(conjugate(root, "art-5"));
		conjugationList.add(conjugate(root, "art-6"));
		conjugationList.add(conjugate(root, "art-7"));
		conjugationList.add(conjugate(root, "art-8"));

		return conjugationList;
	}

	public HashMap<String, String> conjugate(String root, String art) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());
		cs.setConjugationClass(art);

		// PRESCHENT
		setPreschent(root, cs);
		// IMPERFECT
		setImperfect(root, cs);
		// CONJUNCTIV
		setConjunctiv(root, cs);
		// CUNDIZIONAL
		setCundizional(root, cs);
		// PARTICIP PERFECT
		setParticipPerfect(root, cs);
		// IMPERATIV
		setImperativ(root, cs);
		// GERUNDIUM
		setGerundium(root, cs);
		// FUTUR
		setFutur(root, cs);

		return cs.getValues();
	}

	public void setPreschent(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-1":
		case "art-2":
		case "art-3":
		case "art-4":
			// 1ps
			cs.setPreschentsing1(root + "[el]");
			// 2ps
			cs.setPreschentsing2(root + "as");
			// 3ps
			cs.setPreschentsing3(root + "a");
			// 1pp
			cs.setPreschentplural1(root + "agn");
			// 2pp
			if (cs.getConjugationclass().equals("art-1")) {
				cs.setPreschentplural2(root + "az");
			} else if (cs.getConjugationclass().equals("art-2")) {
				cs.setPreschentplural2(root + "eaz");
			} else {
				cs.setPreschentplural2(root + "ez");
			}
			// 3pp
			cs.setPreschentplural3(root + "an");
			break;

		case "art-5":
		case "art-6":
		case "art-7":
			// 1ps
			cs.setPreschentsing1(root + "esch[el]");
			// 2ps
			cs.setPreschentsing2(root + "eschas");
			// 3ps
			cs.setPreschentsing3(root + "escha");
			// 1pp
			cs.setPreschentplural1(root + "agn");
			// 2pp
			if (cs.getConjugationclass().equals("art-5")) {
				cs.setPreschentplural2(root + "az");
			} else if (cs.getConjugationclass().equals("art-6")) {
				cs.setPreschentplural2(root + "eaz");
			} else {
				cs.setPreschentplural2(root + "ez");
			}
			// 3pp
			cs.setPreschentplural3(root + "eschan");
			break;
		case "art-8":
			// 1ps
			cs.setPreschentsing1(modRoot + "[el]");
			// 2ps
			cs.setPreschentsing2(modRoot + "as");
			// 3ps
			cs.setPreschentsing3(modRoot + "a");
			// 1pp
			cs.setPreschentplural1(root + "agn");
			// 2pp
			cs.setPreschentplural2(root + "az");
			// 3pp
			cs.setPreschentplural3(modRoot + "an");
			break;
		}
	}

	public void setImperfect(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-1":
		case "art-5":
		case "art-8":
			// 1ps
			cs.setImperfectsing1(root + "ava");
			// 2ps
			cs.setImperfectsing2(root + "avas");
			// 3ps
			cs.setImperfectsing3(root + "ava");
			// 1pp
			cs.setImperfectplural1(root + "avan");
			// 2pp
			cs.setImperfectplural2(root + "avas");
			// 3pp
			cs.setImperfectplural3(root + "avan");
			break;

		case "art-2":
		case "art-6":
			// 1ps
			cs.setImperfectsing1(root + "eava");
			// 2ps
			cs.setImperfectsing2(root + "eavas");
			// 3ps
			cs.setImperfectsing3(root + "eava");
			// 1pp
			cs.setImperfectplural1(root + "eavan");
			// 2pp
			cs.setImperfectplural2(root + "eavas");
			// 3pp
			cs.setImperfectplural3(root + "eavan");
			break;

		case "art-3":
		case "art-4":
		case "art-7":
			// 1ps
			cs.setImperfectsing1(root + "eva");
			// 2ps
			cs.setImperfectsing2(root + "evas");
			// 3ps
			cs.setImperfectsing3(root + "eva");
			// 1pp
			cs.setImperfectplural1(root + "evan");
			// 2pp
			cs.setImperfectplural2(root + "evas");
			// 3pp
			cs.setImperfectplural3(root + "evan");
			break;
		}
	}

	public void setConjunctiv(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-5":
		case "art-6":
		case "art-7":
			// 1ps
			cs.setConjunctivsing1(root + "eschi");
			// 2ps
			cs.setConjunctivsing2(root + "esch(i)as");
			// 3ps
			cs.setConjunctivsing3(root + "eschi");
			// 1pp
			cs.setConjunctivplural1(root + "eian");
			// 2pp
			cs.setConjunctivplural2(root + "eias");
			// 3pp
			cs.setConjunctivplural3(root + "esch(i)an");
			break;
		case "art-8":
			// 1ps
			cs.setConjunctivsing1(modRoot + "i");
			// 2ps
			cs.setConjunctivsing2(modRoot + "(i)as");
			// 3ps
			cs.setConjunctivsing3(modRoot + "i");
			// 1pp
			cs.setConjunctivplural1(root + "eian");
			// 2pp
			cs.setConjunctivplural2(root + "eias");
			// 3pp
			cs.setConjunctivplural3(modRoot + "(i)an");
			break;
		default: // 1-4
			// 1ps
			cs.setConjunctivsing1(root + "i");
			// 2ps
			cs.setConjunctivsing2(root + "(i)as");
			// 3ps
			cs.setConjunctivsing3(root + "i");
			// 1pp
			cs.setConjunctivplural1(root + "eian");
			// 2pp
			cs.setConjunctivplural2(root + "eias");
			// 3pp
			cs.setConjunctivplural3(root + "(i)an");
			break;
		}
	}

	public void setCundizional(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-1":
		case "art-5":
		case "art-8":
			// 1ps
			cs.setCundizionalsing1(root + "ass");
			// 2ps
			cs.setCundizionalsing2(root + "assas");
			// 3ps
			cs.setCundizionalsing3(root + "ass");
			// 1pp
			cs.setCundizionalplural1(root + "assan");
			// 2pp
			cs.setCundizionalplural2(root + "assas");
			// 3pp
			cs.setCundizionalplural3(root + "assan");
			break;

		default: // 2, 3, 4, 6, 7
			// 1ps
			cs.setCundizionalsing1(root + "ess");
			// 2ps
			cs.setCundizionalsing2(root + "essas");
			// 3ps
			cs.setCundizionalsing3(root + "ess");
			// 1pp
			cs.setCundizionalplural1(root + "essan");
			// 2pp
			cs.setCundizionalplural2(root + "essas");
			// 3pp
			cs.setCundizionalplural3(root + "essan");
			break;
		}
	}

	public void setParticipPerfect(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-1":
		case "art-5":
		case "art-8":
			cs.setParticipperfectms(root + "o");
			cs.setParticipperfectfs(root + "ada");
			cs.setParticipperfectmp(root + "os");
			cs.setParticipperfectfp(root + "adas");
			break;

		case "art-2":
		case "art-6":
			cs.setParticipperfectms(root + "ieu");
			cs.setParticipperfectfs(root + "eada");
			cs.setParticipperfectmp(root + "ieus");
			cs.setParticipperfectfp(root + "eadas");
			break;

		default: // 3,4,7
			cs.setParticipperfectms(root + "ieu");
			cs.setParticipperfectfs(root + "ida");
			cs.setParticipperfectmp(root + "ieus");
			cs.setParticipperfectfp(root + "idas");
			break;
		}
	}

	public void setGerundium(String root, ConjugationStructure cs) {
		cs.setGerundium(root + "ànd");
	}

	public void setImperativ(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-1":
			cs.setImperativ1(root + "a!");
			cs.setImperativ2(root + "ad!");
			break;

		case "art-2":
			cs.setImperativ1(root + "a!");
			cs.setImperativ2(root + "ead!");
			break;

		case "art-5":
			cs.setImperativ1(root + "escha!");
			cs.setImperativ2(root + "ad!");
			break;

		case "art-6":
			cs.setImperativ1(root + "escha!");
			cs.setImperativ2(root + "ead!");
			break;

		case "art-7":
			cs.setImperativ1(root + "escha!");
			cs.setImperativ2(root + "ed!");
			break;

		case "art-8":
			cs.setImperativ1(modRoot + "a!");
			cs.setImperativ2(root + "ad!");
			break;

		default: // 3,4
			cs.setImperativ1(root + "a!");
			cs.setImperativ2(root + "ed!");
			break;
		}
	}

	public void setFutur(String root, ConjugationStructure cs) {

		if (isVocal(root)) {
			cs.setFutursing1("vignt ad " + cs.getInfinitiv());
			cs.setFutursing2("veans ad " + cs.getInfinitiv());
			cs.setFutursing3("vean ad " + cs.getInfinitiv());
			cs.setFuturplural1("vagnagn ad " + cs.getInfinitiv());
			cs.setFuturplural2("vagnez ad " + cs.getInfinitiv());
			cs.setFuturplural3("vignan ad " + cs.getInfinitiv());
		} else {
			cs.setFutursing1("vignt a " + cs.getInfinitiv());
			cs.setFutursing2("veans a " + cs.getInfinitiv());
			cs.setFutursing3("vean a " + cs.getInfinitiv());
			cs.setFuturplural1("vagnagn a " + cs.getInfinitiv());
			cs.setFuturplural2("vagnez a " + cs.getInfinitiv());
			cs.setFuturplural3("vignan a " + cs.getInfinitiv());
		}
	}

	public boolean isVocal(String root) {

		switch (root.substring(0, 1)) {
		case "a":
		case "e":
		case "i":
		case "o":
		case "u":
		case "ä":
		case "ö":
		case "ü":
			return true;
		default:
			return false;
		}
	}

	public HashMap<String, String> addPronouns(
			HashMap<String, String> conjugation) {

		ConjugationStructure cs = new ConjugationStructure();

		cs.setVerb(conjugation.get("verb"));
		cs.setInfinitiv(conjugation.get(ConjugationStructure.infinitiv));
		cs.setRoot(conjugation.get(ConjugationStructure.root));
		cs.setEnding(conjugation.get(ConjugationStructure.ending));
		cs.setReflexive(conjugation.get(ConjugationStructure.reflexive));
		cs.setConjugationClass(conjugation
				.get(ConjugationStructure.conjugationclass));

		// PRESCHENT
		cs.setPreschentsing1(Pronouns.pron_1ps
				+ conjugation.get(ConjugationStructure.preschentsing1));
		cs.setPreschentsing2(Pronouns.pron_2ps
				+ conjugation.get(ConjugationStructure.preschentsing2));
		cs.setPreschentsing3(Pronouns.pron_3ps
				+ conjugation.get(ConjugationStructure.preschentsing3));
		cs.setPreschentplural1(Pronouns.pron_1pp
				+ conjugation.get(ConjugationStructure.preschentplural1));
		cs.setPreschentplural2(Pronouns.pron_2pp
				+ conjugation.get(ConjugationStructure.preschentplural2));
		cs.setPreschentplural3(Pronouns.pron_3pp
				+ conjugation.get(ConjugationStructure.preschentplural3));

		// IMPERFECT
		cs.setImperfectsing1(Pronouns.pron_1ps
				+ conjugation.get(ConjugationStructure.imperfectsing1));
		cs.setImperfectsing2(Pronouns.pron_2ps
				+ conjugation.get(ConjugationStructure.imperfectsing2));
		cs.setImperfectsing3(Pronouns.pron_3ps
				+ conjugation.get(ConjugationStructure.imperfectsing3));
		cs.setImperfectplural1(Pronouns.pron_1pp
				+ conjugation.get(ConjugationStructure.imperfectplural1));
		cs.setImperfectplural2(Pronouns.pron_2pp
				+ conjugation.get(ConjugationStructure.imperfectplural2));
		cs.setImperfectplural3(Pronouns.pron_3pp
				+ conjugation.get(ConjugationStructure.imperfectplural3));

		// CONJUNCTIV
		cs.setConjunctivsing1(Pronouns.pron_conjunctiv_c + Pronouns.pron_1ps
				+ conjugation.get(ConjugationStructure.conjunctivsing1));
		cs.setConjunctivsing2(Pronouns.pron_conjunctiv_c + Pronouns.pron_2ps
				+ conjugation.get(ConjugationStructure.conjunctivsing2));
		cs.setConjunctivsing3(Pronouns.pron_conjunctiv_v + Pronouns.pron_3ps
				+ conjugation.get(ConjugationStructure.conjunctivsing3));
		cs.setConjunctivplural1(Pronouns.pron_conjunctiv_c + Pronouns.pron_1pp
				+ conjugation.get(ConjugationStructure.conjunctivplural1));
		cs.setConjunctivplural2(Pronouns.pron_conjunctiv_c + Pronouns.pron_2pp
				+ conjugation.get(ConjugationStructure.conjunctivplural2));
		cs.setConjunctivplural3(Pronouns.pron_conjunctiv_v + Pronouns.pron_3pp
				+ conjugation.get(ConjugationStructure.conjunctivplural3));

		// CUNDIZIONAL
		cs.setCundizionalsing1(Pronouns.pron_1ps
				+ conjugation.get(ConjugationStructure.cundizionalsing1));
		cs.setCundizionalsing2(Pronouns.pron_2ps
				+ conjugation.get(ConjugationStructure.cundizionalsing2));
		cs.setCundizionalsing3(Pronouns.pron_3ps
				+ conjugation.get(ConjugationStructure.cundizionalsing3));
		cs.setCundizionalplural1(Pronouns.pron_1pp
				+ conjugation.get(ConjugationStructure.cundizionalplural1));
		cs.setCundizionalplural2(Pronouns.pron_2pp
				+ conjugation.get(ConjugationStructure.cundizionalplural2));
		cs.setCundizionalplural3(Pronouns.pron_3pp
				+ conjugation.get(ConjugationStructure.cundizionalplural3));

		// IMPERATIV
		cs.setImperativ1(conjugation.get(ConjugationStructure.imperativ1));
		cs.setImperativ2(conjugation.get(ConjugationStructure.imperativ2));

		// PARTICIP_PERFECT
		cs.setParticipperfectms(conjugation
				.get(ConjugationStructure.participperfectms));
		cs.setParticipperfectfs(conjugation
				.get(ConjugationStructure.participperfectfs));
		cs.setParticipperfectmp(conjugation
				.get(ConjugationStructure.participperfectmp));
		cs.setParticipperfectfp(conjugation
				.get(ConjugationStructure.participperfectfp));

		// GERUNDIUM
		cs.setGerundium(conjugation.get(ConjugationStructure.gerundium));

		// FUTUR
		cs.setFutursing1(Pronouns.pron_1ps
				+ conjugation.get(ConjugationStructure.futursing1));
		cs.setFutursing2(Pronouns.pron_2ps
				+ conjugation.get(ConjugationStructure.futursing2));
		cs.setFutursing3(Pronouns.pron_3ps
				+ conjugation.get(ConjugationStructure.futursing3));
		cs.setFuturplural1(Pronouns.pron_1pp
				+ conjugation.get(ConjugationStructure.futurplural1));
		cs.setFuturplural2(Pronouns.pron_2pp
				+ conjugation.get(ConjugationStructure.futurplural2));
		cs.setFuturplural3(Pronouns.pron_3pp
				+ conjugation.get(ConjugationStructure.futurplural3));

		return cs.getValues();
	}

	public void printConjugation(Map<String, String> conjugation) {

		System.out.println("---");
		for (Map.Entry<String, String> entry : conjugation.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		System.out.println();

	}

	public <K, V> File printConjToFile(Map<K, V> map, String destPath,
			String fileName) throws IOException {

		File file = new File(destPath + fileName + ".txt");
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		out.append("verb : " + map.get("verb") + "\n");
		out.append("\n");
		out.append("infinitiv : " + map.get("infinitiv") + "\n");
		out.append("root : " + map.get("root") + "\n");
		out.append("conjugationclass : " + map.get("conjugationclass") + "\n");
		out.append("ending : " + map.get("ending") + "\n");
		out.append("reflexive : " + map.get("reflexive") + "\n");
		out.append("\n");

		out.append("preschentsing1 : " + map.get("preschentsing1") + "\n");
		out.append("preschentsing2 : " + map.get("preschentsing2") + "\n");
		out.append("preschentsing3 : " + map.get("preschentsing3") + "\n");
		out.append("preschentplural1 : " + map.get("preschentplural1") + "\n");
		out.append("preschentplural2 : " + map.get("preschentplural2") + "\n");
		out.append("preschentplural3 : " + map.get("preschentplural3") + "\n");
		out.append("\n");

		out.append("imperfectsing1 : " + map.get("imperfectsing1") + "\n");
		out.append("imperfectsing2 : " + map.get("imperfectsing2") + "\n");
		out.append("imperfectsing3 : " + map.get("imperfectsing3") + "\n");
		out.append("imperfectplural1 : " + map.get("imperfectplural1") + "\n");
		out.append("imperfectplural2 : " + map.get("imperfectplural2") + "\n");
		out.append("imperfectplural3 : " + map.get("imperfectplural3") + "\n");
		out.append("\n");

		out.append("futursing1 : " + map.get("futursing1") + "\n");
		out.append("futursing2 : " + map.get("futursing2") + "\n");
		out.append("futursing3 : " + map.get("futursing3") + "\n");
		out.append("futurplural1 : " + map.get("futurplural1") + "\n");
		out.append("futurplural2 : " + map.get("futurplural2") + "\n");
		out.append("futurplural3 : " + map.get("futurplural3") + "\n");
		out.append("\n");

		out.append("conjunctivsing1 : " + map.get("conjunctivsing1") + "\n");
		out.append("conjunctivsing2 : " + map.get("conjunctivsing2") + "\n");
		out.append("conjunctivsing3 : " + map.get("conjunctivsing3") + "\n");
		out.append("conjunctivplural1 : " + map.get("conjunctivplural1") + "\n");
		out.append("conjunctivplural2 : " + map.get("conjunctivplural2") + "\n");
		out.append("conjunctivplural3 : " + map.get("conjunctivplural3") + "\n");
		out.append("\n");

		out.append("cundizionalsing1 : " + map.get("cundizionalsing1") + "\n");
		out.append("cundizionalsing2 : " + map.get("cundizionalsing2") + "\n");
		out.append("cundizionalsing3 : " + map.get("cundizionalsing3") + "\n");
		out.append("cundizionalplural1 : " + map.get("cundizionalplural1")
				+ "\n");
		out.append("cundizionalplural2 : " + map.get("cundizionalplural2")
				+ "\n");
		out.append("cundizionalplural3 : " + map.get("cundizionalplural3")
				+ "\n");
		out.append("\n");

		out.append("participperfectms : " + map.get("participperfectms") + "\n");
		out.append("participperfectfs : " + map.get("participperfectfs") + "\n");
		out.append("participperfectmp : " + map.get("participperfectmp") + "\n");
		out.append("participperfectfp : " + map.get("participperfectfp") + "\n");
		out.append("\n");

		out.append("gerundium : " + map.get("gerundium") + "\n");
		out.append("\n");

		out.append("imperativ1 : " + map.get("imperativ1") + "\n");
		out.append("imperativ2 : " + map.get("imperativ2") + "\n");
		out.append("\n");

		out.flush();
		out.close();

		return file;
	}

	public <K, V> File printMap(Map<K, V> map, String destPath, String fileName)
			throws IOException {

		File file = new File(destPath + fileName + ".txt");
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Map.Entry<K, V> entry : map.entrySet()) {
			out.append(entry.getKey() + " : " + entry.getValue());
			out.append("\n");
		}

		out.flush();
		out.close();

		return file;
	}

	public <T> File printSet(Set<T> set, String destPath, String filename)
			throws IOException {

		File file = new File(destPath + filename + ".txt");

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Object o : set) {
			writer.append(o + "\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public ArrayList<HashMap<String, String>> getConjugations() {
		return conjugationList;
	}

	public void setConjugations(ArrayList<HashMap<String, String>> decs) {
		this.conjugationList = decs;
	}

	public HashMap<String, String> getConjugation() {
		return conjugation;
	}

	public void setConjugation(HashMap<String, String> conjugation) {
		this.conjugation = conjugation;
	}

	public String getInfinitiv() {
		return infinitiv;
	}

	public void setInfinitiv(String infinitiv) {
		this.infinitiv = infinitiv;
	}

	public String getEnding() {
		return ending;
	}

	public void setEnding(String ending) {
		this.ending = ending;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getLastTwo() {
		return lastTwo;
	}

	public void setLastTwo(String lastTwo) {
		this.lastTwo = lastTwo;
	}

	public String getLastThree() {
		return lastThree;
	}

	public void setLastThree(String lastThree) {
		this.lastThree = lastThree;
	}

	public char getVocalInRoot() {
		return vocalInRoot;
	}

	public void setVocalInRoot(char vocalInRoot) {
		this.vocalInRoot = vocalInRoot;
	}

	public boolean endsWithDoubleConsonant(String root) {
		if (root.charAt(root.length() - 1) == root.charAt(root.length() - 2)) {
			return true;
		} else
			return false;
	}
}

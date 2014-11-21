/*******************************************************************************
 * Copyright 2013 Sprachliche Informationsverarbeitung, University of Cologne
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
package de.uni_koeln.spinfo.maalr.overlays.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ConjugationGenerator {

	private ConjugationStructure cs;

	private Pronouns pronouns;

	private String isReflexive;

	private String infinitiv;

	private String verb;

	private String ending;

	private ArrayList<HashMap<String, String>> conjugationList;

	private HashMap<String, String> conjugation;

	private Set<String> avoid;

	private String lastTwo;
	private String lastThree;

	public String processQuery(String query) {

		String l2 = query.substring(query.length() - 2);
		String l3 = query.substring(query.length() - 3);

		switch (l2) {
		case "ar":
		case "er":
		case "ir":
			if (!l3.equals("air")) {

				setLastTwo(l2);
				setEnding(getLastTwo());
				query = checkReflexiveness(query);
				query = query.substring(0, query.length() - 2);
				return query;

			}
			break;

		}

		if (l3.equals("air")) {
			setLastThree(l3);
			setEnding(getLastThree());
			query = checkReflexiveness(query);
			query = query.substring(0, query.length() - 3);

			return query;
		}

		return query;

	}

	public HashMap<String, String> generateConjugation(String infinitiv,
			int conjugationCLass) {

		String root = getRoot(infinitiv);

		if (conjugationCLass < 1 || conjugationCLass > 7) {
			throw new RuntimeException(conjugationCLass
					+ " is not a valid conjugation class." + "\n"
					+ "The range of conjugations is from 1 to 7.");
		} else if (getEnding() == null) {
			throw new RuntimeException(infinitiv + "  is not a valid verb."
					+ "\n" + "Please type a verb in its infinitive form.");
		}

		switch (conjugationCLass) {
		case 1:
			conjugation = firstConjugation(root);
			break;
		case 2:
			conjugation = secondAndThirdConjugation(root);
			break;
		case 3:
			conjugation = secondAndThirdConjugation(root);
			break;
		case 4:
			conjugation = fourthConjugation(root);
			break;
		case 5:
			conjugation = fifthConjugation(root);
			break;
		case 6:
			conjugation = sixthConjugation(root);
			break;
		case 7:
			conjugation = seventhConjugation(root);
			break;

		}

		return addPronouns(conjugation);

	}

	public String getRoot(String query) {

		if (query != null) {

			query = removeWhitespaces(query);

			if (query.equals("ir")) {
				setVerb(query);
				setEnding(query);
				query = checkReflexiveness(query);
				return query;
			}

			switch (query.length()) {

			case 0:
				throw new RuntimeException("'" + query + "'"
						+ " is not a valid verb." + "\n"
						+ "Please type a verb in its infinitive form.");

			case 1:
				throw new RuntimeException("'" + query + "'"
						+ " is not a valid verb." + "\n"
						+ "Please type a verb in its infinitive form.");

			case 2:
				throw new RuntimeException("'" + query + "'"
						+ " is not a valid verb." + "\n"
						+ "Please type a verb in its infinitive form.");

			default:

				avoid = new HashSet<String>();
				avoid.add("ar");
				avoid.add("er");
				avoid.add("air");
				avoid.add("s'ar");
				avoid.add("s'ir");
				avoid.add("s'er");
				avoid.add("s'air");
				avoid.add("sa ar");
				avoid.add("sa er");
				avoid.add("sa ir");
				avoid.add("sa air");
				avoid.add("sa'ar");
				avoid.add("sa'ir");
				avoid.add("sa'er");
				avoid.add("sa'air");

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

		query = query.toLowerCase();
		query = query.replaceAll("^\\s+|\\s+$", "");

		return query;
	}

	public String checkReflexiveness(String query) {

		if (query.startsWith("sa ")) {
			setVerb(query);
			query = query.length() > 2 ? query.substring(3) : query;
			setIsReflexive(new String("true"));

		} else if (query.startsWith("s'")) {
			setVerb(query);
			query = query.length() > 2 ? query.substring(2) : query;
			setIsReflexive(new String("true"));
		} else {
			setIsReflexive(new String("false"));
			setVerb(query);
		}

		setInfinitiv(query);

		return query;
	}

	public ArrayList<HashMap<String, String>> generateAll(String infinitiv) {

		String root = getRoot(infinitiv);

		conjugationList = new ArrayList<HashMap<String, String>>();

		conjugationList.add(firstConjugation(root));
		conjugationList.add(secondAndThirdConjugation(root));
		conjugationList.add(fourthConjugation(root));
		conjugationList.add(fifthConjugation(root));
		conjugationList.add(sixthConjugation(root));
		conjugationList.add(seventhConjugation(root));

		return conjugationList;
	}

	public HashMap<String, String> firstConjugation(String root) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());
		cs.setConjugationClass("art-1");
		cs.setReflexive(getIsReflexive());

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

	public HashMap<String, String> secondAndThirdConjugation(String root) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());
		cs.setConjugationClass("art-2_3");
		cs.setReflexive(getIsReflexive());

		// PRESCHENT
		setPreschent(root, cs);

		// IMPERFECT
		setImperfect(root, cs);

		// CONJUNCTIV
		setConjunctiv(root, cs);

		// CUNDIZIONAL
		setCundizional(root, cs);

		// PARTICIP_PERFECT
		setParticipPerfect(root, cs);

		// IMPERATIV
		setImperativ(root, cs);

		// GERUNDIUM
		setGerundium(root, cs);

		// FUTUR
		setFutur(root, cs);

		return cs.getValues();

	}

	public HashMap<String, String> fourthConjugation(String root) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());
		cs.setConjugationClass("art-4");
		cs.setReflexive(getIsReflexive());

		// PRESCHENT
		setPreschent(root, cs);

		// IMPERFECT
		setImperfect(root, cs);

		// CONJUNCTIV
		setConjunctiv(root, cs);

		// CUNDIZIONAL
		setCundizional(root, cs);

		// PARTICIP_PERFECT
		setParticipPerfect(root, cs);

		// IMPERATIV
		setImperativ(root, cs);

		// GERUNDIUM
		setGerundium(root, cs);

		// FUTUR
		setFutur(root, cs);

		return cs.getValues();

	}

	public HashMap<String, String> fifthConjugation(String root) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());
		cs.setConjugationClass("art-5");
		cs.setReflexive(getIsReflexive());

		// PRESCHENT
		setPreschent(root, cs);

		// IMPERFECT
		setImperfect(root, cs);

		// CONJUNCTIV
		setConjunctiv(root, cs);

		// CUNDIZIONAL
		setCundizional(root, cs);

		// PARTICIP_PERFECT
		setParticipPerfect(root, cs);

		// IMPERATIV
		setImperativ(root, cs);

		// GERUNDIUM
		setGerundium(root, cs);

		// FUTUR
		setFutur(root, cs);

		return cs.getValues();

	}

	public HashMap<String, String> sixthConjugation(String root) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());
		cs.setConjugationClass("art-6");
		cs.setReflexive(getIsReflexive());

		// PRESCHENT
		setPreschent(root, cs);

		// IMPERFECT
		setImperfect(root, cs);

		// CONJUNCTIV
		setConjunctiv(root, cs);

		// CUNDIZIONAL
		setCundizional(root, cs);

		// PARTICIP_PERFECT
		setParticipPerfect(root, cs);

		// IMPERATIV
		setImperativ(root, cs);

		// GERUNDIUM
		setGerundium(root, cs);

		// FUTUR
		setFutur(root, cs);

		return cs.getValues();

	}

	public HashMap<String, String> seventhConjugation(String root) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());
		cs.setConjugationClass("art-7");
		cs.setReflexive(getIsReflexive());

		// PRESCHENT
		setPreschent(root, cs);

		// IMPERFECT
		setImperfect(root, cs);

		// CONJUNCTIV
		setConjunctiv(root, cs);

		// CUNDIZIONAL
		setCundizional(root, cs);

		// PARTICIP_PERFECT
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
		case "art-2_3":
		case "art-4":
		case "art-7":

			if (cs.getConjugationclass().equals("art-7")) {
				// 1ps
				cs.setPreschentsing1(root + "el");
			} else {
				cs.setPreschentsing1(root);
			}

			// 2ps
			cs.setPreschentsing2(root + "as");
			// 3ps
			cs.setPreschentsing3(root + "a");

			if (cs.getConjugationclass().equals("art-4")) {
				// 1pp
				cs.setPreschentplural1(root + "in");
				// 2pp
				cs.setPreschentplural2(root + "is");
			} else {
				// 1pp
				cs.setPreschentplural1(root + "ain");
				// 2pp
				cs.setPreschentplural2(root + "ais");
			}
			// 3pp
			cs.setPreschentplural3(root + "an");
			break;

		case "art-5":
		case "art-6":

			// PRESCHENT
			// 1ps
			cs.setPreschentsing1(root + "esch");
			// 2ps
			cs.setPreschentsing2(root + "eschas");
			// 3ps
			cs.setPreschentsing3(root + "escha");

			if (cs.getConjugationclass().equals("art-5")) {
				// 1pp
				cs.setPreschentplural1(root + "in");
				// 2pp
				cs.setPreschentplural2(root + "is");
			} else {
				// 1pp
				cs.setPreschentplural1(root + "ain");
				// 2pp
				cs.setPreschentplural2(root + "ais");
			}

			// 3pp
			cs.setPreschentplural3(root + "eschan");

			break;

		}

	}

	public void setImperfect(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {

		case "art-1":
		case "art-6":
		case "art-7":
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

		case "art-4":
		case "art-5":

			// 1ps
			cs.setImperfectsing1(root + "iva");
			// 2ps
			cs.setImperfectsing2(root + "ivas");
			// 3ps
			cs.setImperfectsing3(root + "iva");
			// 1pp
			cs.setImperfectplural1(root + "ivan");
			// 2pp
			cs.setImperfectplural2(root + "ivas");
			// 3pp
			cs.setImperfectplural3(root + "ivan");
			break;

		case "art-2_3":
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
			// 1ps
			cs.setConjunctivsing1(root + "eschia");
			// 2ps
			cs.setConjunctivsing2(root + "eschias");
			// 3ps
			cs.setConjunctivsing3(root + "eschia");
			// 1pp
			cs.setConjunctivplural1(root + "eschian");
			// 2pp
			cs.setConjunctivplural2(root + "eschias");
			// 3pp
			cs.setConjunctivplural3(root + "eschian");
			break;

		default:
			// 1ps
			cs.setConjunctivsing1(root + "ia");
			// 2ps
			cs.setConjunctivsing2(root + "ias");
			// 3ps
			cs.setConjunctivsing3(root + "ia");
			// 1pp
			cs.setConjunctivplural1(root + "ian");
			// 2pp
			cs.setConjunctivplural2(root + "ias");
			// 3pp
			cs.setConjunctivplural3(root + "ian");
			break;
		}
	}

	public void setCundizional(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-1":
		case "art-6":
		case "art-7":
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

		case "art-4":
		case "art-5":

			// 1ps
			cs.setCundizionalsing1(root + "iss");
			// 2ps
			cs.setCundizionalsing2(root + "issas");
			// 3ps
			cs.setCundizionalsing3(root + "iss");
			// 1pp
			cs.setCundizionalplural1(root + "issan");
			// 2pp
			cs.setCundizionalplural2(root + "issas");
			// 3pp
			cs.setCundizionalplural3(root + "issan");
			break;

		case "art-2_3":
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
		case "art-6":
		case "art-7":
			cs.setParticipperfectms(root + "à");
			cs.setParticipperfectfs(root + "ada");
			cs.setParticipperfectmp(root + "ads");
			cs.setParticipperfectfp(root + "adas");
			break;

		default:
			cs.setParticipperfectms(root + "ì");
			cs.setParticipperfectfs(root + "ida");
			cs.setParticipperfectmp(root + "ids");
			cs.setParticipperfectfp(root + "idas");
			break;

		}

	}

	public void setGerundium(String root, ConjugationStructure cs) {

		switch (getEnding()) {
		case "ar":
			cs.setGerundium(root + "ond");
			break;
		case "er":
			cs.setGerundium(root + "end");
			break;
		case "ir":
			cs.setGerundium(root + "ind");
			break;
		case "air":
			cs.setGerundium(root + "end");
			break;
		default:
			break;
		}

	}

	public void setImperativ(String root, ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {

		case "art-5":
		case "art-6":

			cs.setImperativ1(root + "escha!");

			switch (getEnding()) {
			case "ar":
				cs.setImperativ2(root + "ai!");
				break;
			case "er":
				cs.setImperativ2(root + "si!");
				break;
			case "ir":
			case "air":
				cs.setImperativ2(root + "i!");
				break;
			}
			break;

		default:
			cs.setImperativ1(root + "a!");
			cs.setImperativ2(root + "ai!");
			break;

		}

	}

	public void setFutur(String root, ConjugationStructure cs) {

		switch (getIsReflexive()) {

		case "true":

			if (isVocal(root)) {
				cs.setFutursing1("vegn a " + Pronouns.pron_r_v_1ps
						+ cs.getInfinitiv());
				cs.setFutursing2("vegns a " + Pronouns.pron_r_v_2ps
						+ cs.getInfinitiv());
				cs.setFutursing3("vegn a " + Pronouns.pron_r_v_3ps
						+ cs.getInfinitiv());
				cs.setFuturplural1("vegnin ad " + Pronouns.pron_r_v_1pp
						+ cs.getInfinitiv());
				cs.setFuturplural2("vegnis ad " + Pronouns.pron_r_v_2pp
						+ cs.getInfinitiv());
				cs.setFuturplural3("vegnan a " + Pronouns.pron_r_v_3pp
						+ cs.getInfinitiv());

			} else {
				cs.setFutursing1("vegn a " + Pronouns.pron_r_1ps
						+ cs.getInfinitiv());
				cs.setFutursing2("vegns a " + Pronouns.pron_r_2ps
						+ cs.getInfinitiv());
				cs.setFutursing3("vegn a " + Pronouns.pron_r_3ps
						+ cs.getInfinitiv());
				cs.setFuturplural1("vegnin ad " + Pronouns.pron_r_1pp
						+ cs.getInfinitiv());
				cs.setFuturplural2("vegnis ad " + Pronouns.pron_r_2pp
						+ cs.getInfinitiv());
				cs.setFuturplural3("vegnan a " + Pronouns.pron_r_3pp
						+ cs.getInfinitiv());

			}
			break;

		case "false":

			if (isVocal(root)) {
				cs.setFutursing1("vegn ad " + cs.getInfinitiv());
				cs.setFutursing2("vegns ad " + cs.getInfinitiv());
				cs.setFutursing3("vegn ad " + cs.getInfinitiv());
				cs.setFuturplural1("vegnin ad " + cs.getInfinitiv());
				cs.setFuturplural2("vegnis ad " + cs.getInfinitiv());
				cs.setFuturplural3("vegnan ad " + cs.getInfinitiv());

			} else {
				cs.setFutursing1("vegn a " + cs.getInfinitiv());
				cs.setFutursing2("vegns a " + cs.getInfinitiv());
				cs.setFutursing3("vegn a " + cs.getInfinitiv());
				cs.setFuturplural1("vegnin a " + cs.getInfinitiv());
				cs.setFuturplural2("vegnis a " + cs.getInfinitiv());
				cs.setFuturplural3("vegnan a " + cs.getInfinitiv());
			}

			break;
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
		Map<String, String> pronouns;

		// HashMap<String, String> reflexiveConjugation = new HashMap<>();

		String verb = conjugation.get("verb");

		if (verb.startsWith("sa ")) {
			// Reflexive Verbs that start with Consonants
			pronouns = pronounsForReflexiveConsonantalVerbs();
			return addReflexivePronouns(conjugation, pronouns);

		} else if (verb.startsWith("s'")) {
			// Reflexive Verbs that start with Vocals
			pronouns = pronounsForReflexiveVocalicVerbs();
			return addReflexivePronouns(conjugation, pronouns);

		} else {
			// Standard Verbs
			return addStandardPronouns(conjugation);
		}

	}

	public HashMap<String, String> addReflexivePronouns(
			Map<String, String> conjugation, Map<String, String> pronouns) {
		cs = new ConjugationStructure();
		cs.setVerb(conjugation.get("verb"));
		cs.setInfinitiv(conjugation.get(ConjugationStructure.infinitiv));
		cs.setRoot(conjugation.get(ConjugationStructure.root));
		cs.setEnding(conjugation.get(ConjugationStructure.ending));
		cs.setReflexive(conjugation.get(ConjugationStructure.reflexive));
		cs.setConjugationClass(conjugation
				.get(ConjugationStructure.conjugationclass));

		// PRESCHENT
		cs.setPreschentsing1(pronouns.get(Pronouns.first_ps)
				+ conjugation.get(ConjugationStructure.preschentsing1));
		cs.setPreschentsing2(pronouns.get(Pronouns.second_ps)
				+ conjugation.get(ConjugationStructure.preschentsing2));
		cs.setPreschentsing3(pronouns.get(Pronouns.third_ps)
				+ conjugation.get(ConjugationStructure.preschentsing3));
		cs.setPreschentplural1(pronouns.get(Pronouns.first_pp)
				+ conjugation.get(ConjugationStructure.preschentplural1));
		cs.setPreschentplural2(pronouns.get(Pronouns.second_pp)
				+ conjugation.get(ConjugationStructure.preschentplural2));
		cs.setPreschentplural3(pronouns.get(Pronouns.third_pp)
				+ conjugation.get(ConjugationStructure.preschentplural3));

		// IMPERFECT
		cs.setImperfectsing1(pronouns.get(Pronouns.first_ps)
				+ conjugation.get(ConjugationStructure.imperfectsing1));
		cs.setImperfectsing2(pronouns.get(Pronouns.second_ps)
				+ conjugation.get(ConjugationStructure.imperfectsing2));
		cs.setImperfectsing3(pronouns.get(Pronouns.third_ps)
				+ conjugation.get(ConjugationStructure.imperfectsing3));
		cs.setImperfectplural1(pronouns.get(Pronouns.first_pp)
				+ conjugation.get(ConjugationStructure.imperfectplural1));
		cs.setImperfectplural2(pronouns.get(Pronouns.second_pp)
				+ conjugation.get(ConjugationStructure.imperfectplural2));
		cs.setImperfectplural3(pronouns.get(Pronouns.third_pp)
				+ conjugation.get(ConjugationStructure.imperfectplural3));

		// CONJUNCTIV
		cs.setConjunctivsing1(pronouns.get(Pronouns.first_ps_c)
				+ conjugation.get(ConjugationStructure.conjunctivsing1));
		cs.setConjunctivsing2(pronouns.get(Pronouns.second_ps_c)
				+ conjugation.get(ConjugationStructure.conjunctivsing2));
		cs.setConjunctivsing3(pronouns.get(Pronouns.third_ps_c)
				+ conjugation.get(ConjugationStructure.conjunctivsing3));
		cs.setConjunctivplural1(pronouns.get(Pronouns.first_pp_c)
				+ conjugation.get(ConjugationStructure.conjunctivplural1));
		cs.setConjunctivplural2(pronouns.get(Pronouns.second_pp_c)
				+ conjugation.get(ConjugationStructure.conjunctivplural2));
		cs.setConjunctivplural3(pronouns.get(Pronouns.third_pp_c)
				+ conjugation.get(ConjugationStructure.conjunctivplural3));

		// CUNDIZIONAL
		cs.setCundizionalsing1(pronouns.get(Pronouns.first_ps)
				+ conjugation.get(ConjugationStructure.cundizionalsing1));
		cs.setCundizionalsing2(pronouns.get(Pronouns.second_ps)
				+ conjugation.get(ConjugationStructure.cundizionalsing2));
		cs.setCundizionalsing3(pronouns.get(Pronouns.third_ps)
				+ conjugation.get(ConjugationStructure.cundizionalsing3));
		cs.setCundizionalplural1(pronouns.get(Pronouns.first_pp)
				+ conjugation.get(ConjugationStructure.cundizionalplural1));
		cs.setCundizionalplural2(pronouns.get(Pronouns.second_pp)
				+ conjugation.get(ConjugationStructure.cundizionalplural2));
		cs.setCundizionalplural3(pronouns.get(Pronouns.third_pp)
				+ conjugation.get(ConjugationStructure.cundizionalplural3));

		// IMPERATIV
		cs.setImperativ1(pronouns.get(Pronouns.imperat_1)
				+ conjugation.get(ConjugationStructure.imperativ1));
		cs.setImperativ2(pronouns.get(Pronouns.imperat_2)
				+ conjugation.get(ConjugationStructure.imperativ2));

		// PARTICIP_PERFECT
		cs.setParticipperfectms(pronouns.get(Pronouns.pp_1)
				+ conjugation.get(ConjugationStructure.participperfectms));
		cs.setParticipperfectfs(pronouns.get(Pronouns.pp_2)
				+ conjugation.get(ConjugationStructure.participperfectms) + "/"
				+ conjugation.get(ConjugationStructure.participperfectfs));

		// GERUNDIUM
		cs.setGerundium(pronouns.get(Pronouns.gerund)
				+ conjugation.get(ConjugationStructure.gerundium));

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

	public HashMap<String, String> addStandardPronouns(
			Map<String, String> conjugation) {

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

	public Map<String, String> pronounsForReflexiveConsonantalVerbs() {
		pronouns = new Pronouns();
		// STANDARD
		pronouns.setFirstPs(Pronouns.pron_1ps + Pronouns.pron_r_1ps);
		pronouns.setSecondPs(Pronouns.pron_2ps + Pronouns.pron_r_2ps);
		pronouns.setThirdPs(Pronouns.pron_3ps + Pronouns.pron_r_3ps);
		pronouns.setFirstPp(Pronouns.pron_1pp + Pronouns.pron_r_1pp);
		pronouns.setSecondPp(Pronouns.pron_2pp + Pronouns.pron_r_2pp);
		pronouns.setThirdPp(Pronouns.pron_3pp + Pronouns.pron_r_3pp);

		// CONJUNCTIV
		pronouns.setFirstPsC(Pronouns.pron_conjunctiv_c + Pronouns.pron_1ps
				+ Pronouns.pron_r_1ps);
		pronouns.setSecondPsC(Pronouns.pron_conjunctiv_c + Pronouns.pron_2ps
				+ Pronouns.pron_r_2ps);
		pronouns.setThirdPsC(Pronouns.pron_conjunctiv_v + Pronouns.pron_3ps
				+ Pronouns.pron_r_3ps);
		pronouns.setFirstPpC(Pronouns.pron_conjunctiv_c + Pronouns.pron_1pp
				+ Pronouns.pron_r_1pp);
		pronouns.setSecondPpC(Pronouns.pron_conjunctiv_c + Pronouns.pron_2pp
				+ Pronouns.pron_r_2pp);
		pronouns.setThirdPpC(Pronouns.pron_conjunctiv_v + Pronouns.pron_3pp
				+ Pronouns.pron_r_3pp);

		// PARTICIP PERFECT
		pronouns.setPp_1(Pronouns.pp_r1 + " " + Pronouns.pron_r_3ps);
		pronouns.setPp_2(Pronouns.pp_r2 + " " + Pronouns.pron_r_3ps);

		// IMPERATIV
		pronouns.setImperat1(Pronouns.pron_r_2ps);
		pronouns.setImperat2(Pronouns.pron_r_2pp);

		// GERUNDIUM
		pronouns.setGer(Pronouns.pron_r_3ps);

		return pronouns.getValues();
	}

	public Map<String, String> pronounsForReflexiveVocalicVerbs() {
		pronouns = new Pronouns();
		// STANDARD
		pronouns.setFirstPs(Pronouns.pron_1ps + Pronouns.pron_r_v_1ps);
		pronouns.setSecondPs(Pronouns.pron_2ps + Pronouns.pron_r_v_2ps);
		pronouns.setThirdPs(Pronouns.pron_3ps + Pronouns.pron_r_v_3ps);
		pronouns.setFirstPp(Pronouns.pron_1pp + Pronouns.pron_r_v_1pp);
		pronouns.setSecondPp(Pronouns.pron_2pp + Pronouns.pron_r_v_2pp);
		pronouns.setThirdPp(Pronouns.pron_3pp + Pronouns.pron_r_v_3pp);

		// CONJUNCTIV
		pronouns.setFirstPsC(Pronouns.pron_conjunctiv_c + Pronouns.pron_1ps
				+ Pronouns.pron_r_v_1ps);
		pronouns.setSecondPsC(Pronouns.pron_conjunctiv_c + Pronouns.pron_2ps
				+ Pronouns.pron_r_v_2ps);
		pronouns.setThirdPsC(Pronouns.pron_conjunctiv_v + Pronouns.pron_3ps
				+ Pronouns.pron_r_v_3ps);
		pronouns.setFirstPpC(Pronouns.pron_conjunctiv_c + Pronouns.pron_1pp
				+ Pronouns.pron_r_v_1pp);
		pronouns.setSecondPpC(Pronouns.pron_conjunctiv_c + Pronouns.pron_2pp
				+ Pronouns.pron_r_v_2pp);
		pronouns.setThirdPpC(Pronouns.pron_conjunctiv_v + Pronouns.pron_3pp
				+ Pronouns.pron_r_v_3pp);

		// PARTICIP PERFECT
		pronouns.setPp_1(Pronouns.pp_r1 + " " + Pronouns.pron_r_v_3ps);
		pronouns.setPp_2(Pronouns.pp_r2 + " " + Pronouns.pron_r_v_3ps);

		// IMPERATIV
		pronouns.setImperat1(Pronouns.pron_r_v_2ps);
		pronouns.setImperat2(Pronouns.pron_r_v_2pp);

		// GERUNDIUM
		pronouns.setGer(Pronouns.pron_r_v_3ps);

		return pronouns.getValues();
	}

	public void printConjugation(Map<String, String> conjugation) {

		for (Map.Entry<String, String> entry : conjugation.entrySet()) {

			System.out.println(entry.getKey() + " " + entry.getValue());

		}

	}

	public File printMap(Map<String, String> map, String filename)
			throws IOException, FileNotFoundException {

		File file = new File("/Users/franciscomondaca/Desktop/" + filename);
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			writer.append(key + "\t" + value + "\n");
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

	public String getIsReflexive() {
		return isReflexive;
	}

	public void setIsReflexive(String isReflexive) {
		this.isReflexive = isReflexive;
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

}

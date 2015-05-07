package de.uni_koeln.spinfo.maalr.conjugator.generator;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConjugationGenerator {

	// (Endung - Klasse)
	//
	// -ar - 1
	//
	// -er - 2
	//
	// -ier - 3
	//
	// -eir [è] - 4
	//
	// -er - 5
	//
	// -eir [é] - 6
	//
	// -eir [é](esch) - 7
	//
	// -ar (esch) - 8
	//
	// Vokaländerung in der Wurzel - 9

	private ConjugationStructure cs;

	private Pronouns pronouns;

	private String isReflexive;

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

			if (!l3.equals("eir") && !l3.equals("ier")) {

				setLastTwo(l2);
				setEnding(getLastTwo());
				query = checkReflexiveness(query);
				query = query.substring(0, query.length() - 2);
				return query;

			}

			break;

		}

		if (l3.equals("eir")) {
			setLastThree(l3);
			setEnding(getLastThree());
			query = checkReflexiveness(query);
			query = query.substring(0, query.length() - 3);

			return query;
		}

		if (l3.equals("ier")) {
			setLastThree(l3);
			setEnding(getLastThree());
			query = checkReflexiveness(query);
			query = query.substring(0, query.length() - 3);

			return query;
		}

		return query;

	}

	public HashMap<String, String> generateConjugation(String infinitiv,
			int conjugationClass) {

		root = getRoot(infinitiv);

		if (conjugationClass == 9) {
			modRoot = changeVocalInRoot(root, conjugationClass);

			if (modRoot == null) {
				throw new RuntimeException(
						"For this conjugation you need to enter a verb with a vowel in its root!");
			}

		}

		if (conjugationClass < 1 || conjugationClass > 9) {
			throw new RuntimeException(conjugationClass
					+ " is not a valid conjugation class." + "\n"
					+ "The range of conjugations is from 1 to 9.");
		} else if (getEnding() == null) {
			throw new RuntimeException(infinitiv + "  is not a valid verb."
					+ "\n" + "Please enter a verb in its infinitive form.");
		}

		conjugation = conjugate(root, conjugationClass);

		return addPronouns(conjugation);

	}

	public String getRoot(String query) {

		if (query != null) {

			query = removeWhitespaces(query);

			if (query.equals("eir")) {
				setVerb(query);
				setEnding(query);
				query = checkReflexiveness(query);
				return query;
			}

			switch (query.length()) {

			case 0:
				throw new RuntimeException("'" + query + "'"
						+ " is not a valid verb." + "\n"
						+ "Please enter a verb in its infinitive form.");

			case 1:
				throw new RuntimeException("'" + query + "'"
						+ " is not a valid verb." + "\n"
						+ "Please enter a verb in its infinitive form.");

			case 2:
				throw new RuntimeException("'" + query + "'"
						+ " is not a valid verb." + "\n"
						+ "Please enter a verb in its infinitive form.");

			default:

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

	public String changeVocalInRoot(String root, int conjugationClass) {
		StringBuilder builder = null;

		for (int i = root.length() - 1; i >= 0; i--) {

			char ch = root.charAt(i);

			if (isVocal(ch)) {
				setVocalInRoot(ch);
				builder = new StringBuilder(root);

				switch (ch) {

				case 'a':
					builder.setCharAt(i, 'o');
					break;

				case 'e':
					builder.setCharAt(i, 'a');
					break;

				case 'i':
					builder.setCharAt(i, 'e');
					break;

				case 'o':
					builder.setCharAt(i, 'u');
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

	public HashMap<String, String> conjugate(String root, int conjugationClass) {

		cs = new ConjugationStructure();
		cs.setVerb(getVerb());
		cs.setInfinitiv(getInfinitiv());
		cs.setRoot(root);
		cs.setEnding(getEnding());

		switch (conjugationClass) {

		case 1:
			cs.setConjugationClass("art-1");
			break;
		case 2:
			cs.setConjugationClass("art-2");
			break;
		case 3:
			cs.setConjugationClass("art-3");
			break;
		case 4:
			cs.setConjugationClass("art-4");
			break;
		case 5:
			cs.setConjugationClass("art-5");
			break;
		case 6:
			cs.setConjugationClass("art-6");
			break;
		case 7:
			cs.setConjugationClass("art-7");
			break;
		case 8:
			cs.setConjugationClass("art-8");
			break;
		case 9:
			cs.setConjugationClass("art-9");
			break;

		}

		cs.setReflexive(getIsReflexive());

		// PRESCHENT
		setPreschent(cs);

		// IMPERFECT
		setImperfect(cs);

		// CONJUNCTIV
		setConjunctiv(cs);

		// CUNDIZIONAL
		setCundizional(cs);

		// PARTICIP PERFECT
		setParticipPerfect(cs);

		// IMPERATIV
		setImperativ(cs);

		// GERUNDIUM
		setGerundium(cs);

		// FUTUR
		setFutur(cs);

		return cs.getValues();

	}

	public void setPreschent(ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {

		case "art-1":
		case "art-2":
		case "art-3":
		case "art-4":
		case "art-5":
		case "art-6":

			// 1ps
			if (ending.equals("ar") && !getInfinitiv().equals("mussar")) {

				if (endsWithDoubleConsonant(root)) {
					String firstSingular = root.substring(0, root.length() - 1);
					cs.setPreschentsing1(firstSingular);
				} else {

					cs.setPreschentsing1(root);
				}

			}

			else {
				cs.setPreschentsing1(root);
			}

			// 2ps
			cs.setPreschentsing2(root + "as");
			// 3ps
			cs.setPreschentsing3(root + "a");

			if (cs.getConjugationclass().equals("art-6")) {
				// 1pp
				cs.setPreschentplural1(root + "ign");
				// 2pp
				cs.setPreschentplural2(root + "iz");
			} else {

				// 1pp
				cs.setPreschentplural1(root + "agn");
				// 2pp
				cs.setPreschentplural2(root + "ez");
			}

			cs.setPreschentplural3(root + "an");

			break;

		case "art-7":
		case "art-8":

			// PRESCHENT
			// 1ps
			cs.setPreschentsing1(root + "esch");
			// 2ps
			cs.setPreschentsing2(root + "eschas");
			// 3ps
			cs.setPreschentsing3(root + "escha");

			if (cs.getConjugationclass().equals("art-7")) {
				// 1pp
				cs.setPreschentplural1(root + "ign");
				// 2pp
				cs.setPreschentplural2(root + "iz");
			} else {
				// 1pp
				cs.setPreschentplural1(root + "agn");
				// 2pp
				cs.setPreschentplural2(root + "ez");
			}

			// 3pp
			cs.setPreschentplural3(root + "eschan");

			break;

		case "art-9":

			switch (getVocalInRoot()) {

			case 'a':
			case 'u':
			case 'i':

				// PRESCHENT
				// 1ps
				if (ending.equals("ar") && !getInfinitiv().equals("mussar")) {

					if (endsWithDoubleConsonant(root)) {
						String firstSingular = modRoot.substring(0,
								modRoot.length() - 1);
						cs.setPreschentsing1(firstSingular);
					} else {

						cs.setPreschentsing1(modRoot);
					}

				}

				else {
					cs.setPreschentsing1(modRoot);
				}

				cs.setPreschentsing2(modRoot + "as");
				cs.setPreschentsing3(modRoot + "a");
				cs.setPreschentplural1(root + "agn");
				cs.setPreschentplural2(root + "ez");
				cs.setPreschentplural3(modRoot + "an");

				break;

			case 'e':
			case 'o':

				// PRESCHENT
				// 1ps
				if (ending.equals("ar")) {

					if (endsWithDoubleConsonant(root)) {
						String firstSingular = root.substring(0,
								root.length() - 1);
						cs.setPreschentsing1(firstSingular);
					} else {

						cs.setPreschentsing1(root);
					}

				}

				else {
					cs.setPreschentsing1(root);
				}

				cs.setPreschentsing2(root + "as");
				cs.setPreschentsing3(root + "a");
				cs.setPreschentplural1(modRoot + "agn");
				cs.setPreschentplural2(modRoot + "ez");
				cs.setPreschentplural3(root + "an");
				break;

			}

		}

	}

	public void setImperfect(ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {

		case "art-1":
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

		case "art-3":
		case "art-6":
		case "art-7":

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

		case "art-2":
		case "art-4":
		case "art-5":
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

		case "art-9":

			if (getEnding().equals("ar") && getVocalInRoot() == 'u') {

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

			} else {

				switch (getVocalInRoot()) {

				case 'a':
				case 'i':

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

				case 'e':
				case 'o':
				case 'u':
					// 1ps
					cs.setImperfectsing1(modRoot + "eva");
					// 2ps
					cs.setImperfectsing2(modRoot + "evas");
					// 3ps
					cs.setImperfectsing3(modRoot + "eva");
					// 1pp
					cs.setImperfectplural1(modRoot + "evan");
					// 2pp
					cs.setImperfectplural2(modRoot + "evas");
					// 3pp
					cs.setImperfectplural3(modRoot + "evan");
					break;

				}

			}
			break;
		}

	}

	public void setConjunctiv(ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {

		case "art-7":
		case "art-8":
			// 1ps
			cs.setConjunctivsing1(root + "escha");
			// 2ps
			cs.setConjunctivsing2(root + "eschas");
			// 3ps
			cs.setConjunctivsing3(root + "escha");
			// 1pp
			cs.setConjunctivplural1(root + "eschan");
			// 2pp
			cs.setConjunctivplural2(root + "eschas");
			// 3pp
			cs.setConjunctivplural3(root + "eschan");
			break;

		case "art-9":

			switch (getVocalInRoot()) {
			case 'e':
			case 'o':
				// 1ps
				cs.setConjunctivsing1(root + "a");
				// 2ps
				cs.setConjunctivsing2(root + "as");
				// 3ps
				cs.setConjunctivsing3(root + "a");
				// 1pp
				cs.setConjunctivplural1(root + "an");
				// 2pp
				cs.setConjunctivplural2(root + "as");
				// 3pp
				cs.setConjunctivplural3(root + "an");

				break;

			case 'a':
			case 'i':
			case 'u':

				// 1ps
				cs.setConjunctivsing1(modRoot + "a");
				// 2ps
				cs.setConjunctivsing2(modRoot + "as");
				// 3ps
				cs.setConjunctivsing3(modRoot + "a");
				// 1pp
				cs.setConjunctivplural1(modRoot + "an");
				// 2pp
				cs.setConjunctivplural2(modRoot + "as");
				// 3pp
				cs.setConjunctivplural3(modRoot + "an");

				break;

			}

			break;

		default:
			// 1ps
			cs.setConjunctivsing1(root + "a");
			// 2ps
			cs.setConjunctivsing2(root + "as");
			// 3ps
			cs.setConjunctivsing3(root + "a");
			// 1pp
			cs.setConjunctivplural1(root + "an");
			// 2pp
			cs.setConjunctivplural2(root + "as");
			// 3pp
			cs.setConjunctivplural3(root + "an");
			break;
		}
	}

	public void setCundizional(ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {

		case "art-6":
		case "art-7":
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

		case "art-9":

			switch (getVocalInRoot()) {

			case 'e':
			case 'o':

				// 1ps
				cs.setCundizionalsing1(modRoot + "ess");
				// 2ps
				cs.setCundizionalsing2(modRoot + "essas");
				// 3ps
				cs.setCundizionalsing3(modRoot + "ess");
				// 1pp
				cs.setCundizionalplural1(modRoot + "essan");
				// 2pp
				cs.setCundizionalplural2(modRoot + "essas");
				// 3pp
				cs.setCundizionalplural3(modRoot + "essan");

				break;

			case 'a':
			case 'i':
			case 'u':

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
			break;

		default:
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

	public void setParticipPerfect(ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-1":
		case "art-8":

			cs.setParticipperfectms(root + "o");
			cs.setParticipperfectmp(root + "os");
			cs.setParticipperfectfs(root + "ada");
			cs.setParticipperfectfp(root + "adas");

			break;

		case "art-2":

			cs.setParticipperfectms(root + "ea");
			cs.setParticipperfectmp(root + "eas");
			cs.setParticipperfectfs(root + "eda");
			cs.setParticipperfectfp(root + "edas");

			break;

		case "art-9":

			if (ending.equals("ar")) {

				cs.setParticipperfectms(root + "o");
				cs.setParticipperfectmp(root + "os");
				cs.setParticipperfectfs(root + "ada");
				cs.setParticipperfectfp(root + "adas");

			}

			else {

				switch (getVocalInRoot()) {

				case 'e':
				case 'o':

					cs.setParticipperfectms(modRoot + "ia");
					cs.setParticipperfectmp(modRoot + "ias");
					cs.setParticipperfectfs(modRoot + "eida");
					cs.setParticipperfectfp(modRoot + "eidas");

					break;

				case 'a':

					cs.setParticipperfectms(root + "o");
					cs.setParticipperfectmp(root + "os");
					cs.setParticipperfectfs(root + "ada");
					cs.setParticipperfectfp(root + "adas");

					break;

				case 'i':

					cs.setParticipperfectms(modRoot + "a");
					cs.setParticipperfectmp(modRoot + "as");
					cs.setParticipperfectfs(modRoot + "ada");
					cs.setParticipperfectfp(modRoot + "adas");

					break;

				case 'u':

					cs.setParticipperfectms(root + "ia");
					cs.setParticipperfectmp(root + "ias");
					cs.setParticipperfectfs(root + "eida");
					cs.setParticipperfectfp(root + "eidas");

					break;

				}
			}

			break;

		default:

			cs.setParticipperfectms(root + "ia");
			cs.setParticipperfectmp(root + "ias");
			cs.setParticipperfectfs(root + "eida");
			cs.setParticipperfectfp(root + "eidas");

			break;

		}

	}

	public void setGerundium(ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {
		case "art-6":
		case "art-7":
			cs.setGerundium(root + "ond" + "/" + root + "end");
			break;

		case "art-9":

			switch (getVocalInRoot()) {

			case 'a':
			case 'i':
			case 'u':
				cs.setGerundium(root + "ond");
				break;

			case 'o':
			case 'e':
				cs.setGerundium(modRoot + "ond");
				break;

			}
			break;
		default:
			cs.setGerundium(root + "ond");
			break;
		}

	}

	public void setImperativ(ConjugationStructure cs) {

		switch (cs.getConjugationclass()) {

		case "art-7":
		case "art-8":

			cs.setImperativ1(root + "escha!");

			if (cs.getConjugationclass().equals("art-7")) {
				cs.setImperativ2(root + "i!");

			} else {
				cs.setImperativ2(root + "e!");

			}

			break;

		case "art-9":

			switch (getVocalInRoot()) {

			case 'a':
			case 'i':
			case 'u':

				cs.setImperativ1(modRoot + "a!");
				cs.setImperativ2(root + "e!");

				break;

			case 'e':
			case 'o':

				cs.setImperativ1(root + "a!");
				cs.setImperativ2(modRoot + "e!");
				break;
			}
			break;
		default:

			cs.setImperativ1(root + "a!");
			if (cs.getConjugationclass().equals("art-6")) {
				cs.setImperativ2(root + "i!");

			} else {
				cs.setImperativ2(root + "e!");

			}

			break;

		}

	}

	public void setFutur(ConjugationStructure cs) {

		switch (getIsReflexive()) {

		case "true":

			if (startsWithVocal(root)) {

				switch (cs.getConjugationclass()) {

				case "art-6":
				case "art-7":

					cs.setFutursing1(Pronouns.pron_r_v_1ps + root + "iro");
					cs.setFutursing2(Pronouns.pron_r_v_2ps + root + "irossas");
					cs.setFutursing3(Pronouns.pron_r_v_3ps + root + "iro");
					cs.setFuturplural1(Pronouns.pron_r_v_1pp + root + "iron");
					cs.setFuturplural2(Pronouns.pron_r_v_2pp + root + "irossas");
					cs.setFuturplural3(Pronouns.pron_r_v_3pp + root + "iron");

					break;

				case "art-9":

					switch (getVocalInRoot()) {

					case 'e':
					case 'o':

						cs.setFutursing1(Pronouns.pron_r_v_1ps + modRoot
								+ "aro");
						cs.setFutursing2(Pronouns.pron_r_v_2ps + modRoot
								+ "arossas");
						cs.setFutursing3(Pronouns.pron_r_v_3ps + modRoot
								+ "aro");
						cs.setFuturplural1(Pronouns.pron_r_v_1pp + modRoot
								+ "aron");
						cs.setFuturplural2(Pronouns.pron_r_v_2pp + modRoot
								+ "arossas");
						cs.setFuturplural3(Pronouns.pron_r_v_3pp + modRoot
								+ "aron");

						break;

					case 'a':
					case 'i':
					case 'u':

						cs.setFutursing1(Pronouns.pron_r_v_1ps + root + "aro");
						cs.setFutursing2(Pronouns.pron_r_v_2ps + root
								+ "arossas");
						cs.setFutursing3(Pronouns.pron_r_v_3ps + root + "aro");
						cs.setFuturplural1(Pronouns.pron_r_v_1pp + root
								+ "aron");
						cs.setFuturplural2(Pronouns.pron_r_v_2pp + root
								+ "arossas");
						cs.setFuturplural3(Pronouns.pron_r_v_3pp + root
								+ "aron");

						break;

					}

					break;

				default:

					cs.setFutursing1(Pronouns.pron_r_v_1ps + root + "aro");
					cs.setFutursing2(Pronouns.pron_r_v_2ps + root + "arossas");
					cs.setFutursing3(Pronouns.pron_r_v_3ps + root + "aro");
					cs.setFuturplural1(Pronouns.pron_r_v_1pp + root + "aron");
					cs.setFuturplural2(Pronouns.pron_r_v_2pp + root + "arossas");
					cs.setFuturplural3(Pronouns.pron_r_v_3pp + root + "aron");

					break;

				}

			} else {

				switch (cs.getConjugationclass()) {

				case "art-6":
				case "art-7":

					cs.setFutursing1(Pronouns.pron_r_1ps + root + "iro");
					cs.setFutursing2(Pronouns.pron_r_2ps + root + "irossas");
					cs.setFutursing3(Pronouns.pron_r_3ps + root + "iro");
					cs.setFuturplural1(Pronouns.pron_r_1pp + root + "iron");
					cs.setFuturplural2(Pronouns.pron_r_2pp + root + "irossas");
					cs.setFuturplural3(Pronouns.pron_r_3pp + root + "iron");

					break;

				case "art-9":

					switch (getVocalInRoot()) {

					case 'e':
					case 'o':

						cs.setFutursing1(Pronouns.pron_r_1ps + modRoot + "aro");
						cs.setFutursing2(Pronouns.pron_r_2ps + modRoot
								+ "arossas");
						cs.setFutursing3(Pronouns.pron_r_3ps + modRoot + "aro");
						cs.setFuturplural1(Pronouns.pron_r_1pp + modRoot
								+ "aron");
						cs.setFuturplural2(Pronouns.pron_r_2pp + modRoot
								+ "arossas");
						cs.setFuturplural3(Pronouns.pron_r_3pp + modRoot
								+ "aron");

						break;

					case 'a':
					case 'i':
					case 'u':

						cs.setFutursing1(Pronouns.pron_r_1ps + root + "aro");
						cs.setFutursing2(Pronouns.pron_r_2ps + root + "arossas");
						cs.setFutursing3(Pronouns.pron_r_3ps + root + "aro");
						cs.setFuturplural1(Pronouns.pron_r_1pp + root + "aron");
						cs.setFuturplural2(Pronouns.pron_r_2pp + root
								+ "arossas");
						cs.setFuturplural3(Pronouns.pron_r_3pp + root + "aron");

						break;

					}
					break;

				default:

					cs.setFutursing1(Pronouns.pron_r_1ps + root + "aro");
					cs.setFutursing2(Pronouns.pron_r_2ps + root + "arossas");
					cs.setFutursing3(Pronouns.pron_r_3ps + root + "aro");
					cs.setFuturplural1(Pronouns.pron_r_1pp + root + "aron");
					cs.setFuturplural2(Pronouns.pron_r_2pp + root + "arossas");
					cs.setFuturplural3(Pronouns.pron_r_3pp + root + "aron");

					break;

				}

			}

			break;

		case "false":

			switch (cs.getConjugationclass()) {

			case "art-6":
			case "art-7":

				cs.setFutursing1(root + "iro");
				cs.setFutursing2(root + "irossas");
				cs.setFutursing3(root + "iro");
				cs.setFuturplural1(root + "iron");
				cs.setFuturplural2(root + "irossas");
				cs.setFuturplural3(root + "iron");

				break;

			case "art-9":

				switch (getVocalInRoot()) {

				case 'e':
				case 'o':

					cs.setFutursing1(modRoot + "aro");
					cs.setFutursing2(modRoot + "arossas");
					cs.setFutursing3(modRoot + "aro");
					cs.setFuturplural1(modRoot + "aron");
					cs.setFuturplural2(modRoot + "arossas");
					cs.setFuturplural3(modRoot + "aron");
					break;

				case 'a':
				case 'i':
				case 'u':

					cs.setFutursing1(root + "aro");
					cs.setFutursing2(root + "arossas");
					cs.setFutursing3(root + "aro");
					cs.setFuturplural1(root + "aron");
					cs.setFuturplural2(root + "arossas");
					cs.setFuturplural3(root + "aron");
					break;

				}

				break;

			default:

				cs.setFutursing1(root + "aro");
				cs.setFutursing2(root + "arossas");
				cs.setFutursing3(root + "aro");
				cs.setFuturplural1(root + "aron");
				cs.setFuturplural2(root + "arossas");
				cs.setFuturplural3(root + "aron");

				break;

			}

		}

	}

	public boolean startsWithVocal(String root) {

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

	public boolean endsWithDoubleConsonant(String root) {

		if (root.charAt(root.length() - 1) == root.charAt(root.length() - 2)) {

			return true;

		} else
			return false;
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
		cs.setConjunctivsing1(Pronouns.pron_conjunctiv_v + Pronouns.pron_1ps
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
		pronouns.setFirstPsC(Pronouns.pron_conjunctiv_v + Pronouns.pron_1ps
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
		pronouns.setPp_1(Pronouns.pron_r_3ps);
		pronouns.setPp_2(Pronouns.pron_r_3ps);

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
		pronouns.setFirstPsC(Pronouns.pron_conjunctiv_v + Pronouns.pron_1ps
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
		pronouns.setPp_1(Pronouns.pron_r_v_3ps);
		pronouns.setPp_2(Pronouns.pron_r_v_3ps);

		// IMPERATIV
		pronouns.setImperat1(Pronouns.pron_r_v_2ps);
		pronouns.setImperat2(Pronouns.pron_r_v_2pp);

		// GERUNDIUM
		pronouns.setGer(Pronouns.pron_r_v_3ps);

		return pronouns.getValues();
	}

	public void printConjugation(Map<String, String> conjugation, String conj)
			throws IOException, FileNotFoundException {

		File file = new File("/Users/franciscomondaca/Desktop/" + conj + ".txt");
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		out.append(conjugation.get("verb"));
		out.append("\n");
		out.append("\n");
		out.append(conjugation.get("conjugationclass"));
		out.append("\n");
		out.append("\n");

		out.append("Preschaint");
		out.append("\n");
		out.append(conjugation.get("preschentsing1"));
		out.append("\n");
		out.append(conjugation.get("preschentsing2"));
		out.append("\n");
		out.append(conjugation.get("preschentsing3"));
		out.append("\n");
		out.append(conjugation.get("preschentplural1"));
		out.append("\n");
		out.append(conjugation.get("preschentplural2"));
		out.append("\n");
		out.append(conjugation.get("preschentplural3"));
		out.append("\n");
		out.append("\n");

		out.append("Imperfect");
		out.append("\n");
		out.append(conjugation.get("imperfectsing1"));
		out.append("\n");
		out.append(conjugation.get("imperfectsing2"));
		out.append("\n");
		out.append(conjugation.get("imperfectsing3"));
		out.append("\n");
		out.append(conjugation.get("imperfectplural1"));
		out.append("\n");
		out.append(conjugation.get("imperfectplural2"));
		out.append("\n");
		out.append(conjugation.get("imperfectplural3"));
		out.append("\n");
		out.append("\n");

		out.append("Futur");
		out.append("\n");
		out.append(conjugation.get("futursing1"));
		out.append("\n");
		out.append(conjugation.get("futursing2"));
		out.append("\n");
		out.append(conjugation.get("futursing3"));
		out.append("\n");
		out.append(conjugation.get("futurplural1"));
		out.append("\n");
		out.append(conjugation.get("futurplural2"));
		out.append("\n");
		out.append(conjugation.get("futurplural3"));
		out.append("\n");
		out.append("\n");

		out.append("Conjunctiv");
		out.append("\n");
		out.append(conjugation.get("conjunctivsing1"));
		out.append("\n");
		out.append(conjugation.get("conjunctivsing2"));
		out.append("\n");
		out.append(conjugation.get("conjunctivsing3"));
		out.append("\n");
		out.append(conjugation.get("conjunctivplural1"));
		out.append("\n");
		out.append(conjugation.get("conjunctivplural2"));
		out.append("\n");
		out.append(conjugation.get("conjunctivplural3"));
		out.append("\n");
		out.append("\n");

		out.append("Cundiziunal");
		out.append("\n");
		out.append(conjugation.get("cundizionalsing1"));
		out.append("\n");
		out.append(conjugation.get("cundizionalsing2"));
		out.append("\n");
		out.append(conjugation.get("cundizionalsing3"));
		out.append("\n");
		out.append(conjugation.get("cundizionalplural1"));
		out.append("\n");
		out.append(conjugation.get("cundizionalplural2"));
		out.append("\n");
		out.append(conjugation.get("cundizionalplural3"));
		out.append("\n");
		out.append("\n");

		out.append("Particip Perfect");
		out.append("\n");
		out.append(conjugation.get("participperfectms"));
		out.append("\n");
		out.append(conjugation.get("participperfectmp"));
		out.append("\n");
		out.append(conjugation.get("participperfectfs"));
		out.append("\n");
		out.append(conjugation.get("participperfectfp"));
		out.append("\n");
		out.append("\n");

		out.append("Gerundi");
		out.append("\n");
		out.append(conjugation.get("gerundium"));
		out.append("\n");
		out.append("\n");

		out.append("Imperativ");
		out.append("\n");
		out.append(conjugation.get("imperativ1"));
		out.append("\n");
		out.append(conjugation.get("imperativ2"));
		out.append("\n");
		out.append("\n");

		out.close();

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

	public char getVocalInRoot() {
		return vocalInRoot;
	}

	public void setVocalInRoot(char vocalInRoot) {
		this.vocalInRoot = vocalInRoot;
	}

}

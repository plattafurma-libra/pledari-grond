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
package de.uni_koeln.spinfo.maalr.conjugator.comparator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationGenerator;
import de.uni_koeln.spinfo.maalr.conjugator.generator.ConjugationStructure;
import de.uni_koeln.spinfo.maalr.conjugator.parser.DBParser;

public class ConjugationComparator {

	private ArrayList<String> mismatches;
	private int mismatchCounter;
	private HashMap<String, String> diff;
	private ArrayList<HashMap<String, String>> diffList;
	private ArrayList<Integer> mismatchCounterList;

	static String mismatches_path = "data/mismatches/";

	public ArrayList<MismatchOverview> getMismatchOverviewList()
			throws Exception {

		DBParser parser = new DBParser();
		List<ConjugationStructure> bc = parser.deliverConjugationsFromMySQL();
		ArrayList<MismatchOverview> mismatchList = new ArrayList<MismatchOverview>();

		for (ConjugationStructure c : bc) {
			String query = c.getInfinitiv();
			ConjugationGenerator conjugations = new ConjugationGenerator();
			ConjugationComparator comparator = new ConjugationComparator();
			conjugations.setConjugations(conjugations.generateAll(query));
			MismatchOverview mismatchOverview = comparator.getMismatchOverview(
					conjugations.getConjugations(), c);
			mismatchList.add(mismatchOverview);
		}

		return mismatchList;
	}

	public ArrayList<MismatchOverview> getOneMismatchList(
			ArrayList<MismatchOverview> mismatchList) {
		ArrayList<MismatchOverview> mismatchList_1 = new ArrayList<MismatchOverview>();

		for (MismatchOverview overview : mismatchList) {
			ArrayList<Integer> misInts = overview.getMisInts();
			// Add verbs with 1 mismatch
			if (misInts.contains(1) && !misInts.contains(0)) {
				mismatchList_1.add(overview);
			}
		}

		return mismatchList_1;
	}

	// Outputs a general overview of all verbs and their mismatches
	public File outputOverview(ArrayList<MismatchOverview> mismatchesList,
			String filename) throws Exception {

		File file = new File("data/mismatches/" + filename);
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));
		Collections.sort(mismatchesList,
				new MismatchOverview.MismatchOverviewComparator());

		for (MismatchOverview overview : mismatchesList) {
			ArrayList<Integer> misInts = overview.getMisInts();
			ArrayList<HashMap<String, String>> diffList = overview
					.getDiffList();

			writer.append(misInts.get(0) + "\t" + overview.getVerb() + "\t"
					+ "ART-1" + "\t" + diffList.get(0) + "\n");
			writer.append(misInts.get(1) + "\t" + overview.getVerb() + "\t"
					+ "ART-2_3" + "\t" + diffList.get(1) + "\n");
			writer.append(misInts.get(2) + "\t" + overview.getVerb() + "\t"
					+ "ART-4" + "\t" + diffList.get(2) + "\n");
			writer.append(misInts.get(3) + "\t" + overview.getVerb() + "\t"
					+ "ART-5" + "\t" + diffList.get(3) + "\n");
			writer.append(misInts.get(4) + "\t" + overview.getVerb() + "\t"
					+ "ART-6" + "\t" + diffList.get(4) + "\n");
			writer.append(misInts.get(5) + "\t" + overview.getVerb() + "\t"
					+ "ART-7" + "\t" + diffList.get(5) + "\n");
			writer.append(misInts.get(6) + "\t" + overview.getVerb() + "\t"
					+ "ART-8" + "\t" + diffList.get(6) + "\n");

		}
		writer.flush();
		writer.close();

		return file;

	}

	/**
	 * 
	 * Outputs verbs with a specific number of mismatches
	 * 
	 * @param mismatchList
	 * @param mismatches
	 * @return mismatchMap
	 */
	public Map<String, List<Integer>> outputSpecMismatches(
			ArrayList<MismatchOverview> mismatchList, int mismatches) {

		Collections.sort(mismatchList,
				new MismatchOverview.MismatchOverviewComparator());
		Map<String, List<Integer>> mismatchMap = new TreeMap<String, List<Integer>>();

		for (MismatchOverview overview : mismatchList) {
			List<Integer> misInts = overview.getMisInts();
			List<Integer> conjClass = new ArrayList<>();
			String verb = overview.getVerb();

			for (int i = 0; i < misInts.size(); i++) {
				int v = misInts.get(i);

				if (v == mismatches) {

					switch (i) {
					case 0:
						conjClass.add(0);
						break;
					case 1:
						conjClass.add(1);
						break;
					case 2:
						conjClass.add(2);
						break;
					case 3:
						conjClass.add(3);
						break;
					case 4:
						conjClass.add(4);
						break;
					case 5:
						conjClass.add(5);
						break;
					case 6:
						conjClass.add(6);
						break;
					}
				}
			}
			if (!conjClass.isEmpty()) {
				mismatchMap.put(verb, conjClass);
			}

		}

		return mismatchMap;
	}

	/**
	 * Outputs verbs with a specific number of mismatches, avoiding others
	 * 
	 * @param mismatchList
	 * @param search
	 * @param avoid
	 * @return mismatchMap
	 */
	public Map<String, List<Integer>> outputMismatches(
			ArrayList<MismatchOverview> mismatchList, int search, int avoid) {

		Collections.sort(mismatchList,
				new MismatchOverview.MismatchOverviewComparator());
		Map<String, List<Integer>> mismatchMap = new TreeMap<String, List<Integer>>();

		for (MismatchOverview overview : mismatchList) {

			List<Integer> misInts = overview.getMisInts();
			List<Integer> searchList = new ArrayList<>();
			List<Integer> avoidList = new ArrayList<>();
			String verb = overview.getVerb();

			for (int i = 0; i < misInts.size(); i++) {

				int v = misInts.get(i);
				if (v == search) {

					switch (i) {
					case 0:
						searchList.add(0);
						break;
					case 1:
						searchList.add(1);
						break;
					case 2:
						searchList.add(2);
						break;
					case 3:
						searchList.add(3);
						break;
					case 4:
						searchList.add(4);
						break;
					case 5:
						searchList.add(5);
						break;
					case 6:
						searchList.add(6);
						break;
					}
				}

				if (v == avoid) {

					switch (i) {
					case 0:
						avoidList.add(0);
						break;
					case 1:
						avoidList.add(1);
						break;
					case 2:
						avoidList.add(2);
						break;
					case 3:
						avoidList.add(3);
						break;
					case 4:
						avoidList.add(4);
						break;
					case 5:
						avoidList.add(5);
						break;
					case 6:
						avoidList.add(6);
						break;
					}
				}

			}
			if (!searchList.isEmpty() && avoidList.isEmpty()) {
				mismatchMap.put(verb, searchList);
			}

		}

		return mismatchMap;
	}

	public Map<String, Map<String, Map<String, String>>> outputMismatchesB(
			ArrayList<MismatchOverview> mismatchList, int search, int avoid) {

		Collections.sort(mismatchList,
				new MismatchOverview.MismatchOverviewComparator());
		Map<String, Map<String, Map<String, String>>> mismatchMap = new TreeMap<String, Map<String, Map<String, String>>>();
		Map<String, Map<String, String>> detailsMap = new LinkedHashMap<String, Map<String, String>>();

		for (MismatchOverview overview : mismatchList) {

			List<Integer> misInts = overview.getMisInts();
			ArrayList<HashMap<String, String>> diffList = overview
					.getDiffList();
			List<String> searchList = new ArrayList<>();
			List<Integer> avoidList = new ArrayList<>();
			String verb = overview.getVerb();
			String conjugation = null;

			for (int i = 0; i < misInts.size(); i++) {
				int v = misInts.get(i);

				if (v == search) {

					switch (i) {
					case 0:
						conjugation = "ART-1";
						searchList.add(conjugation);
						detailsMap.put(conjugation, diffList.get(0));
						break;
					case 1:
						searchList.add("ART-2_3");
						conjugation = "ART-2_3";
						detailsMap.put(conjugation, diffList.get(1));
						break;
					case 2:
						searchList.add("ART-4");
						conjugation = "ART-4";
						detailsMap.put(conjugation, diffList.get(2));
						break;
					case 3:
						searchList.add("ART-5");
						conjugation = "ART-5";
						detailsMap.put(conjugation, diffList.get(3));
						break;
					case 4:
						searchList.add("ART-6");
						conjugation = "ART-6";
						detailsMap.put(conjugation, diffList.get(4));
						break;
					case 5:
						searchList.add("ART-7");
						conjugation = "ART-7";
						detailsMap.put(conjugation, diffList.get(5));
						break;
					case 6:
						searchList.add("ART-8");
						conjugation = "ART-7";
						detailsMap.put(conjugation, diffList.get(6));
						break;
					}
				}

				if (v == avoid) {

					switch (i) {
					case 0:
						avoidList.add(0);
						break;
					case 1:
						avoidList.add(1);
						break;
					case 2:
						avoidList.add(2);
						break;
					case 3:
						avoidList.add(3);
						break;
					case 4:
						avoidList.add(4);
						break;
					case 5:
						avoidList.add(5);
						break;
					case 6:
						avoidList.add(6);
						break;
					}
				}

			}
			if (!searchList.isEmpty() && avoidList.isEmpty()) {
				mismatchMap.put(verb, detailsMap);
			}

		}

		return mismatchMap;
	}

	public File printMismatchesB(
			Map<String, Map<String, Map<String, String>>> bigMap,
			String filename) throws IOException {
		File file = new File("data/mismatches/" + filename);

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));
		String verb;
		String conjugation;
		String real = "";
		String expected = "";

		for (Map.Entry<String, Map<String, Map<String, String>>> entryBigMap : bigMap
				.entrySet()) {

			verb = entryBigMap.getKey();

			// writer.append(verb + "\t");

			Map<String, Map<String, String>> detailsMap = entryBigMap
					.getValue();

			for (Map.Entry<String, Map<String, String>> entryDetailsMap : detailsMap
					.entrySet()) {

				conjugation = entryDetailsMap.getKey();

				Map<String, String> diffMap = entryDetailsMap.getValue();

				for (Map.Entry<String, String> entryDiffMap : diffMap
						.entrySet()) {
					expected = entryDiffMap.getKey();
					real = entryDiffMap.getValue();
					writer.append(expected + " " + real + "\n");
				}
				writer.append(verb + "\t" + conjugation + "\t");

			}

		}

		writer.flush();
		writer.close();
		return file;
	}

	public File outputDetails(ArrayList<MismatchOverview> mismatchList,
			String filename) throws IOException {

		File file = new File("data/mismatches/" + filename);

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		Collections.sort(mismatchList,
				new MismatchOverview.MismatchOverviewComparator());

		for (MismatchOverview overview : mismatchList) {

			ArrayList<Integer> misInts = overview.getMisInts();
			ArrayList<HashMap<String, String>> diffList = overview
					.getDiffList();

			for (int i = 0; i < misInts.size(); i++) {

				int v = misInts.get(i);

				if (v == 1) {

					switch (i) {

					case 0:
						writer.append(overview.getVerb() + "\t");

						writer.append(misInts.get(0) + "\t" + "ART-1" + "\t"
								+ diffList.get(0));
						break;

					case 1:
						writer.append(overview.getVerb() + "\t");

						writer.append(misInts.get(1) + "\t" + "ART-2_3" + "\t"
								+ diffList.get(1));
						break;

					case 2:
						writer.append(overview.getVerb() + "\t");

						writer.append(misInts.get(2) + "\t" + "ART-4" + "\t"
								+ diffList.get(2));
						break;

					case 3:
						writer.append(overview.getVerb() + "\t");

						writer.append(misInts.get(3) + "\t" + "ART-5" + "\t"
								+ diffList.get(3));
						break;

					case 4:
						writer.append(overview.getVerb() + "\t");

						writer.append(misInts.get(4) + "\t" + "ART-6" + "\t"
								+ diffList.get(4));
						break;

					case 5:
						writer.append(overview.getVerb() + "\t");

						writer.append(misInts.get(5) + "\t" + "ART-7" + "\t"
								+ diffList.get(5));
						break;

					case 6:
						writer.append(overview.getVerb() + "\t");

						writer.append(misInts.get(6) + "\t" + "ART-8" + "\t"
								+ diffList.get(6));
						break;
					}
					writer.append("\n");

				}

			}

		}
		writer.flush();
		writer.close();

		return file;
	}

	/**
	 * Outputs verbs with one mismatch, with their respective conjugations and
	 * strings that mismatch
	 * 
	 * @param mismatchList_1
	 *            MissmatchList with Verbs with one mismatch
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public File outputOneMismatch(ArrayList<MismatchOverview> mismatchList_1,
			String filename) throws IOException {

		File file = new File("data/mismatches/" + filename);

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		Collections.sort(mismatchList_1,
				new MismatchOverview.MismatchOverviewComparator());

		for (MismatchOverview overview : mismatchList_1) {

			ArrayList<Integer> misInts = overview.getMisInts();
			ArrayList<HashMap<String, String>> diffList = overview
					.getDiffList();

			for (int i = 0; i < misInts.size(); i++) {
				switch (i) {
				case 0:
					writer.append(overview.getVerb() + "\t");
					writer.append(misInts.get(0) + "\t" + "ART-1" + "\t"
							+ diffList.get(0));
					break;
				case 1:
					writer.append(overview.getVerb() + "\t");
					writer.append(misInts.get(1) + "\t" + "ART-2_3" + "\t"
							+ diffList.get(1));
					break;
				case 2:
					writer.append(overview.getVerb() + "\t");
					writer.append(misInts.get(2) + "\t" + "ART-4" + "\t"
							+ diffList.get(2));
					break;
				case 3:
					writer.append(overview.getVerb() + "\t");
					writer.append(misInts.get(3) + "\t" + "ART-5" + "\t"
							+ diffList.get(3));
					break;
				case 4:
					writer.append(overview.getVerb() + "\t");
					writer.append(misInts.get(4) + "\t" + "ART-6" + "\t"
							+ diffList.get(4));
					break;
				case 5:
					writer.append(overview.getVerb() + "\t");
					writer.append(misInts.get(5) + "\t" + "ART-7" + "\t"
							+ diffList.get(5));
					break;
				case 6:
					writer.append(overview.getVerb() + "\t");
					writer.append(misInts.get(6) + "\t" + "ART-8" + "\t"
							+ diffList.get(6));
					break;
				}
				writer.append("\n");
				// }
			}

		}
		writer.flush();
		writer.close();

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

	public MismatchOverview getMismatchOverview(
			ArrayList<HashMap<String, String>> generatedConjugations,
			ConjugationStructure conjugationStructure) throws IOException {

		mismatchCounterList = new ArrayList<Integer>();
		diffList = new ArrayList<HashMap<String, String>>();

		for (HashMap<String, String> genConjugation : generatedConjugations) {

			mismatches = new ArrayList<String>();
			diff = new HashMap<String, String>();
			mismatchCounter = 0;

			for (String key : conjugationStructure.msi) {

				if (genConjugation == null || genConjugation.get(key) == null) {
					continue;
				}

				String[] cstructr = conjugationStructure.getValue(key).split(
						"/");
				ArrayList<String> al = new ArrayList<String>(
						Arrays.asList(cstructr));

				// if there is only one string atfer splitting
				if (al.size() == 1) {
					String string = al.get(0);

					if (!string.equals(genConjugation.get(key))) {
						mismatchCounter++;
						diff.put(key, genConjugation.get(key) + " " + string);
						mismatches.add(genConjugation.get(key) + " " + string
								+ " " + key);
					}
				}

				// if there are two strings after splitting
				if (al.size() == 2) {
					String ftoken = al.get(0);
					String stoken = al.get(1);
					// increase mismatchCounter only if both strings don't
					// match the
					// generated form.
					if (!ftoken.equals(genConjugation.get(key))
							&& !stoken.equals(genConjugation.get(key))) {
						mismatchCounter++;
						diff.put(key, genConjugation.get(key) + " " + ftoken
								+ "/" + stoken);
						mismatches.add(genConjugation.get(key) + " " + ftoken
								+ "/" + stoken + " " + key);
					}
				}

				al.clear();

			}
			diffList.add(diff);
			mismatchCounterList.add(mismatchCounter);
		}

		if (mismatchCounterList.size() == 7) {
			MismatchOverview mismatchOverview = new MismatchOverview(
					conjugationStructure.getInfinitiv(), mismatchCounterList,
					diffList);
			// System.out.println(missList);
			// System.out.println(diffList);

			return mismatchOverview;
		}

		return null;
	}

}

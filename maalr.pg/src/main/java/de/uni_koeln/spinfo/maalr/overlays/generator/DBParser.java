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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DBParser {

	// Path to the original DB in working copy
	static String path_to_unparsed_verbs = "data/db/verbs.sql";
	static String path_to_unparsed_verbs_filemaker = "data/db/verbs_rgfm.tab";

	// Path to a parsed version of the original DB
	static String path_to_verbs_rg = "jdbc:mysql://localhost/verbs_rg";

	ConjugationStructure bc = new ConjugationStructure();

	String whitespace_chars = "" /* dummy empty string for homogeneity */
			+ "\\u0009" // CHARACTER TABULATION
			+ "\\u000A" // LINE FEED (LF)
			+ "\\u000B" // LINE TABULATION
			+ "\\u000C" // FORM FEED (FF)
			+ "\\u000D" // CARRIAGE RETURN (CR)
			+ "\\u0020" // SPACE
			+ "\\u0085" // NEXT LINE (NEL)
			+ "\\u00A0" // NO-BREAK SPACE
			+ "\\u1680" // OGHAM SPACE MARK
			+ "\\u180E" // MONGOLIAN VOWEL SEPARATOR
			+ "\\u2000" // EN QUAD
			+ "\\u2001" // EM QUAD
			+ "\\u2002" // EN SPACE
			+ "\\u2003" // EM SPACE
			+ "\\u2004" // THREE-PER-EM SPACE
			+ "\\u2005" // FOUR-PER-EM SPACE
			+ "\\u2006" // SIX-PER-EM SPACE
			+ "\\u2007" // FIGURE SPACE
			+ "\\u2008" // PUNCTUATION SPACE
			+ "\\u2009" // THIN SPACE
			+ "\\u200A" // HAIR SPACE
			+ "\\u2028" // LINE SEPARATOR
			+ "\\u2029" // PARAGRAPH SEPARATOR
			+ "\\u202F" // NARROW NO-BREAK SPACE
			+ "\\u205F" // MEDIUM MATHEMATICAL SPACE
			+ "\\u3000" // IDEOGRAPHIC SPACE
			+ "\u001D"; // VETICAL TAB

	String whitespace_charclass = "[" + whitespace_chars + "]";

	private boolean reflexive;
	private boolean vocal;

	private String stamm;

	public Map<String, Map<String, String>> parseDB(BufferedReader br)
			throws Exception {

		Map<String, Map<String, String>> bigMap = new LinkedHashMap<>();

		String brl;

		while ((brl = br.readLine()) != null) {

			List<String> tokens = new ArrayList<String>(Arrays.asList(brl
					.split("\\t")));

			// TODO: Hier alle Elemente auflisten, die nicht importiert werden
			// sollen
			List<String> filterKeys = Arrays.asList("preschentVorbereitung");
			Set<String> filter = new HashSet<>(filterKeys);

			Iterator<String> iterator = tokens.iterator();

			while (iterator.hasNext()) {

				Map<String, String> typeMap = new LinkedHashMap<>();

				// See line numbers with TextWrangler (showTabsModus) or
				// similar Program in
				// /genfiles/fm_0.txt

				// 0
				typeMap.put(FMStrings.verb, iterator.next());
				// 1
				typeMap.put(FMStrings.text, iterator.next());
				// 2
				typeMap.put(FMStrings.recId, iterator.next());
				// 3
				typeMap.put(FMStrings.stamm, iterator.next());
				// 4
				typeMap.put(FMStrings.art, iterator.next());
				// 5
				typeMap.put(FMStrings.u_A, iterator.next());
				// 6
				typeMap.put(FMStrings.sonderart, iterator.next());
				// 7
				typeMap.put(FMStrings.sonderPreschent, iterator.next());
				// 8
				typeMap.put(FMStrings.sonderConjunctiv, iterator.next());
				// 9
				typeMap.put(FMStrings.sonderImperfect, iterator.next());
				// 10
				typeMap.put(FMStrings.sonderCundiziunal, iterator.next());
				// 11
				typeMap.put(FMStrings.sonderPart_Perfect, iterator.next());
				// 12
				typeMap.put(FMStrings.unregelm, iterator.next());
				// 13
				typeMap.put(FMStrings.sonderImperativ, iterator.next());
				// 14
				typeMap.put(FMStrings.sonderGerundium, iterator.next());
				// 15
				typeMap.put(FMStrings.preschent, iterator.next());
				// 16
				typeMap.put(FMStrings.preschentVorbereitung, iterator.next());
				// 17
				typeMap.put(FMStrings.imperfect, iterator.next());
				// 18
				typeMap.put(FMStrings.conjunctiv, iterator.next());
				// 19
				typeMap.put(FMStrings.cundizional, iterator.next());
				// 20
				typeMap.put(FMStrings.part_Perfect, iterator.next());
				// 21
				typeMap.put(FMStrings.part_PerfectVorbereitung, iterator.next());
				// 22
				typeMap.put(FMStrings.imperativ, iterator.next());
				// 23
				typeMap.put(FMStrings.imperativVorbereitung, iterator.next());
				// 24
				typeMap.put(FMStrings.gerundium, iterator.next());
				// 25
				typeMap.put(FMStrings.futur, iterator.next());
				// 26
				typeMap.put(FMStrings.formPerson, iterator.next());
				// 27
				typeMap.put(FMStrings.formPersonConjunctiv, iterator.next());
				// 28
				typeMap.put(FMStrings.formPart_Perfect, iterator.next());
				// 29
				typeMap.put(FMStrings.formUnregelm, iterator.next());
				// 30
				typeMap.put(FMStrings.formImperativ, iterator.next());
				// 31
				typeMap.put(FMStrings.xSonderPreschent, iterator.next());
				// 32
				typeMap.put(FMStrings.xSonderImperfect, iterator.next());
				// 33
				typeMap.put(FMStrings.xSonderConjunctiv, iterator.next());
				// 34
				typeMap.put(FMStrings.xSonderCundizional, iterator.next());
				// 35
				typeMap.put(FMStrings.xU_A, iterator.next());
				// 36
				typeMap.put(FMStrings.xSonderPart_Perfect, iterator.next());
				// 37
				typeMap.put(FMStrings.xSonderImperativ, iterator.next());
				// 38
				typeMap.put(FMStrings.xSonderGerundium, iterator.next());
				// 39
				typeMap.put(FMStrings.varPerson, iterator.next());
				// 40
				typeMap.put(FMStrings.varPerson_Unpers, iterator.next());
				// 41
				typeMap.put(FMStrings.varPart_Perfect, iterator.next());
				// 42
				typeMap.put(FMStrings.varPart_Perfect_Unpers, iterator.next());
				// 43
				typeMap.put(FMStrings.varReturn, iterator.next());
				// 44
				typeMap.put(FMStrings.varTab, iterator.next());
				// 45
				typeMap.put(FMStrings.varPerson_Reflexiv, iterator.next());
				// 46
				typeMap.put(FMStrings.varPerson_Reflexiv_Kurz, iterator.next());
				// 47
				typeMap.put(FMStrings.varImperativ_Reflexiv, iterator.next());
				// 48
				typeMap.put(FMStrings.varImperativ_Reflexiv_Kurz,
						iterator.next());
				// 49
				typeMap.put(FMStrings.varPart_Perfect_Reflexiv, iterator.next());
				// 50
				typeMap.put(FMStrings.varFutur, iterator.next());
				// 51
				typeMap.put(FMStrings.varFutur_Reflexiv, iterator.next());
				// 52
				typeMap.put(FMStrings.varFutur_Reflexiv_Kurz, iterator.next());
				// 53
				typeMap.put(FMStrings.varImperativ, iterator.next());

				typeMap.keySet().removeAll(filter);

				Map<String, String> largeMap = new LinkedHashMap<String, String>();
				Set<Entry<String, String>> entries = typeMap.entrySet();
				for (Entry<String, String> entry : entries) {
					String[] parts = entry.getValue()
							.split("\u000B|\u001D", -1);
					if (parts.length == 1) {
						largeMap.put(entry.getKey(), parts[0]);
					} else {
						for (int i = 0; i < parts.length; i++) {
							largeMap.put(entry.getKey() + "_" + i, parts[i]);
						}
					}
				}

				bigMap.put(typeMap.get(FMStrings.verb), largeMap);
			}

		}

		br.close();

		return bigMap;

	}

	public void createSQLTable() {

		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = getMySQLConnection();
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Creating table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS conjugation "
					+ "(id int NOT NULL AUTO_INCREMENT,"

					// Basic Info

					+ "recId  int, "
					+ "infinitiv varchar(100),"
					+ "root varchar(100),"
					+ "type varchar(50),"
					+ "subtype varchar(50),"
					+ "irregular varchar(50),"

					// Preschent
					+ "preschentsing1 varchar(100), "
					+ "preschentsing2 varchar(100), "
					+ "preschentsing3 varchar(100), "
					+ "preschentplural1 varchar(100), "
					+ "preschentplural2 varchar(100), "
					+ "preschentplural3 varchar(100), "

					// Imperfect
					+ "imperfectsing1 varchar(100), "
					+ "imperfectsing2 varchar(100), "
					+ "imperfectsing3 varchar(100), "
					+ "imperfectplural1 varchar(100), "
					+ "imperfectplural2 varchar(100), "
					+ "imperfectplural3 varchar(100), "

					// Conjunctiv
					+ "conjunctivsing1 varchar(100), "
					+ "conjunctivsing2 varchar(100), "
					+ "conjunctivsing3 varchar(100), "
					+ "conjunctivplural1 varchar(100), "
					+ "conjunctivplural2 varchar(100), "
					+ "conjunctivplural3 varchar(100), "

					// Cundizional
					+ "cundizionalsing1 varchar(100), "
					+ "cundizionalsing2 varchar(100), "
					+ "cundizionalsing3 varchar(100), "
					+ "cundizionalplural1 varchar(100), "
					+ "cundizionalplural2 varchar(100), "
					+ "cundizionalplural3 varchar(100), "

					// Particip_Perfect

					+ "participperfectms varchar(100), "
					+ "participperfectfs varchar(100), "
					+ "participperfectmp varchar(100), "
					+ "participperfectfp varchar(100), "

					// Imperativ

					+ "imperativ1 varchar(100),"
					+ "imperativ2 varchar(100),"

					// Gerundium
					+ "gerundium varchar(100),"

					// Futur

					+ "futursing1 varchar(100)," + "futursing2 varchar(100),"
					+ "futursing3 varchar(100)," + "futurplural1 varchar(100),"
					+ "futurplural2 varchar(100),"
					+ "futurplural3 varchar(100),"

					+ "PRIMARY KEY ( id ))";

			stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
	}

	public void createSQLTable_RP() {

		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");

			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = getMySQLConnection();
			System.out.println("Connected database successfully...");

			// STEP 4: Execute a query
			System.out.println("Creating table in given database...");
			stmt = conn.createStatement();

			String sql = "CREATE TABLE IF NOT EXISTS conjugation_rp "
					+ "(id int NOT NULL AUTO_INCREMENT,"

					// Basic Info

					+ "recId  int, "
					+ "infinitiv varchar(100),"
					+ "root varchar(100),"
					+ "type varchar(50),"
					+ "subtype varchar(50),"
					+ "irregular varchar(50),"

					// Preschent
					+ "preschentsing1 varchar(100), "
					+ "preschentsing2 varchar(100), "
					+ "preschentsing3 varchar(100), "
					+ "preschentplural1 varchar(100), "
					+ "preschentplural2 varchar(100), "
					+ "preschentplural3 varchar(100), "

					// Imperfect
					+ "imperfectsing1 varchar(100), "
					+ "imperfectsing2 varchar(100), "
					+ "imperfectsing3 varchar(100), "
					+ "imperfectplural1 varchar(100), "
					+ "imperfectplural2 varchar(100), "
					+ "imperfectplural3 varchar(100), "

					// Conjunctiv
					+ "conjunctivsing1 varchar(100), "
					+ "conjunctivsing2 varchar(100), "
					+ "conjunctivsing3 varchar(100), "
					+ "conjunctivplural1 varchar(100), "
					+ "conjunctivplural2 varchar(100), "
					+ "conjunctivplural3 varchar(100), "

					// Cundizional
					+ "cundizionalsing1 varchar(100), "
					+ "cundizionalsing2 varchar(100), "
					+ "cundizionalsing3 varchar(100), "
					+ "cundizionalplural1 varchar(100), "
					+ "cundizionalplural2 varchar(100), "
					+ "cundizionalplural3 varchar(100), "

					// Particip_Perfect

					+ "participperfectms varchar(100), "
					+ "participperfectfs varchar(100), "
					+ "participperfectmp varchar(100), "
					+ "participperfectfp varchar(100), "

					// Imperativ

					+ "imperativ1 varchar(100),"
					+ "imperativ2 varchar(100),"

					// Gerundium
					+ "gerundium varchar(100),"

					// Futur

					+ "futursing1 varchar(100)," + "futursing2 varchar(100),"
					+ "futursing3 varchar(100)," + "futurplural1 varchar(100),"
					+ "futurplural2 varchar(100),"
					+ "futurplural3 varchar(100),"

					+ "PRIMARY KEY ( id ))";

			stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		System.out.println("Goodbye!");
	}

	public void mapToSQL(Map<String, Map<String, String>> bigMap)
			throws Exception {

		Connection conn = getMySQLConnection();
		PreparedStatement pst = null;

		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {

			Map<String, String> map = entry.getValue();

			String befehl = "INSERT INTO conjugation ("
					+ "recId, "
					+ "infinitiv,"
					+ "root,"
					+ "type,"
					+ "subtype,"
					+ "irregular,"
					+

					// Preschent
					"preschentsing1, preschentsing2, preschentsing3 ,"
					+ "preschentplural1,preschentplural2, preschentplural3, "
					+
					// Imperfect
					"imperfectsing1,imperfectsing2, imperfectsing3,"
					+ "imperfectplural1, imperfectplural2, imperfectplural3,"
					+
					// Conjunctiv
					" conjunctivsing1, conjunctivsing2, conjunctivsing3,"
					+ "conjunctivplural1, conjunctivplural2, conjunctivplural3, "
					+
					// Cundizional
					" cundizionalsing1, cundizionalsing2, cundizionalsing3,"
					+ "cundizionalplural1, cundizionalplural2, cundizionalplural3, "

					// Particip_Perfect
					+ " participperfectms, participperfectfs, participperfectmp,"
					+ " participperfectfp,"

					// Imperativ

					+ "imperativ1," + "imperativ2,"

					// Gerundium

					+ "gerundium,"

					// Futur

					+ "futursing1," + "futursing2," + "futursing3,"
					+ "futurplural1," + "futurplural2," + "futurplural3"

					+ ") "

					+ "VALUES "// 43 TOTAL
					// Preschent
					+ "(" + "?" // map.get("recId") 1
					+ "," + "?" // map.get("verb") 2
					+ "," + "?" // map.get("stamm") 3
					+ "," + "?" // map.get("art") 4
					+ "," + "?" // map.get("u_Art") 5
					+ "," + "?" // map.get("unregelm") 6

					+ "," + "?" // map.get("preschent_0") 7
					+ "," + "?" // map.get("preschent_1") 8
					+ "," + "?" // map.get("preschent_2") 9
					+ "," + "?" // map.get("preschent_3") 10
					+ "," + "?" // map.get("preschent_4") 11
					+ "," + "?" // map.get("preschent_5") 12
					+ "," + "?" // map.get("imperfect_0") 13
					+ "," + "?" // map.get("imperfect_1") 14
					+ "," + "?" // map.get("imperfect_2") 15
					+ "," + "?" // map.get("imperfect_3") 16
					+ "," + "?" // map.get("imperfect_4") 17
					+ "," + "?" // map.get("imperfect_5") 18
					+ "," + "?" // map.get("conjunctiv_0") 19
					+ "," + "?" // map.get("conjunctiv_1") 20
					+ "," + "?" // map.get("conjunctiv_2") 21
					+ "," + "?" // map.get("conjunctiv_3") 22
					+ "," + "?" // map.get("conjunctiv_4") 23
					+ "," + "?" // map.get("conjunctiv_5") 24
					+ "," + "?" // map.get("cundizional_0") 25
					+ "," + "?" // map.get("cundizional_1") 26
					+ "," + "?" // map.get("cundizional_2") 27
					+ "," + "?" // map.get("cundizional_3") 28
					+ "," + "?" // map.get("cundizional_4") 29
					+ "," + "?" // map.get("cundizional_5") 30
					+ "," + "?" // map.get("part_Perfect_0") 31
					+ "," + "?" // map.get("part_Perfect_1") 32
					+ "," + "?" // map.get("part_Perfect_2") 33
					+ "," + "?" // map.get("part_Perfect_3") 34
					+ "," + "?" // map.get("imperativ_0") 35
					+ "," + "?" // map.get("imperativ_1") 36
					+ "," + "?" // map.get("gerundium") 37
					+ "," + "?" // map.get("futur_0") 38
					+ "," + "?"// map.get("futur_1") 39
					+ "," + "?" // map.get("futur_2")40
					+ "," + "?" // map.get("futur_3") 41
					+ "," + "?" // map.get("futur_4") 42
					+ "," + "?"// map.get("futur_5") 43

					+ ")";
			// System.out.println(befehl);

			pst = conn.prepareStatement(befehl);

			pst.setString(1, map.get("recId"));
			pst.setString(2, avoidNullPointer(map.get("verb")));
			pst.setString(3, map.get("stamm"));
			pst.setString(4, map.get("art"));
			pst.setString(5, map.get("u_Art"));
			pst.setString(6, map.get("unregelm"));

			pst.setString(7, map.get("preschent_0"));
			pst.setString(8, map.get("preschent_1"));
			pst.setString(9, map.get("preschent_2"));
			pst.setString(10, map.get("preschent_3"));
			pst.setString(11, map.get("preschent_4"));
			pst.setString(12, map.get("preschent_5"));
			pst.setString(13, map.get("imperfect_0"));
			pst.setString(14, map.get("imperfect_1"));
			pst.setString(15, map.get("imperfect_2"));
			pst.setString(16, map.get("imperfect_3"));
			pst.setString(17, map.get("imperfect_4"));
			pst.setString(18, map.get("imperfect_5"));
			pst.setString(19, map.get("conjunctiv_0"));
			pst.setString(20, map.get("conjunctiv_1"));
			pst.setString(21, map.get("conjunctiv_2"));
			pst.setString(22, map.get("conjunctiv_3"));
			pst.setString(23, map.get("conjunctiv_4"));
			pst.setString(24, map.get("conjunctiv_5"));
			pst.setString(25, map.get("cundizional_0"));
			pst.setString(26, map.get("cundizional_1"));
			pst.setString(27, map.get("cundizional_2"));
			pst.setString(28, map.get("cundizional_3"));
			pst.setString(29, map.get("cundizional_4"));
			pst.setString(30, map.get("cundizional_5"));
			pst.setString(31, map.get("part_Perfect_0"));
			pst.setString(32, map.get("part_Perfect_1"));
			pst.setString(33, map.get("part_Perfect_2"));
			pst.setString(34, map.get("part_Perfect_3"));

			pst.setString(35, map.get("imperativ_0"));
			pst.setString(36, map.get("imperativ_1"));

			pst.setString(37, map.get("gerundium"));

			pst.setString(38, map.get("futur_0"));
			pst.setString(39, map.get("futur_1"));
			pst.setString(40, map.get("futur_2"));
			pst.setString(41, map.get("futur_3"));
			pst.setString(42, map.get("futur_4"));
			pst.setString(43, map.get("futur_5"));

			pst.executeUpdate();
			System.out.println("Record inserted");

		}

		pst.close();
		closeMySQLConnection(conn);

	}

	public void mapToSQL_RP(Map<String, Map<String, String>> bigMap)
			throws Exception {

		Connection conn = getMySQLConnection();
		PreparedStatement pst = null;

		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {

			Map<String, String> map = entry.getValue();

			String befehl = "INSERT INTO conjugation ("
					+ "recId, "
					+ "infinitiv,"
					+ "root,"
					+ "type,"
					+ "subtype,"
					+ "irregular,"
					+

					// Preschent
					"preschentsing1, preschentsing2, preschentsing3 ,"
					+ "preschentplural1,preschentplural2, preschentplural3, "
					+
					// Imperfect
					"imperfectsing1,imperfectsing2, imperfectsing3,"
					+ "imperfectplural1, imperfectplural2, imperfectplural3,"
					+
					// Conjunctiv
					" conjunctivsing1, conjunctivsing2, conjunctivsing3,"
					+ "conjunctivplural1, conjunctivplural2, conjunctivplural3, "
					+
					// Cundizional
					" cundizionalsing1, cundizionalsing2, cundizionalsing3,"
					+ "cundizionalplural1, cundizionalplural2, cundizionalplural3, "

					// Particip_Perfect
					+ " participperfectms, participperfectfs, participperfectmp,"
					+ " participperfectfp,"

					// Imperativ

					+ "imperativ1," + "imperativ2,"

					// Gerundium

					+ "gerundium,"

					// Futur

					+ "futursing1," + "futursing2," + "futursing3,"
					+ "futurplural1," + "futurplural2," + "futurplural3"

					+ ") "

					+ "VALUES "// 43 TOTAL
					// Preschent
					+ "(" + "?" // map.get("recId") 1
					+ "," + "?" // map.get("verb") 2
					+ "," + "?" // map.get("stamm") 3
					+ "," + "?" // map.get("art") 4
					+ "," + "?" // map.get("u_Art") 5
					+ "," + "?" // map.get("unregelm") 6

					+ "," + "?" // map.get("preschent_0") 7
					+ "," + "?" // map.get("preschent_1") 8
					+ "," + "?" // map.get("preschent_2") 9
					+ "," + "?" // map.get("preschent_3") 10
					+ "," + "?" // map.get("preschent_4") 11
					+ "," + "?" // map.get("preschent_5") 12
					+ "," + "?" // map.get("imperfect_0") 13
					+ "," + "?" // map.get("imperfect_1") 14
					+ "," + "?" // map.get("imperfect_2") 15
					+ "," + "?" // map.get("imperfect_3") 16
					+ "," + "?" // map.get("imperfect_4") 17
					+ "," + "?" // map.get("imperfect_5") 18
					+ "," + "?" // map.get("conjunctiv_0") 19
					+ "," + "?" // map.get("conjunctiv_1") 20
					+ "," + "?" // map.get("conjunctiv_2") 21
					+ "," + "?" // map.get("conjunctiv_3") 22
					+ "," + "?" // map.get("conjunctiv_4") 23
					+ "," + "?" // map.get("conjunctiv_5") 24
					+ "," + "?" // map.get("cundizional_0") 25
					+ "," + "?" // map.get("cundizional_1") 26
					+ "," + "?" // map.get("cundizional_2") 27
					+ "," + "?" // map.get("cundizional_3") 28
					+ "," + "?" // map.get("cundizional_4") 29
					+ "," + "?" // map.get("cundizional_5") 30
					+ "," + "?" // map.get("part_Perfect_0") 31
					+ "," + "?" // map.get("part_Perfect_1") 32
					+ "," + "?" // map.get("part_Perfect_2") 33
					+ "," + "?" // map.get("part_Perfect_3") 34
					+ "," + "?" // map.get("imperativ_0") 35
					+ "," + "?" // map.get("imperativ_1") 36
					+ "," + "?" // map.get("gerundium") 37
					+ "," + "?" // map.get("futur_0") 38
					+ "," + "?"// map.get("futur_1") 39
					+ "," + "?" // map.get("futur_2")40
					+ "," + "?" // map.get("futur_3") 41
					+ "," + "?" // map.get("futur_4") 42
					+ "," + "?"// map.get("futur_5") 43

					+ ")";
			// System.out.println(befehl);

			String verb = avoidNullPointer(map.get("verb"));

			verb = avoidEmptyString(verb);

			if (verb.equals("NOT DEFINED")) {
				continue;
			}

			checkReflexiveness(verb);
			System.out.println(getStamm());
			if (isVocal(getStamm())) {
				setVocal(true);

			} else {
				setVocal(false);
			}

			pst = conn.prepareStatement(befehl);

			pst.setString(1, map.get("recId"));
			pst.setString(2, verb);
			pst.setString(3, map.get("stamm"));
			pst.setString(4, map.get("art"));
			pst.setString(5, map.get("u_Art"));
			pst.setString(6, map.get("unregelm"));

			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1ps, Pronouns.pron_r_1ps, 7,
					"preschent_0");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2ps, Pronouns.pron_r_2ps, 8,
					"preschent_1");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3ps, Pronouns.pron_r_3ps, 9,
					"preschent_2");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1pp, Pronouns.pron_r_1pp, 10,
					"preschent_3");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2pp, Pronouns.pron_r_2pp, 11,
					"preschent_4");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3pp, Pronouns.pron_r_3pp, 12,
					"preschent_5");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1ps, Pronouns.pron_r_1ps, 13,
					"imperfect_0");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2ps, Pronouns.pron_r_2ps, 14,
					"imperfect_1");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3ps, Pronouns.pron_r_3ps, 15,
					"imperfect_2");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1pp, Pronouns.pron_r_1pp, 16,
					"imperfect_3");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2pp, Pronouns.pron_r_2pp, 17,
					"imperfect_4");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3pp, Pronouns.pron_r_3pp, 18,
					"imperfect_5");

			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1ps, Pronouns.pron_r_1ps, 19,
					"conjunctiv_0");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2ps, Pronouns.pron_r_2ps, 20,
					"conjunctiv_1");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3ps, Pronouns.pron_r_3ps, 21,
					"conjunctiv_2");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1pp, Pronouns.pron_r_1pp, 22,
					"conjunctiv_3");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2pp, Pronouns.pron_r_2pp, 23,
					"conjunctiv_4");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3pp, Pronouns.pron_r_3pp, 24,
					"conjunctiv_5");

			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1ps, Pronouns.pron_r_1ps, 25,
					"cundizional_0");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2ps, Pronouns.pron_r_2ps, 26,
					"cundizional_1");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3ps, Pronouns.pron_r_3ps, 27,
					"cundizional_2");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_1pp, Pronouns.pron_r_1pp, 28,
					"cundizional_3");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_2pp, Pronouns.pron_r_2pp, 29,
					"cundizional_4");
			addReflexivePronouns(isReflexive(), isVocal(), pst, map,
					Pronouns.pron_r_v_3pp, Pronouns.pron_r_3pp, 30,
					"cundizional_5");

			pst.setString(31, map.get("part_Perfect_0"));
			pst.setString(32, map.get("part_Perfect_1"));
			pst.setString(33, map.get("part_Perfect_2"));
			pst.setString(34, map.get("part_Perfect_3"));

			pst.setString(35, map.get("imperativ_0"));
			pst.setString(36, map.get("imperativ_1"));

			pst.setString(37, map.get("gerundium"));

			pst.setString(38, map.get("futur_0"));
			pst.setString(39, map.get("futur_1"));
			pst.setString(40, map.get("futur_2"));
			pst.setString(41, map.get("futur_3"));
			pst.setString(42, map.get("futur_4"));
			pst.setString(43, map.get("futur_5"));

			pst.executeUpdate();
			System.out.println("Record inserted");

		}

		pst.close();
		closeMySQLConnection(conn);

	}

	public PreparedStatement addReflexivePronouns(boolean reflexive,
			boolean vocal, PreparedStatement pst, Map<String, String> map,
			String refPronV, String refPronK, int psn, String cForm)
			throws SQLException {

		if (reflexive == true) {
			if (vocal == true) {
				pst.setString(psn, refPronV + map.get(cForm));
			} else if (vocal == false) {
				pst.setString(psn, refPronK + map.get(cForm));
			}
		} else {
			pst.setString(psn, map.get(cForm));

		}

		return pst;
	}

	public void checkReflexiveness(String verb) {

		if (verb.startsWith("sa ")) {
			setReflexive(true);
			setStamm(verb.length() > 2 ? verb.substring(3) : verb);

		} else if (verb.startsWith("s'")) {
			setReflexive(true);
			setStamm(verb.length() > 2 ? verb.substring(2) : verb);

		} else {
			setReflexive(false);
			// we are not setting the stamm, we just want to know if the verb is
			// starts with a vocal
			setStamm(verb);
		}

	}

	public Set<String> getInfinitives(String table) throws Exception {
		Set<String> infinitives = new TreeSet<>();

		Connection connection = getMySQLConnection();
		Statement stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery("select infinitiv from " + table);

		while (rs.next()) {

			infinitives.add(rs.getString("infinitiv"));

		}

		return infinitives;
	}

	public List<String> getAllInfinitives(String table) throws Exception {
		List<String> infinitives = new LinkedList<>();

		Connection connection = getMySQLConnection();
		Statement stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery("select infinitiv from " + table);

		while (rs.next()) {

			infinitives.add(rs.getString("infinitiv"));

		}

		return infinitives;
	}

	public List<ConjugationStructure> deliverConjugationsFromMySQL()
			throws Exception {

		Connection connection = getMySQLConnection();
		Statement stmt = connection.createStatement();

		ResultSet rs = stmt.executeQuery("select * from conjugation");

		List<ConjugationStructure> list = new LinkedList<ConjugationStructure>();

		while (rs.next()) {

			ConjugationStructure c = new ConjugationStructure();

			c.setInfinitiv(rs.getString(ConjugationStructure.infinitiv));

			// preschent
			c.setPreschentsing1(rs
					.getString(ConjugationStructure.preschentsing1));
			c.setPreschentsing2(rs
					.getString(ConjugationStructure.preschentsing2));
			c.setPreschentsing3(rs
					.getString(ConjugationStructure.preschentsing3));

			c.setPreschentplural1(rs
					.getString(ConjugationStructure.preschentplural1));
			c.setPreschentplural2(rs
					.getString(ConjugationStructure.preschentplural2));
			c.setPreschentplural3(rs
					.getString(ConjugationStructure.preschentplural3));

			// imperfect
			c.setImperfectsing1(rs
					.getString(ConjugationStructure.imperfectsing1));
			c.setImperfectsing2(rs
					.getString(ConjugationStructure.imperfectsing2));
			c.setImperfectsing3(rs
					.getString(ConjugationStructure.imperfectsing3));

			c.setImperfectplural1(rs
					.getString(ConjugationStructure.imperfectplural1));
			c.setImperfectplural2(rs
					.getString(ConjugationStructure.imperfectplural2));
			c.setImperfectplural3(rs
					.getString(ConjugationStructure.imperfectplural3));

			// conjunctiv

			c.setConjunctivsing1(rs
					.getString(ConjugationStructure.conjunctivsing1));
			c.setConjunctivsing2(rs
					.getString(ConjugationStructure.conjunctivsing2));
			c.setConjunctivsing3(rs
					.getString(ConjugationStructure.conjunctivsing3));

			c.setConjunctivplural1(rs
					.getString(ConjugationStructure.conjunctivplural1));
			c.setConjunctivplural2(rs
					.getString(ConjugationStructure.conjunctivplural2));
			c.setConjunctivplural3(rs
					.getString(ConjugationStructure.conjunctivplural3));

			// cundizional

			c.setCundizionalsing1(rs
					.getString(ConjugationStructure.cundizionalsing1));
			c.setCundizionalsing2(rs
					.getString(ConjugationStructure.cundizionalsing2));
			c.setCundizionalsing3(rs
					.getString(ConjugationStructure.cundizionalsing3));

			c.setCundizionalplural1(rs
					.getString(ConjugationStructure.cundizionalplural1));
			c.setCundizionalplural2(rs
					.getString(ConjugationStructure.cundizionalplural2));
			c.setCundizionalplural3(rs
					.getString(ConjugationStructure.cundizionalplural3));

			list.add(c);

		}
		closeMySQLConnection(connection);

		return list;

	}

	public List<ConjugationStructure> deliverCongugationsfromFM(
			Map<String, Map<String, String>> bigMap) {
		List<ConjugationStructure> list = new LinkedList<ConjugationStructure>();

		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {
			ConjugationStructure cs = new ConjugationStructure();
			ConjugationGenerator cg = new ConjugationGenerator();
			Map<String, String> map = entry.getValue();

			String root = cg.getRoot(map.get("verb"));

			cs.setRoot(root);
			cs.setInfinitiv(cg.getInfinitiv());
			cs.setEnding(cg.getEnding());
			cs.setReflexive(cg.getIsReflexive());

			cs.setPreschentsing1(map.get("preschent_0"));
			cs.setPreschentsing2(map.get("preschent_1"));
			cs.setPreschentsing3(map.get("preschent_2"));
			cs.setPreschentplural1(map.get("preschent_3"));
			cs.setPreschentplural2(map.get("preschent_4"));
			cs.setPreschentplural3(map.get("preschent_5"));

			cs.setImperfectsing1(map.get("imperfect_0"));
			cs.setImperfectsing2(map.get("imperfect_1"));
			cs.setImperfectsing3(map.get("imperfect_2"));
			cs.setImperfectplural1(map.get("imperfect_3"));
			cs.setImperfectplural2(map.get("imperfect_4"));
			cs.setImperfectplural3(map.get("imperfect_5"));

			cs.setConjunctivsing1(map.get("conjunctiv_0"));
			cs.setConjunctivsing2(map.get("conjunctiv_1"));
			cs.setConjunctivsing3(map.get("conjunctiv_2"));
			cs.setConjunctivplural1(map.get("conjunctiv_3"));
			cs.setConjunctivplural2(map.get("conjunctiv_4"));
			cs.setConjunctivplural3(map.get("conjunctiv_5"));

			cs.setCundizionalsing1(map.get("cundizional_0"));
			cs.setCundizionalsing2(map.get("cundizional_1"));
			cs.setCundizionalsing3(map.get("cundizional_2"));
			cs.setCundizionalplural1(map.get("cundizional_3"));
			cs.setCundizionalplural2(map.get("cundizional_4"));
			cs.setCundizionalplural3(map.get("cundizional_5"));

			cs.setParticipperfectms(map.get("part_Perfect_0"));
			cs.setParticipperfectfs(map.get("part_Perfect_1"));
			cs.setParticipperfectmp(map.get("part_Perfect_2"));
			cs.setParticipperfectfp(map.get("part_Perfect_3"));

			cs.setImperativ1(map.get("imperativ_0"));
			cs.setImperativ2(map.get("imperativ_1"));

			cs.setGerundium(map.get("gerundium"));

			cs.setFutursing1(map.get("futur_0"));
			cs.setFutursing2(map.get("futur_1"));
			cs.setFutursing3(map.get("futur_2"));
			cs.setFuturplural1(map.get("futur_3"));
			cs.setFuturplural2(map.get("futur_4"));
			cs.setFuturplural3(map.get("futur_5"));
		}

		return list;
	}

	public File getPPInfo(Map<String, Map<String, String>> bigMap,
			String fileName) throws Exception, FileNotFoundException {

		File file = new File("data/genfiles/" + fileName);

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		String part_Perfect_0 = "part_Perfect_0";
		String part_Perfect_1 = "part_Perfect_1";
		String part_Perfect_2 = "part_Perfect_2";
		String part_Perfect_3 = "part_Perfect_3";
		String part_PerfectVorbereitung_0 = "part_PerfectVorbereitung_0";
		String part_PerfectVorbereitung_1 = "part_PerfectVorbereitung_1";
		String part_PerfectVorbereitung_2 = "part_PerfectVorbereitung_2";
		String part_PerfectVorbereitung_3 = "part_PerfectVorbereitung_3";

		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {

			Map<String, String> map = entry.getValue();

			TreeMap<String, String> oMap = new TreeMap<>();
			oMap.putAll(map);

			boolean reflexive = false;

			String verb = avoidNullPointer(map.get("verb"));

			if (verb.startsWith("s'") || verb.startsWith("sa ")) {
				reflexive = true;
			}

			if (reflexive == true) {

				writer.append("++++++++++++++" + "\n");
				writer.append("verb: " + oMap.get("verb") + "\n");
				writer.append("--------------" + "\n");
				writer.append("ms: " + oMap.get(part_Perfect_0) + "\n");
				writer.append("fs: " + oMap.get(part_Perfect_1) + "\n");
				writer.append("mp: " + oMap.get(part_Perfect_2) + "\n");
				writer.append("fp: " + oMap.get(part_Perfect_3) + "\n");
				writer.append("--------------" + "\n");
				writer.append("ms: " + oMap.get(part_PerfectVorbereitung_0)
						+ "\n");
				writer.append("fs: " + oMap.get(part_PerfectVorbereitung_1)
						+ "\n");
				writer.append("mp: " + oMap.get(part_PerfectVorbereitung_2)
						+ "\n");
				writer.append("fp: " + oMap.get(part_PerfectVorbereitung_3)
						+ "\n");
				writer.append("++++++++++++++" + "\n");
			}

		}

		writer.flush();
		writer.close();

		return file;

	}

	public int countInfinitives(Map<String, Map<String, String>> bigMap)
			throws Exception {
		int j = 0;
		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {
			Map<String, String> map = entry.getValue();
			String infinitiv = map.get("verb");
			if (infinitiv != null) {
				j++;
			}
		}
		return j;
	}

	public File printAllInfinitives(Map<String, Map<String, String>> bigMap,
			String fileName) throws IOException {
		File file = new File("data/genfiles/" + fileName + ".txt");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, true);
		ArrayList<String> infinitives = new ArrayList<String>();
		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {
			Map<String, String> map = entry.getValue();
			String infinitiv = map.get("verb");
			if (infinitiv != null) {
				infinitives.add(infinitiv);
			}
		}
		Collections.sort(infinitives);
		for (String infinitiv : infinitives) {
			writer.append(infinitiv);
			writer.append("\n");
		}

		writer.flush();
		writer.close();
		return file;
	}

	public int countReflexives(Map<String, Map<String, String>> bigMap) {
		int j = 0;
		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {
			Map<String, String> map = entry.getValue();
			String infinitiv = map.get("verb");
			if (infinitiv != null) {
				// Check if it's reflexive
				if (infinitiv.startsWith("sa ") || infinitiv.startsWith("s'")) {
					j++;
				}

			}
		}
		return j;

	}

	public File printReflexives(Map<String, Map<String, String>> bigMap,
			String fileName) throws IOException {

		File file = new File("data/genfiles/" + fileName + ".txt");
		file.createNewFile();
		FileWriter writer = new FileWriter(file, true);

		ArrayList<String> reflexives = new ArrayList<String>();

		for (Map.Entry<String, Map<String, String>> entry : bigMap.entrySet()) {

			Map<String, String> map = entry.getValue();

			String infinitiv = map.get("verb");

			if (infinitiv != null) {
				// Check if it's reflexive
				if (infinitiv.startsWith("sa ") || infinitiv.startsWith("s'")) {
					reflexives.add(infinitiv);
				}

			}

		}

		Collections.sort(reflexives);

		for (String reflexive : reflexives) {

			writer.append(reflexive);
			writer.append("\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public File printSet(Set<String> set, String filename) throws IOException {

		File file = new File("data/genfiles/" + filename);

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (String string : set) {
			writer.append(string + "\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public File printList(List<String> list, String filename)
			throws IOException {

		File file = new File("data/genfiles/" + filename);

		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "UTF8"));

		for (String string : list) {
			writer.append(string + "\n");
		}

		writer.flush();
		writer.close();

		return file;
	}

	public String avoidNullPointer(String str) {

		String content = "NOT DEFINED";
		if (str != null) {
			content = str;
		}

		return content;
	}

	public String avoidEmptyString(String str) {

		String content = "NOT DEFINED";

		if (str.equals("\t") || str.equals(" ") || str.equals("")) {
			str = content;
		}

		return str;
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

	private static Connection getMySQLConnection() throws Exception {

		Class.forName("com.mysql.jdbc.Driver");

		return DriverManager.getConnection(path_to_verbs_rg, "tester", "test");
	}

	private static void closeMySQLConnection(Connection connection)
			throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	public boolean isReflexive() {
		return reflexive;
	}

	public void setReflexive(boolean reflexive) {
		this.reflexive = reflexive;
	}

	public boolean isVocal() {
		return vocal;
	}

	public void setVocal(boolean vocal) {
		this.vocal = vocal;
	}

	public String getStamm() {
		return stamm;
	}

	public void setStamm(String stamm) {
		this.stamm = stamm;
	}

}

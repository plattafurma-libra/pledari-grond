package de.uni_koeln.spinfo.maalr.lucene.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.BrokenIndexException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.InvalidQueryException;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneConfiguration;

public class TestDefaultQueryBuilder {
	
private Dictionary index;
	
	private LuceneConfiguration environment;

	private File indexDir;

	@Before
	public void beforeTest() throws Exception {
		File file = File.createTempFile("maalr", "test");
		indexDir = new File(file.getParentFile(), "maalr_test" + UUID.randomUUID().toString() + "_idx");
		Assert.assertFalse(indexDir.exists());
		indexDir.mkdir();
		file.deleteOnExit();
		environment = new LuceneConfiguration();
		environment.setBaseDirectory(indexDir.getAbsolutePath());
		index = new Dictionary();
		index.setEnvironment(environment);
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("dictionary.tsv");
		BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
		String line = null;
		List<LexEntry> entries = new ArrayList<LexEntry>();
		int counter = 0;
		while((line = br.readLine()) != null) {
			String[] parts = line.split("\t");
			LemmaVersion lv = new LemmaVersion();
			lv.setValue("German", parts[0]);
			lv.setValue("English", parts[1]);
			lv.setVerification(Verification.ACCEPTED);
			lv.setStatus(Status.NEW_ENTRY);
			LexEntry entry = new LexEntry(lv);
			entry.setId(counter+"");
			counter++;
			entries.add(entry);
		}
		index.addToIndex(entries.iterator());
	}
	
	@After
	public void afterTest() throws Exception {
		deleteRecursive(indexDir);
	}
	
	@Test
	public void testPrefixQuery() throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = index.query(getMaalrQuery("german", "prefix", "haus"));
		Set<String> found = getStrings("German", result);
		String[] expected = new String[] {"hausen", "Haushalt", "Haus", "Haus/Häuser"};
		validateResult(found, expected);
	}

	private void validateResult(Set<String> found, String[] expected) {
		List<String> expectedList = Arrays.asList(expected);
		Assert.assertTrue("Was expecting to find " + expectedList + " in " + found, found.containsAll(expectedList));
		found.removeAll(expectedList);
		Assert.assertTrue("Found more items than expecting: " + found, found.isEmpty());
	}
	
	@Test
	public void testInfixQuery() throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = index.query(getMaalrQuery("german", "infix", "haus"));
		Set<String> found = getStrings("German", result);
		String[] expected = new String[] {"nach Hause", "Das Haus brennt", "Flohausstellung", "hausen", "Haushalt", "zu Hause", "Wohnhaus", "Haus", "Haus/Häuser", "Kranken(haus)"};
		validateResult(found, expected);
	}
	
	@Test
	public void testSuffix() throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = index.query(getMaalrQuery("german", "suffix", "haus"));
		Set<String> found = getStrings("German", result);
		String[] expected = new String[] {"Wohnhaus", "Haus", "Kranken(haus)"};
		validateResult(found, expected);
	}
	
	@Test
	public void testExact() throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = index.query(getMaalrQuery("german", "exact", "haus"));
		Set<String> found = getStrings("German", result);
		String[] expected = new String[] {"Haus"};
		validateResult(found, expected);
	}
	
	@Test
	public void testDefault() throws InvalidQueryException, NoIndexAvailableException, BrokenIndexException, IOException, InvalidTokenOffsetsException {
		QueryResult result = index.query(getMaalrQuery("german", "default", "haus"));
		Set<String> found = getStrings("German", result);
		String[] expected = new String[] {"Das Haus brennt", "hausen", "Haushalt", "Haus", "Haus/Häuser", "Kranken(haus)"};
		validateResult(found, expected);
	}
	
	@Test
	public void testAlphabeticalIndex() throws NoIndexAvailableException, BrokenIndexException, InvalidQueryException {
		QueryResult result = index.getAllStartingWith("german", "h", 0);
		Set<String> found = getStrings("German", result);
		String[] expected = new String[] {"heimwärts", "hausen", "Homer", "Haushalt", "Haus/Häuser", "Home-Run"};
		validateResult(found, expected);
	}
	
	@Test
	public void testExactQuery() throws NoIndexAvailableException, BrokenIndexException, InvalidQueryException {
		QueryResult result = index.queryExact("haus", true);
		Set<String> found = getStrings("German", result);
		String[] expected = new String[] {"Haus"};
		validateResult(found, expected);
	}
	
	
	private Set<String> getStrings(String key, QueryResult result) {
		HashSet<String> toReturn = new HashSet<String>();
		List<LemmaVersion> entries = result.getEntries();
		for (LemmaVersion lv : entries) {
			toReturn.add(lv.getEntryValue(key));
		}
		return toReturn;
	}

	private MaalrQuery getMaalrQuery(String language, String method,
			String value) {
		MaalrQuery query = new MaalrQuery();
		query.setQueryValue("language", language);
		query.setQueryValue("method", method);
		query.setQueryValue("searchPhrase", value);
		return query;
	}

	private void deleteRecursive(File fileOrDir) {
		if(fileOrDir.isDirectory()) {
			File[] files = fileOrDir.listFiles();
			for (File file : files) {
				deleteRecursive(file);
			}
		} else {
			fileOrDir.delete();
		}
	}

}

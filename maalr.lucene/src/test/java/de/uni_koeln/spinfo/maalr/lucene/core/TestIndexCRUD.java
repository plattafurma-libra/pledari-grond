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
package de.uni_koeln.spinfo.maalr.lucene.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueFormat;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.query.QueryResult;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneConfiguration;


public class TestIndexCRUD {

	private Dictionary index;
	
	private LuceneConfiguration environment;

	private File indexDir;

	private ValueFormat firstLang;

	private ValueFormat secondLang;
	
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
		LemmaDescription ld = Configuration.getInstance().getLemmaDescription();
		firstLang = ld.getResultList(true).get(0);
		secondLang = ld.getResultList(false).get(0);
	}
	
	@After
	public void afterTest() throws Exception {
		deleteRecursive(indexDir);
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

	private LexEntry generateValidEntry() {
		LemmaVersion lv = new LemmaVersion();
		lv.putEntryValue(firstLang.getKey(), "a" + UUID.randomUUID().toString());
		lv.putEntryValue(secondLang.getKey(), "b" + UUID.randomUUID().toString());
		LexEntry entry = new LexEntry(lv);
		lv.setVerification(Verification.ACCEPTED);
		entry.setId(UUID.randomUUID().toString());
		return entry;
	}

	@Ignore
	@Test
	public void testGetStatistics() throws NoIndexAvailableException {
		IndexStatistics stats = index.getIndexStatistics();
		Assert.assertNotNull(stats);
	}
	
	@Test
	public void testDropIndex() throws Exception  {
		index.dropIndex();
		index.reloadIndex();
		IndexStatistics statistics = index.getIndexStatistics();
		Assert.assertEquals(0, statistics.getNumberOfEntries());
	}
	
	
	@Test
	public void testCreateIndex() throws Exception  {
		testDropIndex();
		List<LexEntry> entries = new ArrayList<LexEntry>();
		for(int i = 0;  i < 5; i++) {
			entries.add(generateValidEntry());
		}
		index.addToIndex(entries.iterator());
		index.reloadIndex();
		IndexStatistics statistics = index.getIndexStatistics();
		Assert.assertEquals(entries.size(),statistics.getNumberOfEntries());
		QueryResult results = index.getAllStartingWith("german", "a", 0);
		Assert.assertTrue(results.getEntries().size() > 0);
		results = index.getAllStartingWith("english", "x", 0);
		Assert.assertTrue(results.getEntries().size() == 0);
	}
	
	@Test
	public void testUpdateIndex() throws Exception  {
		testCreateIndex();
		IndexStatistics beforeUpdate = index.getIndexStatistics();
		List<LexEntry> entries = new ArrayList<LexEntry>();
		for(int i = 0;  i < 5; i++) {
			entries.add(generateValidEntry());
		}
		index.addToIndex(entries.iterator());
		index.reloadIndex();
		IndexStatistics afterUpdate = index.getIndexStatistics();
		Assert.assertTrue(afterUpdate.getNumberOfEntries() == beforeUpdate.getNumberOfEntries() * 2);
	}
	

}

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
package de.uni_koeln.spinfo.maalr.mongo.integration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.uni_koeln.spinfo.maalr.MongoTestHelper;
import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.ValueFormat;
import de.uni_koeln.spinfo.maalr.configuration.Environment;
import de.uni_koeln.spinfo.maalr.login.LoginManager;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.exceptions.NoIndexAvailableException;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-maalr-test.xml")
public class ITIndexCreator extends MongoTestHelper {

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private Index index;
	
	@Autowired
	private Environment environment;

	private File indexDir;

	private ValueFormat secondLang;

	private ValueFormat firstLang;
	
	@Before
	public void beforeTest() throws Exception {
		loginManager.logout();
		File file = File.createTempFile("maalr", "test");
		indexDir = new File(file.getParentFile(), "maalr_test" + UUID.randomUUID().toString() + "_idx");
		Assert.assertFalse(indexDir.exists());
		indexDir.mkdir();
		file.deleteOnExit();
		LuceneConfiguration config = environment.getLuceneConfig();
		config.setBaseDirectory(indexDir.getAbsolutePath());
		environment.setLuceneConfig(config);
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
		entry.setId(UUID.randomUUID().toString());
		return entry;
	}

//	@Test(expected=AuthenticationException.class)
//	public void testGetStatistics_Unauthorized() throws NoIndexAvailableException {
//		index.getIndexStatistics();	
//	}
//	
//	@Test(expected=AccessDeniedException.class)
//	public void testGetStatistics_AccessDenied() throws NoIndexAvailableException {
//		loginManager.login("guest", "guest");
//		index.getIndexStatistics();	
//	}
	
	@Ignore
	@Test
	public void testGetStatistics() throws NoIndexAvailableException {
		loginManager.login("admin", "admin");
		IndexStatistics stats = index.getIndexStatistics();
		Assert.assertNotNull(stats);
	}
	
	
	@Ignore
	@Test(expected=AccessDeniedException.class)
	public void testUpdateIndex_AccessDenied() throws Exception  {
		loginManager.login("guest", "guest");
		List<LexEntry> entries = new ArrayList<LexEntry>();
		for(int i = 0;  i < 5; i++) {
			entries.add(generateValidEntry());
		}
		index.addToIndex(entries.iterator());
	}
	
	

}

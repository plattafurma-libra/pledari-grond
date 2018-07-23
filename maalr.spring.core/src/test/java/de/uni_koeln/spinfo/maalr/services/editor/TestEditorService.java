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
package de.uni_koeln.spinfo.maalr.services.editor;

import de.uni_koeln.spinfo.maalr.MongoTestHelper;

// TODO: Rewrite tests! Use de.uni_koeln.spinfo.maalr.login.custom.PGAutenticationProvider instead of "LoginManager"
// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(locations = "classpath:/spring-maalr-test.xml")
public class TestEditorService extends MongoTestHelper {

//	@Autowired private LoginManager loginManager;
//	@Autowired private SpringBackend backend;
//	@Autowired private EditorServiceImpl service;
//	@Autowired private Index index;
//
//	private static Logger logger = LoggerFactory.getLogger(ITSpringMongoBackend.class);
//	
//
//	@Before
//	public void beforeTest() throws Exception {
//		Database.getInstance().deleteAllEntries();
//		loginManager.logout();
//		GenericWebApplicationContext context = new GenericWebApplicationContext();
//		MockServletContext servlet = new MockServletContext();
//		context.setServletContext(servlet);
//
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		MockHttpSession session = new MockHttpSession();
//		request.setSession(session);
//		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//
//		context.refresh();
//		context.getBeanFactory().registerScope("session", new SessionScope());
//		loginManager.login("admin", "admin");
//		File file = File.createTempFile("maalr", "test");
//		File indexDir = new File(file.getParentFile(), "maalr_test" + UUID.randomUUID().toString() + "_idx");
//		Assert.assertFalse(indexDir.exists());
//		indexDir.mkdir();
//		file.deleteOnExit();
//		LuceneConfiguration environment = new LuceneConfiguration();
//		environment.setBaseDirectory(indexDir.getAbsolutePath());
//		Dictionary d = new Dictionary();
//		d.setEnvironment(environment);
//	}
//
//	@Test
//	public void testModifyOrder() throws Exception {
//		insertSampleEntries();
//		QueryResult result = index.queryExact("Haus", true, true);
//		List<LemmaVersion> old = result.getEntries();
//		for(int i = 0; i < old.size(); i++) {
//			logger.info("Old Order: " + i + " is " + old.get(i));
//		}
//		List<LemmaVersion> modified = new ArrayList<LemmaVersion>();
//		Assert.assertTrue(old.size() > 1);
//		modified.addAll(old);
//		do {
//			Collections.shuffle(modified);
//		} while (modified.equals(old));
//		for(int i = 0; i < modified.size(); i++) {
//			logger.info("New Order: " + i + " is " + modified.get(i));
//		}
//		List<LexEntry> updated = backend.updateOrder(true, modified);
//		for(int i = 0; i < updated.size(); i++) {
//			Assert.assertEquals(modified.get(i), updated.get(i).getCurrent());
//			logger.info("Upd Order: " + i + " is " + updated.get(i).getCurrent());
//		}
//	}
//	
//	/**
//	 * Helper method to insert and return a sample entry.
//	 * @return
//	 * @throws Exception
//	 */
//	private void insertSampleEntries() throws Exception {
//		List<LexEntry> entries = new ArrayList<LexEntry>();
//		LemmaVersion lemmaVersion = new LemmaVersion();
//		lemmaVersion.putEntryValue("german", "Haus");
//		lemmaVersion.putEntryValue("english", "House");
//		lemmaVersion.setVerification(Verification.ACCEPTED);
//		LexEntry entry = new LexEntry(lemmaVersion);
//		entries.add(entry);
//		lemmaVersion = new LemmaVersion();
//		lemmaVersion.putEntryValue("german", "Haus");
//		lemmaVersion.putEntryValue("english", "home");
//		lemmaVersion.setVerification(Verification.ACCEPTED);
//		entry = new LexEntry(lemmaVersion);
//		entries.add(entry);
//		lemmaVersion = new LemmaVersion();
//		lemmaVersion.putEntryValue("german", "Haus");
//		lemmaVersion.putEntryValue("english", "domicile");
//		lemmaVersion.setVerification(Verification.ACCEPTED);
//		entry = new LexEntry(lemmaVersion);
//		entries.add(entry);
//		lemmaVersion = new LemmaVersion();
//		lemmaVersion.putEntryValue("german", "Wohnhaus");
//		lemmaVersion.putEntryValue("english", "House");
//		lemmaVersion.putEntryValue("german_sort", "0");
//		lemmaVersion.putEntryValue("english_sort", "0");
//		lemmaVersion.setVerification(Verification.ACCEPTED);
//		entry = new LexEntry(lemmaVersion);
//		entries.add(entry);
//		lemmaVersion = new LemmaVersion();
//		lemmaVersion.putEntryValue("german", "Hausaufgaben");
//		lemmaVersion.putEntryValue("english", "homework");
//		lemmaVersion.putEntryValue("german_sort", "0");
//		lemmaVersion.putEntryValue("english_sort", "0");
//		lemmaVersion.setVerification(Verification.ACCEPTED);
//		entry = new LexEntry(lemmaVersion);
//		entries.add(entry);
//		for (LexEntry le : entries) {
//			Database.getInstance().insert(le);
//		}
//		index.dropIndex();
//		index.addToIndex(entries.iterator());
//		index.reloadIndex();
//	}
}

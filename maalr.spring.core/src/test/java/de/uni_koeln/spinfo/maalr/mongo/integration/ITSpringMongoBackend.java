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

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.SessionScope;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.mongodb.BasicDBObject;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.login.LoginManager;
import de.uni_koeln.spinfo.maalr.mongo.SpringBackend;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.util.MongoTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-maalr-test.xml")
public class ITSpringMongoBackend extends MongoTestHelper {

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private SpringBackend backend;
	

	private static Logger logger = LoggerFactory.getLogger(ITSpringMongoBackend.class);
	

	@Before
	public void beforeTest() throws Exception {
		Database.getInstance().deleteAllEntries();
		loginManager.logout();
		GenericWebApplicationContext context = new GenericWebApplicationContext();
		MockServletContext servlet = new MockServletContext();
		context.setServletContext(servlet);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		context.refresh();
		context.getBeanFactory().registerScope("session", new SessionScope());
	}
	
	/**
	 * Test that a guest cannot insert entries
	 */
	@Test(expected = AccessDeniedException.class)
	public void testInvalidInsert_AccessDenied() throws Exception {
		loginManager.login("guest", "guest");
		insertSampleEntry();
	}
	
	/**
	 * Test that a guest cannot insert entries
	 */
	@Test(expected = AuthenticationException.class)
	public void testInvalidInsert_Unauthorized() throws Exception {
		loginManager.login("asdf", "asdf");
		LemmaVersion lemmaVersion = new LemmaVersion();
		lemmaVersion.putEntryValue("german", "haus");
		LexEntry entry = new LexEntry(lemmaVersion);
		backend.insert(entry);
	}
	
	/**
	 * Test that an entry without any lemma version cannot
	 * be inserted.
	 */
	@Test(expected = RuntimeException.class)
	public void testInvalidInsert_MissingLemmaVersion() throws Exception {
		loginManager.login("admin", "admin");
		LexEntry entry = new LexEntry(null);
		backend.insert(entry);
	}
	
	/**
	 * Test that a lemma version without any content
	 * cannot be inserted
	 * @throws Exception
	 */
	@Test(expected = InvalidEntryException.class)
	public void testInvalidInsert_MissingVersionContent() throws Exception {
		loginManager.login("admin", "admin");
		LemmaVersion lemmaVersion = new LemmaVersion();
		LexEntry entry = new LexEntry(lemmaVersion);
		backend.insert(entry);
	}
	
	/**
	 * Test that a lemma cannot be inserted with a given
	 * user id
	 * @return 
	 * @throws Exception
	 */
	@Test(expected=InvalidEntryException.class)
	public void testInvalidInsert_UserSet() throws Exception {
		loginManager.login("admin", "admin");
		LemmaVersion lemmaVersion = new LemmaVersion();
		lemmaVersion.putEntryValue("german", "Haus");
		lemmaVersion.setUserId("admin");
		LexEntry entry = new LexEntry(lemmaVersion);
		backend.insert(entry);
	}
	
	/**
	 * Test that a lemma cannot be inserted with a given
	 * status
	 * @return 
	 * @throws Exception
	 */
	@Test(expected=InvalidEntryException.class)
	public void testInvalidInsert_StatusSet() throws Exception {
		loginManager.login("admin", "admin");
		LemmaVersion lemmaVersion = new LemmaVersion();
		lemmaVersion.putEntryValue("german", "Haus");
		lemmaVersion.setVerification(Verification.ACCEPTED);
		LexEntry entry = new LexEntry(lemmaVersion);
		backend.insert(entry);
	}
	
	/**
	 * Test that a lemma cannot be inserted with a given
	 * timestamp
	 * @return 
	 * @throws Exception
	 */
	@Test(expected=InvalidEntryException.class)
	public void testInvalidInsert_TimestampSet() throws Exception {
		loginManager.login("admin", "admin");
		LemmaVersion lemmaVersion = new LemmaVersion();
		lemmaVersion.putEntryValue("german", "Haus");
		lemmaVersion.setTimestamp(System.currentTimeMillis());
		LexEntry entry = new LexEntry(lemmaVersion);
		backend.insert(entry);
	}

	/**
	 * Test that a registered user can insert entries.
	 * @return 
	 * @throws Exception
	 */
	@Test
	public void testValidInsert_Authorized() throws Exception {
		long oldSize = Database.getInstance().getNumberOfEntries();
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		Assert.assertTrue(entry.getCurrent().isApproved());
		long newSize = Database.getInstance().getNumberOfEntries();
		Assert.assertEquals(oldSize+1, newSize);
		checkAcceptedCount(entry, 1);
	}
	
	/**
	 * Test that a user may suggest a new version
	 * @return 
	 * @throws Exception
	 */
	@Test
	public void testValidSuggestInsert() throws Exception {
		loginManager.login("guest", "guest");
		LemmaVersion lemmaVersion = new LemmaVersion();
		lemmaVersion.putEntryValue("german", "Haus");
		LexEntry entry = new LexEntry(lemmaVersion);
		backend.suggestNewEntry(entry);
		compareWithVersionFromDB(entry);
		Assert.assertNotNull(entry.getId());
		Assert.assertEquals(Status.NEW_ENTRY, entry.getStatus());
		checkAcceptedCount(entry, 0);
	}
	
	/**
	 * Test that a user may suggest a new version
	 * @return 
	 * @throws Exception
	 */
	@Test
	public void testValidSuggestInsertAndVerify() throws Exception {
		loginManager.login("guest", "guest");
		LemmaVersion suggestion = new LemmaVersion();
		suggestion.putEntryValue("german", "Haus");
		LexEntry entry = new LexEntry(suggestion);
		backend.suggestNewEntry(entry);
		int count = entry.getVersionHistory().size();
		compareWithVersionFromDB(entry);
		Assert.assertNotNull(entry.getId());
		Assert.assertEquals(Status.NEW_ENTRY, entry.getStatus());
		Assert.assertEquals(Verification.UNVERIFIED, entry.getMostRecent().getVerification());
		checkAcceptedCount(entry, 0);
		loginManager.login("admin", "admin");
		LemmaVersion modified = new LemmaVersion();
		modified.getEntryValues().putAll(suggestion.getEntryValues());
		modified.getMaalrValues().putAll(suggestion.getMaalrValues());
		modified.putEntryValue("german", "Haus");
		modified.putEntryValue("rr", "casa");
		entry.addLemma(modified);
		backend.acceptAfterUpdate(entry, suggestion, modified);
		compareWithVersionFromDB(entry);
		Assert.assertNotSame(suggestion, modified);
		int newCount = entry.getVersionHistory().size();
		Assert.assertEquals(count+1, newCount);
		Assert.assertNotNull(entry.getCurrent());
		Assert.assertNotSame(entry.getCurrent().getUserId(), entry.getCurrent().getVerifierId());
		Assert.assertSame(entry.getCurrent().getUserId(), suggestion.getUserId());
		Assert.assertEquals(Verification.OUTDATED, suggestion.getVerification());
		Assert.assertSame(Verification.ACCEPTED, modified.getVerification());
		checkAcceptedCount(entry, 1);
	}
	
	private void checkAcceptedCount(LexEntry entry, int expected) {
		if(expected > 1 || expected < 0) throw new IllegalArgumentException("Expectation must be 0 or 1!");
		List<LemmaVersion> versions = entry.getVersionHistory();
		int counter = 0;
		for (LemmaVersion lemmaVersion : versions) {
			if(lemmaVersion.isApproved()) {
				counter++;
			}
		}
		Assert.assertSame("Wrong number of accepted entries!", expected, counter);
	}
	
	@Test
	public void testValidUpdate() throws Exception {
		loginManager.login("admin", "admin");
		// Get valid and inserted entry
		LexEntry entry = insertSampleEntry();
		checkAcceptedCount(entry, 1);
		// Test that a user may insert updates
		createUpdates(entry, false);
		checkAcceptedCount(entry, 1);
		List<LemmaVersion> versionHistory = entry.getVersionHistory();
		Assert.assertTrue(versionHistory.get(0).isApproved());
	}

	@Test(expected=InvalidEntryException.class)
	public void testInvalidUpdate_UserSet() throws Exception {
		loginManager.login("admin", "admin");
		// Get valid and inserted entry
		LexEntry entry = insertSampleEntry();
		LemmaVersion update = new LemmaVersion();
		update.setUserId("admin");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("haus", "chase");
		update.setEntryValues(map);
		backend.update(entry, update);
	}
	
	@Test(expected=InvalidEntryException.class)
	public void testInvalidUpdate_TimestampSet() throws Exception {
		loginManager.login("admin", "admin");
		// Get valid and inserted entry
		LexEntry entry = insertSampleEntry();
		LemmaVersion update = new LemmaVersion();
		update.setTimestamp(System.currentTimeMillis());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("haus", "chase");
		update.setEntryValues(map);
		backend.update(entry, update);
	}
	
//	@Test(expected=InvalidEntryException.class)
//	public void testInvalidUpdate_StateSet() throws Exception {
//		loginManager.login("admin", "admin");
//		// Get valid and inserted entry
//		LexEntry entry = insertSampleEntry();
//		LemmaVersion update = new LemmaVersion();
//		update.setVerification(Verification.ACCEPTED);
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("haus", "chase");
//		update.setEntryValues(map);
//		backend.update(entry, update);
//	}

	/**
	 * Test that a lemma version without any content
	 * cannot be updated
	 * @throws Exception
	 */
	@Test(expected = InvalidEntryException.class)
	public void testInvalidUpdate_MissingVersionContent() throws Exception {
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		LemmaVersion lemmaVersion = new LemmaVersion();
		backend.update(entry, lemmaVersion);
	}
	
	/**
	 * Test that a lemma version without any content
	 * cannot be updated
	 * @throws Exception
	 */
	@Test(expected = InvalidEntryException.class)
	public void testInvalidUpdate_MissingLemmaVersion() throws Exception {
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		backend.update(entry, null);
	}
	
	/**
	 * Test that unauthorized users can't update a lemma version
	 * @throws Exception
	 */
	@Test(expected = AuthenticationException.class)
	public void testInvalidUpdate_AccessDenied() throws Exception {
		// Get valid and inserted entry
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		loginManager.login("asdf", "asdf");
		createUpdates(entry, false);
	}
	
	/**
	 * Test that unauthorized users can't update a lemma version
	 * @throws Exception
	 */
	@Test(expected = AccessDeniedException.class)
	public void testInvalidUpdate_Unauthorized() throws Exception {
		// Get valid and inserted entry
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		loginManager.login("guest", "guest");
		createUpdates(entry, false);
	}
	
	/**
	 * Test that unauthorized users can vote
	 * @throws Exception
	 */
	@Test
	public void testValidSuggestUpdate() throws Exception {
		// Get valid and inserted entry
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		checkAcceptedCount(entry, 1);
		loginManager.login("guest", "guest");
		createUpdates(entry, true);
		checkAcceptedCount(entry, 1);
		List<LemmaVersion> versionHistory = entry.getVersionHistory();
		for(int i = 0; i < versionHistory.size()-1; i++) {
			Assert.assertFalse(versionHistory.get(i).isApproved());
		}
	}
	
	/**
	 * Test that admin-users can accept a lemma version
	 * @throws Exception
	 */
	@Test
	public void testValidApprove() throws Exception {
		// Get valid and inserted entry
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		checkAcceptedCount(entry, 1);
		loginManager.login("guest", "guest");
		createUpdates(entry, true);
		List<LemmaVersion> versionHistory = entry.getVersionHistory();
		checkAcceptedCount(entry, 1);
		loginManager.login("admin", "admin");
		// Accept first change
		backend.accept(entry, versionHistory.get(1));
		Assert.assertTrue(versionHistory.get(1).isApproved());
		Assert.assertFalse(versionHistory.get(0).isApproved());
		Assert.assertEquals(entry.getCurrent(), versionHistory.get(1));
		checkAcceptedCount(entry, 1);
		backend.accept(entry, versionHistory.get(0));
		Assert.assertFalse(versionHistory.get(1).isApproved());
		Assert.assertTrue(versionHistory.get(0).isApproved());
		Assert.assertEquals(entry.getCurrent(), versionHistory.get(0));
		checkAcceptedCount(entry, 1);
	}
	
	/**
	 * Test that users with insufficient role can't accept a lemma version
	 * @throws Exception
	 */
	@Test(expected=AccessDeniedException.class)
	public void testInvalidApprove_AccessDenied() throws Exception {
		// Get valid and inserted entry
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		loginManager.login("guest", "guest");
		createUpdates(entry, true);
		List<LemmaVersion> versionHistory = entry.getVersionHistory();
		backend.accept(entry, versionHistory.get(1));
	}
	
	/**
	 * Test that unauthorized users can't accept a lemma version
	 * @throws Exception
	 */
	@Test(expected=AuthenticationException.class)
	public void testInvalidApprove_Unauthorized() throws Exception {
		// Get valid and inserted entry
		loginManager.login("admin", "admin");
		LexEntry entry = insertSampleEntry();
		loginManager.login("asdf", "asdf");
		createUpdates(entry, true);
		List<LemmaVersion> versionHistory = entry.getVersionHistory();
		backend.accept(entry, versionHistory.get(1));
	}
	
	
	@Ignore // Not yet implemented
	@Test
	public void testValidDelete() throws Exception {
		loginManager.login("admin", "admin");
		// Get valid and inserted entry
		LexEntry entry = insertSampleEntry();
		createUpdates(entry, false);
		
		// TODO: Define pre- and post-conditions for 'delete'.
		// TODO: Separate 'delete' from 'hide' ?
		// 'hidden' elements would be ignored when displaying
		// entries, 'deleted' elements would be removed
		// from the database.
		
		/*
		 * when deleting a lemma version,
		 * - it must not be the current version
		 *   (user must define another current version first)
		 */
		
	}
	
	// Helper Methods //
	
	/**
	 * Helper method to compare a version history with
	 * expected values
	 */
	private void compareVersionHistory(List<LemmaVersion> versions, LemmaVersion...expected) {
		Assert.assertEquals(expected.length, versions.size());
		for(int i = 0; i < versions.size(); i++) {
			Assert.assertEquals(expected[i], versions.get(i));
		}
	}
	
	/**
	 * Helper method to insert and return a sample entry.
	 * @return
	 * @throws Exception
	 */
	private LexEntry insertSampleEntry() throws Exception {
		try {
			LemmaVersion lemmaVersion = new LemmaVersion();
			lemmaVersion.putEntryValue("german", "Haus");
			LexEntry entry = new LexEntry(lemmaVersion);
			backend.insert(entry);
			compareWithVersionFromDB(entry);
			return entry;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Helper method to compare an entry with the currently
	 * stored db representation. Will fail if the stored
	 * version is not equal to the current version. 
	 * @param entry
	 * @throws NoDatabaseAvailableException 
	 */
	private void compareWithVersionFromDB(LexEntry entry) throws NoDatabaseAvailableException {
		Assert.assertNotNull(entry.getId());
		BasicDBObject obj = Database.getInstance().getById(entry.getId());
		Assert.assertNotNull(obj);
		LexEntry retrieved = Converter.convertToLexEntry(obj);
		Assert.assertEquals(entry, retrieved);
		Assert.assertEquals(entry.getStatus(), retrieved.getStatus());
		Assert.assertEquals(entry.getUnapprovedVersions(), retrieved.getUnapprovedVersions());
		Assert.assertEquals(entry.hasUnapprovedVersions(), retrieved.hasUnapprovedVersions());
	}
	
	/**
	 * Helper method to create some updates for a given
	 * entry. Verifies that the entries are stored in
	 * the db.
	 * @param entry
	 * @throws InvalidEntryException
	 * @throws Exception
	 */
	private void createUpdates(LexEntry entry, boolean suggest)
			throws InvalidEntryException, Exception {
		LemmaVersion lemmaVersion = entry.getCurrent();
		// Create a LemmaVersion for update
		LemmaVersion update = new LemmaVersion();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("haus", "chase");
		update.setEntryValues(map);

		// Update operation
		if(suggest) {
			backend.suggestUpdate(entry, update);
		} else {
			backend.update(entry, update);
		}
		
		// Wait and compare entry with version from db 
		compareWithVersionFromDB(entry);
		Assert.assertNotNull(update.getInternalId());
		// Check version history
		compareVersionHistory(entry.getVersionHistory(), update, lemmaVersion);

		// Create a other LemmaVersion for update
		LemmaVersion update_2 = new LemmaVersion();
		map = new HashMap<String, String>();
		map.put("haus", "casa");
		update_2.setEntryValues(map);
		
		// Update operation
		if(suggest) {
			backend.suggestUpdate(entry, update_2);
		} else {
			backend.update(entry, update_2);
		}
		// Wait and compare entry with version from db
		compareWithVersionFromDB(entry);
		Assert.assertNotNull(update_2.getInternalId());
		// Check version history
		compareVersionHistory(entry.getVersionHistory(), update_2, update, lemmaVersion);
	}
	
}

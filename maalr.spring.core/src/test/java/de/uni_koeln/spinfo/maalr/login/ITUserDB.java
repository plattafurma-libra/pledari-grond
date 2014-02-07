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
package de.uni_koeln.spinfo.maalr.login;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;
import de.uni_koeln.spinfo.maalr.mongo.util.MongoTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-maalr-test.xml")
public class ITUserDB extends MongoTestHelper {

	@Autowired
	private LoginManager loginManager;

	@Autowired
	private UserInfoBackend backend;
		
	@Before
	public void beforeTest() throws Exception {
		Authentication login = loginManager.login("admin", "admin");
		backend.deleteAllEntries();
		loginManager.logout();
	}
	
	@After
	public void afterTest() {
		loginManager.logout();
	}
	
	@Test
	public void testInsert() throws InvalidUserException {
		String login = "someone@somewhere.com";
		Assert.assertFalse(backend.userExists(login));
		MaalrUserInfo user = insertUser(login);
		Assert.assertTrue(backend.userExists(login));
		MaalrUserInfo stored = backend.getByLogin(login);
		Assert.assertEquals(user, stored);
		Assert.assertEquals(Role.GUEST_1, user.getRole());
	}
		
	@Test(expected=InvalidUserException.class)
	public void testInvalidInsert() throws InvalidUserException {
		String login = "someone@somewhere.com";
		MaalrUserInfo user;
		insertUser(login);
		user = new MaalrUserInfo(login, Role.GUEST_1);
		backend.insert(user);
	}

	@Test
	public void testValidRoleChange() throws InvalidUserException {
		String login = "someone@somewhere.com";
		MaalrUserInfo user = insertUser(login);
		loginManager.login("admin", "admin");
		backend.updateUserRole(user, Role.ADMIN_5);
		MaalrUserInfo dbUser = backend.getByLogin(user.getLogin());
		Assert.assertEquals(Role.ADMIN_5, dbUser.getRole());
		Assert.assertEquals(Role.ADMIN_5, user.getRole());
	}
	
	@Test(expected=AccessDeniedException.class)
	public void testInvalidRoleChange() throws InvalidUserException {
		String login = "someone@somewhere.com";
		MaalrUserInfo user = insertUser(login);
		loginManager.login("guest", "guest");
		backend.updateUserRole(user, Role.ADMIN_5);
	}
	
	@Test
	public void testValidPropertiesUpdate() throws InvalidUserException {
		String login = "test";
		MaalrUserInfo user = insertUser(login);
		loginManager.login("admin", "admin");
		backend.updateUserRole(user, Role.OPENID_2);
		loginManager.logout();
		loginManager.login("test", "test");
		Assert.assertEquals(Role.OPENID_2,backend.getByLogin(login).getRole());
		user.setTitle("Herr");
		user.setFirstname("Josua");
		user.setLastname("Maaler");
		user.setRole(Role.ADMIN_5);
		backend.updateUserFields(user);
		MaalrUserInfo dbUser = backend.getByLogin(user.getLogin());
		Assert.assertEquals(user.getTitle(), dbUser.getTitle());
		Assert.assertEquals("Herr", dbUser.getTitle());
		Assert.assertEquals(user.getFirstname(), dbUser.getFirstname());
		Assert.assertEquals("Josua", dbUser.getFirstname());
		Assert.assertEquals(user.getLastname(), dbUser.getLastname());
		Assert.assertEquals("Maaler", dbUser.getLastname());
		Assert.assertNotSame(user.getRole(), dbUser.getRole());
		Assert.assertEquals(Role.OPENID_2, dbUser.getRole());
	}
	
	private MaalrUserInfo insertUser(String login) throws InvalidUserException {
		MaalrUserInfo user = new MaalrUserInfo(login, Role.GUEST_1);
		Assert.assertFalse(backend.userExists(login));
		backend.insert(user);
		return user;
	}
}

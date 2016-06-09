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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.uni_koeln.spinfo.maalr.MongoTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-maalr-test.xml")
public class TestLoginManager {

	@Autowired
	private LoginManager loginManager;
	
	@Before
	public void beforeTest() {
		loginManager.logout();
	}
	
	@BeforeClass
	public static void startTestMongoDB() throws Exception {
		MongoTestHelper.startTestMongoDB();
	}

	@AfterClass
	public static void stopTestMongoDB() throws Exception {
		MongoTestHelper.stopTestMongoDB();
	}


	/**
	 * Test that no one is logged in by default
	 */
	@Test
	public void testInitialLogin() {
		Assert.assertFalse(loginManager.loggedIn());
	}
	
	
	@Test
	public void testLogin() {
		Assert.assertFalse(loginManager.loggedIn());
		Authentication auth = loginManager.login("guest", "guest");
		Assert.assertNotNull(auth);
		Assert.assertTrue(loginManager.loggedIn());
		String userId = loginManager.getCurrentUserId();
		Assert.assertEquals("guest", userId);
		loginManager.logout();
		Assert.assertFalse(loginManager.loggedIn());
		userId = loginManager.getCurrentUserId();
		Assert.assertNull(userId);
	}

}

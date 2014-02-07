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

import de.flapdoodle.embedmongo.runtime.Network;
import de.uni_koeln.spinfo.maalr.mongo.util.embedmongo.MongoDBRuntime;
import de.uni_koeln.spinfo.maalr.mongo.util.embedmongo.MongodConfig;
import de.uni_koeln.spinfo.maalr.mongo.util.embedmongo.MongodExecutable;
import de.uni_koeln.spinfo.maalr.mongo.util.embedmongo.MongodProcess;
import de.uni_koeln.spinfo.maalr.mongo.util.embedmongo.Version;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-maalr-test.xml")
public class TestLoginManager {
	
	private static MongodExecutable mongodExe;
	private static MongodProcess mongod;
	
	@Autowired
	private LoginManager loginManager;
	
	@Before
	public void beforeTest() {
		loginManager.logout();
	}
	
	@BeforeClass
	public static void startTestMongoDB() throws Exception {
		try {
			MongoDBRuntime runtime = MongoDBRuntime.getDefaultInstance();
			mongodExe = runtime.prepare(new MongodConfig(Version.V2_2_0, 27017,
					Network.localhostIsIPv6()));
			mongod = mongodExe.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@AfterClass
	public static void stopTestMongoDB() throws Exception {
		mongod.stop();
		mongodExe.cleanup();
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

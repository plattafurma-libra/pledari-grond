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
package de.uni_koeln.spinfo.maalr.mongo.tests;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;

public class TestLemmaVersion {

	@Test
	public void testStatus() {
		LemmaVersion l = new LemmaVersion();
		l.setStatus(Status.NEW_ENTRY);
		Status s = l.getStatus();
		Assert.assertEquals(Status.NEW_ENTRY, s);
		l.setStatus(Status.DELETED);
		s = l.getStatus();
		Assert.assertEquals(Status.DELETED, s);
	}

	@Test
	public void testUserId() {
		LemmaVersion l = new LemmaVersion();
		l.setUserId("a");
		Assert.assertEquals("a", l.getUserId());
		l.setUserId("b");
		Assert.assertEquals("b", l.getUserId());
	}

	@Test
	public void testTimeStamp() {
		LemmaVersion l = new LemmaVersion();
		l.setTimestamp(10L);
		Assert.assertEquals(10, (long) l.getTimestamp());
		l.setTimestamp(20L);
		Assert.assertEquals(20, (long) l.getTimestamp());
	}

	// @Test
	// public void testRating() {
	// LemmaVersion l = new LemmaVersion();
	// Assert.assertEquals(0, l.getRating());
	// l.setRating("a", true);
	// Assert.assertEquals(1, l.getRating());
	// l.setRating("b", true);
	// Assert.assertEquals(2, l.getRating());
	// l.setRating("b", true);
	// Assert.assertEquals(2, l.getRating());
	// l.setRating("b", false);
	// Assert.assertEquals(1, l.getRating());
	// }

	@Test
	@Ignore
	public void testValues() {
		LemmaVersion l = new LemmaVersion();
		Assert.assertNotNull(l.getEntryValues());
		Assert.assertEquals(0, l.getEntryValues().size());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("a", "b");
		map.put("c", "d");
		l.setEntryValues(map);
		Assert.assertEquals(map, l.getEntryValues());
	}

}

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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;

public class TestLexEntry {

	@Test
	public void testHistory() {
		LemmaVersion version = new LemmaVersion();
		version.putEntryValue("a", "b");
		LexEntry entry = new LexEntry(version);
		List<LemmaVersion> history = entry.getVersionHistory();
		Assert.assertEquals(1, history.size());
		Assert.assertTrue(history.contains(version));
		Assert.assertTrue(entry.hasUnapprovedVersions());
		Assert.assertTrue(entry.getUnapprovedVersions().size() == 1);
		Assert.assertEquals(version, entry.getCurrent());
		LemmaVersion version2 = new LemmaVersion();
		version2.putEntryValue("c", "d");
		Assert.assertNotSame(version, version2);
		entry.addLemma(version2);
		history = entry.getVersionHistory();
		Assert.assertEquals(2, history.size());
		Assert.assertTrue(history.contains(version));
		Assert.assertTrue(history.contains(version2));
		Assert.assertTrue(entry.getUnapprovedVersions().size() == 2);
		Assert.assertEquals(version, entry.getCurrent());
		entry.setCurrent(version2);
		Assert.assertEquals(version2, entry.getCurrent());
		
	}
	
}

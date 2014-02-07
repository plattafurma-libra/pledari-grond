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
package de.uni_koeln.spinfo.maalr.sigar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.hyperic.sigar.Sigar;
import org.junit.Test;

import de.uni_koeln.spinfo.maalr.sigar.info.DiskStats;
import de.uni_koeln.spinfo.maalr.sigar.info.NetStats;
import de.uni_koeln.spinfo.maalr.sigar.info.SigarSummary;

public class TestSigar {
	
	@Test
	public void testCreation() {
		Sigar instance = SigarWrapper.getSigarInstance();
		Assert.assertNotNull(instance);
		instance.close();
	}
	
	@Test
	public void testReporter() throws InterruptedException {
		SigarReporter reporter = SigarWrapper.newReporter(100);
		final List<SigarSummary> summaries = new ArrayList<SigarSummary>();
		reporter.addHandler(new SigarHandler() {
			
			public void updated(SigarSummary summary) {
				summaries.add(summary);
			}
		});
		Thread.sleep(2000);
		Assert.assertNotSame(0, summaries.size());
		SigarSummary summary = summaries.get(0);
		Assert.assertNotNull(summary);
		Assert.assertNotSame(0, summary.getJavaMemoryCommitted());
		Assert.assertNotSame(0, summary.getJavaMemoryMax());
		Assert.assertNotSame(0, summary.getJavaMemoryUsed());
		Assert.assertNotSame(0, summary.getOsFreeMemory());
		Assert.assertNotSame(0, summary.getOsTotalMemory());
		Assert.assertNotSame(0, summary.getOsUsedMemory());
		Assert.assertNotSame(0, summary.getUptime());
		Assert.assertNotSame(0, summary.getJavaFreeMemoryPercent());
		Assert.assertNotSame(0, summary.getJavaUsedMemoryPercent());
		Assert.assertNotSame(0, summary.getOsFreeMemoryPercent());
		Assert.assertNotSame(0, summary.getOsUsedMemoryPercent());
		Assert.assertNotNull(summary.getDiskStats());
		ArrayList<DiskStats> diskStats = summary.getDiskStats();
		Assert.assertNotSame(0, diskStats.size());
		int checked = 0;
		for (DiskStats ds : diskStats) {
			if(ds.getTotal() == 0) continue;
			checked++;
			Assert.assertNotSame(0, ds.getTotal());
			Assert.assertNotSame(0, ds.getAvailable());
			Assert.assertNotSame(0, ds.getUsed());
		}
		Assert.assertTrue(checked > 0);
		Map<String, NetStats> netStats = summary.getNetStats();
		Assert.assertNotNull(summary.getNetStats());
		//Assert.assertNotSame(0, netStats.size());
	}

}

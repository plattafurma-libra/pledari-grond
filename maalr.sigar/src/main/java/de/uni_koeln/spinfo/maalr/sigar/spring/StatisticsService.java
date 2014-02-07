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
package de.uni_koeln.spinfo.maalr.sigar.spring;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.sigar.SigarHandler;
import de.uni_koeln.spinfo.maalr.sigar.SigarWrapper;
import de.uni_koeln.spinfo.maalr.sigar.info.SigarSummary;

@Service
public class StatisticsService {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	private SigarSummary current;

	public SigarSummary getCurrent() {
		return current;
	}

	@PostConstruct
	public void postConstruct() throws Exception  {
		testStatistics();
	}

	private void testStatistics() {
		SigarWrapper.newReporter(1000).addHandler(new SigarHandler() {
			
//			NumberFormat nf = (NumberFormat) NumberFormat.getInstance().clone();
//			private static final double MB = 1024*1024;
			
			@Override
			public void updated(SigarSummary summary) {
				current = summary;
				//logger.info("Java-Memory: " + nf.format(summary.getJavaMemoryUsed()/MB) + " MB used, " + nf.format(summary.getJavaMemoryCommitted()/MB) + "MB committed, " + nf.format(summary.getJavaMemoryMax()/MB) + " MB max.");
//				ArrayList<DiskStats> diskStats = summary.getDiskStats();
//				for (DiskStats stats : diskStats) {
//					logger.info("Disk " + stats.getDevName()+" (" + stats.getDirName() + "): " + nf.format(stats.getAvailable()/MB) + " of " + nf.format(stats.getTotal()/MB) + " GB available, " + nf.format(stats.getUsed()/MB) + " GB used.");
//				}
			}
		});
	}

}

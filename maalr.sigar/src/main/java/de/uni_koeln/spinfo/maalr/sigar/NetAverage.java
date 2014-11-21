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

import java.util.LinkedList;

public class NetAverage {
	
	private LinkedList<Long> values = new LinkedList<Long>();
	private int size;
	private long max;
	
	public long getMax() {
		return max;
	}

	public NetAverage(int size) {
		this.size = size;
	}
	
	public synchronized void addValue(Long value) {
		values.add(value);
		if(values.size() > size) {
			values.removeFirst();
		}
		updateMax();
	}
	
	private void updateMax() {
		max = Math.max(max, getThroughput());
	}

	public synchronized long getThroughput() {
		if(values.size() < 2) return 0;
		return values.get(values.size()-1) - values.get(0);
	}

}

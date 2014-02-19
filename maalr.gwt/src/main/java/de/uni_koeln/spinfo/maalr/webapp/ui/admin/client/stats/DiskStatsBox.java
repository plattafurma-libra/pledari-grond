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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.statistics.DiskStats;
import de.uni_koeln.spinfo.maalr.common.shared.statistics.SystemSummary;

public class DiskStatsBox extends Composite {

	private static DiskStatsUiBinder uiBinder = GWT
			.create(DiskStatsUiBinder.class);
	
	
	interface DiskStatsUiBinder extends UiBinder<Widget, DiskStatsBox> {
	}
	
	private Map<String, DiskStatsRow> mounted;
	
	@UiField
	VerticalPanel disks;
	
	public DiskStatsBox() {
		mounted = new HashMap<String, DiskStatsRow>();
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
	
	public List<DiskStatsRow> getDisks() {
		List<DiskStatsRow> all = new ArrayList<DiskStatsRow>();
		for(int i = 0; i < disks.getWidgetCount(); i++) {
			all.add((DiskStatsRow) disks.getWidget(i));
		}
		return all;
	}
	private void checkMounted(Map<String, DiskStatsRow> currentlyMounted) {
		Iterator<DiskStatsRow> iterator = getDisks().iterator();
		while(iterator.hasNext()) {
			DiskStatsRow stats = iterator.next();
			if(!currentlyMounted.containsKey(stats.getDevName())) {
				stats.removeFromParent();
			}
		}
		mounted.clear();
		mounted.putAll(currentlyMounted);
	}
	
	public void update(SystemSummary summary) {
		ArrayList<DiskStats> diskStats = summary.getDiskStats();
		Map<String, DiskStatsRow> currentlyMounted = new HashMap<String, DiskStatsRow>();
		for (DiskStats stat : diskStats) {
			DiskStatsRow current = mounted.get(stat.getDevName());
			if(current == null) {
				current = newRow(stat);
				mounted.put(stat.getDevName(), current);
				disks.add(current);
			}
			currentlyMounted.put(stat.getDevName(), current);
			current.update(stat);
		}
		checkMounted(currentlyMounted);
	}


	private DiskStatsRow newRow(DiskStats stat) {
		return new DiskStatsRow(stat);
	}
	
}

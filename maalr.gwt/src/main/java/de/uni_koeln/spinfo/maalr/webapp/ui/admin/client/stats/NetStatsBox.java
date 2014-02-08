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

import de.uni_koeln.spinfo.maalr.common.shared.statistics.NetStats;
import de.uni_koeln.spinfo.maalr.common.shared.statistics.SystemSummary;

public class NetStatsBox extends Composite {

	private static NetStatsUiBinder uiBinder = GWT
			.create(NetStatsUiBinder.class);
	
	
	interface NetStatsUiBinder extends UiBinder<Widget, NetStatsBox> {
	}
	
	private Map<String, NetStatsRow> existing;
	
	@UiField
	VerticalPanel netInterfaces;
	
	public NetStatsBox() {
		existing = new HashMap<String, NetStatsRow>();
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public List<NetStatsRow> getNetInterfaces() {
		List<NetStatsRow> all = new ArrayList<NetStatsRow>();
		for(int i = 0; i < netInterfaces.getWidgetCount(); i++) {
			all.add((NetStatsRow) netInterfaces.getWidget(i));
		}
		return all;
	}
	
	private void checkNetInterfaces(Map<String, NetStatsRow> current) {
		Iterator<NetStatsRow> iterator = getNetInterfaces().iterator();
		while(iterator.hasNext()) {
			NetStatsRow stats = iterator.next();
			if(!current.containsKey(stats.getName())) {
				stats.removeFromParent();
			}
		}
		existing.clear();
		existing.putAll(current);
	}
	
	public void update(SystemSummary summary) {
		Map<String, NetStats> netStats = summary.getNetStats();
		Map<String, NetStatsRow> currentlyMounted = new HashMap<String, NetStatsRow>();
		for (NetStats stat : netStats.values()) {
			NetStatsRow current = existing.get(stat.getName());
			if(current == null) {
				current = newRow(stat);
				existing.put(stat.getName(), current);
				netInterfaces.add(current);
			}
			currentlyMounted.put(stat.getName(), current);
			current.update(stat);
		}
		checkNetInterfaces(currentlyMounted);
	}


	private NetStatsRow newRow(NetStats stat) {
		return new NetStatsRow(stat);
	}
	
}

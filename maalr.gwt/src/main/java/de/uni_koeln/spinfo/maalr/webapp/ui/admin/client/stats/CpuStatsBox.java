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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.sigar.info.SigarSummary;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.CpuGauge;


public class CpuStatsBox extends Composite {

	private static CpuStatsUiBinder uiBinder = GWT
			.create(CpuStatsUiBinder.class);
	
	interface CpuStatsUiBinder extends UiBinder<Widget, CpuStatsBox> {
	}
	
	@UiField
	CpuProcPie cpuPie;
	
	@UiField
	CpuGauge cpuGauge;
	
	public CpuStatsBox() {
		initWidget(uiBinder.createAndBindUi(this));
		cpuGauge.setDimension(300, 200);
		cpuPie.setDimension(400, 250);
	}

	public void draw() {
		cpuPie.draw();
		cpuGauge.draw();
	}

	public void update(SigarSummary summary) {
		cpuGauge.setNumberOfCPUs(summary.getNumberOfCPUs());
		cpuGauge.setLoadAverage(summary.getLoadAverage()[0]);
		cpuGauge.draw();
		try {
			cpuPie.setData(summary.getCpuSystem(), summary.getCpuUser(), summary.getCpuNice(), summary.getCpuIdle());
			cpuPie.draw();
		} catch (Exception e) {
			Window.alert("Falure: " + e);
		}
	}


	
	
	
	
}

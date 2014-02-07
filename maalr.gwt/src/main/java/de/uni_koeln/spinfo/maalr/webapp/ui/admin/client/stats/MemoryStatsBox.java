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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.sigar.info.SigarSummary;


public class MemoryStatsBox extends Composite {

	private static MemStatsUiBinder uiBinder = GWT
			.create(MemStatsUiBinder.class);
	
	private static final long MB = 1024*1024;
	
	interface MemStatsUiBinder extends UiBinder<Widget, MemoryStatsBox> {
	}
	
	@UiField
	MemPie javaMem;
	
	@UiField
	MemPie systemMem;
	
	
	public MemoryStatsBox() {
		initWidget(uiBinder.createAndBindUi(this));
		systemMem.setDimension(400, 250);
		javaMem.setDimension(400, 250);
	}

	public void draw() {
		javaMem.draw();
		systemMem.draw();
	}

	public void setOsData(long osTotalMemory, long osUsedMemory) {
		systemMem.setData((osTotalMemory-osUsedMemory)/MB, osUsedMemory/MB);
	}


	public void setJavaData(long javaMemoryMax, long javaMemoryUsed) {
		javaMem.setData((javaMemoryMax-javaMemoryUsed)/MB, javaMemoryUsed/MB);
	}

	public void update(SigarSummary summary) {
		setOsData(summary.getOsTotalMemory(), summary.getOsUsedMemory());
		setJavaData(summary.getJavaMemoryMax(), summary.getJavaMemoryUsed());
		draw();
	}
	
}

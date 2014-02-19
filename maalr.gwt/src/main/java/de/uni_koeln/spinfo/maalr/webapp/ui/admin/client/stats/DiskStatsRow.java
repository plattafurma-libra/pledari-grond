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
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.common.shared.statistics.DiskStats;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.DiskPie;

public class DiskStatsRow extends Composite {

	private static DiskStatsUiBinder uiBinder = GWT
			.create(DiskStatsUiBinder.class);
	
	
	interface DiskStatsUiBinder extends UiBinder<Widget, DiskStatsRow> {
	}

	
	@UiField
	Label name;
	
	@UiField
	Label dev;
	
	@UiField
	Label free;
	
	@UiField
	Label used;
	
	@UiField
	Label rwBytes;
	
	@UiField
	Label rwTotal;
	
	@UiField
	DiskPie pie;

	private DiskStats stats;
	
	private long lastAvailable = 0;
	
	private static final NumberFormat nf = NumberFormat.getDecimalFormat();
	private static final NumberFormat nf2 = NumberFormat.getPercentFormat();
	private static final double MILLION = 1024*1024;
	private static final double BILLION = MILLION*1024;

	public DiskStatsRow(DiskStats stat) {
		initWidget(uiBinder.createAndBindUi(this));
		name.setText("Disk: " + stat.getDirName());
		dev.setText("Mount-Point: " + stat.getDevName());
		this.stats = stat;
	}

	public String getDevName() {
		return stats.getDevName();
	}

	public void update(DiskStats stat) {
		if(pie != null) {
			if(Math.abs(stat.getAvailable() - lastAvailable) > 1000) {
				pie.setData(stat.getAvailable(), stat.getUsed());
				pie.draw();
			}
		}
		lastAvailable = stat.getAvailable();
		free.setText("Free: " + nf.format(stat.getAvailable()/MILLION) + " GB (" + nf2.format(stat.getAvailablePercent()) + ")");
		used.setText("Used: " + nf.format(stat.getUsed()/MILLION) + " GB (" + nf2.format(stat.getUsedPercent()) + ")");
		rwBytes.setText("Reads: " + nf.format(stats.getReadBytes()/BILLION) + " GB, Writes: " + nf.format(stats.getWriteBytes()/BILLION) + " GB");
		rwTotal.setText("#Reads: " + nf.format(stats.getReads()) + ", #Writes: " + nf.format(stats.getWrites()));
	}
	
}

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

import de.uni_koeln.spinfo.maalr.sigar.info.NetStats;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;

public class NetStatsRow extends Composite {

	private static DiskStatsUiBinder uiBinder = GWT
			.create(DiskStatsUiBinder.class);
	
	
	interface DiskStatsUiBinder extends UiBinder<Widget, NetStatsRow> {
	}

	
	@UiField
	Label name;
	
	@UiField
	Label speed;
	
	@UiField
	Label out;
	
	@UiField
	Label outErrors;
	
	@UiField
	Label in;
	
	@UiField
	Label inErrors;
	
	@UiField
	NetBar bar;

	private String netName;

	
	
	
	private static final NumberFormat nf = NumberFormat.getDecimalFormat();
	private static final double DEC_MILLION = 1000*1000;
	private static final double TWO_MILLION = 1024*1024;

	public NetStatsRow(NetStats stat) {
		initWidget(uiBinder.createAndBindUi(this));
		name.setText("Interface: " + stat.getName());
		speed.setText("Speed: " + stat.getSpeedInBit()/DEC_MILLION + " MBit/sec");
		this.netName = stat.getName();
	}

	public String getName() {
		return netName;
	}

	public void update(NetStats stats) {
		if(bar != null) {
			try {
				int speedInMBit = (int) (stats.getSpeedInBit()/DEC_MILLION);
				bar.setData((int)(stats.getAverageRxInByte()*8/DEC_MILLION), (int)(stats.getAverageTxInByte()*8/DEC_MILLION), speedInMBit);
				bar.draw();
			} catch (Exception e) {
				Logger.getLogger(getClass()).info("Failure: " + e);
			}
		}
		out.setText("Out: " + nf.format(stats.getAverageTxInByte()/TWO_MILLION) + " MB/sec");
		outErrors.setText("Absolute: " + nf.format(stats.getTxInByte()/TWO_MILLION) + " MB");
		in.setText("In: " + nf.format(stats.getAverageRxInByte()/TWO_MILLION) + " MB/sec");
		inErrors.setText("Absolute: " + nf.format(stats.getRxInByte()/TWO_MILLION) + " MB");
	}
	
}

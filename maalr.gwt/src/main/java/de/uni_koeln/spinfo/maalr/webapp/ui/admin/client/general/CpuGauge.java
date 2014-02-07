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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.formatters.NumberFormat;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.Gauge.Options;

import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.stats.Colors;

public class CpuGauge extends Composite {

	private Gauge chart;
	
	private Options options;

	private DataTable data;
	
	private int oldCpus = 0;

	private NumberFormat nf;

	private double warnStart;

	public CpuGauge() {
		chart = new Gauge();
		data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Label");
		data.addColumn(ColumnType.NUMBER, "Value");
		data.addRows(1);
		data.setValue(0, 0, "");
		options = Gauge.Options.create();
		com.google.gwt.visualization.client.formatters.NumberFormat.Options loadOptions = NumberFormat.Options.create();
		loadOptions.setFractionDigits(2);
		nf = NumberFormat.create(loadOptions);
		initWidget(chart);
	}

	public void setNumberOfCPUs(int numberOfCPUs) {
		if(oldCpus != numberOfCPUs) {
			double d = numberOfCPUs;
			double greenEnd = d *0.65;
			double yellowEnd = d * 0.85;
			double dif = (yellowEnd - greenEnd)/3;
			warnStart = greenEnd + dif;
			options.setMinorTicks(5);
			options.setOption("max", (int)d);
			options.setGreenRange(0, (int) greenEnd);
			options.setYellowRange((int) greenEnd, (int) yellowEnd);
			options.setRedRange((int) yellowEnd, (int)(d*1.3));
			options.setOption("greenColor", Colors.DEFAULT_ONE);
			options.setOption("yellowColor", Colors.DEFAULT_TWO);
			options.setOption("redColor", Colors.DEFAULT_THREE);
			options.setMajorTicks("IDLE","FINE", "BUSY", "CRITICAL");
			oldCpus = numberOfCPUs;
		}
	}

	public void setLoadAverage(double d) {
		data.setValue(0, 1, d);
		nf.format(data, 1);
		if(d > warnStart) {
			options.setOption("greenColor", Colors.ERROR_ONE);
			options.setOption("yellowColor", Colors.ERROR_TWO);
			options.setOption("redColor", Colors.ERROR_FOUR);
		} else {
			options.setOption("greenColor", Colors.DEFAULT_ONE);
			options.setOption("yellowColor", Colors.DEFAULT_TWO);
			options.setOption("redColor", Colors.DEFAULT_THREE);
		}
	}

	public void draw() {
		chart.draw(data, options);
	}
	
	public void setDimension(int width, int height) {
		options.setWidth(width);
		options.setHeight(height);
	}

}

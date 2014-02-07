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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;

class MemPie extends Composite {
	
	private Options options;
	
	private DataTable data;

	private PieChart chart;
	
	private String[] defaultColors = {Colors.GOOD, Colors.DEFAULT_ONE};
	private String[] errorColors = {Colors.ERROR_ONE, Colors.ERROR_TWO};
	
	public MemPie() {
		options = Options.create();
	    options.set3D(true);
	    options.setOption("pieSliceText", "percentage");
	    options.setLegend(LegendPosition.BOTTOM);
	    options.setBackgroundColor("transparent");
	    options.setLegendBackgroundColor("transparent");
	    options.setColors(defaultColors);
		data = getMemoryTable();
		chart = new PieChart(data, options);
		initWidget(chart);
	}

	private DataTable getMemoryTable() {
		DataTable data = DataTable.create();
		double usedPercent = 30;
		double freePercent = 70;
	    data.addColumn(ColumnType.STRING, "Label");
	    data.addColumn(ColumnType.NUMBER, "MB");
	    data.addRows(2);
	    data.setValue(0, 0, "Used RAM (MB)");
	    data.setValue(0, 1, usedPercent);
	    data.setValue(1, 0, "Free RAM (MB)");
	    data.setValue(1, 1, freePercent);
	    return data;
	}
	
	public void setDimension(int width, int height) {
		options.setWidth(width);
		options.setHeight(height);
	}

	public void setData(long free, long used) {
		 data.setValue(0, 1, used);
		 data.setValue(1, 1, free);
		 double d = free+used;
		 d = free/d;
		 if(d < 0.15) {
			 options.setColors(errorColors);
		 } else {
			 options.setColors(defaultColors);
		 }
	}

	public void draw() {
		chart.draw(data, options);
	}
	
}

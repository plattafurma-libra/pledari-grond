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

class CpuProcPie extends Composite {
	
	private Options options;
	
	private DataTable data;

	private PieChart chart;
	
	private String[] defaultColors = {Colors.DEFAULT_THREE,Colors.GOOD,Colors.DEFAULT_ONE,Colors.DEFAULT_TWO};
	private String[] errorColors = {Colors.ERROR_ONE,Colors.ERROR_TWO,Colors.ERROR_THREE,Colors.ERROR_FOUR};
	
	public CpuProcPie() {
		options = Options.create();
	    options.set3D(true);
	    options.setOption("pieSliceText", "percentage");
	    options.setLegend(LegendPosition.BOTTOM);
	    options.setColors(defaultColors);
	    options.setBackgroundColor("transparent");
	    options.setLegendBackgroundColor("transparent");
	    data = DataTable.create();
	    data.addColumn(ColumnType.STRING, "Label");
	    data.addColumn(ColumnType.NUMBER, "percentage");
	    data.addRows(4);
	    data.setValue(0, 0, "System");
	    data.setValue(1, 0, "User");
	    data.setValue(2, 0, "Idle");
	    data.setValue(3, 0, "Nice");
		chart = new PieChart(data, options);
		initWidget(chart);
	}

	
	public void setTitle(String title) {
		options.setTitle(title);
	}
	
	public void setDimension(int width, int height) {
		options.setWidth(width);
		options.setHeight(height);
	}

	public void setData(double system, double user, double nice, double idle) {
		 data.setValue(0, 1, system);
		 data.setValue(1, 1, user);
		 data.setValue(2, 1, idle);
		 data.setValue(3, 1, nice);
		 double d = system + user + nice;
		 if(idle/(idle+d) < 0.1) {
			 options.setColors(errorColors);
		 } else {
			 options.setColors(defaultColors);
		 }
	}

	public void draw() {
		chart.draw(data, options);
	}
	
}

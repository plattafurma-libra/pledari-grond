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
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.PieChart.Options;

import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.stats.Colors;

public class DiskPie extends Composite {
	
	private Options options;
	
	private DataTable data;

	private PieChart chart;
	
	public PieChart getChart() {
		return chart;
	}

	public String getDevName() {
		return devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	private String devName, dirName;
	
	private String[] defaultColors = {Colors.DEFAULT_ONE, Colors.DEFAULT_TWO};
	private String[] errorColors = {Colors.ERROR_ONE, Colors.ERROR_TWO};
	
	private static final long MIN = 1024*1024 * 5;
	
	public DiskPie() {
		options = Options.create();
	    options.setOption("pieSliceText", "percentage");
	    options.setColors(defaultColors);
	    options.setLegend(LegendPosition.NONE);
	    options.setOption("isHtml", true);
	    options.setBackgroundColor("transparent");
	    options.setOption("isStacked", true);
	    options.setOption("tooltipFontSize", 22);
		options.setOption("tooltipWidth", 400);
		options.setOption("tooltipHeight", 200);
		data = getMemoryTable();
		chart = new PieChart(data, options);
		chart.setWidth("80px");
		chart.setHeight("80px");
		initWidget(chart);
	}

	private DataTable getMemoryTable() {
		DataTable data = DataTable.create();
		double usedPercent = 30;
		double freePercent = 70;
	    data.addColumn(ColumnType.STRING, "Label");
	    data.addColumn(ColumnType.NUMBER, "GB");
	    data.addRows(2);
	    data.setValue(0, 0, "Used Capacity (GB)");
	    data.setValue(0, 1, usedPercent);
	    data.setValue(1, 0, "Free Capacity (GB)");
	    data.setValue(1, 1, freePercent);
	    return data;
	}
	
	public void setTitle(String title) {
		options.setTitle(title);
	}
	
	public void setDimension(int width, int height) {
		options.setWidth(width);
		options.setHeight(height);
	}

	public void setData(long free, long used) {
		 data.setValue(0, 1, used);
		 data.setValue(1, 1, free);
		 double d = free + used;
		 d = free/d;
		 if(free < MIN || d < 0.02) {
			 options.setColors(errorColors);
		 } else {
			 options.setColors(defaultColors);
		 }
	}

	public void draw() {
		chart.draw(data, options);
	}
	
}

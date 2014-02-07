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
import com.google.gwt.visualization.client.visualizations.BarChart;
import com.google.gwt.visualization.client.visualizations.BarChart.Options;

import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;

class NetBar extends Composite {

	private Options options;

	private DataTable data;

	private BarChart chart;

	public NetBar() {
		chart = new BarChart();
		options = Options.create();
		options.setHeight(80);
		options.setWidth(200);
		options.setColors("#585858", "#000000");
		options.setLegend(LegendPosition.TOP);
		options.setBackgroundColor("transparent");
		options.setLegendBackgroundColor("transparent");
		//options.setAxisColor("black");
		data = DataTable.create();
		data.addColumn(ColumnType.STRING, "");
		data.addColumn(ColumnType.NUMBER, "Incoming");
		data.addColumn(ColumnType.NUMBER, "Outgoing");
		data.addRows(1);
		initWidget(chart);
	}


	public void setTitle(String title) {
		options.setTitle(title);
	}

	public void setDimension(int width, int height) {
		options.setWidth(width);
		options.setHeight(height);
	}

	public void setData(int tx, int rx, int max) {
		data.setValue(0, 0, "");
		data.setValue(0, 1, tx);
		data.setValue(0, 2, rx);
		options.setMax(max);
	}
	

	public void draw() {
		try {
			chart.draw(data, options);
		} catch (Exception e) {
			Logger.getLogger(getClass()).info("FailedDrawing: " + e);
		}
	}

}

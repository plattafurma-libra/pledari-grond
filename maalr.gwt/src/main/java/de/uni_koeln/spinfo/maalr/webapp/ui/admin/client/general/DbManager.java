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

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Container;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.BarChart;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.PieChart;

import de.uni_koeln.spinfo.maalr.common.shared.statistics.SystemSummary;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.common.help.HelpBox;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.backupsettings.BackupSettings;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.dbsettings.DatabaseSettings;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.indexsettings.IndexSettings;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.stats.CpuStatsBox;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.stats.DiskStatsBox;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.stats.MemoryStatsBox;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.stats.NetStatsBox;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;

public class DbManager extends Composite {

	private static RoleEditorUiBinder uiBinder = GWT.create(RoleEditorUiBinder.class);
	

	interface RoleEditorUiBinder extends UiBinder<Widget, DbManager> {
	}
	
	@UiField
	DatabaseSettings dbSettings;
	
	@UiField
	IndexSettings indexSettings;
	
	@UiField
	BackupSettings backupSettings;
	
	@UiField
	HelpBox helpBox;
	
//	@UiField
//	VerticalPanel charts;
	
	// private MemoryStatsBox memStats;
	
	// private DiskStatsBox diskStats;
	
	// private CpuStatsBox cpuStats;
	
	// private NetStatsBox netStats;
	
	private BackendServiceAsync service;
	
//	@UiField
//	Column chartsColumn;
	
	@UiField
	Column settingsColumn;
	
	@UiField
	Container container;
	
	public DbManager() {
		try {
			initWidget(uiBinder.createAndBindUi(this));
			helpBox.setHelpText("Backup or import a dictionary. Note that you cannot undo or cancel any operation.");
			service = GWT.create(BackendService.class);
			//initVisualization();
		} catch (Exception e) {
			Window.alert("Failed to init db mgr: " + e);
		}
	}
	
//	private void initVisualization() {
//		service.getSystemSummary(new AsyncCallback<SystemSummary>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				hideSystemSummary();
//			}
//
//			@Override
//			public void onSuccess(SystemSummary result) {
//				if(result == null) {
//					hideSystemSummary();
//				} else {
//					 // Create a callback to be called when the visualization API
//				    // has been loaded.
//				    Runnable onLoadCallback = new Runnable() {
//				      public void run() {
//				    	 initializeCharts();
//				      }
//
//				    };
//				    // Load the visualization api, passing the onLoadCallback to be called
//				    // when loading is done.
//				    VisualizationUtils.loadVisualizationApi(onLoadCallback, PieChart.PACKAGE, Gauge.PACKAGE, BarChart.PACKAGE);
//				}
//			}
//
//			private void hideSystemSummary() {
//				chartsColumn.setVisible(false);
//				settingsColumn.setSize(12);
//			}
//		});
//	}
	
//	private void initializeCharts() {
//		container.setWidth("99%");
//		initializeCpuCharts();
//		initializeMemCharts();
//		initializeNetCharts();
//		initializeDiskCharts();
//		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
//			
//			private boolean shouldContinue = true;
//			
//			@Override
//			public boolean execute() {
//				service.getSystemSummary(new AsyncCallback<SystemSummary>() {
//					
//					@Override
//					public void onSuccess(SystemSummary result) {
//						if(result == null) {
//							shouldContinue = false;
//						}
//						updateCharts(result);
//					}
//
//					@Override
//					public void onFailure(Throwable caught) {
//						shouldContinue = false;
//					}
//				});
//				return shouldContinue;
//			}
//		}, 1000);
//	}

//	private void initializeMemCharts() {
//		memStats = new MemoryStatsBox();
//		charts.add(memStats);
//	}

//	private void initializeDiskCharts() {
//		diskStats = new DiskStatsBox();
//		charts.add(diskStats);
//	}
	
	
//	private void initializeNetCharts() {
//		netStats = new NetStatsBox();
//		charts.add(netStats);
//	}

//	private void initializeCpuCharts() {
//		try {
//			cpuStats = new CpuStatsBox();
//			charts.add(cpuStats);
//		} catch (Exception e) {
//			Window.alert("Failed to init" + e);
//		}
//	}

//	private void updateCharts(SystemSummary summary) {
//		if(memStats != null) {
//			memStats.update(summary);
//		}
//		if(cpuStats != null) {
//			cpuStats.update(summary);
//		}
//		if(diskStats != null) {
//			diskStats.update(summary);	
//		}
//		try {
//			if(netStats != null) {
//			    netStats.update(summary);
//			}
//			
//		} catch (Exception e) {
//			Logger.getLogger(getClass()).info("Error while updating: " + e);
//		}
//	}

	
	
	
}

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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.indexsettings;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.base.ProgressBarBase.Style;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendService;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Dialog;

/**
 * Filter-Composite with provides filter options to query for
 * users by text (email, login, firstname, lastname) and/or
 * role. The layout of this {@link Composite} is defined in
 * the corresponding ui.xml-file.
 * 
 * @author sschwieb
 *
 */
public class IndexSettings extends Composite {

	private static IndexSettingsUiBinder uiBinder = GWT
			.create(IndexSettingsUiBinder.class);
	
	@UiField
	Button reload;
	
	@UiField
	Paragraph indexInfos;
	
	@UiField
	Button updateStats;
	
	@UiField
	ProgressBar busyIndicator;
	
	@UiField
	ButtonGroup leftGroup;
	
	@UiField
	ButtonGroup rightGroup;

	private BackendServiceAsync service;

	private NumberFormat nf;
	
	interface IndexSettingsUiBinder extends UiBinder<Widget, IndexSettings> {
	}
	
	public IndexSettings() {
		service = GWT.create(BackendService.class);
		nf = NumberFormat.getDecimalFormat();
		initWidget(uiBinder.createAndBindUi(this));
		leftGroup.getElement().getStyle().setFloat(Float.LEFT);
		rightGroup.getElement().getStyle().setFloat(Float.RIGHT);
		busyIndicator.setPercent(100);
		busyIndicator.setType(Style.ANIMATED);
		initHandlers();
		updateIndexStatistics();
	}
	
	private void updateIndexStatistics() {
		service.getIndexStats(new AsyncCallback<IndexStatistics>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(IndexStatistics result) {
				indexInfos.clear();
				indexInfos.add(new Paragraph("Indexed: " + nf.format(result.getNumberOfEntries())));
				indexInfos.add(new Paragraph("Approved: " + nf.format(result.getApprovedEntries())));
				indexInfos.add(new Paragraph("Unverified: " + nf.format(result.getUnverifiedEntries())));
				indexInfos.add(new Paragraph("Other: " + nf.format(result.getUnknown())));
			}
		});
	}

	/**
	 * Initializes handlers for input fields
	 * and buttons.
	 */
	private void initHandlers() {
		final AsyncCallback<String> resultCallback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				busyIndicator.setVisible(false);
				Dialog.showError("Operation failed", caught);
			}

			@Override
			public void onSuccess(String result) {
				updateIndexStatistics();
				busyIndicator.setVisible(false);
			}
		};
		updateStats.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				updateIndexStatistics();
			}
		});
		reload.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Command ok = new Command() {
					
					@Override
					public void execute() {
						busyIndicator.setVisible(true);
						service.rebuildIndex(resultCallback);
					}
				};
				Dialog.confirm("Rebuild Index", "This will rebuild the search index. Are you sure?", "OK", "Cancel", ok, null, false);
			}
		});
	}
	
	}

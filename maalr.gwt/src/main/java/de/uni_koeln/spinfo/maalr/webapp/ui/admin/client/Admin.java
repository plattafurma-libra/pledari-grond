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
package de.uni_koeln.spinfo.maalr.webapp.ui.admin.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import de.uni_koeln.spinfo.maalr.common.shared.ClientOptions;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.DbManager;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.user.RoleEditor;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonService;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.CommonServiceAsync;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.client.Navigation;
import de.uni_koeln.spinfo.maalr.webapp.ui.common.shared.util.Logger;


public class Admin implements EntryPoint {

	private SimplePanel panel;
	private Map<String, Composite> modules = new HashMap<String, Composite>();
	private static ClientOptions options;

	public void showModule(Composite module) {
		panel.setWidget(module);
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final RootPanel panel = RootPanel.get("navigation");
		panel.clear();
		final Navigation navigation = new Navigation();
		CommonServiceAsync service = GWT.create(CommonService.class);
		service.getClientOptions(new AsyncCallback<ClientOptions>() {
			
			@Override
			public void onSuccess(ClientOptions result) {
				options = result;
				navigation.setAppName(options.getShortAppName() + " - Admin Backend");
				panel.add(navigation);
				initializeMainPanel();
				initHistory();
				initModules(navigation);
				Logger.getLogger(getClass()).info("Admin module has been loaded!!!");
				if(History.getToken().isEmpty()) {
					History.newItem(Modules.ANCHOR_DB_MANAGER);
				}
				History.fireCurrentHistoryState();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	
	private void initializeMainPanel() {
		RootLayoutPanel rootPanel = RootLayoutPanel.get();
		rootPanel.getElement().getStyle().setTop(5, Unit.EM);
		rootPanel.getElement().getStyle().setBottom(2, Unit.EM);
		panel = new SimplePanel();
		panel.getElement().getStyle().setOverflow(Overflow.SCROLL);
		rootPanel.add(panel);
	}

	private void initModules(Navigation navigation) {
		RoleEditor roleEditor = new RoleEditor();
		DbManager dbManager = new DbManager();
		registerModule(roleEditor, Modules.ANCHOR_ROLE_MANAGER);
		registerModule(dbManager, Modules.ANCHOR_DB_MANAGER);
		navigation.addLinkLeft("Role Manager", "#" + Modules.ANCHOR_ROLE_MANAGER);
		navigation.addLinkLeft("DB Manager", "#" + Modules.ANCHOR_DB_MANAGER);
		navigation.addLinkLeft("Logout", "/rumantschgrischun/j_spring_security_logout");
	}

	private void initHistory() {
		 History.addValueChangeHandler(new ValueChangeHandler<String>() {
		      public void onValueChange(ValueChangeEvent<String> event) {
		        String historyToken = event.getValue();
		        if(historyToken.trim().length() == 0) {
		        	showModule(modules.get(Modules.ANCHOR_DB_MANAGER));
		        } else {
		        	showModule(modules.get(historyToken));
		        }
		      }
		    });
	}

	private void registerModule(Composite composite, String key) {
		modules.put(key, composite);
	}


}

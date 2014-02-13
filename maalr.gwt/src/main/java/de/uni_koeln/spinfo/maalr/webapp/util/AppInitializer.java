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
package de.uni_koeln.spinfo.maalr.webapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.DictionaryConfiguration;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.ColumnSelector;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.ColumnSelectorOption;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryBuilder;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryBuilderOption;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.QueryKey;
import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiField;
import de.uni_koeln.spinfo.maalr.configuration.Environment;
import de.uni_koeln.spinfo.maalr.login.LoginManager;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.lucene.query.MaalrQueryFormatter;
import de.uni_koeln.spinfo.maalr.lucene.stats.IndexStatistics;
import de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics;
import de.uni_koeln.spinfo.maalr.mongo.util.BackUpHelper;
import de.uni_koeln.spinfo.maalr.webapp.ui.admin.client.general.BackendService;

@Service
public class AppInitializer {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Environment environment;
	
	@Autowired
	private BackendService adminController;
	
	@Autowired
	private LoginManager loginManager;
	
	@Autowired
	private UserInfoBackend userBackend;
	
	@Autowired
	private Index index;

	@PostConstruct
	public void postConstruct() throws Exception  {
		String shouldImport = System.getProperty("maalr.import");
		if(shouldImport != null) {
			logger.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			logger.warn("Importing Data...");
			try {
				loginManager.login("admin", "admin");
				adminController.importDatabase(20000);	
				MaalrUserInfo editor = new MaalrUserInfo("editor", Role.TRUSTED_IN_4);
				userBackend.insert(editor);
			} finally {
				loginManager.logout();
			}
			
			logger.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		
		// SCHEDULED BACKUP
		logger.info("STARTING SCHEDULED BACKUP...");
		String dir = Configuration.getInstance().getBackupLocation();
		int nums = Integer.parseInt(Configuration.getInstance().getBackupNums());
		String time = Configuration.getInstance().getTriggerTime();
		BackUpHelper.getInstance().setBackup(BackUpHelper.Period.DAILY, time, dir, nums, false);
		// END SCHEDULED BACKUP
		
		configureSearchUi();
		MaalrQueryFormatter.setUiConfiguration(Configuration.getInstance().getUserDefaultSearchUiConfig());
		IndexStatistics statistics = index.getIndexStatistics();
		DictionaryStatistics.initialize(statistics.getUnverifiedEntries(), statistics.getApprovedEntries(), statistics.getLastUpdated(), statistics.getOverlayCount());
	}

	private void configureSearchUi() {
		DictionaryConfiguration dictionaryConfig = Configuration.getInstance().getDictionaryConfig();
		ArrayList<String> mainFields = new ArrayList<String>();
		List<QueryKey> queryKeys = dictionaryConfig.getQueryKeys();
		for (QueryKey key : queryKeys) {
			mainFields.add(key.getId());
		}
		List<ColumnSelector> fcList = dictionaryConfig.getColumnSelectors();
		Map<String, ColumnSelector> columnSelectors = new HashMap<String, ColumnSelector>();
		for (ColumnSelector choice : fcList) {
			columnSelectors.put(choice.getId(), choice);
		}
//		List<FieldValueChoice> fcvList = dictionaryConfig.getFieldValueChoices();
//		Map<String, FieldValueChoice> fieldValueChoices = new HashMap<String, FieldValueChoice>();
//		for (FieldValueChoice choice : fcvList) {
//			fieldValueChoices.put(choice.getId(), choice);
//		}
		List<QueryBuilder> qmList = dictionaryConfig.getQueryModifier();
		Map<String, QueryBuilder> queryModifiers = new HashMap<String, QueryBuilder>(); 
		for (QueryBuilder modifier : qmList) {
			queryModifiers.put(modifier.getId(), modifier);
		}
		UiConfiguration[] configs = Configuration.getInstance().getUIConfigurations();
		for (UiConfiguration uiConfig : configs) {
			if(uiConfig != null) {
				initialize(mainFields, columnSelectors, queryModifiers,
						uiConfig);
			}
		}
	}

	private void initialize(ArrayList<String> mainFields,
			Map<String, ColumnSelector> fieldChoices,
			Map<String, QueryBuilder> queryModifiers, UiConfiguration uiConfig) {
		uiConfig.setMainFields(mainFields);
		List<UiField> fields = uiConfig.getFields();
		for (UiField field : fields) {
			if(field.isBuildIn()) {
				setBuildinDefaults(field);
				continue;
			}
			ColumnSelector choice = fieldChoices.get(field.getId());
			if(choice != null) {
				ArrayList<String> values = new ArrayList<String>();
				field.setValues(values);
				List<ColumnSelectorOption> options = choice.getOptions();
				for(int i = 0; i < options.size(); i++) {
					ColumnSelectorOption option = options.get(i);
					values.add(option.getId());
					if(option.isDefault()) {
						field.setInitialValue(i);
					}
				}
				continue;
			}
//			FieldValueChoice valueChoice = fieldValueChoices.get(field.getId());
//			if(valueChoice != null) {
//				ArrayList<String> values = new ArrayList<String>();
//				field.setValues(values);
//				List<FieldValueChoiceOption> options = valueChoice.getOptions();
//				for(int i = 0; i < options.size(); i++) {
//					FieldValueChoiceOption option = options.get(i);
//					values.add(option.getId());
//					// TODO: Implement... grab and return values from db
////					if(option.isDefault()) {
////						field.setInitialValue(i);
////					}
//				}
//				continue;
//			}
			QueryBuilder queryModifier = queryModifiers.get(field.getId());
			if(queryModifier != null) {
				ArrayList<String> values = new ArrayList<String>();
				field.setValues(values);
				List<QueryBuilderOption> options = queryModifier.getOptions();
				for(int i = 0; i < options.size(); i++) {
					QueryBuilderOption option = options.get(i);
					values.add(option.getId());
					if(option.isDefault()) {
						field.setInitialValue(i);
					}
				}
				continue;
			}
			
		}
	}

	private void setBuildinDefaults(UiField field) {
		if("pageSize".equals(field.getId())) {
			ArrayList<String> sizes = new ArrayList<String>();
			sizes.add("15");
			sizes.add("25");
			sizes.add("50");
			sizes.add("100");
			sizes.add("200");
			field.setValues(sizes);
			field.setInitialValue(0);
		}
		if("highlight".equals(field.getId())) {
			ArrayList<String> list = new ArrayList<String>();
			list.add("false");
			field.setValues(list);
		}
		if("suggestions".equals(field.getId())) {
			ArrayList<String> list = new ArrayList<String>();
			list.add("false");
			field.setValues(list);
		}
	}

}

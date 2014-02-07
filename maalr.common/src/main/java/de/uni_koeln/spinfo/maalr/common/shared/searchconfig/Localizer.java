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
package de.uni_koeln.spinfo.maalr.common.shared.searchconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Localizer {

	private static Map<String, Properties> translations = new HashMap<String, Properties>();
	private static HashMap<String, Properties> editorTranslations = new HashMap<String, Properties>();
	
	public static UiConfiguration localize(UiConfiguration uiConfiguration, String locale) {
		Properties translation = translations.get(locale);
		Logger logger = LoggerFactory.getLogger(Localizer.class);
		if(translation == null) {
			translation = new Properties();
			try {
				if(locale == null) locale = "";
				File file = new File("maalr_config/i18n/user-searchui_" + locale + ".properties");
				if(!file.exists()) {
					file = new File("maalr_config/i18n/user-searchui.properties");
				}
				logger.info("Loading strings from file " + file + "...");
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
				translation.load(br);
				br.close();
			} catch (IOException e) {
				logger.error("Failed to load ui config for locale " + locale + ", returning default", e);
				return uiConfiguration;
			}
			translations.put(locale, translation);
		}
		UiConfiguration localized = new UiConfiguration();
		List<UiField> fields = uiConfiguration.getFields();
		List<UiField> localizedFields = new ArrayList<UiField>();
		for (UiField field : fields) {
			UiField copy = localizeField(translation, field);
			localizedFields.add(copy);
		}
		localized.setFields(localizedFields);
		if(uiConfiguration.getMainFields() != null) {
			localized.setMainFields(new ArrayList<String>(uiConfiguration.getMainFields()));
		}
		localized.setMoreLabel(translation.getProperty("more_options"));
		localized.setLessLabel(translation.getProperty("less_options"));
		return localized;
	}

	private static UiField localizeField(Properties translation, UiField field) {
		UiField copy = new UiField();
		copy.setBuildIn(field.isBuildIn());
		copy.setId(field.getId());
		copy.setType(field.getType());
		copy.setLabel(translation.getProperty(field.getId()));
		copy.setHasSubmit(field.hasSubmitButton());
		if(field.hasSubmitButton()) {
			copy.setSubmitLabel(translation.getProperty(field.getId()+"_submit"));
		}
		if(field.getValues() != null) {
			copy.setValues(new ArrayList<String>());
			copy.getValues().addAll(field.getValues());
			copy.setValueLabels(new ArrayList<String>());
			for (String valueId : field.getValues()) {
				String valueName = translation.getProperty(valueId);
				if(valueName == null) valueName = valueId;
				copy.getValueLabels().add(valueName);
			}
		}
		copy.setInitialValue(field.getInitialValueIndex());
		return copy;
	}

	public static String getTranslation(String locale, String key) {
		HashMap<String, String> map = getEditorTranslations(locale);
		String toReturn = map.get(key);
		if(toReturn == null) {
			return "???" + key + "???";
		}
		return toReturn;
	}
	
	public static HashMap<String, String> getEditorTranslations(String locale) {
		Logger logger = LoggerFactory.getLogger(Localizer.class);
//		logger.info("Requesting translated editor strings for locale " + locale);
		Properties properties = editorTranslations.get(locale);
		if(properties == null) {
			properties = new Properties();
			try {
				String fileName = "maalr_config/i18n/lemma-description_" + locale + ".properties";
				File file = new File(fileName);
				if(!file.exists()) {
					file = new File("maalr_config/i18n/lemma-description.properties");
				}
				logger.info("Loading strings from file " + file);
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				properties.load(br);
				br.close();
				editorTranslations.put(locale, properties);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HashMap<String, String> map = new HashMap<String, String>();
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			map.put((String) entry.getKey(), (String)entry.getValue());
		}
//		logger.info("Returning translation: " + map);
		return map;
	}

}

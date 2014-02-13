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
package de.uni_koeln.spinfo.maalr.common.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.uni_koeln.spinfo.maalr.common.server.searchconfig.DictionaryConfiguration;
import de.uni_koeln.spinfo.maalr.common.server.searchconfig.DictionaryConfiguration.UiConfigurations;
import de.uni_koeln.spinfo.maalr.common.shared.ClientOptions;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;

public class Configuration {
	
	private static final String LUCENE_DIR = "lucene.dir";

	private static final String LEX_FILE = "lex.file";

	private static final String MONGODB_PORT = "mongodb.port";
	
	private static final String MONGODB_HOST="mongodb.host";
	
	private static final String LONG_NAME="maalr.long.name";
	
	private static final String SHORT_NAME="maalr.short.name";
	
	private static final String BACKUP_LOCATION = "backup.location";
	
	private static final String BACKUP_TRIGGER_TIME = "backup.trigger.time";
	
	private static final String BACKUP_NUMS = "backup.nums";
	
	
	private Properties properties;
	
	private static Configuration instance;
	
	private ClientOptions clientOptions;

	private DictionaryConfiguration dictConfig;

	private Configuration() throws IOException {
		properties = new Properties();
		InputStreamReader input = null;
		try {
			input = new InputStreamReader(new FileInputStream("maalr_config/maalr.properties"), "UTF-8");
			properties.load(input);
			clientOptions = new ClientOptions();
			clientOptions.setShortAppName(getShortName());
			clientOptions.setLongAppName(getLongName());
		} catch (IOException e) {
			throw e;
		} finally {
			if(input != null) {
				input.close();	
			}
		}
		try {
			JAXBContext ctx = JAXBContext.newInstance(DictionaryConfiguration.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			InputStreamReader reader = new InputStreamReader(new FileInputStream(new File("maalr_config/searchconfig.xml")), "UTF-8");
			dictConfig = (DictionaryConfiguration) unmarshaller.unmarshal(reader);
			reader.close();
		} catch (JAXBException e) {
			throw new IOException("Failed to parse search configuration files", e);
		}
	}
	
	private static UiConfiguration getConfiguration(String path) throws JAXBException, IOException {
		JAXBContext ctx = JAXBContext.newInstance(UiConfiguration.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(path)), "UTF-8");
		UiConfiguration config = (UiConfiguration) unmarshaller.unmarshal(reader);
		reader.close();
		return config;
	}
	
	public synchronized static Configuration getInstance() {
		if(instance == null) {
			try {
				instance = new Configuration();
			} catch (IOException e) {
				throw new RuntimeException("Failed to initialize configuration", e);
			}
		}
		return instance;
	}
	
	public String getLuceneDir() {
		return properties.getProperty(LUCENE_DIR);
	}

	/**
	 * @deprecated
	 */
	public String getLexFile() {
		return properties.getProperty(LEX_FILE);
	}

	public int getMongoPort() {
		return Integer.parseInt(properties.getProperty(MONGODB_PORT));
	}


	public String getMongoDBHost() {
		return properties.getProperty(MONGODB_HOST);
	}

	public void setLuceneDir(String dir) {
		properties.put(LUCENE_DIR, dir);
	}
	
	public LemmaDescription getLemmaDescription() {
		return dictConfig.getLemmaDescription();
	}
	
	public String getShortName() {
		return properties.getProperty(SHORT_NAME);
	}
	
	public String getLongName() {
		return properties.getProperty(LONG_NAME);
	}

	public ClientOptions getClientOptions() {
		return clientOptions;
	}

	public DictionaryConfiguration getDictionaryConfig() {
		return dictConfig;
	}

	public String getBackupLocation() {
		return properties.getProperty(BACKUP_LOCATION);
	}
	
	public String getTriggerTime() {
		return properties.getProperty(BACKUP_TRIGGER_TIME);
	}
	
	public String getBackupNums() {
		return properties.getProperty(BACKUP_NUMS);
	}
	
	public UiConfiguration getEditorDefaultSearchUiConfig() {
		UiConfigurations uiConfigs = dictConfig.getUiConfigurations();
		if(uiConfigs == null) return null;
		return uiConfigs.getEditorDefaultUiConfiguration();
	}

	public UiConfiguration getEditorExtendedSearchUiConfig() {
		UiConfigurations uiConfigs = dictConfig.getUiConfigurations();
		if(uiConfigs == null) return null;
		return uiConfigs.getEditorAdvancedUiConfiguration();
	}
	
	public UiConfiguration getUserDefaultSearchUiConfig() {
		UiConfigurations uiConfigs = dictConfig.getUiConfigurations();
		if(uiConfigs == null) return null;
		return uiConfigs.getUserDefaultUiConfiguration();
	}
	public UiConfiguration getUserExtendedSearchUiConfig() {
		UiConfigurations uiConfigs = dictConfig.getUiConfigurations();
		if(uiConfigs == null) return null;
		return uiConfigs.getUserAdvancedUiConfiguration();
	}

	public UiConfiguration[] getUIConfigurations() {
		return new UiConfiguration[] {getUserDefaultSearchUiConfig(), getUserExtendedSearchUiConfig(), getEditorDefaultSearchUiConfig(), getEditorExtendedSearchUiConfig()};
	}
	
	public String getFaceBookClientId() {
		return properties.getProperty("facebook.consumerKey");
	}

	public String getFaceBookClientSecret() {
		return properties.getProperty("facebook.consumerSecret");
	}

	public String getTwitterClientId() {
		return properties.getProperty("twitter.consumerKey");
	}

	public String getTwitterClientSecret() {
		return properties.getProperty("twitter.consumerSecret");
	}

	public String getRedirectUrl() {
		return properties.getProperty("social.redirect.page");
	}

}

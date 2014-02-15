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
package de.uni_koeln.spinfo.maalr.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationObjectSupport;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.lucene.util.LuceneConfiguration;


@Service
public class Environment extends WebApplicationObjectSupport {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Configuration configuration;

	private String build;

	private String version;

	private String name;
	
	private AppProperties appProperties;

	private LuceneConfiguration luceneConfig;
	
	public Environment() {
		build = "Unknown";
		version = "Unknown";
		configuration = Configuration.getInstance();
		/*
		 * Load application properties, which contain build and version
		 * number
		 */
		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			logger.error("Failed to read application properties!", e);
		}
		build = (String) props.get("application.build");
		version = (String) props.get("application.version");
		name = (String) props.getProperty("application.name");
		
		appProperties = new AppProperties();
		appProperties.setAppName(name);
		appProperties.setAppVersion(version);
		appProperties.setAppBuild(build);
		
		configuration.getLemmaDescription();
		luceneConfig = new LuceneConfiguration();
		luceneConfig.setBaseDirectory(configuration.getLuceneDir());
		logger.info("**********************************************************************************");
		logger.info("Initializing " + appProperties.getAppName() + " " + appProperties.getAppVersion() + " " + appProperties.getAppBuild());
		logger.info("**********************************************************************************");
	}

	public LuceneConfiguration getLuceneConfig() {
		return luceneConfig;
	}

	public void setLuceneConfig(LuceneConfiguration luceneConfig) {
		this.luceneConfig = luceneConfig;
	}

	public File getLexFile() {
		return  new File(configuration.getLexFile());
	}

	public Configuration getAppConfiguration() {
		return configuration;
	}

}

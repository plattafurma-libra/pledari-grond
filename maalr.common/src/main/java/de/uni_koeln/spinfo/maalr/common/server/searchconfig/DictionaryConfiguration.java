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
package de.uni_koeln.spinfo.maalr.common.server.searchconfig;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DictionaryConfiguration {

	@XmlElementWrapper(name="databaseColumns")
	@XmlElement(name="column")
	private List<IndexedColumn> indexedColumns = new ArrayList<IndexedColumn>();
	
	@XmlElementWrapper(name="columnSelectors")
	@XmlElement(name="columnSelector")
	private List<ColumnSelector> fieldChoices = new ArrayList<ColumnSelector>();

	@XmlElementWrapper(name="queryBuilders")
	@XmlElement(name="queryBuilder")
	private List<QueryBuilder> queryBuilders = new ArrayList<QueryBuilder>();

	@XmlElementWrapper(name="queryKeys")
	@XmlElement(name="queryKey")
	private List<QueryKey> queryKeys = new ArrayList<QueryKey>();
	
	@XmlElement
	private UiConfigurations uiConfigurations;
	
	public UiConfigurations getUiConfigurations() {
		return uiConfigurations;
	}

	public static class UiConfigurations {
		
		@XmlElement(name="defaultUserConfiguration")
		private UiConfiguration userDefaultUiConfiguration;
		
		@XmlElement(name="advancedUserConfiguration")
		private UiConfiguration userAdvancedUiConfiguration;
		
		@XmlElement(name="defaultEditorConfiguration")
		private UiConfiguration editorDefaultUiConfiguration;
		
		@XmlElement(name="advancedEditorConfiguration")
		private UiConfiguration editorAdvancedUiConfiguration;

		public UiConfiguration getUserDefaultUiConfiguration() {
			return userDefaultUiConfiguration;
		}

		public UiConfiguration getUserAdvancedUiConfiguration() {
			return userAdvancedUiConfiguration;
		}

		public UiConfiguration getEditorDefaultUiConfiguration() {
			return editorDefaultUiConfiguration;
		}

		public UiConfiguration getEditorAdvancedUiConfiguration() {
			return editorAdvancedUiConfiguration;
		}
		
		
		
	}

	@XmlElement
	private LemmaDescription lemmaDescription;
	
	public LemmaDescription getLemmaDescription() {
		return lemmaDescription;
	}

	public List<QueryBuilder> getQueryModifier() {
		return queryBuilders;
	}

	public List<ColumnSelector> getColumnSelectors() {
		return fieldChoices;
	}

	public List<QueryKey> getQueryKeys() {
		return queryKeys;
	}

	public List<IndexedColumn> getIndexedColumns() {
		return indexedColumns;
	}
	
}

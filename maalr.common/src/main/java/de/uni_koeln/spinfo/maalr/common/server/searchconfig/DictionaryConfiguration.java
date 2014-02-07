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

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DictionaryConfiguration {

	private IndexConfiguration indexConfiguration;
	
	@XmlElementWrapper(name="fieldChoices")
	@XmlElement(name="fieldChoice")
	private List<FieldChoice> fieldChoices = new ArrayList<FieldChoice>();

	@XmlElementWrapper(name="fieldValueChoices")
	@XmlElement(name="fieldValueChoice")
	private List<FieldValueChoice> fieldValueChoices = new ArrayList<FieldValueChoice>();
	
	@XmlElementWrapper(name="queryModifiers")
	@XmlElement(name="queryModifier")
	private List<QueryModifier> queryModifier = new ArrayList<QueryModifier>();
	
	public List<FieldValueChoice> getFieldValueChoices() {
		return fieldValueChoices;
	}

	public void setFieldValueChoices(List<FieldValueChoice> fieldValueChoices) {
		this.fieldValueChoices = fieldValueChoices;
	}

	public List<QueryModifier> getQueryModifier() {
		return queryModifier;
	}

	public void setQueryModifier(List<QueryModifier> queryModifier) {
		this.queryModifier = queryModifier;
	}

	public IndexConfiguration getIndexConfiguration() {
		return indexConfiguration;
	}

	public void setIndexConfiguration(IndexConfiguration indexConfiguration) {
		this.indexConfiguration = indexConfiguration;
	}

	public List<FieldChoice> getFieldChoices() {
		return fieldChoices;
	}

	public void setFieldChoices(List<FieldChoice> fieldChoices) {
		this.fieldChoices = fieldChoices;
	}
	

	public List<TextField> getTextFields() {
		return textFields;
	}

	public void setTextFields(List<TextField> textFields) {
		this.textFields = textFields;
	}

	@XmlElementWrapper(name="textFields")
	@XmlElement(name="textField")
	private List<TextField> textFields = new ArrayList<TextField>();

	@XmlElement(name="exactModifier")
	private String exactModifier;
	
	public String getExactModifier() {
		return exactModifier;
	}

	
}

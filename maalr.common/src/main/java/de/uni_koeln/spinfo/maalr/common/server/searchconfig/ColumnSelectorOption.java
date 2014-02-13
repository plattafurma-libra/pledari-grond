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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This class represents an "option"-element within a "fieldchoice"-element
 * in the search configuration.
 * @author sschwieb
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ColumnSelectorOption {

	@XmlAttribute
	private String id;
	
	@XmlElementWrapper(name="columns")
	@XmlElement(name="column")
	private List<ColumnReference> columnReferences = new ArrayList<ColumnReference>();
	
	@XmlAttribute(required=false, name="default")
	private boolean isDefault;
	
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ColumnReference> getColumnReferences() {
		return columnReferences;
	}

	public void setFields(List<ColumnReference> fields) {
		this.columnReferences = fields;
	}

	@Override
	public String toString() {
		return "ColumnSelectorOption [id=" + id + ", columnReferences="
				+ columnReferences + ", isDefault=" + isDefault + "]";
	}
	
	

}

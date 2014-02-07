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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="uiconfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class UiConfiguration implements Serializable {
	
	private static final long serialVersionUID = 7665690879609977840L;
	
	@XmlElement(name="uifield")
	private List<UiField> fields;
	
	private String moreLabel, lessLabel;
	
	private ArrayList<String> mainFields;

	public String getMoreLabel() {
		return moreLabel;
	}

	public void setMoreLabel(String moreLabel) {
		this.moreLabel = moreLabel;
	}

	public String getLessLabel() {
		return lessLabel;
	}

	public void setLessLabel(String lessLabel) {
		this.lessLabel = lessLabel;
	}

	public List<UiField> getFields() {
		return fields;
	}

	public void setFields(List<UiField> fields) {
		this.fields = fields;
	}

	public void setMainFields(ArrayList<String> mainFields) {
		this.mainFields = mainFields;
	}

	public ArrayList<String> getMainFields() {
		return mainFields;
	}

	@Override
	public String toString() {
		return "UiConfiguration [fields=" + fields + ", mainFields="
				+ mainFields + "]";
	}
	
	
}
	

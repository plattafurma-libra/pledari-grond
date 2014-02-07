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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class UiField implements Serializable {

	private static final long serialVersionUID = 2771717056166086580L;

	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="type")
	private UiFieldType type;
	
	@XmlAttribute(name="buildin")
	private boolean buildIn;
	
	@XmlAttribute(name="label")
	private String label;

	@XmlAttribute(name="submit")
	private boolean hasSubmit;

	@XmlAttribute(name="submitlabel")
	private String submitLabel;

	private ArrayList<String> values;
	
	private ArrayList<String> valueLabels;
	
	private int initialValue;
	
	public void setSubmitLabel(String submitLabel) {
		this.submitLabel = submitLabel;
	}
	
	public ArrayList<String> getValueLabels() {
		return valueLabels;
	}

	public void setValueLabels(ArrayList<String> valueLabels) {
		this.valueLabels = valueLabels;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UiFieldType getType() {
		return type;
	}

	public void setType(UiFieldType type) {
		this.type = type;
	}

	public boolean isBuildIn() {
		return buildIn;
	}

	public void setBuildIn(boolean buildIn) {
		this.buildIn = buildIn;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean hasSubmitButton() {
		return hasSubmit;
	}

	public void setHasSubmit(boolean hasSubmit) {
		this.hasSubmit = hasSubmit;
	}

	public String getSubmitLabel() {
		return submitLabel;
	}

	public String getInitialValue() {
		if(values == null || values.size() <= initialValue) return null;
		return values.get(initialValue);
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	public void setInitialValue(int initialValue) {
		this.initialValue = initialValue;
	}
	
	public int getInitialValueIndex() {
		return initialValue;
	}

	@Override
	public String toString() {
		return "UiField [id=" + id + ", type=" + type + ", buildIn=" + buildIn
				+ ", label=" + label + ", hasSubmit=" + hasSubmit
				+ ", submitLabel=" + submitLabel + ", values=" + values
				+ ", initialValue=" + initialValue + "]";
	}
	
	
	
}

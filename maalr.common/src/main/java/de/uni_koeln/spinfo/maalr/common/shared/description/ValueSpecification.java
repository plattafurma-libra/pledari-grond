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
package de.uni_koeln.spinfo.maalr.common.shared.description;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ValueSpecification implements Serializable {
	
	private static final long serialVersionUID = -6405869468766430078L;
	
	@XmlAttribute
	private String internalName;
	
	private ValueType type;
	
	private ValueValidator validator;

	
	public ValueValidator getValidator() {
		return validator;
	}


	public void setValidator(ValueValidator validator) {
		this.validator = validator;
	}


	public ValueSpecification() {
	}
	

	public ValueSpecification(String internalName,
			ValueType type) {
		this(internalName, type, null);
	}


	public ValueSpecification(String internalName,
			ValueType type, ValueValidator validator) {
		super();
		this.internalName = internalName;
		this.type = type;
		this.validator = validator;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public ValueType getType() {
		return type;
	}

	public void setType(ValueType type) {
		this.type = type;
	}

}

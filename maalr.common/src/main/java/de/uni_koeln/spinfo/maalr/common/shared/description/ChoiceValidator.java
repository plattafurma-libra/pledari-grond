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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class ChoiceValidator extends ValueValidator implements Serializable {

	private static final long serialVersionUID = -3569535685281213541L;
	
	@XmlElementWrapper(name="values")
	@XmlElement(name="value", nillable=false, required=true)
	private Set<String> allowedValues = new TreeSet<String>();
	
	@XmlAttribute
	private boolean allowNull;
	
	public ChoiceValidator(boolean allowNull, String... values) {
		this.allowNull = allowNull;
		allowedValues.addAll(Arrays.asList(values));
	}
	
	public ChoiceValidator(boolean allowNull, List<String> values) {
		this.allowNull = allowNull;
		allowedValues.addAll(values);
	}
	
	public ChoiceValidator() {
		
	}

	@Override
	public String validate(String input) {
		if(input == null) {
			if(allowNull) return null;
			return "This value must be defined";
		}
		if (allowedValues.contains(input)) return null;
		return "Invalid choice: " + input;
	}

	@Override
	public List<String> getAllowedValues() {
		List<String> values = new ArrayList<String>();
		if (allowNull) values.add(null);
		values.addAll(allowedValues);
		return values;
	}

}

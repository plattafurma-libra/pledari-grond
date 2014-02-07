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

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;

@XmlAccessorType(XmlAccessType.FIELD)
public class ValueFormat implements Serializable {

	private static final long serialVersionUID = 3488700483352908252L;

	@XmlAttribute(name="key")
	private String key;
	
	@XmlAttribute(name="format")
	private String format;
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public String apply(LemmaVersion lv, Escaper escaper) {
		String value = lv.getEntryValue(key);
		if(escaper != null) {
			value = escaper.escape(value);
		}
		return format.replaceAll("\\{0\\}", value);
	}

}

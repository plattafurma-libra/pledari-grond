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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class QueryBuilderOption {

	@XmlAttribute
	private String id;
	
	@XmlAttribute(required=false)
	private String builderClass;
	
	@XmlAttribute(required=false)
	private String preset;
	
	@XmlAttribute(name="default")
	private boolean isDefault;

	public boolean isDefault() {
		return isDefault;
	}

	public String getId() {
		return id;
	}

	public String getBuilderClass() {
		return builderClass;
	}
	
	public String getPreset() {
		return preset;
	}

}

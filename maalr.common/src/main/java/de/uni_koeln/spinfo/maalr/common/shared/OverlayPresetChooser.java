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
package de.uni_koeln.spinfo.maalr.common.shared;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class OverlayPresetChooser implements Serializable {
	
	private static final long serialVersionUID = 4520992855784256369L;

	@XmlElementWrapper(name="options")
	@XmlElement(name="option")
	private List<OverlayOption> options;
	
	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="base")
	private String base;
	
	@XmlAttribute(name="generator")
	private String presetBuilderClass;
	
	

	public List<OverlayOption> getOptions() {
		return options;
	}

	public String getId() {
		return id;
	}

	public String getBase() {
		return base;
	}

	public String getPresetBuilderClass() {
		return presetBuilderClass;
	}

	@Override
	public String toString() {
		return "OverlayPresetChooser [options=" + options + ", id=" + id
				+ ", base=" + base + ", presetBuilderClass="
				+ presetBuilderClass + "]";
	}
	
}

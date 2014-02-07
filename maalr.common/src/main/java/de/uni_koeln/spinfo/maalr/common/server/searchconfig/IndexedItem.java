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
public class IndexedItem {

	@XmlAttribute(required=true)
	private String source;
	
	@XmlAttribute(required=true)
	private String dest;
	
	@XmlAttribute(required=true)
	private boolean stored;
	
	@XmlAttribute(required=true)
	private boolean analyzed;
	
	@XmlAttribute(required=true)
	private MaalrFieldType type;
	
	@XmlAttribute
	private boolean whitespaceAnalyzer;

	@XmlAttribute
	private boolean lowerCase;

	public boolean isLowerCase() {
		return lowerCase;
	}

	public void setLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public boolean isStored() {
		return stored;
	}

	public void setStored(boolean stored) {
		this.stored = stored;
	}

	public boolean isAnalyzed() {
		return analyzed;
	}

	public void setAnalyzed(boolean analyzed) {
		this.analyzed = analyzed;
	}

	public MaalrFieldType getType() {
		return type;
	}

	public void setType(MaalrFieldType type) {
		this.type = type;
	}

	public boolean isWhitespaceAnalyzer() {
		return whitespaceAnalyzer;
	}

	public void setWhitespaceAnalyzer(boolean analyzer) {
		this.whitespaceAnalyzer = analyzer;
	}
	
}

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

/**
 * This class represents a mapping from a database column to a
 * lucene index field configuration. The field prefix will be
 * identical to the database column name, whereas the suffix
 * is a representation of the index field attributes.
 * 
 * @author sschwieb
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IndexedColumn {

	@XmlAttribute(name="name", required=true)
	private String columnName;
	
	private String indexFieldName;
	
	private boolean stored;
	
	private boolean analyzed;
	
	private MaalrFieldType type;
	
	private boolean whitespaceAnalyzer;

	private boolean lowerCase;

	public boolean isLowerCase() {
		return lowerCase;
	}

	public void setLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
	}

	public String getSource() {
		return columnName;
	}

	public void setSource(String source) {
		this.columnName = source;
	}

	public String getIndexFieldName() {
		return indexFieldName;
	}

	public void setIndexFieldName(String dest) {
		this.indexFieldName = dest;
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

	/**
	 * Returns true if the field should be analyzed with a whitespaceanalyzer, false
	 * if a standard analyzer should be used instead.
	 * @return
	 */
	public boolean usesWhitespaceAnalyzer() {
		return whitespaceAnalyzer;
	}

	public void setUsesWhitespaceAnalyzer(boolean analyzer) {
		this.whitespaceAnalyzer = analyzer;
	}

	@Override
	public String toString() {
		return "IndexedItem [source=" + columnName + ", dest=" + indexFieldName + ", stored="
				+ stored + ", analyzed=" + analyzed + ", type=" + type
				+ ", whitespaceAnalyzer=" + whitespaceAnalyzer + ", lowerCase="
				+ lowerCase + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (analyzed ? 1231 : 1237);
		result = prime * result + ((indexFieldName == null) ? 0 : indexFieldName.hashCode());
		result = prime * result + (lowerCase ? 1231 : 1237);
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + (stored ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + (whitespaceAnalyzer ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexedColumn other = (IndexedColumn) obj;
		if (analyzed != other.analyzed)
			return false;
		if (indexFieldName == null) {
			if (other.indexFieldName != null)
				return false;
		} else if (!indexFieldName.equals(other.indexFieldName))
			return false;
		if (lowerCase != other.lowerCase)
			return false;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (stored != other.stored)
			return false;
		if (type != other.type)
			return false;
		if (whitespaceAnalyzer != other.whitespaceAnalyzer)
			return false;
		return true;
	}	
	
}

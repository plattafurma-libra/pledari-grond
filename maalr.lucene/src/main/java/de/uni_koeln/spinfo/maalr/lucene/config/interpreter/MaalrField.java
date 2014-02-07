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
package de.uni_koeln.spinfo.maalr.lucene.config.interpreter;

/**
 * Helper class to represent a single term of a query.
 * See {@link FieldChoiceTransformer#transform(de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery)} 
 * @author sschwieb
 *
 */
public class MaalrField {

	private String value;
	private String field;

	public MaalrField(String field, String value) {
		this.field = field;
		this.value = value;
	}
	
	/*public MaalrField() {
		
	}*/

	public String getValue() {
		return value;
	}

	public String getField() {
		return field;
	}

	@Override
	public String toString() {
		return "MaalrField [value=" + value + ", field=" + field + "]";
	}
	
	

}

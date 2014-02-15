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

/**
 * Defines the way an {@link IndexedColumn} is mapped onto Lucene fields.
 * TEXT, STRING and INTEGER correspond to TextField, StringField and 
 * IntField, whereas CSV is a customized option to generate multiple
 * StringFields.
 * 
 * @author sschwieb
 *
 */
public enum MaalrFieldType {
	
	/**
	 * A field that is indexed and tokenized, without term vectors. 
	 */
	TEXT, 
	
	/**
	 * A field that is indexed but not tokenized: the entire
	 * String value is indexed as a single token.
	 */
	STRING, 
	
	/**
	 * Field that indexes <code>int</code> values for efficient range filtering and sorting.
	 */
	INTEGER, 
	
	/**
	 * Multiple fields of type STRING, separated by commas.
	 */
	CSV;

}

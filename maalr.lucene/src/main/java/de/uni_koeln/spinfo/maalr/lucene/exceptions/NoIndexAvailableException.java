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
package de.uni_koeln.spinfo.maalr.lucene.exceptions;


public class NoIndexAvailableException extends IndexException {

	private static final long serialVersionUID = 1775718170081317099L;

	public NoIndexAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoIndexAvailableException() {
		super();
	}

	public NoIndexAvailableException(String message) {
		super(message);
	}

	public NoIndexAvailableException(Throwable cause) {
		super(cause);
	}
	
	

}

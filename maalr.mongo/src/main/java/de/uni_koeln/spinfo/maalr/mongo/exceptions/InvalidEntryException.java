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
package de.uni_koeln.spinfo.maalr.mongo.exceptions;

import de.uni_koeln.spinfo.maalr.common.shared.DatabaseException;

public class InvalidEntryException extends DatabaseException {
	
	private static final long serialVersionUID = 5146590037732464247L;
	
	public InvalidEntryException() {
	}
	
	public InvalidEntryException(String message) {
		super(message);
	}

	public InvalidEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidEntryException(Throwable cause) {
		super(cause);
	}


}

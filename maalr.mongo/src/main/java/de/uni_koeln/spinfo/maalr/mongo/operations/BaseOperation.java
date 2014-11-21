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
package de.uni_koeln.spinfo.maalr.mongo.operations;

import de.uni_koeln.spinfo.maalr.common.server.util.IDBOperation;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.util.Validator;

public abstract class BaseOperation implements IDBOperation {

	private final long timeStamp;
	private boolean suggestion;
	private String userId;

	public BaseOperation() {
		timeStamp = System.nanoTime();
	}
	
	@Override
	public final long getTimeStamp() {
		return timeStamp;
	}

	protected void validate(LemmaVersion entry) throws InvalidEntryException {
		Validator.validate(entry);
	}

	protected void validate(LexEntry entry) throws InvalidEntryException {
		Validator.validate(entry);
	}
	
	public BaseOperation asSuggestion() {
		this.suggestion = true;
		return this;
	}
	
	public BaseOperation setLogin(String userId) {
		this.userId = userId;
		return this;
	}

	protected boolean isSuggestion() {
		return suggestion;
	}
	
	protected String getUserId() {
		return userId;
	}

}

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


import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.OperationRejectedException;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics;
import de.uni_koeln.spinfo.maalr.mongo.util.Validator;

public class InsertOperation extends BaseOperation {

	private LexEntry entry;

	public InsertOperation(LexEntry entry) {
		this.entry = entry;
	}

	@Override
	public void execute() throws InvalidEntryException,
			OperationRejectedException {
		//entry.prepareForUpdateOrInsert();
		Validator.validatePreInsert(entry);
		LemmaVersion current = entry.getCurrent();
		current.setStatus(Status.NEW_ENTRY);
		if(isSuggestion()) {
			current.setVerification(Verification.UNVERIFIED);
		} else {
			current.setVerification(Verification.ACCEPTED);
			current.setVerifierId(getUserId());
		}
		current.setUserId(getUserId());
		current.setTimestamp(System.currentTimeMillis());
		entry.setCurrent(current);
		try {
			Database.getInstance().insert(entry);
			if(isSuggestion()) {
				DictionaryStatistics.newSuggestion();
			} else {
				DictionaryStatistics.newEntry();
			}
		} catch (NoDatabaseAvailableException e) {
			throw new OperationRejectedException(e);
		}
		Validator.validatePostInsert(entry);
	}

	@Override
	public LexEntry getLexEntry() {
		return entry;
	}

}

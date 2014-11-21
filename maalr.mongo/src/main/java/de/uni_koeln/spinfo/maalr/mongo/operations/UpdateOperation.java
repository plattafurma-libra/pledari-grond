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
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.OperationRejectedException;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics;
import de.uni_koeln.spinfo.maalr.mongo.util.Validator;

public class UpdateOperation extends BaseOperation implements IDBOperation {

	private LexEntry entry;
	private LemmaVersion newVersion;

	public UpdateOperation(LexEntry oldEntry, LemmaVersion newEntry) {
		this.entry = oldEntry;
		this.newVersion = newEntry;
	}

	@Override
	public void execute() throws InvalidEntryException,
			OperationRejectedException {
		Validator.validatePostInsert(entry);
		newVersion.setStatus(Status.UNDEFINED);
		newVersion.setVerification(Verification.UNVERIFIED);
		Validator.validatePreUpdate(newVersion);
		if(isSuggestion()) {
			newVersion.setVerification(Verification.UNVERIFIED);
		} else {
			newVersion.setVerification(Verification.ACCEPTED);
			newVersion.setVerifierId(getUserId());
		}
		newVersion.setUserId(getUserId());
		newVersion.setTimestamp(System.currentTimeMillis());
		try {
			Database.getInstance().update(entry, newVersion);
			if(isSuggestion()) {
				DictionaryStatistics.newEntryModificationSuggestion();
			} else {
				DictionaryStatistics.newEntryModification();
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

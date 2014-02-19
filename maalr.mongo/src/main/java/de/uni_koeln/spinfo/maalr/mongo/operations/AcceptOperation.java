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
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.NoDatabaseAvailableException;
import de.uni_koeln.spinfo.maalr.common.shared.OperationRejectedException;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;

public class AcceptOperation extends BaseOperation implements IDBOperation {

	private LexEntry entry;
	private LemmaVersion version;

	public AcceptOperation(LexEntry entry, LemmaVersion version) {
		this.entry = entry;
		this.version = version;
	}

	@Override
	public void execute() throws OperationRejectedException {
		validate(entry);
		try {
			LemmaVersion current = entry.getCurrent();
			if(current != null) {
				current.setVerification(Verification.OUTDATED);
//				String sortField = Configuration.getInstance().getLemmaDescription().getSortOrderLangA();
//				version.setValue(sortField, current.getEntryValue(sortField));
//				sortField = Configuration.getInstance().getLemmaDescription().getSortOrderLangB();
//				version.setValue(sortField, version.getEntryValue(sortField));
			}
			version.setVerification(Verification.ACCEPTED);
			version.setVerifierId(getUserId());
			Database.getInstance().accept(entry, version);
		} catch (NoDatabaseAvailableException e) {
			throw new OperationRejectedException(e);
		}
	}
	
	@Override
	public LexEntry getLexEntry() {
		return entry;
	}

}

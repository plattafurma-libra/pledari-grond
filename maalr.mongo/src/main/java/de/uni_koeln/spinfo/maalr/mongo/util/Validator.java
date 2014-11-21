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
package de.uni_koeln.spinfo.maalr.mongo.util;

import java.util.List;
import java.util.Map;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Status;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion.Verification;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;

public class Validator {

	public static void validatePreInsert(LemmaVersion entry) throws InvalidEntryException {
		validatePreUpdate(entry);
		if(entry.getInternalId() == null || entry.getInternalId() != 0) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have internal id '0' before insert!");
		}
	}
	
	public static void validatePreUpdate(LemmaVersion entry) throws InvalidEntryException {
		validate(entry);
		if(entry.getTimestamp() != 0) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must not have a timestamp before update!");
		}
		if(entry.getUserId() != null) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must not have a user id before update!");
		}
		if(entry.getStatus() != Status.UNDEFINED) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have status 'undefined' before update!");
		}
		if(entry.getVerification() != Verification.UNVERIFIED) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have verification 'unverified' before update!");
		}
	}
	
	public static void validatePreInsert(LexEntry entry) throws InvalidEntryException {
		if(entry.getId() != null) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must not have an id before insert!");
		}
		validate(entry);
		LemmaVersion current = entry.getCurrent();
		validatePreInsert(current);
	}
	
	public static void validatePostInsert(LemmaVersion entry) throws InvalidEntryException {
		validate(entry);
		if(entry.getInternalId() == null) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have an id after insert!");
		}
		if(entry.getTimestamp() == 0) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have a timestamp after insert!");
		}
		if(entry.getUserId() == null) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have a user id after insert!");
		}
		if(entry.getStatus() == Status.UNDEFINED) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must not have status 'undefined' after insert!");
		}
	}
	
	public static void validatePostInsert(LexEntry entry) throws InvalidEntryException {
		validate(entry);
		if(entry.getId() == null || entry.getId().length() == 0) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have an id after insert!");
		}
		LemmaVersion current = entry.getCurrent();
		validatePostInsert(current);
	}
	
	public static void validate(LemmaVersion entry) throws InvalidEntryException {
		if (entry == null) {
			throw new InvalidEntryException("LemmaVersion must not be null!");
		}
		Map<String, String> values = entry.getEntryValues();
		if(values.size() == 0) {
			//throw new InvalidEntryException(entry.getClass().getSimpleName() + " must contain at least one value!");
			throw new InvalidEntryException("dialog.failure.field");
		}
	}

	public static void validate(LexEntry entry) throws InvalidEntryException {
		if (entry == null) {
			throw new InvalidEntryException("LexEntry must not be null!");
		}	
		if (entry.getCurrent() == null) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " must have a current lemmaversion!");
		}
		List<LemmaVersion> versions = entry.getVersionHistory();
		if (versions == null) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " version list must not be null");
		}
		if (versions.size() == 0) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " version list must not be empty");
		}
		if (!versions.contains(entry.getCurrent())) {
			throw new InvalidEntryException(entry.getClass().getSimpleName() + " version list must contain the current entry");
		}
		for (LemmaVersion version : versions) {
			validate(version);
		}
	}

}

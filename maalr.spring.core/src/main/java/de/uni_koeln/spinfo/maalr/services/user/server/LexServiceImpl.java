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
package de.uni_koeln.spinfo.maalr.services.user.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;

import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.MaalrException;
import de.uni_koeln.spinfo.maalr.mongo.SpringBackend;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.services.user.shared.LexService;

@Service("lexService" /* Don't forget to add new services to gwt-servlet.xml! */)
public class LexServiceImpl implements LexService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	SpringBackend db;

	@Override
	public String suggestNewEntry(LemmaVersion entry) throws MaalrException {
		try {
			LexEntry le = new LexEntry(entry);
			db.suggestNewEntry(le);
			return null;
		} catch (Exception e) {
			logger.error("Failed to suggest new entry", e);
//			throw new MaalrException("Failed to suggest new entry: " + e);
			throw new MaalrException(e.getMessage());
		}
	}

	@Override
	public String suggestModification(LemmaVersion entry) throws MaalrException {
		try {
			BasicDBObject old = Database.getInstance().getById(
					entry.getLexEntryId());
			LexEntry oldEntry = Converter.convertToLexEntry(old);
			db.suggestUpdate(oldEntry, entry);
			return null;
		} catch (Exception e) {
			logger.error("Failed to suggest modification", e);
//			throw new MaalrException("Failed to suggest modification: " + e);
			throw new MaalrException(e.getMessage());
		}
	}

}

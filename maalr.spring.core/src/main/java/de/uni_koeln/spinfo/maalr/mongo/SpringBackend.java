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
package de.uni_koeln.spinfo.maalr.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion;
import de.uni_koeln.spinfo.maalr.common.shared.LexEntry;
import de.uni_koeln.spinfo.maalr.common.shared.MaalrException;
import de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription;
import de.uni_koeln.spinfo.maalr.common.shared.description.UseCase;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;
import de.uni_koeln.spinfo.maalr.lucene.Index;
import de.uni_koeln.spinfo.maalr.mongo.core.Converter;
import de.uni_koeln.spinfo.maalr.mongo.core.Database;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidEntryException;
import de.uni_koeln.spinfo.maalr.mongo.operations.AcceptAfterUpdateOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.AcceptOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.DeleteOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.DropHistoryOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.InsertOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.RejectOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.RestoreOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.UpdateOperation;
import de.uni_koeln.spinfo.maalr.mongo.operations.UpdateOrderOperation;
import de.uni_koeln.spinfo.maalr.mongo.util.DBCommandQueue;

@Service
@Scope(value = "singleton")
public class SpringBackend {
	
	private DBCommandQueue queue = DBCommandQueue.getInstance();
	
	@Autowired
	private UserInfoBackend userInfos;
	
	@Autowired
	private Index index;
	
	private String getUserLogin() {
		try {
			MaalrUserInfo user = userInfos.getOrCreateCurrentUser();
			return user.getLogin();
			
		} catch (Exception e) {
			throw new BadCredentialsException("Failed to get user login");
		}
	}
	
	private void addUserInfo(LemmaVersion lemma) {
		String ip = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
		lemma.setIP(ip);
		lemma.setCreatorRole(userInfos.getOrCreateCurrentUser().getRole());
	}
	
	@Secured( { Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void insert(LexEntry entry) throws Exception {
		String login = getUserLogin();
		addUserInfo(entry.getCurrent());
		LexEntry modified = queue.push(new InsertOperation(entry).setLogin(login));
		index.update(modified);
	}
	
	public void suggestNewEntry(LexEntry entry) throws Exception {
		if(entry == null) throw new InvalidEntryException("LexEntry must not be null!");
		if(entry.getVersionHistory() == null || entry.getVersionHistory().size() != 1) throw new InvalidEntryException("Invalid suggestion!");
		validateUserModification(entry.getCurrent(), entry.getMostRecent());
		String login = getUserLogin();
		addUserInfo(entry.getMostRecent());
		LexEntry modified = queue.push(new InsertOperation(entry).setLogin(login).asSuggestion());
		index.update(modified);
	}
	
	private void validateUserModification(LemmaVersion current, LemmaVersion version) throws MaalrException {
		LemmaDescription description = Configuration.getInstance().getLemmaDescription();
		// Server-Side validation of a user modification: Check that only fields which are allowed
		// to modify are sent
		ArrayList<String> fields = new ArrayList<String>(description.getFields(UseCase.FIELDS_FOR_SIMPLE_EDITOR, true));
		fields.addAll(description.getFields(UseCase.FIELDS_FOR_SIMPLE_EDITOR, false));
		// Add email and comment as default values
		fields.add(LemmaVersion.EMAIL);
		fields.add(LemmaVersion.COMMENT);
		version.getEntryValues().keySet().retainAll(fields);
		if(current != null) {
			// Add values of non-user-fields to the suggestion
			Map<String, String> allowed = new HashMap<String, String>(version.getEntryValues());
			version.getEntryValues().putAll(current.getEntryValues());
			// Overwrite user-fields with suggested entries
			version.getEntryValues().putAll(allowed);
		}
		// Check the length of each field: Normal entries must be less than 200 characters,
		// a comment must be less than 500 chars.
		for (String key : fields) {
			String value = version.getEntryValue(key);
			if(value != null) {
				if(LemmaVersion.COMMENT.equals(key)) {
					if(value.length() > 500) {
//						throw new MaalrException("Please limit your comment to 500 characters.");

						//workaround for i18n
						throw new MaalrException("dialog.failure.comment");
					}
				} else {
					if(value.length() > 200) {
//						throw new MaalrException("A field cannot contain more than 200 characters.");
						
						//workaround for i18n
						throw new MaalrException("dialog.failure.lemma");
					}
				}
			}
		}
	}

	public void suggestUpdate(LexEntry oldEntry, LemmaVersion newEntry) throws Exception {
		if(newEntry == null) throw new InvalidEntryException("Lemma must not be null!");
		if(oldEntry == null) throw new InvalidEntryException("LexEntry must not be null!");
		oldEntry = Converter.convertToLexEntry(Database.getInstance().getById(oldEntry.getId()));
		if(oldEntry == null) throw new InvalidEntryException("Entry not found!");
		validateUserModification(oldEntry.getCurrent(), newEntry);
		String login = getUserLogin();
		addUserInfo(newEntry);
		LexEntry modified = queue.push(new UpdateOperation(oldEntry, newEntry).setLogin(login).asSuggestion());
		index.update(modified);
	}
	
	@Secured( { Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void update(LexEntry oldEntry, LemmaVersion newEntry) throws Exception {
		if(newEntry == null) throw new InvalidEntryException("Lemma must not be null!");
		if(oldEntry == null) throw new InvalidEntryException("LexEntry must not be null!");
		String login = getUserLogin();
		addUserInfo(newEntry);
		LexEntry modified = queue.push(new UpdateOperation(oldEntry, newEntry).setLogin(login));
		index.update(modified);
	}


	@Secured( { Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void delete(LexEntry entry) throws Exception {
		String login = getUserLogin();
		queue.push(new DeleteOperation(entry).setLogin(login));
		index.delete(entry);
	}

	@Secured( { Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void restore(LemmaVersion invalid, LemmaVersion valid) throws Exception {
//		if(valid == null) throw new InvalidEntryException("Lemma must not be null!");
//		if(invalid == null) throw new InvalidEntryException("Lemma must not be null!");
		String login = getUserLogin();
		addUserInfo(valid);
		LexEntry modified = queue.push(new RestoreOperation(invalid, valid).setLogin(login));
		index.update(modified);
	}

	@Secured( { Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void accept(LexEntry entry, LemmaVersion version) throws Exception {
		String login = getUserLogin();
		LexEntry modified = queue.push(new AcceptOperation(entry, version).setLogin(login));
		index.update(modified);
	}
	
	@Secured( { Constants.Roles.TRUSTED_EX_3, Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void reject(LexEntry entry, LemmaVersion version) throws Exception {
		String login = getUserLogin();
		addUserInfo(version);
		LexEntry modified = queue.push(new RejectOperation(entry, version).setLogin(login));
		index.update(modified);
	}
	
	@Secured( { Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void acceptAfterUpdate(LexEntry entry, LemmaVersion suggested, LemmaVersion modified) throws Exception {
		String login = getUserLogin();
		LexEntry mod = queue.push(new AcceptAfterUpdateOperation(entry, suggested, modified).setLogin(login));
		index.update(mod);
	}

	@Secured( { Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public void dropOutdatedHistory(LexEntry entry) throws Exception {
		String login = getUserLogin();
		LexEntry modified = queue.push(new DropHistoryOperation(entry).setLogin(login));
		index.update(modified);
	}

	@Secured( { Constants.Roles.TRUSTED_IN_4, Constants.Roles.ADMIN_5 })
	public List<LexEntry> updateOrder(boolean firstLang, List<LemmaVersion> ordered) throws Exception {
		String login = getUserLogin();
		List<LexEntry> modified = queue.pushMulti(new UpdateOrderOperation(firstLang, ordered).setLogin(login));
		index.updateAll(modified);
		return modified;
	}

}

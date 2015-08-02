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
package de.uni_koeln.spinfo.maalr.login;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;

public class MaalrUserInfo extends BasicDBObject {
	
	private static final long serialVersionUID = 1188500902567455955L;
	
	// Used to avoid duplicates
	private String _id;

	public MaalrUserInfo(String login, Role role) {
		setLogin(login);
		setRole(role);
	}
	
	public MaalrUserInfo() {
	}

	public MaalrUserInfo(DBObject obj) {
		super.putAll(obj);
	}
	
	public void setPassword(String password) {
		super.put(Constants.Users.PASSWORD, password);
	}
	
	public String getPassword() {
		return super.getString(Constants.Users.PASSWORD);
	}
	
	public void setLogin(String login) {
		super.put(Constants.Users.LOGIN, login);
	}
	
	public String getLogin() {
		return super.getString(Constants.Users.LOGIN);
	}
	
	public String getEmail() {
		return super.getString(Constants.Users.EMAIL);
	}

	public void setEmail(String email) {
		super.put(Constants.Users.EMAIL, email);
	}

	public String getFirstname() {
		return super.getString(Constants.Users.FIRSTNAME);
	}

	public void setFirstname(String firstname) {
		super.put(Constants.Users.FIRSTNAME, firstname);
	}

	public String getLastname() {
		return super.getString(Constants.Users.LASTNAME);
	}

	public void setLastname(String lastname) {
		super.put(Constants.Users.LASTNAME, lastname);
	}
	
	public void setRole(Role role) {
		super.put(Constants.Users.ROLE, role.toString());
	}
	
	public Role getRole() {
		String name = super.getString(Constants.Users.ROLE);
		if(name == null)
			return null;
		return Role.valueOf(name);
	}
	
	public int getUpVotes() {
		return super.getInt(Constants.Users.UPVOTES);
	}
	
	public int getDownVotes() {
		return super.getInt(Constants.Users.DOWNVOTES);
	}

	public String getTitle() {
		return super.getString(Constants.Users.TITLE);
	}
	
	public void setTitle(String title) {
		super.put(Constants.Users.TITLE, title);
	}

	public LightUserInfo toLightUser() {
		LightUserInfo userInfo = new LightUserInfo();
		userInfo.setLogin(getLogin());
		userInfo.setFirstName(getFirstname());
		userInfo.setLastName(getLastname());
		userInfo.setEmail(getEmail());
		userInfo.setRole(getRole());
		userInfo.setLastModificationDate(getLastModificationDate());
		userInfo.setCreationDate(getCreationDate());
		return userInfo;
	}

	public void setCreationDate(long currentTimeMillis) {
		super.put(Constants.Users.CREATION_DATE, currentTimeMillis);
	}
	
	public void setLastModificationDate(long currentTimeMillis) {
		super.put(Constants.Users.LAST_MODIFICATION, currentTimeMillis);
	}
	
	public long getCreationDate() {
		return super.getLong(Constants.Users.CREATION_DATE);
	}
	
	public long getLastModificationDate() {
		return super.getLong(Constants.Users.LAST_MODIFICATION);
	}
	
	public String getDisplayName() {
		if(getFirstname() != null) return getFirstname();
		return getLogin();
	}

	// SPRING SOCIAL SPECIFIC VALUES
	
	public void setProviderUserId(String providerUserId) {
		super.put(Constants.Users.PROVIDER_USER_ID, providerUserId);
	}

	public void setProviderId(String providerId) {
		super.put(Constants.Users.PROVIDER_ID, providerId);
	}
	
	public String getProviderUserId() {
		return super.getString(Constants.Users.PROVIDER_USER_ID);
	}
	
	public String getProviderId() {
		return super.getString(Constants.Users.PROVIDER_ID);
	}

	public String getID() {
		return _id;
	}

	public void setID() {
		this._id = super.getString("_id");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((_id == null) ? 0 : _id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MaalrUserInfo other = (MaalrUserInfo) obj;
		if (_id == null) {
			if (other._id != null)
				return false;
		} else if (!_id.equals(other._id))
			return false;
		return true;
	}
	
	
	
	
}

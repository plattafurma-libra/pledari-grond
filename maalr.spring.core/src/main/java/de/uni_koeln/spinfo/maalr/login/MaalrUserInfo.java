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

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;

public class MaalrUserInfo extends BasicDBObject {
	
	private static final long serialVersionUID = 1188500902567455955L;

	public MaalrUserInfo() {
	}
	
	public MaalrUserInfo(DBObject obj) {
		super.putAll(obj);
	}

	public MaalrUserInfo(String login, Role role) {
		setLogin(login);
		setRole(role);
	}
	
	public MaalrUserInfo(final String login, final String password, Role role) {
		setLogin(login);
		setPassword(password);
		setRole(role);
	}
	
	public void setLogin(final String login) {
		super.put(Constants.Users.LOGIN, login);
	}
	
	public String getLogin() {
		return super.getString(Constants.Users.LOGIN);
	}
	
	public void setPassword(final String password) {
		super.put(Constants.Users.PASSWORD, BCrypt.hashpw(password, BCrypt.gensalt()));
	}
	
	public String getPassword() {
		return super.getString(Constants.Users.PASSWORD);
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
	
	public LightUserInfo toLightUser() {
		LightUserInfo userInfo = new LightUserInfo();
		userInfo.setLogin(getLogin());
		userInfo.setRole(getRole());
		userInfo.setLastModificationDate(getLastModificationDate());
		userInfo.setCreationDate(getCreationDate());
		return userInfo;
	}

	public void setCreationDate(long currentTimeMillis) {
		super.put(Constants.Users.CREATION_DATE, currentTimeMillis);
	}
	
	public long getCreationDate() {
		return super.getLong(Constants.Users.CREATION_DATE);
	}
	
	public void setLastModificationDate(long currentTimeMillis) {
		super.put(Constants.Users.LAST_MODIFICATION, currentTimeMillis);
	}
	
	public long getLastModificationDate() {
		return super.getLong(Constants.Users.LAST_MODIFICATION);
	}
	
	public String getDisplayName() {
		return getLogin();
	}

	@Deprecated
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

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
}

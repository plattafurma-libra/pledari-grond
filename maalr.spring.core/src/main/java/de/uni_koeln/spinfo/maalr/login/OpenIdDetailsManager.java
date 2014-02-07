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

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;

/**
 * User Details service for OpenID. If a new user logs in the first time,
 * a new entry will be inserted into the user database, and the new user
 * will get the role 'openid'. Firstname, lastname and email will be null,
 * as this information is not yet available. If a user logs in afterwards,
 * these properties will be taken from the database, as well as the 
 * assigned role. 
 * 
 * @author sschwieb
 *
 */
public class OpenIdDetailsManager implements UserDetailsManager {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserInfoBackend userInfos;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		if(userInfos.userExists(username)) {
			MaalrUserInfo info = userInfos.getByLogin(username);
			logger.info("Successfully logged in " + info);
			return infoToDetails(info);
		} else {
			MaalrUserInfo info = new MaalrUserInfo(username, Role.OPENID_2);
			logger.info("Created new login for " + info);
			return infoToDetails(info);
		}
	}

	private UserDetails infoToDetails(MaalrUserInfo info) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority(info.getRole().getRoleId());
		authorities.add(authority);
		User details = new User(info.getLogin(), "ignored", authorities);
		return details;
	}

	@Override
	public void createUser(UserDetails user) {
		MaalrUserInfo info = new MaalrUserInfo(user.getUsername(), Role.OPENID_2);
		try {
			userInfos.insert(info);
		} catch (InvalidUserException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void updateUser(UserDetails user) {
		// Not needed, as this is the responsibility of the OpenID-provider
	}

	@Override
	public void deleteUser(String username) {
		// Not needed, as this is the responsibility of the OpenID-provider
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// Not needed, as this is the responsibility of the OpenID-provider
	}

	@Override
	public boolean userExists(String username) {
		return userInfos.userExists(username);
	}

}

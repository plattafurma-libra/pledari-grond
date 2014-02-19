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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;


@Service("MaalrLoginManager")
public class LoginManager implements AuthenticationProvider {
	
	@Autowired
	private JaasAuthenticationProvider provider;
	
	@Autowired
	private UserInfoBackend backend;
	
	private static final String anonymous = "anonymousUser";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public Authentication login(String name, String password) {
		logout();
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(name, password);
			Authentication authenticate = provider.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authenticate);
			logger.info("Successfully logged in user " + name);
			return authenticate;
		} catch (AuthenticationException e) {
			logger.warn("Failed to log in user " + name + ", reason: " + e);
			logger.warn("Details", e);
			return null;
		}
	}
	
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	public boolean loggedIn() {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		return loggedIn(currentUser);
	}
	
	public boolean loggedIn(Authentication user) {
		if(user == null) return false;
		// TODO: Find better way to identify anonymous user...
		return user.isAuthenticated() && !anonymous.equals(user.getName());

	}

	public boolean notLoggedIn() {
		return !loggedIn();
	}
	
	public String getCurrentUserName() {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if(!loggedIn(currentUser)) return null;
		// FIXME: Do not query for the current user if it is already known!!
		MaalrUserInfo current = backend.getByLogin(currentUser.getName());
		if(current != null && current.getFirstname() != null) {
			return current.getFirstname();
		}
		return currentUser.getName();
	}
	
	public LightUserInfo getCurrentUser() {
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if(!loggedIn(currentUser)) return null;
		// FIXME: Do not query for the current user if it is already known!!
		MaalrUserInfo current = backend.getByLogin(currentUser.getName());
		if(current != null) {
			return current.toLightUser();
		}
		return null;
	}

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if(authentication.isAuthenticated()) {
			return authentication;
		}
		return login(authentication.getName(), (String) authentication.getCredentials());
	}

	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

	public String getCurrentUserId() {
		return getCurrentUserName();
	}

	/**
	 * ONLY FOR TESTING!
	 * @param jaasAuthenticationProvider
	 */
	public void setJaasAuthProvider(JaasAuthenticationProvider jaasAuthenticationProvider) {
		if(provider == null)
			provider = jaasAuthenticationProvider;
	}
	
	
}

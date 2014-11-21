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
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;

/**
 * Signs up a user automatically if not found in the local database.
 * 
 * @author Mihail Atanassov (atanassov.mihail@gmail.com)
 *
 */

public final class AutoConnectionSignUp implements ConnectionSignUp {

//	@Autowired
//	private UserInfoBackend userInfos;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public String execute(Connection<?> connection) {
		
		UserProfile profile = connection.fetchUserProfile();
		ConnectionData connectionData = connection.createData();
		
		MaalrUserInfo user = createAndRegisterUser(profile, connectionData);
		
		logger.info("SIGN-UP USER: " + user);
		return user.getLogin();
	}

	private MaalrUserInfo createAndRegisterUser(UserProfile profile,
			ConnectionData connectionData) {
		MaalrUserInfo user = new MaalrUserInfo();
		user.setFirstname(profile.getFirstName());
		user.setLastname(profile.getLastName());
		user.setEmail(profile.getEmail());
		user.setLogin(profile.getUsername());
		user.setRole(Role.OPENID_2);
		user.setProviderUserId(connectionData.getProviderUserId());
		user.setProviderId(connectionData.getProviderId());
		return store(user);
	}
	
	private MaalrUserInfo store(MaalrUserInfo user) {
		UserInfoDB userDb = new UserInfoDB();
		try {
			return userDb.insert(user);
		} catch (InvalidUserException e) {
			e.printStackTrace();
		}
		return null;
	}

}

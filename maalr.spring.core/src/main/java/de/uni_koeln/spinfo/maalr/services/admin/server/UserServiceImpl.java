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
package de.uni_koeln.spinfo.maalr.services.admin.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;
import de.uni_koeln.spinfo.maalr.services.admin.shared.UserService;

@Service("userService" /* Don't forget to add new services to gwt-servlet.xml! */)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoBackend userInfos;
	
	@Override
	public LightUserInfo getUserInfo(String login) {
		MaalrUserInfo user = userInfos.getByLogin(login);
		if(user == null) {
			return null;
		}
		return user.toLightUser();
	}

	@Override
	public void updateRole(String login, Role role) throws InvalidUserException {
		MaalrUserInfo maalrUser = userInfos.getByLogin(login);
		if(maalrUser.getRole().equals(role)) return;
		userInfos.updateUserRole(maalrUser, role);
	}

	@Override
	public void updateUserFields(LightUserInfo user) throws InvalidUserException {
		MaalrUserInfo maalrUser = userInfos.getByLogin(user.getLogin());
		maalrUser.setLogin(user.getLogin());
		maalrUser.setEmail(user.getEmail());
		maalrUser.setFirstname(user.getFirstName());
		maalrUser.setLastname(user.getLastName());
		userInfos.updateUserFields(maalrUser);
	}

	@Override
	public int getNumberOfUsers() {
		return userInfos.getNumberOfUsers();
	}

	@Override
	public List<LightUserInfo> getAllUsers(Role role, String text, String sortColumn, boolean sortAscending, int from, int length) {
		List<MaalrUserInfo> users = userInfos.getAllUsers(role, text, sortColumn, sortAscending, from, length);
		List<LightUserInfo> toReturn = new ArrayList<LightUserInfo>();
		for (MaalrUserInfo maalrUser : users) {
			toReturn.add(maalrUser.toLightUser());
		}
		return toReturn;
	}

	@Override
	public LightUserInfo insertNewUser(LightUserInfo user) throws InvalidUserException {
		MaalrUserInfo maalrUser = new MaalrUserInfo();
		maalrUser.setLogin(user.getLogin());
		maalrUser.setFirstname(user.getFirstName());
		maalrUser.setLastname(user.getLastName());
		maalrUser.setEmail(user.getEmail());
		maalrUser.setRole(user.getRole());
		userInfos.insert(maalrUser);
		return maalrUser.toLightUser();
	}

	public void adminUpdate(LightUserInfo user) throws InvalidUserException {
		updateUserFields(user);
		updateRole(user.getLogin(), user.getRole());
	}

	@Override
	public boolean deleteUser(LightUserInfo user) {
		return userInfos.deleteUser(user);
	}
	
	
}

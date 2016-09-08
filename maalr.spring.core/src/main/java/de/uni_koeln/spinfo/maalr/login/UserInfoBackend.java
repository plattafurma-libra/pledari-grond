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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.uni_koeln.spinfo.maalr.common.shared.Constants;
import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;

/**
 * Spring service which manages the user database.
 * 
 * @author sschwieb
 * 
 */
@Service
@Scope(value = "singleton")
public class UserInfoBackend {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoBackend.class);

	/**
	 * Returns the user infos of the user currently logged in. If no user infos
	 * exist for this user, a new user will be stored. <br>
	 * <strong>Security:</strong> Any user may call this method.
	 * 
	 * @return
	 */
	// @Secured( { Constants.Roles.GUEST_1, Constants.Roles.OPENID_2,
	// Constants.Roles.TRUSTED_EXTERNAL_3, Constants.Roles.TRUSTED_INTERNAL_4,
	// Constants.Roles.ADMIN_5 })
	@Deprecated
	public MaalrUserInfo getOrCreateCurrentUser() {

		UserInfoDB userInfos = new UserInfoDB();
		String name;
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			name = SecurityContextHolder.getContext().getAuthentication().getName();
		} else {
			name = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
		}
		try {
			return userInfos.getOrCreate(name);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Modifies the role of the referenced user. The referenced user must exist. <br>
	 * <strong>Security:</strong> Only admins may call this method.
	 * 
	 * @param login
	 * @param role
	 * @throws InvalidUserException
	 */
	@Secured(Constants.Roles.ADMIN_5)
	public void updateUserRole(MaalrUserInfo user, Role role) throws InvalidUserException {
		
		if (user.getLogin().equals("admin")) {
			throw new InvalidUserException("Not allowed to change admin role!");
		}
		if (user.getRole().equals(role))
			return;
		
		logger.info("Updating role of '" + user.getLogin() +"' from " + user.getRole() + " to " + role);

		user.setRole(role);
		UserInfoDB userInfos = new UserInfoDB();
		userInfos.updateUser(user);
	}

	/**
	 * Modifies personal information of the user. Everyone with a unique user
	 * name (but no guests) may execute this method. Only personal information
	 * will be updated: email, firstname, lastname. <br>
	 * <strong>Security:</strong> Any non-guest may call this method for
	 * himself. Admins may call this method for everyone.
	 * 
	 * @param updated
	 *            The updated user
	 * @throws InvalidUserException
	 *             If the currently logged in user is not the user to update,
	 *             and the currently logged in user is not an admin.
	 */
	@Secured(Constants.Roles.ADMIN_5)
	public void updateUserFields(MaalrUserInfo updated) throws InvalidUserException {
		MaalrUserInfo current = getOrCreateCurrentUser();
		MaalrUserInfo toUpdate = null;
		if (current.getRole() == Role.ADMIN_5) {
			toUpdate = updated;
		} else {
			if (!current.getLogin().equals(updated.getLogin())) {
				// Only the logged in user may change these properties
				throw new InvalidUserException("Action not allowed");
			}
			toUpdate = current;
		}
		logger.info("Updating user data - old:" + toUpdate + ", new: " + updated + " (ignoring any role changes)");
		UserInfoDB userInfos = new UserInfoDB();
		userInfos.updateUser(toUpdate);
	}

	/**
	 * For tests only! This method deletes all users in the database. <br>
	 * <strong>Security:</strong> Only admins may call this method. However,
	 * they should not.
	 */
	@Secured(Constants.Roles.ADMIN_5)
	public void deleteAllEntries() {
		UserInfoDB userInfos = new UserInfoDB();
		userInfos.deleteAllEntries();
	}

	/**
	 * Returns <code>true</code> if a user with the given login exists. <br>
	 * <strong>Security:</strong> Any user may call this method.
	 * 
	 * @param login
	 * @return
	 */
	public boolean userExists(String login) {
		if (login == null)
			return false;
		UserInfoDB userInfos = new UserInfoDB();
		return userInfos.userExists(login);
	}

	/**
	 * Returns the user with the given login, or <code>null</code>, if it
	 * doesn't exist. <br>
	 * <strong>Security:</strong> Any user may call this method.
	 * 
	 * @param login
	 * @return
	 */
	public MaalrUserInfo getByLogin(String login) {
		UserInfoDB userInfos = new UserInfoDB();
		return userInfos.getByLogin(login);
	}

	/**
	 * Returns the user with the given Email, or <code>null</code>, if it
	 * doesn't exist. <br>
	 * <strong>Security:</strong> Any user may call this method.
	 * 
	 * @param email
	 * @return
	 */
	public MaalrUserInfo getByEmail(String email) {
		UserInfoDB userInfos = new UserInfoDB();
		return userInfos.getByEmail(email);
	}

	/**
	 * Inserts a new user into the database. <br>
	 * <strong>Security:</strong> Any user may call this method.
	 * 
	 * @param user
	 * @throws InvalidUserException
	 */
	public MaalrUserInfo insert(MaalrUserInfo user) throws InvalidUserException {
		UserInfoDB userInfos = new UserInfoDB();
		return userInfos.insert(user);
	}

	/**
	 * Returns the list of all users. <br>
	 * <strong>Security:</strong> Only admins may call this method.
	 * 
	 * @param role
	 *            the role to match, or <code>null</code> for any role
	 * @param text
	 *            a substring which must match one of email, login, firstname,
	 *            or lastname. <code>null</code> for any.
	 * @param sortColumn
	 *            the column used for sorting, as defined in
	 *            {@link LightUserInfo}. <code>null</code> for any.
	 * @param sortAscending
	 *            true or false, for ascending or descending
	 * @param from
	 *            the start index in the returned list
	 * @param length
	 *            the number of elements to return
	 * @return
	 */

	@Secured(Constants.Roles.ADMIN_5)
	public List<MaalrUserInfo> getAllUsers(Role role, String text, String sortColumn, boolean sortAscending, int from, int length) {
		UserInfoDB userInfos = new UserInfoDB();
		return userInfos.getAllUsers(role, text, sortColumn, sortAscending, from, length);
	}

	@Secured(Constants.Roles.ADMIN_5)
	public List<MaalrUserInfo> getAllUsers(int from, int length, String sortColumn, boolean sortAscending) {
		UserInfoDB userInfos = new UserInfoDB();
		return userInfos.getAllUsers(from, length, sortColumn, sortAscending);
	}

	/**
	 * Returns the number of users in the database. <br>
	 * <strong>Security:</strong> Only admins may call this method.
	 * 
	 * @return
	 */
	@Secured(Constants.Roles.ADMIN_5)
	public int getNumberOfUsers() {
		UserInfoDB userInfos = new UserInfoDB();
		return userInfos.getNumberOfUsers();
	}

	@Secured(Constants.Roles.ADMIN_5)
	public boolean deleteUser(LightUserInfo user) {
		UserInfoDB db = new UserInfoDB();
		return db.deleteUser(user);
	}

}

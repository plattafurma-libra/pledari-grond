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

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.mongo.exceptions.InvalidUserException;

@Service
@Scope(value = "singleton")
public final class MaalrAuthorityGranter implements AuthorityGranter {
	
	@Autowired
	private UserInfoBackend userInfos;
	
	public Set<String> grant(Principal principal) {
		HashSet<String> roles = new HashSet<String>();
		String name = principal.getName();
		if("admin".equals(name)) {
			roles.add(Role.ADMIN_5.getRoleId());
			return roles;
		}
		MaalrUserInfo user = userInfos.getByLogin(name);
		if(user == null) {
			user = new MaalrUserInfo(name, Role.GUEST_1);
			try {
				userInfos.insert(user);
			} catch (InvalidUserException e) {
				throw new RuntimeException(e);
			}
			roles.add(user.getRole().getRoleId());
			return roles;
		}
		roles.add(user.getRole().getRoleId());
		return roles;
	}

}

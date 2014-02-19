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
package de.uni_koeln.spinfo.maalr.common.shared;

import java.io.Serializable;

import de.uni_koeln.spinfo.maalr.common.shared.Constants.Roles;

public enum Role implements Serializable {
	
	ADMIN_5(Roles.ADMIN_5, "Admin"), TRUSTED_IN_4(Roles.TRUSTED_IN_4, "Internal"), TRUSTED_EX_3(Roles.TRUSTED_EX_3, "External"), PERSONA(Roles.PERSONA, "Persona"), OPENID_2(Roles.OPENID_2, "Open ID"), GUEST_1(Roles.GUEST_1, "Guest");
	
	private String role;
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}

	public static final String[] ROLE_DISPLAY_NAMES = new String[] {GUEST_1.roleName, OPENID_2.roleName, PERSONA.roleName, TRUSTED_EX_3.roleName, TRUSTED_IN_4.roleName, ADMIN_5.roleName};

	private Role(String roleId, String roleName) {
		this.role = roleId;
		this.roleName = roleName;
	}

	public String getRoleId() {
		return role;
	}
	
}

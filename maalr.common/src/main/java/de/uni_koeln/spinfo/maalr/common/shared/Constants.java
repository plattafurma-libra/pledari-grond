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



public interface Constants {
	
	public interface Roles {
		
		public static final String ROLE = "Role";
		
		public static final String GUEST_1 = "ROLE_GUEST";
		public static final String OPENID_2 = "ROLE_OPENID";
		public static final String PERSONA = "ROLE_PERSONA";
		public static final String TRUSTED_EX_3 = "ROLE_TRUSTED_EX";
		public static final String TRUSTED_IN_4 = "ROLE_TRUSTED_IN";
		public static final String ADMIN_5 = "ROLE_ADMIN";
		
		public static final String[] ALL_ROLES = new String[] {GUEST_1, OPENID_2, PERSONA, TRUSTED_EX_3, TRUSTED_IN_4, ADMIN_5};
				
	}
	
	public interface Users {
		public static final String LOGIN = "login";
		public static final String PASSWORD = "password";
		public static final String EMAIL = "email";
		public static final String FIRSTNAME = "firstname";
		public static final String LASTNAME = "lastname";
		public static final String TITLE = "title";
		public static final String ROLE = "role";
		public static final String EDITS = "edits";
		public static final String UPVOTES = "upvotes";
		public static final String DOWNVOTES = "downvotes";
		public static final String CREATION_DATE = "creationDate";
		public static final String LAST_MODIFICATION = "lastModDate";
		// SPRING SOCIAL SPECIFIC KEYS
		public static final String PROVIDER_USER_ID = "providerUserId";
		public static final String PROVIDER_ID = "providerId";
	}

	/** ID for reference from the ServletContext. */
	public static final String ID = Constants.class.getName().toLowerCase();

	

	public static final int RQ_ARG_DEFAULT_PAGE_SIZE = 25;
	public static final int RQ_ARG_DEFAULT_PAGE_INDEX = 0;


}

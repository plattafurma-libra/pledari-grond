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

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import de.uni_koeln.spinfo.maalr.common.server.util.Configuration;
import de.uni_koeln.spinfo.maalr.common.shared.Constants;

@Service("postLoginHandler")
public class PostLoginHandler implements AuthenticationSuccessHandler {

	@Autowired private UserInfoBackend backend;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		// Set Session Timeout to one hour
		request.getSession().setMaxInactiveInterval(60 * 60);

		if (authentication != null) {
			request.getSession().setAttribute("uname", authentication.getName());

			String dictContext = Configuration.getInstance().getDictContext();
			if (authentication != null) {
				Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
				if (roles.contains(Constants.Roles.ADMIN_5)) {
					response.sendRedirect(dictContext + "/admin/admin.html");
					return;
				} else if (roles.contains(Constants.Roles.TRUSTED_IN_4)) {
					response.sendRedirect(dictContext + "/editor/editor.html");
					return;
				}
			}
			response.sendRedirect(dictContext + "/index.html");
		}

	}
}

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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@code SocialSignInAdapter} allows you to tie the {@link ProviderSignInController} 
 * in to the application’s sign in process. This implementation is what will talk to Spring Security 
 * to sign a user in to the application.<br><br>
 * 
 * {@code SocialSignInAdapter} implements also the interface {@link UserDetailsService} to support 
 * {@link TokenBasedRememberMeServices}, which sets a remember-me cookie.
 * 
 * 
 * @author Mihail Atanassov (atanassov.mihail@gmail.com)
 */

public final class SocialSignInAdapter implements SignInAdapter, UserDetailsService {

	@Inject
	private UserInfoBackend userInfos;
	
	private TokenBasedRememberMeServices tokenBasedRememberMeServices;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	public SocialSignInAdapter(UserInfoBackend userInfos) {
		this.userInfos = userInfos;
	}

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		
		MaalrUserInfo user = findUserByLogin(userId);
		
		if(user != null) {
			
			signIn(user);
			
			// (optional) set remember-me cookie
			tokenBasedRememberMeServices = new TokenBasedRememberMeServices(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, this);
			tokenBasedRememberMeServices.onLoginSuccess((HttpServletRequest) request.getNativeRequest(), (HttpServletResponse) request.getNativeResponse(), SecurityContextHolder.getContext().getAuthentication());
			tokenBasedRememberMeServices.setTokenValiditySeconds(20 * 60);
			
			// Set session timeout to 20 minutes
			//HttpServletRequest httpServletRequest = (HttpServletRequest) request.getNativeRequest();
			//httpServletRequest.getSession().setMaxInactiveInterval(20 * 60);
			return user.getProviderUserId();
		}
		return null;
	}

	private MaalrUserInfo findUserByLogin(String userId) {
		UserInfoDB db = new UserInfoDB();
		MaalrUserInfo user = db.getByLogin(userId);
		logger.info("USER FOUND: " + user);
		return user;
	}

	private UserDetails signIn(MaalrUserInfo user) {
		UserDetails details = getUserDetails(user);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, details.getPassword(), details.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
		logger.info("AUTHENTICATION: " + authToken);
		return details;
	}

	private UserDetails getUserDetails(MaalrUserInfo user) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleId());
		authorities.add(authority);
		return new User(user.getLogin(), "ignored", authorities);
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		MaalrUserInfo user = findUserByLogin(userId);
		return getUserDetails(user);
	}
}

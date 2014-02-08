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
package de.uni_koeln.spinfo.maalr.login.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.provider.OAuth1AuthenticationService;
import org.springframework.social.security.provider.OAuth2AuthenticationService;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import de.uni_koeln.spinfo.maalr.configuration.Environment;
import de.uni_koeln.spinfo.maalr.login.AutoConnectionSignUp;
import de.uni_koeln.spinfo.maalr.login.SocialSignInAdapter;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;


/**
 * @author  Mihail Atanassov (atanassov.mihail@gmail.com)
 */
@Configuration
public class SocialConfig {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private UserInfoBackend userInfos;

	@Inject
	private DataSource dataSource;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		
		SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();
		
		// add twitter
		if(environment.getAppConfiguration().getTwitterClientId() != null) {
			logger.info("Initializing twitter authentication service...");
			OAuth1ConnectionFactory<Twitter> twitterConnectionFactory = new TwitterConnectionFactory(
					environment.getAppConfiguration().getTwitterClientId(),
					environment.getAppConfiguration().getTwitterClientSecret());
			OAuth1AuthenticationService<Twitter> twitterAuthenticationService = new OAuth1AuthenticationService<Twitter>(
					twitterConnectionFactory);
			registry.addAuthenticationService(twitterAuthenticationService);
		} else {
			logger.info("NOT initializing twitter authentication service...");
		}

		// add facebook
		if(environment.getAppConfiguration().getFaceBookClientId() != null) {
			logger.info("Initializing facebook authentication service...");
			OAuth2ConnectionFactory<Facebook> facebookConnectionFactory = new FacebookConnectionFactory(
					environment.getAppConfiguration().getFaceBookClientId(),
					environment.getAppConfiguration().getFaceBookClientSecret());
			OAuth2AuthenticationService<Facebook> facebookAuthenticationService = new OAuth2AuthenticationService<Facebook>(
					facebookConnectionFactory);
			registry.addAuthenticationService(facebookAuthenticationService);

		} else {
			logger.info("NOT initializing facebook authentication service...");
		}
		
		// add linkedIn
//		OAuth2ConnectionFactory<LinkedIn> linkedInConnectionFactory = new LinkedInConnectionFactory(
//				properties.getLinkedInConsumerKey(),
//				properties.getLinkedInConsumerSecret());
//		OAuth2AuthenticationService<LinkedIn> linkedInAuthenticationService = new OAuth2AuthenticationService<LinkedIn>(
//				linkedInConnectionFactory);
//		registry.addAuthenticationService(linkedInAuthenticationService);
		return registry;
	}

	/**
	 * Singleton data access object providing access to connections across all
	 * users.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(
				dataSource, connectionFactoryLocator(), Encryptors.noOpText());
		repository.setConnectionSignUp(new AutoConnectionSignUp());
		return repository;
	}

	/**
	 * Request-scoped data access object providing access to the current user's
	 * connections.
	 */
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return usersConnectionRepository().createConnectionRepository(userName);
	}

	/**
	 * A proxy to a request-scoped object representing the current user's
	 * primary Facebook account.
	 * 
	 * @throws NotConnectedException
	 *             if the user is not connected to facebook.
	 */
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook() {
		return connectionRepository().getPrimaryConnection(Facebook.class).getApi();
	}
	
	/**
	 * A proxy to a request-scoped object representing the current user's
	 * primary Twitter account.
	 * 
	 * @throws NotConnectedException
	 *             if the user is not connected to twitter.
	 */
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Twitter twitter() {
		return connectionRepository().getPrimaryConnection(Twitter.class).getApi();
	}

	/**
	 * The Spring MVC Controller that allows users to sign-in with their
	 * provider accounts.
	 */
	@Bean
	public ProviderSignInController providerSignInController() {
		ProviderSignInController controller = new ProviderSignInController(connectionFactoryLocator(),
				usersConnectionRepository(), new SocialSignInAdapter(userInfos));
		//FIXME: SpringSocial: Handling not right...
		//Set redirect page after external login (specified in /maalr.gwt/src/main/resources/application.properties)
		String signInUrl = environment.getAppConfiguration().getRedirectUrl();
		//controller.setSignInUrl(signInUrl);
		controller.setPostSignInUrl(signInUrl);
		return controller;
	}
	
	@Bean
	public ConnectController connectController() {
		return new ConnectController(connectionFactoryLocator(), connectionRepository());
	}

}

package de.uni_koeln.spinfo.maalr.login.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;

import de.uni_koeln.spinfo.maalr.common.shared.LightUserInfo;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;

/**
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 */
public class PGAutenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = LoggerFactory.getLogger(PGAutenticationProvider.class);
	
	@Autowired 
	private UserInfoBackend backend;
	
	private UserDetailsService userDetailsService;
	
	@Override
	public Authentication authenticate(Authentication authentication) 
	{
		logout();
		
		String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        UserDetails principal = userDetailsService.loadUserByUsername(username);
        
        if(principal != null) 
        {
        	try {
				if (BCrypt.checkpw(password, principal.getPassword())) 
				{
					Authentication authenticate = new UsernamePasswordAuthenticationToken(principal, 
							"ignored", principal.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authenticate);
					return authenticate;
				}
			} catch (Exception e) 
			{
				LOG.warn("Error occured {}", e);
			} 
        }
        return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService; 
    }

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	public boolean loggedIn() {
		return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
	}

	public String getCurrentUserId() {
		if(loggedIn()) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			return authentication.getName();
		}
		return null;
	}

	public LightUserInfo getCurrentUser() {
		if (loggedIn()) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			MaalrUserInfo user = backend.getByLogin(authentication.getName());
			if (user != null) {
				return user.toLightUser();
			}
		}
		return null;
	}
}

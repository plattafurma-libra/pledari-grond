package de.uni_koeln.spinfo.maalr.login.custom;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.uni_koeln.spinfo.maalr.common.shared.Role;
import de.uni_koeln.spinfo.maalr.login.MaalrUserInfo;
import de.uni_koeln.spinfo.maalr.login.UserInfoBackend;

/**
 * @author Mihail Atanassov <atanassov.mihail@gmail.com>
 */
public class PGAuthUserDetailsService implements UserDetailsService {
	
	private static final Logger LOG  = LoggerFactory.getLogger(PGAuthUserDetailsService.class);
	
	@Autowired 
	private UserInfoBackend backend;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{
		LOG.info("load user: {}", username);
		
		MaalrUserInfo user = backend.getByLogin(username);

		if (null == user) 
		{
			LOG.info("user not found: {}", username);
			throw new UsernameNotFoundException(String.format("User %s not found.", username));
		}

		return getUserDetails(user);
	}
	
	private UserDetails getUserDetails(MaalrUserInfo user) {
		Role role = user.getRole();
		GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleId());
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(authority);
		return new User(user.getLogin(), user.getPassword(), authorities);
	}

}

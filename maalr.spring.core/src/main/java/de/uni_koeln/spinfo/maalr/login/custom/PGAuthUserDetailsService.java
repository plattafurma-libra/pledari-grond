package de.uni_koeln.spinfo.maalr.login.custom;

import java.util.HashSet;
import java.util.Set;

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
	
	
	@Autowired private UserInfoBackend backend;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		MaalrUserInfo user = backend.getByLogin(username);

		if (null == user) {
			throw new UsernameNotFoundException("User " + username + " not found.");
		}

		return getUserDetails(user);
	}
	
	private UserDetails getUserDetails(MaalrUserInfo user) {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		Role role = user.getRole();
		GrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleId());
		authorities.add(authority);
		User details = new User(user.getLogin(), user.getPassword(), authorities);
		return details;
	}

}

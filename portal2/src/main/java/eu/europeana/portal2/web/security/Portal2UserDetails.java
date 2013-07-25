package eu.europeana.portal2.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import eu.europeana.corelib.definitions.db.entity.relational.User;
import eu.europeana.corelib.definitions.users.Role;

public class Portal2UserDetails implements UserDetails {

	private static final long serialVersionUID = -925096405395777537L;

	private User user;

	public Portal2UserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<String> roles = new ArrayList<String>();
		Role role = user.getRole();
		roles.add(role.name());
		if (!role.name().equals(Role.ROLE_USER.name())) {
			roles.add(Role.ROLE_USER.name());
		}
		return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(roles, ","));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

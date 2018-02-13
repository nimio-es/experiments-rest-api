package io.theam.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {

	static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "username", nullable = false, unique = true)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@Column(name = "roles", nullable = false)
	private String roles;

	public User() {}

	public User(final String username, final String password, final String roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
		this.enabled = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> res = Arrays
				.stream(roles.trim().split(","))
				.map(r -> new SimpleGrantedAuthority("ROLE_" + r.toUpperCase()))
				.collect(Collectors.toSet());

		return res;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// we never lock accounts
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// credentials never expire
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	// ---

	public boolean isAdminUser() {
		return this.roles != null
				&& Arrays.asList(
						this.roles
								.trim()
								.toLowerCase()
								.split(","))
				.contains("admin");
	}
}

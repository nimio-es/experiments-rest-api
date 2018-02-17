package io.theam.service;

import io.theam.model.User;
import io.theam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

	private static final User ADMIN_OWNER_ROLE =
			new User(
					"owner",
					"$2a$10$D4OLKI6yy68crm.3imC9X.P2xqKHs5TloWUcr6z5XdOqnTrAK84ri",
					"OWNER");

	@Autowired
	private UserRepository userRepository;

	@Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// tests if it's is the admin. Admin is only in use when there aren't other users with admin role
		if("owner".equalsIgnoreCase(username)) {
			final User adminUser = findAll().stream()
					.filter(u -> "owner".equalsIgnoreCase(u.getUsername()))
					.findFirst()
					.orElse(null);
				return adminUser;
		}

		return userRepository.findOneByUsername(username);
	}

	// ---

	public List<User> findAll() {
		final List<User> storedUsers = userRepository.findAll();

		// ¿no users? Then returns the admin
		if(storedUsers == null || storedUsers.isEmpty())
			return Arrays.asList(ADMIN_OWNER_ROLE);

		// ¿no admin users? The returns the admin
		if(storedUsers.stream().noneMatch(User::isAdminUser))
			return Stream.concat(
					Stream.of(ADMIN_OWNER_ROLE),
					storedUsers.stream())
			.collect(Collectors.toList());

		return storedUsers;
	}

	public User addUser(final String username, final String password, final boolean isAdmin) {
	    return userRepository.save(
	            new User(
	                    username,
                        passwordEncoder.encode(password),
                        isAdmin ? "ADMIN" : "CUSTOMER"));
    }
}
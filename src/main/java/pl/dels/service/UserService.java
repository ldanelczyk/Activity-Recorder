package pl.dels.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.dels.database.repository.RoleRepository;
import pl.dels.database.repository.UserRepository;
import pl.dels.model.Role;
import pl.dels.model.User;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserService {

	private static final String DEFAULT_ROLE = "ROLE_USER";

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User saveUserInDatabase(String username, String password) {
		String bcryptPass = passwordEncoder.encode(password);
		User user = new User(username, bcryptPass);
		addWithDefaultRole(user);
		return user;
	}

	public Role addWithDefaultRole(User user) {
		Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE);
		user.getRoles().add(defaultRole);
		userRepository.save(user);
		return defaultRole;
	}
}

package pl.dels.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.dels.model.Role;
import pl.dels.model.User;
import pl.dels.repository.RoleRepository;
import pl.dels.repository.UserRepository;

@Service
public class UserService {

	private static final String DEFAULT_ROLE = "ROLE_USER";
	private UserRepository userRepository;
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Autowired
	public void setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public void saveUserInDatabase(String username, String password) {
		String bcryptPass = passwordEncoder.encode(password);
		User user = new User(username, bcryptPass);
		addWithDefaultRole(user);
	}

	private void addWithDefaultRole(User user) {
		Role defaultRole = roleRepository.findByRoleName(DEFAULT_ROLE);
		user.getRoles().add(defaultRole);
		userRepository.save(user);
	}
}

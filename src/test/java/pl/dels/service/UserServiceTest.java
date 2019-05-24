package pl.dels.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import pl.dels.model.Role;
import pl.dels.model.User;
import pl.dels.repository.RoleRepository;
import pl.dels.repository.UserRepository;

class UserServiceTest {

	@Test
	void saveUserInDatabase() {

		//given
		User createdUser = createUser();

		UserRepository userRepository = mock(UserRepository.class);
		RoleRepository roleRepository = mock(RoleRepository.class);
		PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

		UserService userService = new UserService(userRepository, roleRepository, passwordEncoder);

		given(passwordEncoder.encode(any(String.class))).willReturn("encodedPassword");
		given(userService.addWithDefaultRole(createdUser)).willReturn(new Role());
		given(userRepository.save(any(User.class))).willReturn(createdUser);

		//when
		User returnedUser = userService.saveUserInDatabase("username", "password");
		ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);

		//then
		verify(userRepository, times(1)).save(userArgument.capture());
		assertEquals(createdUser, returnedUser);
	}

	private User createUser() {
		User user = new User("username", "encodedPassword");
		return user;
	}
}

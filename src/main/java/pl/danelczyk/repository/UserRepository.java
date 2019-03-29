package pl.danelczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.dels.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	User findByEmail(String email);
}



package pl.danelczyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.dels.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRoleName(String roleName);
}
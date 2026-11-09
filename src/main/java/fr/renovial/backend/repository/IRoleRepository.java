 package fr.renovial.backend.repository;

import fr.renovial.backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String roleUser);
}


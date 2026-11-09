package fr.renovial.backend.repository;

import fr.renovial.backend.model.utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<utilisateur, Long> {
    Optional<utilisateur> findByEmail(String email);
}

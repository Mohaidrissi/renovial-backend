package fr.renovial.backend.repository;

import fr.renovial.backend.model.Unite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUniteRepository extends JpaRepository<Unite, Integer> {
    Optional<Unite> findByCode(String code);
}

package fr.renovial.backend.repository;

import fr.renovial.backend.model.StatutDevis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IStatutDevisRepository extends JpaRepository<StatutDevis, Integer> {

    Optional<StatutDevis> findByCode(String code);
}
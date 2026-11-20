package fr.renovial.backend.repository;

import fr.renovial.backend.model.Devis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDevisRepository extends JpaRepository<Devis, Integer> {
}
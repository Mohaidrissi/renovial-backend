package fr.renovial.backend.repository;

import fr.renovial.backend.model.TypeBien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import fr.renovial.backend.model.TypeBien;

public interface ITypeBienRepository extends JpaRepository<TypeBien, Integer> {
    Optional<TypeBien> findByLibelle(String libelle);
}
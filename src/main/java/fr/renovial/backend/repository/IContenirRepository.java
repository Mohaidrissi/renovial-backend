package fr.renovial.backend.repository;

import fr.renovial.backend.model.Contenir;
import fr.renovial.backend.model.ContenirId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IContenirRepository extends JpaRepository<Contenir, ContenirId> {
    List<Contenir> findByDevisIdDevis(Integer idDevis);
}
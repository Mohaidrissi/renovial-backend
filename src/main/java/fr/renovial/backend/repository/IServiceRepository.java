package fr.renovial.backend.repository;

import fr.renovial.backend.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findByActifTrue();

}
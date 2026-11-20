package fr.renovial.backend.controller;

import fr.renovial.backend.model.Service;
import fr.renovial.backend.repository.IServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final IServiceRepository serviceRepository;



    @GetMapping("/actifs")
    public List<Service> getServicesActifs() {
        return serviceRepository.findByActifTrue();
    }
}
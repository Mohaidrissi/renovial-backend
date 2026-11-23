package fr.renovial.backend.controller;

import fr.renovial.backend.dto.response.PrestationResponse;
import fr.renovial.backend.service.IPrestationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final IPrestationService prestationService;

    @GetMapping("/actifs")
    public List<PrestationResponse> getServicesActifs() {
        return prestationService.getServicesActifs();
    }
}
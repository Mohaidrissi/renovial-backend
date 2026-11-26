package fr.renovial.backend.controller;

import fr.renovial.backend.dto.request.ServiceRequest;
import fr.renovial.backend.dto.response.PrestationResponse;
import fr.renovial.backend.dto.response.UniteResponse;
import fr.renovial.backend.service.IPrestationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Réservé à l'admin : /api/admin/** demande ROLE_ADMIN (SecurityConfiguration)
@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
@Slf4j
public class AdminServiceController {

    private final IPrestationService prestationService;

    @GetMapping
    public ResponseEntity<List<PrestationResponse>> getTousLesServices() {
        log.debug("REST request to get all services (admin)");
        return ResponseEntity.ok(prestationService.getTousLesServices());
    }

    @GetMapping("/unites")
    public ResponseEntity<List<UniteResponse>> getUnites() {
        return ResponseEntity.ok(prestationService.getUnites());
    }

    @PostMapping
    public ResponseEntity<PrestationResponse> creerService(@RequestBody ServiceRequest request) {
        log.debug("REST request to create service {}", request.nom());
        return ResponseEntity.status(HttpStatus.CREATED).body(prestationService.creerService(request));
    }

    @PutMapping("/{idService}")
    public ResponseEntity<PrestationResponse> modifierService(@PathVariable Integer idService,
                                                              @RequestBody ServiceRequest request) {
        log.debug("REST request to update service {}", idService);
        return ResponseEntity.ok(prestationService.modifierService(idService, request));
    }
}

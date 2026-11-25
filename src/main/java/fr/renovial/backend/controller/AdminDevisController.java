package fr.renovial.backend.controller;

import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.service.IDevisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/devis")
@RequiredArgsConstructor
@Slf4j
public class AdminDevisController {

    private final IDevisService devisService;

    @GetMapping
    public ResponseEntity<List<DevisAdminResponse>> getTousLesDevis() {
        log.debug("REST request to get all devis (admin)");
        return ResponseEntity.ok(devisService.getTousLesDevis());
    }
}
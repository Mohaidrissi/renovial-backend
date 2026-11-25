package fr.renovial.backend.controller;

import fr.renovial.backend.dto.request.StatutDevisRequest;
import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.service.IDevisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping("/{idDevis}/statut")
    public ResponseEntity<DevisAdminResponse> changerStatut(@PathVariable Integer idDevis,
                                                            @RequestBody StatutDevisRequest request) {
        log.debug("REST request to change statut of devis {} to {}", idDevis, request.code());
        return ResponseEntity.ok(devisService.changerStatut(idDevis, request.code()));
    }
}

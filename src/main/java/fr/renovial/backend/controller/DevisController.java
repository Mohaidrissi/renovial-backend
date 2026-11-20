package fr.renovial.backend.controller;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.response.DevisResponse;
import fr.renovial.backend.service.IDevisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devis")
@RequiredArgsConstructor
@Slf4j
public class DevisController {

    private final IDevisService devisService;

    @PostMapping
    public ResponseEntity<DevisResponse> creerDevis(@RequestBody DevisRequest request) {
        log.debug("REST request to create devis");
        DevisResponse response = devisService.creerDevis(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
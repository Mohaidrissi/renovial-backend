package fr.renovial.backend.controller;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.dto.response.DevisDetailResponse;
import fr.renovial.backend.dto.response.DevisResponse;
import fr.renovial.backend.model.Utilisateur;
import fr.renovial.backend.service.IDevisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    // Le client connecté voit seulement ses devis : l'id vient du token, pas de l'URL
    @GetMapping("/mes-devis")
    public ResponseEntity<List<DevisAdminResponse>> getMesDevis(@AuthenticationPrincipal Utilisateur utilisateur) {
        log.debug("REST request to get devis of utilisateur {}", utilisateur.getIdUtilisateur());
        return ResponseEntity.ok(devisService.getMesDevis(utilisateur.getIdUtilisateur()));
    }

    // Détail d'un devis : le service vérifie que le devis appartient bien au client connecté
    @GetMapping("/mes-devis/{idDevis}")
    public ResponseEntity<DevisDetailResponse> getMonDevis(@PathVariable Integer idDevis,
                                                           @AuthenticationPrincipal Utilisateur utilisateur) {
        log.debug("REST request to get devis {} of utilisateur {}", idDevis, utilisateur.getIdUtilisateur());
        return ResponseEntity.ok(devisService.getMonDevis(idDevis, utilisateur.getIdUtilisateur()));
    }
}

package fr.renovial.backend.controller;

import fr.renovial.backend.dto.request.ActifRequest;
import fr.renovial.backend.dto.response.UtilisateurAdminResponse;
import fr.renovial.backend.model.Utilisateur;
import fr.renovial.backend.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Réservé à l'admin : /api/admin/** demande ROLE_ADMIN (SecurityConfiguration)
@RestController
@RequestMapping("/api/admin/utilisateurs")
@RequiredArgsConstructor
@Slf4j
public class AdminUtilisateurController {

    private final IUtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<List<UtilisateurAdminResponse>> getTousLesUtilisateurs() {
        log.debug("REST request to get all utilisateurs (admin)");
        return ResponseEntity.ok(utilisateurService.getTousLesUtilisateurs());
    }

    @PutMapping("/{idUtilisateur}/actif")
    public ResponseEntity<UtilisateurAdminResponse> changerActif(@PathVariable Integer idUtilisateur,
                                                                 @RequestBody ActifRequest request,
                                                                 @AuthenticationPrincipal Utilisateur admin) {
        log.debug("REST request to set actif={} for utilisateur {}", request.actif(), idUtilisateur);
        return ResponseEntity.ok(utilisateurService.changerActif(idUtilisateur, request.actif(), admin.getIdUtilisateur()));
    }
}

package fr.renovial.backend.controller;

import fr.renovial.backend.dto.request.ProfilRequest;
import fr.renovial.backend.dto.response.ProfilResponse;
import fr.renovial.backend.model.Utilisateur;
import fr.renovial.backend.service.IProfilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Profil de l'utilisateur connecté : l'id vient du token, jamais de l'URL
@RestController
@RequestMapping("/api/profil")
@RequiredArgsConstructor
@Slf4j
public class ProfilController {

    private final IProfilService profilService;

    @GetMapping
    public ResponseEntity<ProfilResponse> getProfil(@AuthenticationPrincipal Utilisateur utilisateur) {
        log.debug("REST request to get profil of utilisateur {}", utilisateur.getIdUtilisateur());
        return ResponseEntity.ok(profilService.getProfil(utilisateur.getIdUtilisateur()));
    }

    @PutMapping
    public ResponseEntity<ProfilResponse> modifierProfil(@AuthenticationPrincipal Utilisateur utilisateur,
                                                         @RequestBody ProfilRequest request) {
        log.debug("REST request to update profil of utilisateur {}", utilisateur.getIdUtilisateur());
        return ResponseEntity.ok(profilService.modifierProfil(utilisateur.getIdUtilisateur(), request));
    }
}

package fr.renovial.backend.dto.response;

import java.time.LocalDateTime;

// Un compte vu par l'admin (jamais le mot de passe)
public record UtilisateurAdminResponse(
        Integer idUtilisateur,
        String nom,
        String email,
        String telephone,
        String role,
        Boolean actif,
        LocalDateTime dateCreation
) {
}

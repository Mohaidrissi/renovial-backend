package fr.renovial.backend.dto.response;

import java.time.LocalDateTime;

// Ce que le client voit de son compte (jamais le mot de passe)
public record ProfilResponse(
        String nom,
        String email,
        String telephone,
        LocalDateTime dateCreation
) {
}

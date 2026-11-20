package fr.renovial.backend.dto.request;

import java.util.List;

public record DevisRequest(
        String nom,
        String email,
        String telephone,
        String precisions,
        Integer idTypeBien,
        List<LigneDevisRequest> travaux
) {
}
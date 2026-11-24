package fr.renovial.backend.dto.response;

import java.math.BigDecimal;

public record PrestationResponse(
        Integer idService,
        String nom,
        String description,
        BigDecimal prixUnitaire,
        Boolean actif,
        Boolean economieEnergie,
        UniteResponse unite
) {
}
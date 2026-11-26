package fr.renovial.backend.dto.request;

import java.math.BigDecimal;

// Création ou modification d'une prestation par l'admin.
// L'unité est donnée par son code (M2, ML, U...), qui est unique en base.
public record ServiceRequest(
        String nom,
        String description,
        BigDecimal prixUnitaire,
        Boolean economieEnergie,
        Boolean actif,
        String codeUnite
) {
}

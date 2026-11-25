package fr.renovial.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DevisDetailResponse(
        Integer idDevis,
        String reference,
        LocalDateTime dateCreation,
        String typeBien,
        String statut,
        String precisions,
        String commentaireAdmin,
        BigDecimal totalHt,
        BigDecimal totalTtc,
        List<Ligne> lignes
) {
    // Une ligne de travaux du devis
    public record Ligne(
            String service,
            BigDecimal quantite,
            String unite,
            BigDecimal prixUnitaire,
            BigDecimal tauxTva,
            BigDecimal totalHt
    ) {
    }
}

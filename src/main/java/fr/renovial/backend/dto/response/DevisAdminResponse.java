package fr.renovial.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DevisAdminResponse(
        Integer idDevis,
        String reference,
        LocalDateTime dateCreation,
        String nomContact,
        String emailContact,
        String typeBien,
        String statut,
        BigDecimal totalHt,
        BigDecimal totalTtc,
        String commentaireAdmin
) {
}
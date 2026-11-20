package fr.renovial.backend.dto.request;

import java.math.BigDecimal;

public record LigneDevisRequest(
        Integer idService,
        BigDecimal quantite
) {
}
package fr.renovial.backend.dto.response;

import java.math.BigDecimal;

public record DevisResponse(
        String reference,
        BigDecimal totalHt,
        BigDecimal totalTva,
        BigDecimal totalTtc
) {
}
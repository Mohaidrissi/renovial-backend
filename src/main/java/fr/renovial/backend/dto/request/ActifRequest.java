package fr.renovial.backend.dto.request;

// true = débloquer le compte, false = le bloquer
public record ActifRequest(Boolean actif) {
}

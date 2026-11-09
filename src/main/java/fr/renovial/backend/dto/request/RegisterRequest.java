package fr.renovial.backend.dto.request;

public record RegisterRequest(
        String email,
        String password,
        String nom,
        String telephone
) {
}

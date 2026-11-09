package fr.renovial.backend.dto.request;

public record AuthRequest(
        String email,
        String password
) {
}

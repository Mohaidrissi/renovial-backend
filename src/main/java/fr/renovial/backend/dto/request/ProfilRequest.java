package fr.renovial.backend.dto.request;

// Seuls le nom et le téléphone sont modifiables (l'email sert à se connecter)
public record ProfilRequest(
        String nom,
        String telephone
) {
}

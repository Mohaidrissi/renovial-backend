package fr.renovial.backend.dto.request;

// Nouveau statut choisi par l'admin : "ACCEPTE" ou "REFUSE", avec un commentaire pour le client
public record StatutDevisRequest(String code, String commentaire) {
}

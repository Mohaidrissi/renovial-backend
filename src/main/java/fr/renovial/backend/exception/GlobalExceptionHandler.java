package fr.renovial.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Transforme les erreurs de saisie en réponse 400 avec un message clair (au lieu d'une erreur 500)
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Erreurs lancées par les services : "Aucun travail sélectionné.", "Cette adresse email est déjà utilisée."...
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> erreurDeSaisie(IllegalArgumentException e) {
        log.debug("Requête refusée : {}", e.getMessage());
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    // JSON mal formé ou mauvais type (ex : une quantité "abc")
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonInvalide(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Map.of("message", "Les données envoyées sont invalides."));
    }
}

package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.response.UtilisateurAdminResponse;
import fr.renovial.backend.model.Utilisateur;
import fr.renovial.backend.repository.IUserRepository;
import fr.renovial.backend.service.IUtilisateurService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UtilisateurService implements IUtilisateurService {

    private final IUserRepository userRepository;

    @Override
    public List<UtilisateurAdminResponse> getTousLesUtilisateurs() {
        return userRepository.findAllByOrderByDateCreationDesc().stream()
                .map(this::versResponse)
                .toList();
    }

    // Bloquer = actif à false : le compte ne peut plus se connecter, et son token est refusé (JwtAuthFilter)
    @Override
    public UtilisateurAdminResponse changerActif(Integer idUtilisateur, Boolean actif, Integer idAdminConnecte) {
        if (actif == null) {
            throw new IllegalArgumentException("Indiquez si le compte est actif ou non.");
        }
        if (idUtilisateur.equals(idAdminConnecte)) {
            throw new IllegalArgumentException("Vous ne pouvez pas bloquer votre propre compte.");
        }
        Utilisateur utilisateur = userRepository.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
        utilisateur.setActif(actif);
        userRepository.save(utilisateur);
        return versResponse(utilisateur);
    }

    private UtilisateurAdminResponse versResponse(Utilisateur u) {
        return new UtilisateurAdminResponse(
                u.getIdUtilisateur(),
                u.getNom(),
                u.getEmail(),
                u.getTelephone(),
                u.getRole().getName(),
                u.getActif(),
                u.getDateCreation());
    }
}

package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.ProfilRequest;
import fr.renovial.backend.dto.response.ProfilResponse;
import fr.renovial.backend.model.Utilisateur;
import fr.renovial.backend.repository.IUserRepository;
import fr.renovial.backend.service.IProfilService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfilService implements IProfilService {

    private final IUserRepository userRepository;

    @Override
    public ProfilResponse getProfil(Integer idUtilisateur) {
        return versResponse(trouver(idUtilisateur));
    }

    // Le client change son nom et son téléphone. Les limites sont celles des colonnes en base.
    @Override
    public ProfilResponse modifierProfil(Integer idUtilisateur, ProfilRequest request) {
        String nom = request.nom() == null ? "" : request.nom().trim();
        String telephone = request.telephone() == null ? "" : request.telephone().trim();

        if (nom.isEmpty() || nom.length() > 100) {
            throw new IllegalArgumentException("Le nom est obligatoire (100 caractères maximum).");
        }
        if (telephone.isEmpty() || telephone.length() > 20) {
            throw new IllegalArgumentException("Le téléphone est obligatoire (20 caractères maximum).");
        }

        Utilisateur utilisateur = trouver(idUtilisateur);
        utilisateur.setNom(nom);
        utilisateur.setTelephone(telephone);
        userRepository.save(utilisateur);
        return versResponse(utilisateur);
    }

    private Utilisateur trouver(Integer idUtilisateur) {
        return userRepository.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
    }

    private ProfilResponse versResponse(Utilisateur u) {
        return new ProfilResponse(u.getNom(), u.getEmail(), u.getTelephone(), u.getDateCreation());
    }
}

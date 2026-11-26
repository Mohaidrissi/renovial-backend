package fr.renovial.backend.service;

import fr.renovial.backend.dto.response.UtilisateurAdminResponse;

import java.util.List;

public interface IUtilisateurService {
    List<UtilisateurAdminResponse> getTousLesUtilisateurs();
    UtilisateurAdminResponse changerActif(Integer idUtilisateur, Boolean actif, Integer idAdminConnecte);
}

package fr.renovial.backend.service;

import fr.renovial.backend.dto.request.ProfilRequest;
import fr.renovial.backend.dto.response.ProfilResponse;

public interface IProfilService {
    ProfilResponse getProfil(Integer idUtilisateur);
    ProfilResponse modifierProfil(Integer idUtilisateur, ProfilRequest request);
}

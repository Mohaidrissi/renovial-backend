package fr.renovial.backend.service;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.dto.response.DevisDetailResponse;
import fr.renovial.backend.dto.response.DevisResponse;

import java.util.List;

public interface IDevisService {
    DevisResponse creerDevis(DevisRequest request);
    List<DevisAdminResponse> getTousLesDevis();
    DevisAdminResponse changerStatut(Integer idDevis, String code, String commentaire);
    List<DevisAdminResponse> getMesDevis(Integer idUtilisateur);
    DevisDetailResponse getMonDevis(Integer idDevis, Integer idUtilisateur);
}
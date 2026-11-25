package fr.renovial.backend.service;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.dto.response.DevisResponse;

import java.util.List;

public interface IDevisService {
    DevisResponse creerDevis(DevisRequest request);
    List<DevisAdminResponse> getTousLesDevis();
}
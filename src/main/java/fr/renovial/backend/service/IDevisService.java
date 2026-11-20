package fr.renovial.backend.service;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.response.DevisResponse;

public interface IDevisService {
    DevisResponse creerDevis(DevisRequest request);
}
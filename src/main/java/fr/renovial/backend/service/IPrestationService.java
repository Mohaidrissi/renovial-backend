package fr.renovial.backend.service;

import fr.renovial.backend.dto.response.PrestationResponse;

import java.util.List;

public interface IPrestationService {
    List<PrestationResponse> getServicesActifs();
}
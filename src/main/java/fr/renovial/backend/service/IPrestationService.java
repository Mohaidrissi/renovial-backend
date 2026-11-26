package fr.renovial.backend.service;

import fr.renovial.backend.dto.request.ServiceRequest;
import fr.renovial.backend.dto.response.PrestationResponse;
import fr.renovial.backend.dto.response.UniteResponse;

import java.util.List;

public interface IPrestationService {
    List<PrestationResponse> getServicesActifs();

    // Partie admin
    List<PrestationResponse> getTousLesServices();
    List<UniteResponse> getUnites();
    PrestationResponse creerService(ServiceRequest request);
    PrestationResponse modifierService(Integer idService, ServiceRequest request);
}

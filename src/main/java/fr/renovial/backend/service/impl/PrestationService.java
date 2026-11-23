package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.response.PrestationResponse;
import fr.renovial.backend.dto.response.UniteResponse;
import fr.renovial.backend.repository.IServiceRepository;
import fr.renovial.backend.service.IPrestationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrestationService implements IPrestationService {

    private final IServiceRepository serviceRepository;

    @Override
    public List<PrestationResponse> getServicesActifs() {
        return serviceRepository.findByActifTrue().stream()
                .map(s -> new PrestationResponse(
                        s.getIdService(),
                        s.getNom(),
                        s.getDescription(),
                        s.getPrixUnitaire(),
                        s.getActif(),
                        new UniteResponse(s.getUnite().getCode(), s.getUnite().getLibelle())
                ))
                .toList();
    }
}
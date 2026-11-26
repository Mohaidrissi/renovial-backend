package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.ServiceRequest;
import fr.renovial.backend.dto.response.PrestationResponse;
import fr.renovial.backend.dto.response.UniteResponse;
import fr.renovial.backend.model.Unite;
import fr.renovial.backend.repository.IServiceRepository;
import fr.renovial.backend.repository.IUniteRepository;
import fr.renovial.backend.service.IPrestationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PrestationService implements IPrestationService {

    private final IServiceRepository serviceRepository;
    private final IUniteRepository uniteRepository;

    @Override
    public List<PrestationResponse> getServicesActifs() {
        return serviceRepository.findByActifTrue().stream()
                .map(this::versResponse)
                .toList();
    }

    // Admin : tous les services, même ceux désactivés
    @Override
    public List<PrestationResponse> getTousLesServices() {
        return serviceRepository.findAllByOrderByNomAsc().stream()
                .map(this::versResponse)
                .toList();
    }

    @Override
    public List<UniteResponse> getUnites() {
        return uniteRepository.findAll().stream()
                .map(u -> new UniteResponse(u.getCode(), u.getLibelle()))
                .toList();
    }

    @Override
    public PrestationResponse creerService(ServiceRequest request) {
        verifier(request);
        if (serviceRepository.existsByNomIgnoreCase(request.nom().trim())) {
            throw new IllegalArgumentException("Un service porte déjà ce nom.");
        }
        fr.renovial.backend.model.Service service = new fr.renovial.backend.model.Service();
        remplir(service, request);
        return versResponse(serviceRepository.save(service));
    }

    // Pas de suppression : les anciens devis gardent leurs lignes. On désactive avec actif = false.
    @Override
    public PrestationResponse modifierService(Integer idService, ServiceRequest request) {
        verifier(request);
        fr.renovial.backend.model.Service service = serviceRepository.findById(idService)
                .orElseThrow(() -> new IllegalArgumentException("Service introuvable."));
        if (serviceRepository.existsByNomIgnoreCaseAndIdServiceNot(request.nom().trim(), idService)) {
            throw new IllegalArgumentException("Un service porte déjà ce nom.");
        }
        remplir(service, request);
        return versResponse(serviceRepository.save(service));
    }

    // Règles communes à la création et à la modification (limites = colonnes en base)
    private void verifier(ServiceRequest request) {
        if (request.nom() == null || request.nom().isBlank() || request.nom().trim().length() > 100) {
            throw new IllegalArgumentException("Le nom est obligatoire (100 caractères maximum).");
        }
        if (request.prixUnitaire() == null || request.prixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à zéro.");
        }
        if (request.prixUnitaire().compareTo(new BigDecimal("99999999.99")) > 0) {
            throw new IllegalArgumentException("Le prix est trop grand.");
        }
    }

    private void remplir(fr.renovial.backend.model.Service service, ServiceRequest request) {
        Unite unite = uniteRepository.findByCode(request.codeUnite())
                .orElseThrow(() -> new IllegalArgumentException("Unité introuvable."));
        service.setNom(request.nom().trim());
        service.setDescription(request.description() == null || request.description().isBlank()
                ? null : request.description().trim());
        service.setPrixUnitaire(request.prixUnitaire());
        service.setEconomieEnergie(Boolean.TRUE.equals(request.economieEnergie()));
        service.setActif(!Boolean.FALSE.equals(request.actif())); // actif par défaut
        service.setUnite(unite);
    }

    private PrestationResponse versResponse(fr.renovial.backend.model.Service s) {
        return new PrestationResponse(
                s.getIdService(),
                s.getNom(),
                s.getDescription(),
                s.getPrixUnitaire(),
                s.getActif(),
                s.getEconomieEnergie(),
                new UniteResponse(s.getUnite().getCode(), s.getUnite().getLibelle()));
    }
}

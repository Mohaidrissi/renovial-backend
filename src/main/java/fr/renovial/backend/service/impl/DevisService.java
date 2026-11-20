package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.request.LigneDevisRequest;
import fr.renovial.backend.dto.response.DevisResponse;
import fr.renovial.backend.model.*;
import fr.renovial.backend.repository.*;
import fr.renovial.backend.service.IDevisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DevisService implements IDevisService {

    private final IDevisRepository devisRepository;
    private final IContenirRepository contenirRepository;
    private final IServiceRepository serviceRepository;
    private final ITypeBienRepository typeBienRepository;
    private final IStatutDevisRepository statutDevisRepository;
    private final IUserRepository userRepository;

    @Override
    public DevisResponse creerDevis(DevisRequest request) {

        if (request.travaux() == null || request.travaux().isEmpty()) {
            throw new IllegalArgumentException("Aucun travail sélectionné.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateur = null;

        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof Utilisateur) {
            utilisateur = (Utilisateur) authentication.getPrincipal();
        }

        TypeBien typeBien = typeBienRepository.findById(request.idTypeBien())
                .orElseThrow(() -> new IllegalArgumentException("Type de bien introuvable."));

        StatutDevis statut = statutDevisRepository.findByCode("EN_ATTENTE")
                .orElseThrow(() -> new IllegalArgumentException("Statut EN_ATTENTE introuvable."));

        Devis devis = new Devis();

        devis.setReference("DEV-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase());
        devis.setDateCreation(LocalDateTime.now());
        devis.setTauxTva(new BigDecimal("5.50"));
        devis.setTotalHt(BigDecimal.ZERO);
        devis.setTotalTtc(BigDecimal.ZERO);
        devis.setPrecisions(request.precisions());
        devis.setTypeBien(typeBien);
        devis.setStatutDevis(statut);

        if (utilisateur != null) {
            devis.setUtilisateur(utilisateur);
            devis.setNomContact(utilisateur.getNom());
            devis.setEmailContact(utilisateur.getEmail());
            devis.setTelephoneContact(utilisateur.getTelephone());
        } else {
            if (request.nom() == null || request.nom().isBlank()
                    || request.email() == null || request.email().isBlank()) {
                throw new IllegalArgumentException("Le nom et l'email sont obligatoires.");
            }

            devis.setNomContact(request.nom());
            devis.setEmailContact(request.email());
            devis.setTelephoneContact(request.telephone());
        }

        devisRepository.save(devis);

        BigDecimal totalHt = BigDecimal.ZERO;

        for (LigneDevisRequest ligne : request.travaux()) {

            if (ligne.quantite() == null || ligne.quantite().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("La quantité doit être supérieure à zéro.");
            }

            fr.renovial.backend.model.Service prestation =
                    serviceRepository.findById(ligne.idService())
                            .orElseThrow(() -> new IllegalArgumentException("Service introuvable."));

            if (!Boolean.TRUE.equals(prestation.getActif())) {
                throw new IllegalArgumentException("Ce service n'est plus disponible.");
            }

            BigDecimal prixUnitaire = prestation.getPrixUnitaire();

            BigDecimal montantLigne = prixUnitaire.multiply(ligne.quantite())
                    .setScale(2, RoundingMode.HALF_UP);

            totalHt = totalHt.add(montantLigne);

            Contenir contenir = new Contenir();

            ContenirId contenirId = new ContenirId();
            contenirId.setIdService(prestation.getIdService());
            contenirId.setIdDevis(devis.getIdDevis());

            contenir.setId(contenirId);
            contenir.setService(prestation);
            contenir.setDevis(devis);
            contenir.setQuantite(ligne.quantite());
            contenir.setPrixUnitaire(prixUnitaire);

            contenirRepository.save(contenir);
        }

        totalHt = totalHt.setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalTva = totalHt
                .multiply(new BigDecimal("5.50"))
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal totalTtc = totalHt.add(totalTva);

        devis.setTotalHt(totalHt);
        devis.setTotalTtc(totalTtc);

        devisRepository.save(devis);

        return new DevisResponse(
                devis.getReference(),
                totalHt,
                totalTva,
                totalTtc
        );
    }
}
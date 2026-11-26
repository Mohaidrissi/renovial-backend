package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.request.LigneDevisRequest;
import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.dto.response.DevisDetailResponse;
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
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DevisService implements IDevisService {


    private static final BigDecimal TVA_LOCAL_PRO = new BigDecimal("20.00");
    private static final BigDecimal TVA_ECONOMIE_ENERGIE = new BigDecimal("5.50");
    private static final BigDecimal TVA_AUTRES_TRAVAUX = new BigDecimal("10.00");

    private final IDevisRepository devisRepository;
    private final IContenirRepository contenirRepository;
    private final IServiceRepository serviceRepository;
    private final ITypeBienRepository typeBienRepository;
    private final IStatutDevisRepository statutDevisRepository;


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
        BigDecimal totalTva = BigDecimal.ZERO;

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
            BigDecimal tauxTva = choisirTauxTva(typeBien, prestation);

            BigDecimal montantLigne = prixUnitaire.multiply(ligne.quantite())
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal tvaLigne = montantLigne.multiply(tauxTva)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            totalHt = totalHt.add(montantLigne);
            totalTva = totalTva.add(tvaLigne);

            Contenir contenir = new Contenir();

            ContenirId contenirId = new ContenirId();
            contenirId.setIdService(prestation.getIdService());
            contenirId.setIdDevis(devis.getIdDevis());

            contenir.setId(contenirId);
            contenir.setService(prestation);
            contenir.setDevis(devis);
            contenir.setQuantite(ligne.quantite());
            contenir.setPrixUnitaire(prixUnitaire);
            contenir.setTauxTva(tauxTva);

            contenirRepository.save(contenir);
        }

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

    // Liste de tous les devis pour l'administrateur, du plus récent au plus ancien
    @Override
    public List<DevisAdminResponse> getTousLesDevis() {
        return devisRepository.findAllByOrderByDateCreationDesc().stream()
                .map(this::versAdminResponse)
                .toList();
    }

    // Devis du client connecté, du plus récent au plus ancien
    @Override
    public List<DevisAdminResponse> getMesDevis(Integer idUtilisateur) {
        return devisRepository.findByUtilisateurIdUtilisateurOrderByDateCreationDesc(idUtilisateur).stream()
                .map(this::versAdminResponse)
                .toList();
    }

    // Détail d'un devis du client connecté. Le devis d'un autre client donne le même message
    // que un devis inexistant : on ne dit pas qu'il existe.
    @Override
    public DevisDetailResponse getMonDevis(Integer idDevis, Integer idUtilisateur) {
        Devis devis = devisRepository.findById(idDevis)
                .filter(d -> d.getUtilisateur() != null && d.getUtilisateur().getIdUtilisateur().equals(idUtilisateur))
                .orElseThrow(() -> new IllegalArgumentException("Devis introuvable."));

        List<DevisDetailResponse.Ligne> lignes = contenirRepository.findByDevisIdDevis(idDevis).stream()
                .map(c -> new DevisDetailResponse.Ligne(
                        c.getService().getNom(),
                        c.getQuantite(),
                        c.getService().getUnite().getCode(),
                        c.getPrixUnitaire(),
                        c.getTauxTva(),
                        c.getPrixUnitaire().multiply(c.getQuantite()).setScale(2, RoundingMode.HALF_UP)))
                .toList();

        return new DevisDetailResponse(
                devis.getIdDevis(),
                devis.getReference(),
                devis.getDateCreation(),
                devis.getTypeBien().getLibelle(),
                devis.getStatutDevis().getLibelle(),
                devis.getPrecisions(),
                devis.getCommentaireAdmin(),
                devis.getTotalHt(),
                devis.getTotalTtc(),
                lignes);
    }

    // L'admin accepte ou refuse un devis qui est encore en attente.
    // Le commentaire est lu par le client ; il est obligatoire pour un refus.
    @Override
    public DevisAdminResponse changerStatut(Integer idDevis, String code, String commentaire) {
        if (!"ACCEPTE".equals(code) && !"REFUSE".equals(code)) {
            throw new IllegalArgumentException("Statut invalide : choisir ACCEPTE ou REFUSE.");
        }

        String texte = commentaire == null ? "" : commentaire.trim();
        if ("REFUSE".equals(code) && texte.isEmpty()) {
            throw new IllegalArgumentException("Expliquez au client pourquoi le devis est refusé.");
        }
        if (texte.length() > 1000) {
            throw new IllegalArgumentException("Le commentaire est trop long (1000 caractères maximum).");
        }

        Devis devis = devisRepository.findById(idDevis)
                .orElseThrow(() -> new IllegalArgumentException("Devis introuvable."));

        if (!"EN_ATTENTE".equals(devis.getStatutDevis().getCode())) {
            throw new IllegalArgumentException("Ce devis a déjà été traité.");
        }

        StatutDevis statut = statutDevisRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Statut " + code + " introuvable."));

        devis.setStatutDevis(statut);
        devis.setCommentaireAdmin(texte.isEmpty() ? null : texte);
        devis.setDateTraitement(LocalDateTime.now());
        devisRepository.save(devis);
        return versAdminResponse(devis);
    }

    private DevisAdminResponse versAdminResponse(Devis devis) {
        return new DevisAdminResponse(
                devis.getIdDevis(),
                devis.getReference(),
                devis.getDateCreation(),
                devis.getNomContact(),
                devis.getEmailContact(),
                devis.getTypeBien().getLibelle(),
                devis.getStatutDevis().getLibelle(),
                devis.getTotalHt(),
                devis.getTotalTtc(),
                devis.getCommentaireAdmin());
    }

    private BigDecimal choisirTauxTva(TypeBien typeBien, fr.renovial.backend.model.Service prestation) {
        if (Boolean.TRUE.equals(typeBien.getProfessionnel())) {
            return TVA_LOCAL_PRO;
        }
        if (Boolean.TRUE.equals(prestation.getEconomieEnergie())) {
            return TVA_ECONOMIE_ENERGIE;
        }
        return TVA_AUTRES_TRAVAUX;
    }
}
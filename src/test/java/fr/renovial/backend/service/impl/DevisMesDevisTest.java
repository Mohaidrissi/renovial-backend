package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.dto.response.DevisDetailResponse;
import fr.renovial.backend.model.*;
import fr.renovial.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DevisMesDevisTest {

    @Mock private IDevisRepository devisRepository;
    @Mock private IContenirRepository contenirRepository;
    @Mock private IServiceRepository serviceRepository;
    @Mock private ITypeBienRepository typeBienRepository;
    @Mock private IStatutDevisRepository statutDevisRepository;

    @InjectMocks private DevisService devisService;

    @Test
    void leClientRecoitSesDevis() {
        Devis devis = new Devis();
        devis.setReference("DEV-1");
        devis.setTypeBien(new TypeBien());
        StatutDevis statut = new StatutDevis();
        statut.setLibelle("En attente");
        devis.setStatutDevis(statut);
        when(devisRepository.findByUtilisateurIdUtilisateurOrderByDateCreationDesc(5)).thenReturn(List.of(devis));

        List<DevisAdminResponse> mesDevis = devisService.getMesDevis(5);

        assertEquals(1, mesDevis.size());
        assertEquals("DEV-1", mesDevis.get(0).reference());
    }

    private Devis devisDe(Integer idUtilisateur) {
        Utilisateur proprietaire = new Utilisateur();
        proprietaire.setIdUtilisateur(idUtilisateur);
        Devis devis = new Devis();
        devis.setIdDevis(3);
        devis.setUtilisateur(proprietaire);
        when(devisRepository.findById(3)).thenReturn(Optional.of(devis));
        return devis;
    }

    @Test
    void leClientVoitLeDetailDeSonDevis() {
        Devis devis = devisDe(5);
        TypeBien typeBien = new TypeBien();
        typeBien.setLibelle("Maison");
        devis.setTypeBien(typeBien);
        StatutDevis statut = new StatutDevis();
        statut.setLibelle("En attente");
        devis.setStatutDevis(statut);

        Unite unite = new Unite();
        unite.setCode("M2");
        Service service = new Service();
        service.setNom("Isolation");
        service.setUnite(unite);
        Contenir ligne = new Contenir();
        ligne.setService(service);
        ligne.setQuantite(new BigDecimal("2.5"));
        ligne.setPrixUnitaire(new BigDecimal("40.00"));
        when(contenirRepository.findByDevisIdDevis(3)).thenReturn(List.of(ligne));

        DevisDetailResponse detail = devisService.getMonDevis(3, 5);

        assertEquals(1, detail.lignes().size());
        assertEquals(new BigDecimal("100.00"), detail.lignes().get(0).totalHt());
    }

    @Test
    void leClientNeVoitPasLeDevisDunAutre() {
        devisDe(99);

        assertThrows(IllegalArgumentException.class, () -> devisService.getMonDevis(3, 5));
    }

    @Test
    void unClientSansDevisRecoitUneListeVide() {
        when(devisRepository.findByUtilisateurIdUtilisateurOrderByDateCreationDesc(5)).thenReturn(List.of());

        assertTrue(devisService.getMesDevis(5).isEmpty());
    }
}

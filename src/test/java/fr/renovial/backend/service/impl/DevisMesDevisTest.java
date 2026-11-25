package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.response.DevisAdminResponse;
import fr.renovial.backend.model.Devis;
import fr.renovial.backend.model.StatutDevis;
import fr.renovial.backend.model.TypeBien;
import fr.renovial.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void unClientSansDevisRecoitUneListeVide() {
        when(devisRepository.findByUtilisateurIdUtilisateurOrderByDateCreationDesc(5)).thenReturn(List.of());

        assertTrue(devisService.getMesDevis(5).isEmpty());
    }
}

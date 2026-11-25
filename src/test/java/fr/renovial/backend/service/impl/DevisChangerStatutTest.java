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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DevisChangerStatutTest {

    @Mock private IDevisRepository devisRepository;
    @Mock private IContenirRepository contenirRepository;
    @Mock private IServiceRepository serviceRepository;
    @Mock private ITypeBienRepository typeBienRepository;
    @Mock private IStatutDevisRepository statutDevisRepository;

    @InjectMocks private DevisService devisService;

    private StatutDevis statut(String code, String libelle) {
        StatutDevis s = new StatutDevis();
        s.setCode(code);
        s.setLibelle(libelle);
        return s;
    }

    private Devis devisAvecStatut(String code) {
        Devis devis = new Devis();
        devis.setTypeBien(new TypeBien());
        devis.setStatutDevis(statut(code, code));
        when(devisRepository.findById(1)).thenReturn(Optional.of(devis));
        return devis;
    }

    @Test
    void accepterUnDevisEnAttente() {
        devisAvecStatut("EN_ATTENTE");
        when(statutDevisRepository.findByCode("ACCEPTE")).thenReturn(Optional.of(statut("ACCEPTE", "Accepté")));

        DevisAdminResponse response = devisService.changerStatut(1, "ACCEPTE");

        assertEquals("Accepté", response.statut());
    }

    @Test
    void refuserUnDevisDejaTraiteEstInterdit() {
        devisAvecStatut("ACCEPTE");

        assertThrows(IllegalArgumentException.class, () -> devisService.changerStatut(1, "REFUSE"));
    }

    @Test
    void statutInconnuEstInterdit() {
        assertThrows(IllegalArgumentException.class, () -> devisService.changerStatut(1, "EN_ATTENTE"));
    }
}

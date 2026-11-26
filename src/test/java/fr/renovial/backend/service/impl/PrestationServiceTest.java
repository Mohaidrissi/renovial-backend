package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.ServiceRequest;
import fr.renovial.backend.dto.response.PrestationResponse;
import fr.renovial.backend.model.Service;
import fr.renovial.backend.model.Unite;
import fr.renovial.backend.repository.IServiceRepository;
import fr.renovial.backend.repository.IUniteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestationServiceTest {

    @Mock private IServiceRepository serviceRepository;
    @Mock private IUniteRepository uniteRepository;

    @InjectMocks private PrestationService prestationService;

    private Unite m2() {
        Unite unite = new Unite();
        unite.setCode("M2");
        unite.setLibelle("Mètre carré");
        return unite;
    }

    @Test
    void creerUnService() {
        when(uniteRepository.findByCode("M2")).thenReturn(Optional.of(m2()));
        when(serviceRepository.save(any(Service.class))).thenAnswer(i -> i.getArgument(0));

        PrestationResponse cree = prestationService.creerService(
                new ServiceRequest("  Isolation des combles ", null, new BigDecimal("30.00"), true, null, "M2"));

        assertEquals("Isolation des combles", cree.nom()); // espaces retirés
        assertTrue(cree.actif());                          // actif par défaut
        assertEquals("M2", cree.unite().code());
    }

    @Test
    void unNomDejaPrisEstRefuse() {
        when(serviceRepository.existsByNomIgnoreCase("Peinture")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> prestationService.creerService(
                new ServiceRequest("Peinture", null, new BigDecimal("10"), false, true, "M2")));
        verify(serviceRepository, never()).save(any());
    }

    @Test
    void unPrixNulEstRefuse() {
        assertThrows(IllegalArgumentException.class, () -> prestationService.creerService(
                new ServiceRequest("Peinture", null, BigDecimal.ZERO, false, true, "M2")));
    }

    @Test
    void desactiverUnService() {
        Service existant = new Service();
        existant.setIdService(4);
        existant.setActif(true);
        when(serviceRepository.findById(4)).thenReturn(Optional.of(existant));
        when(uniteRepository.findByCode("M2")).thenReturn(Optional.of(m2()));
        when(serviceRepository.save(existant)).thenReturn(existant);

        PrestationResponse modifie = prestationService.modifierService(4,
                new ServiceRequest("Peinture", null, new BigDecimal("25"), false, false, "M2"));

        assertFalse(modifie.actif());
    }
}

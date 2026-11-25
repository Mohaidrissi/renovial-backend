package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.DevisRequest;
import fr.renovial.backend.dto.request.LigneDevisRequest;
import fr.renovial.backend.dto.response.DevisResponse;
import fr.renovial.backend.model.Service;
import fr.renovial.backend.model.StatutDevis;
import fr.renovial.backend.model.TypeBien;
import fr.renovial.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DevisServiceTest {

    @Mock private IDevisRepository devisRepository;
    @Mock private IContenirRepository contenirRepository;
    @Mock private IServiceRepository serviceRepository;
    @Mock private ITypeBienRepository typeBienRepository;
    @Mock private IStatutDevisRepository statutDevisRepository;

    @InjectMocks private DevisService devisService;

    @BeforeEach
    void setUp() {
        when(statutDevisRepository.findByCode("EN_ATTENTE")).thenReturn(Optional.of(new StatutDevis()));
        when(devisRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    private DevisResponse creerDevis(boolean professionnel, boolean economieEnergie) {
        TypeBien typeBien = new TypeBien();
        typeBien.setProfessionnel(professionnel);
        when(typeBienRepository.findById(1)).thenReturn(Optional.of(typeBien));

        Service service = new Service();
        service.setIdService(1);
        service.setPrixUnitaire(new BigDecimal("100.00"));
        service.setEconomieEnergie(economieEnergie);
        service.setActif(true);
        when(serviceRepository.findById(1)).thenReturn(Optional.of(service));

        DevisRequest request = new DevisRequest("Client", "client@mail.fr", null, null, 1,
                List.of(new LigneDevisRequest(1, new BigDecimal("10"))));
        return devisService.creerDevis(request);
    }

    @Test
    void particulier_travauxEconomieEnergie_tva5virgule5() {
        DevisResponse r = creerDevis(false, true);
        assertEquals(new BigDecimal("1000.00"), r.totalHt());
        assertEquals(new BigDecimal("55.00"), r.totalTva());
        assertEquals(new BigDecimal("1055.00"), r.totalTtc());
    }

    @Test
    void particulier_autresTravaux_tva10() {
        DevisResponse r = creerDevis(false, false);
        assertEquals(new BigDecimal("100.00"), r.totalTva());
        assertEquals(new BigDecimal("1100.00"), r.totalTtc());
    }

    @Test
    void localPro_tva20() {
        DevisResponse r = creerDevis(true, true);
        assertEquals(new BigDecimal("200.00"), r.totalTva());
        assertEquals(new BigDecimal("1200.00"), r.totalTtc());
    }
}

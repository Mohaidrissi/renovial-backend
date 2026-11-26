package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.ProfilRequest;
import fr.renovial.backend.dto.response.ProfilResponse;
import fr.renovial.backend.model.Utilisateur;
import fr.renovial.backend.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfilServiceTest {

    @Mock private IUserRepository userRepository;

    @InjectMocks private ProfilService profilService;

    @Test
    void modifierLeNomEtLeTelephone() {
        Utilisateur client = new Utilisateur();
        client.setEmail("client@renovial.test");
        when(userRepository.findById(5)).thenReturn(Optional.of(client));

        ProfilResponse profil = profilService.modifierProfil(5, new ProfilRequest("  Mohamed  ", "0601020304"));

        assertEquals("Mohamed", profil.nom()); // les espaces sont retirés
        assertEquals("0601020304", profil.telephone());
        assertEquals("client@renovial.test", profil.email()); // l'email ne change pas
        verify(userRepository).save(client);
    }

    @Test
    void unNomVideEstRefuse() {
        assertThrows(IllegalArgumentException.class,
                () -> profilService.modifierProfil(5, new ProfilRequest("   ", "0601020304")));
        verify(userRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void unTelephoneTropLongEstRefuse() {
        assertThrows(IllegalArgumentException.class,
                () -> profilService.modifierProfil(5, new ProfilRequest("Mohamed", "0".repeat(21))));
    }
}

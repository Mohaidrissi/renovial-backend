package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.response.UtilisateurAdminResponse;
import fr.renovial.backend.model.Role;
import fr.renovial.backend.model.Utilisateur;
import fr.renovial.backend.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilisateurServiceTest {

    @Mock private IUserRepository userRepository;

    @InjectMocks private UtilisateurService utilisateurService;

    @Test
    void bloquerUnClient() {
        Role role = new Role();
        role.setName("ROLE_CLIENT");
        Utilisateur client = new Utilisateur();
        client.setIdUtilisateur(5);
        client.setActif(true);
        client.setRole(role);
        when(userRepository.findById(5)).thenReturn(Optional.of(client));

        UtilisateurAdminResponse reponse = utilisateurService.changerActif(5, false, 1);

        assertFalse(reponse.actif());
        assertFalse(client.isEnabled()); // Spring Security refusera ce compte
        verify(userRepository).save(client);
    }

    @Test
    void lAdminNePeutPasSeBloquerLuiMeme() {
        assertThrows(IllegalArgumentException.class, () -> utilisateurService.changerActif(1, false, 1));
        verify(userRepository, never()).save(any());
    }
}

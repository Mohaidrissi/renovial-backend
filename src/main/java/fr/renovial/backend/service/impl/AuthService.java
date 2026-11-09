 package fr.renovial.backend.service.impl;

import fr.renovial.backend.dto.request.AuthRequest;
import fr.renovial.backend.dto.request.RegisterRequest;
import fr.renovial.backend.dto.response.AuthResponse;
import fr.renovial.backend.model.Role;
import fr.renovial.backend.model.utilisateur;
import fr.renovial.backend.repository.IRoleRepository;
import fr.renovial.backend.repository.IUserRepository;
import fr.renovial.backend.security.JwtService;
import fr.renovial.backend.service.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest registerRequest) {

        log.debug("Registering user {}", registerRequest.email());

        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée.");
        }

        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new IllegalStateException("Le rôle CLIENT n'existe pas."));

        utilisateur user = new utilisateur();
        user.setNom(registerRequest.nom());
        user.setEmail(registerRequest.email());
        user.setTelephone(registerRequest.telephone());
        user.setMotDePasse(passwordEncoder.encode(registerRequest.password()));
        user.setActif(true);
        user.setDateCreation(LocalDateTime.now());
        user.setRole(clientRole);

        userRepository.save(user);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                );

        authenticationManager.authenticate(authenticationToken);

        utilisateur user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable."));

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
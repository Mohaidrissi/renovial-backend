package fr.renovial.backend.service;

import fr.renovial.backend.dto.request.AuthRequest;
import fr.renovial.backend.dto.request.RegisterRequest;
import fr.renovial.backend.dto.response.AuthResponse;


public interface IAuthService {

    void register(RegisterRequest registerRequest);

    AuthResponse authenticate(AuthRequest request);
}

package com.udea.backendreservas.service.impl;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.AuthResponseDTO;
import com.udea.backendreservas.dto.response.ClientResponseDTO;
import com.udea.backendreservas.dto.response.ProviderResponseDTO;
import com.udea.backendreservas.service.AuthService;
import com.udea.backendreservas.service.ClientService;
import com.udea.backendreservas.service.JwtService;
import com.udea.backendreservas.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientService clientService;
    private final ProviderService providerService;
    private final JwtService jwtService;

    @Override
    public AuthResponseDTO registerClient(CreateClientRequestDTO request) {
        ClientResponseDTO client = clientService.createClient(request);
        String token = jwtService.generateToken(client.getEmail(), "CLIENTE");
        
        return AuthResponseDTO.builder()
                .token(token)
                .user(client)
                .build();
    }

    @Override
    public AuthResponseDTO registerProvider(CreateProviderRequestDTO request) {
        ProviderResponseDTO provider = providerService.createProvider(request);
        String token = jwtService.generateToken(provider.getEmail(), "PROVEEDOR");
        
        return AuthResponseDTO.builder()
                .token(token)
                .user(provider)
                .build();
    }
}

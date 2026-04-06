package com.udea.backendreservas.service;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.AuthResponseDTO;

public interface AuthService {
    AuthResponseDTO registerClient(CreateClientRequestDTO request);
    AuthResponseDTO registerProvider(CreateProviderRequestDTO request);
}

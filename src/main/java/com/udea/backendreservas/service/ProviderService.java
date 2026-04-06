package com.udea.backendreservas.service;

import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.ProviderResponseDTO;

public interface ProviderService {
    ProviderResponseDTO createProvider(CreateProviderRequestDTO request);
}

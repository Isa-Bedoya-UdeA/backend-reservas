package com.udea.backendreservas.service;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.response.ClientResponseDTO;

public interface ClientService {
    ClientResponseDTO createClient(CreateClientRequestDTO request);
}

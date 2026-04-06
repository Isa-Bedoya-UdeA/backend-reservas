package com.udea.backendreservas.service.impl;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.response.ClientResponseDTO;
import com.udea.backendreservas.entity.Client;
import com.udea.backendreservas.mapper.ClientMapper;
import com.udea.backendreservas.repository.ClientRepository;
import com.udea.backendreservas.repository.UserRepository;
import com.udea.backendreservas.service.ClientService;
import com.udea.backendreservas.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ClientResponseDTO createClient(CreateClientRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está en uso");
        }

        Client client = clientMapper.toEntity(request);
        client.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }
}

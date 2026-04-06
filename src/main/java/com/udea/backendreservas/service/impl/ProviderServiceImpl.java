package com.udea.backendreservas.service.impl;

import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.ProviderResponseDTO;
import com.udea.backendreservas.entity.Provider;
import com.udea.backendreservas.mapper.ProviderMapper;
import com.udea.backendreservas.repository.ProviderRepository;
import com.udea.backendreservas.repository.UserRepository;
import com.udea.backendreservas.service.ProviderService;
import com.udea.backendreservas.exception.EmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final UserRepository userRepository;
    private final ProviderMapper providerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ProviderResponseDTO createProvider(CreateProviderRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está en uso");
        }

        Provider provider = providerMapper.toEntity(request);
        provider.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        Provider savedProvider = providerRepository.save(provider);
        return providerMapper.toDto(savedProvider);
    }
}

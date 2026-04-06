package com.udea.backendreservas.service;

import com.udea.backendreservas.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO findByEmail(String email);
    boolean existsByEmail(String email);
}

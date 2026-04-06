package com.udea.backendreservas.service.impl;

import com.udea.backendreservas.dto.response.UserResponseDTO;
import com.udea.backendreservas.entity.User;
import com.udea.backendreservas.mapper.UserMapper;
import com.udea.backendreservas.repository.UserRepository;
import com.udea.backendreservas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return userMapper.toDto(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

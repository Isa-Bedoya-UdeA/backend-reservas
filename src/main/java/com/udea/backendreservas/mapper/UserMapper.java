package com.udea.backendreservas.mapper;

import com.udea.backendreservas.dto.response.UserResponseDTO;
import org.springframework.stereotype.Component;
import com.udea.backendreservas.entity.User;

@Component
public class UserMapper {
    
    public UserResponseDTO toDto(User user) {
        if (user == null) return null;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setEmail(user.getEmail());
        if (user.getTipoUsuario() != null) {
            dto.setTipoUsuario(user.getTipoUsuario().name());
        }
        dto.setEstado(user.getEstado());
        dto.setFechaRegistro(user.getFechaRegistro());
        return dto;
    }
}

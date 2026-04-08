package com.udea.backendreservas.mapper;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.response.ClientResponseDTO;
import com.udea.backendreservas.entity.Client;
import org.springframework.stereotype.Component;
import com.udea.backendreservas.entity.User;

@Component
public class ClientMapper {
    
    public Client toEntity(CreateClientRequestDTO dto) {
        if (dto == null) return null;
        Client client = new Client();
        client.setEmail(dto.getEmail());
        client.setNombre(dto.getNombre());
        client.setTelefono(dto.getTelefono());
        client.setTipoUsuario(User.Role.CLIENTE);
        return client;
    }

    public ClientResponseDTO toDto(Client entity) {
        if (entity == null) return null;
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setEmail(entity.getEmail());
        if (entity.getTipoUsuario() != null) {
            dto.setTipoUsuario(entity.getTipoUsuario().name());
        }
        dto.setEstado(entity.getEstado());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setNombre(entity.getNombre());
        dto.setTelefono(entity.getTelefono());
        return dto;
    }
}

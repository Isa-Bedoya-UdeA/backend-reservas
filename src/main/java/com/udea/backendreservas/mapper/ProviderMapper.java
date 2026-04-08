package com.udea.backendreservas.mapper;

import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.ProviderResponseDTO;
import com.udea.backendreservas.entity.Provider;
import org.springframework.stereotype.Component;
import com.udea.backendreservas.entity.User;

@Component
public class ProviderMapper {

    public Provider toEntity(CreateProviderRequestDTO dto) {
        if (dto == null) return null;
        Provider provider = new Provider();
        provider.setEmail(dto.getEmail());
        provider.setNombreComercial(dto.getNombreComercial());
        provider.setDireccion(dto.getDireccion());
        provider.setTelefonoContacto(dto.getTelefonoContacto());
        provider.setTipoUsuario(User.Role.PROVEEDOR);
        return provider;
    }

    public ProviderResponseDTO toDto(Provider entity) {
        if (entity == null) return null;
        ProviderResponseDTO dto = new ProviderResponseDTO();
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setEmail(entity.getEmail());
        if (entity.getTipoUsuario() != null) {
            dto.setTipoUsuario(entity.getTipoUsuario().name());
        }
        dto.setEstado(entity.getEstado());
        dto.setFechaRegistro(entity.getFechaRegistro());
        dto.setNombreComercial(entity.getNombreComercial());
        dto.setDireccion(entity.getDireccion());
        dto.setTelefonoContacto(entity.getTelefonoContacto());
        return dto;
    }
}

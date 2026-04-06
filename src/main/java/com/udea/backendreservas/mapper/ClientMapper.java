package com.udea.backendreservas.mapper;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.response.ClientResponseDTO;
import com.udea.backendreservas.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {
    
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "tipoUsuario", constant = "CLIENTE")
    Client toEntity(CreateClientRequestDTO dto);

    ClientResponseDTO toDto(Client entity);
}

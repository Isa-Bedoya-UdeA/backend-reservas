package com.udea.backendreservas.mapper;

import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.ProviderResponseDTO;
import com.udea.backendreservas.entity.Provider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProviderMapper {

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "tipoUsuario", constant = "PROVEEDOR")
    Provider toEntity(CreateProviderRequestDTO dto);

    ProviderResponseDTO toDto(Provider entity);
}

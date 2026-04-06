package com.udea.backendreservas.mapper;

import com.udea.backendreservas.dto.response.UserResponseDTO;
import com.udea.backendreservas.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponseDTO toDto(User user);
}

package com.udea.backendreservas.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientResponseDTO extends UserResponseDTO {
    private String nombre;
    private String telefono;
}

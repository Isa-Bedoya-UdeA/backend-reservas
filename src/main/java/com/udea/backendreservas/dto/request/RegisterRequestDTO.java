package com.udea.backendreservas.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "El formato del email es inválido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", 
             message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número")
    private String password;

    @NotBlank(message = "El tipo de usuario es requerido")
    @Pattern(regexp = "^(CLIENTE|PROVEEDOR)$", message = "Tipo de usuario inválido")
    private String tipoUsuario;
}

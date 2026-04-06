package com.udea.backendreservas.controller;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.AuthResponseDTO;
import com.udea.backendreservas.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/client")
    public ResponseEntity<AuthResponseDTO> registerClient(@Valid @RequestBody CreateClientRequestDTO request) {
        return new ResponseEntity<>(authService.registerClient(request), HttpStatus.CREATED);
    }

    @PostMapping("/register/provider")
    public ResponseEntity<AuthResponseDTO> registerProvider(@Valid @RequestBody CreateProviderRequestDTO request) {
        return new ResponseEntity<>(authService.registerProvider(request), HttpStatus.CREATED);
    }
}

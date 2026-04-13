package com.udea.backendreservas.service;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.response.ClientResponseDTO;
import com.udea.backendreservas.entity.Client;
import com.udea.backendreservas.entity.User;
import com.udea.backendreservas.exception.EmailAlreadyExistsException;
import com.udea.backendreservas.mapper.ClientMapper;
import com.udea.backendreservas.repository.ClientRepository;
import com.udea.backendreservas.repository.UserRepository;
import com.udea.backendreservas.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HU-01 - Registro de usuario cliente")
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    private CreateClientRequestDTO requestValido;
    private Client clienteEntidad;
    private ClientResponseDTO clienteRespuesta;

    @BeforeEach
    void setUp() {
        requestValido = new CreateClientRequestDTO();
        requestValido.setEmail("carlos@email.com");
        requestValido.setPassword("Segura#123");
        requestValido.setNombre("Carlos Pérez");
        requestValido.setTelefono("3001234567");

        clienteEntidad = new Client();
        clienteEntidad.setIdUsuario(1L);
        clienteEntidad.setEmail("carlos@email.com");
        clienteEntidad.setNombre("Carlos Pérez");
        clienteEntidad.setTelefono("3001234567");
        clienteEntidad.setTipoUsuario(User.Role.CLIENTE);
        clienteEntidad.setEstado(true);
        clienteEntidad.setFechaRegistro(LocalDateTime.now());

        clienteRespuesta = new ClientResponseDTO();
        clienteRespuesta.setIdUsuario(1L);
        clienteRespuesta.setEmail("carlos@email.com");
        clienteRespuesta.setTipoUsuario("CLIENTE");
        clienteRespuesta.setEstado(true);
        clienteRespuesta.setNombre("Carlos Pérez");
        clienteRespuesta.setTelefono("3001234567");
    }

    @Test
    @DisplayName("CP-01-001: Registro exitoso con datos válidos → retorna DTO sin password")
    void createClient_DatosValidos_RetornaClienteDTO() {
        when(userRepository.existsByEmail("carlos@email.com")).thenReturn(false);
        when(clientMapper.toEntity(requestValido)).thenReturn(clienteEntidad);
        when(passwordEncoder.encode("Segura#123")).thenReturn("hash_seguro");
        when(clientRepository.save(clienteEntidad)).thenReturn(clienteEntidad);
        when(clientMapper.toDto(clienteEntidad)).thenReturn(clienteRespuesta);

        ClientResponseDTO resultado = clientService.createClient(requestValido);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("carlos@email.com");
        assertThat(resultado.getTipoUsuario()).isEqualTo("CLIENTE");
        assertThat(resultado.getIdUsuario()).isEqualTo(1L);

        assertThat(resultado.toString()).doesNotContain("Segura#123");

        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    @DisplayName("CP-01-001b: El password queda hasheado antes de persistir")
    void createClient_PasswordSeHasheaAntesDeGuardar() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(clientMapper.toEntity(requestValido)).thenReturn(clienteEntidad);
        when(passwordEncoder.encode("Segura#123")).thenReturn("$2a$10$hashedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(clienteEntidad);
        when(clientMapper.toDto(clienteEntidad)).thenReturn(clienteRespuesta);

        clientService.createClient(requestValido);

        verify(passwordEncoder, times(1)).encode("Segura#123");
        verify(clientRepository).save(argThat(c -> c.getPasswordHash() != null));
    }

    @Test
    @DisplayName("CP-01-001c: El rol queda establecido como CLIENTE")
    void createClient_RolEstablecidoComoCliente() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(clientMapper.toEntity(requestValido)).thenReturn(clienteEntidad);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(clientRepository.save(any(Client.class))).thenReturn(clienteEntidad);
        when(clientMapper.toDto(clienteEntidad)).thenReturn(clienteRespuesta);

        ClientResponseDTO resultado = clientService.createClient(requestValido);

        assertThat(resultado.getTipoUsuario()).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("CP-01-002: Registro con correo ya registrado → lanza EmailAlreadyExistsException")
    void createClient_CorreoDuplicado_LanzaEmailAlreadyExistsException() {
        when(userRepository.existsByEmail("carlos@email.com")).thenReturn(true);

        assertThatThrownBy(() -> clientService.createClient(requestValido))
            .isInstanceOf(EmailAlreadyExistsException.class)
            .hasMessageContaining("correo electrónico ya está en uso");

        verify(clientRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("CP-01-008 (rama verdadera): email existente → exception, sin llamar a save")
    void createClient_RamaExcepcion_NoLlamaASave() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> clientService.createClient(requestValido))
            .isInstanceOf(EmailAlreadyExistsException.class);

        verify(clientRepository, never()).save(any());
    }

    @Test
    @DisplayName("CP-01-008 (rama falsa): email nuevo → llama a save exactamente una vez")
    void createClient_RamaNormal_LlamaASaveUnaVez() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(clientMapper.toEntity(any())).thenReturn(clienteEntidad);
        when(passwordEncoder.encode(anyString())).thenReturn("hash");
        when(clientRepository.save(any())).thenReturn(clienteEntidad);
        when(clientMapper.toDto(any())).thenReturn(clienteRespuesta);

        clientService.createClient(requestValido);

        verify(clientRepository, times(1)).save(any(Client.class));
    }
}
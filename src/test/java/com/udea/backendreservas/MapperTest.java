package com.udea.backendreservas.mapper;

import com.udea.backendreservas.dto.request.CreateClientRequestDTO;
import com.udea.backendreservas.dto.request.CreateProviderRequestDTO;
import com.udea.backendreservas.dto.response.ClientResponseDTO;
import com.udea.backendreservas.dto.response.ProviderResponseDTO;
import com.udea.backendreservas.entity.Client;
import com.udea.backendreservas.entity.Provider;
import com.udea.backendreservas.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas unitarias de mappers
 * Sprint 1 - HU-01, HU-02: Caja Blanca - Cobertura de caminos
 *
 * Verifica que los mappers:
 *   - Mapean todos los campos correctamente (incluyendo rol)
 *   - Manejan null sin lanzar NullPointerException
 */
@DisplayName("Mappers - ClientMapper y ProviderMapper")
class MapperTest {

    private ClientMapper clientMapper;
    private ProviderMapper providerMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapper();
        providerMapper = new ProviderMapper();
    }

    // =========================================================================
    // ClientMapper
    // =========================================================================

    @Test
    @DisplayName("ClientMapper.toEntity: mapea todos los campos del DTO a la entidad")
    void clientMapper_toEntity_MapaTodosLosCampos() {
        // Arrange
        CreateClientRequestDTO dto = new CreateClientRequestDTO();
        dto.setEmail("carlos@email.com");
        dto.setPassword("Segura#123");
        dto.setNombre("Carlos Pérez");
        dto.setTelefono("3001234567");

        // Act
        Client entidad = clientMapper.toEntity(dto);

        // Assert
        assertThat(entidad).isNotNull();
        assertThat(entidad.getEmail()).isEqualTo("carlos@email.com");
        assertThat(entidad.getNombre()).isEqualTo("Carlos Pérez");
        assertThat(entidad.getTelefono()).isEqualTo("3001234567");
        assertThat(entidad.getTipoUsuario()).isEqualTo(User.Role.CLIENTE);
        // La contraseña NO se mapea aquí, se setea en el servicio
        assertThat(entidad.getPasswordHash()).isNull();
    }

    @Test
    @DisplayName("ClientMapper.toEntity: retorna null si el DTO es null (cobertura de rama)")
    void clientMapper_toEntity_ConNull_RetornaNull() {
        // Act
        Client resultado = clientMapper.toEntity(null);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("ClientMapper.toDto: mapea entidad a DTO con todos los campos")
    void clientMapper_toDto_MapaTodosLosCampos() {
        // Arrange
        Client client = new Client();
        client.setIdUsuario(1L);
        client.setEmail("carlos@email.com");
        client.setNombre("Carlos Pérez");
        client.setTelefono("3001234567");
        client.setTipoUsuario(User.Role.CLIENTE);
        client.setEstado(true);
        LocalDateTime ahora = LocalDateTime.now();
        client.setFechaRegistro(ahora);

        // Act
        ClientResponseDTO dto = clientMapper.toDto(client);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getIdUsuario()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("carlos@email.com");
        assertThat(dto.getNombre()).isEqualTo("Carlos Pérez");
        assertThat(dto.getTelefono()).isEqualTo("3001234567");
        assertThat(dto.getTipoUsuario()).isEqualTo("CLIENTE");
        assertThat(dto.getEstado()).isTrue();
        assertThat(dto.getFechaRegistro()).isEqualTo(ahora);
    }

    @Test
    @DisplayName("ClientMapper.toDto: retorna null si la entidad es null (cobertura de rama)")
    void clientMapper_toDto_ConNull_RetornaNull() {
        // Act
        ClientResponseDTO resultado = clientMapper.toDto(null);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("ClientMapper.toDto: tipoUsuario null en entidad → campo null en DTO")
    void clientMapper_toDto_SinRol_TipoUsuarioNullEnDTO() {
        // Arrange
        Client client = new Client();
        client.setIdUsuario(1L);
        client.setEmail("test@test.com");
        client.setTipoUsuario(null); // rama: tipoUsuario == null

        // Act
        ClientResponseDTO dto = clientMapper.toDto(client);

        // Assert
        assertThat(dto.getTipoUsuario()).isNull();
    }

    // =========================================================================
    // ProviderMapper
    // =========================================================================

    @Test
    @DisplayName("ProviderMapper.toEntity: mapea todos los campos del DTO a la entidad")
    void providerMapper_toEntity_MapaTodosLosCampos() {
        // Arrange
        CreateProviderRequestDTO dto = new CreateProviderRequestDTO();
        dto.setEmail("salon@bellavida.com");
        dto.setPassword("Segura#123");
        dto.setNombreComercial("Salón Bella Vida");
        dto.setDireccion("Calle 50 #30-10");
        dto.setTelefonoContacto("3001234567");

        // Act
        Provider entidad = providerMapper.toEntity(dto);

        // Assert
        assertThat(entidad).isNotNull();
        assertThat(entidad.getEmail()).isEqualTo("salon@bellavida.com");
        assertThat(entidad.getNombreComercial()).isEqualTo("Salón Bella Vida");
        assertThat(entidad.getDireccion()).isEqualTo("Calle 50 #30-10");
        assertThat(entidad.getTelefonoContacto()).isEqualTo("3001234567");
        assertThat(entidad.getTipoUsuario()).isEqualTo(User.Role.PROVEEDOR);
        assertThat(entidad.getPasswordHash()).isNull();
    }

    @Test
    @DisplayName("ProviderMapper.toEntity: retorna null si el DTO es null (cobertura de rama)")
    void providerMapper_toEntity_ConNull_RetornaNull() {
        // Act
        Provider resultado = providerMapper.toEntity(null);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("ProviderMapper.toDto: mapea entidad a DTO con todos los campos")
    void providerMapper_toDto_MapaTodosLosCampos() {
        // Arrange
        Provider provider = new Provider();
        provider.setIdUsuario(2L);
        provider.setEmail("salon@bellavida.com");
        provider.setNombreComercial("Salón Bella Vida");
        provider.setDireccion("Calle 50 #30-10");
        provider.setTelefonoContacto("3001234567");
        provider.setIdCategoria(1L);
        provider.setTipoUsuario(User.Role.PROVEEDOR);
        provider.setEstado(true);

        // Act
        ProviderResponseDTO dto = providerMapper.toDto(provider);

        // Assert
        assertThat(dto).isNotNull();
        assertThat(dto.getIdUsuario()).isEqualTo(2L);
        assertThat(dto.getEmail()).isEqualTo("salon@bellavida.com");
        assertThat(dto.getNombreComercial()).isEqualTo("Salón Bella Vida");
        assertThat(dto.getTelefonoContacto()).isEqualTo("3001234567");
        assertThat(dto.getTipoUsuario()).isEqualTo("PROVEEDOR");
        assertThat(dto.getEstado()).isTrue();
    }

    @Test
    @DisplayName("ProviderMapper.toDto: retorna null si la entidad es null (cobertura de rama)")
    void providerMapper_toDto_ConNull_RetornaNull() {
        // Act
        ProviderResponseDTO resultado = providerMapper.toDto(null);

        // Assert
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("ProviderMapper.toDto: tipoUsuario null en entidad → campo null en DTO")
    void providerMapper_toDto_SinRol_TipoUsuarioNullEnDTO() {
        // Arrange
        Provider provider = new Provider();
        provider.setIdUsuario(2L);
        provider.setTipoUsuario(null);

        // Act
        ProviderResponseDTO dto = providerMapper.toDto(provider);

        // Assert
        assertThat(dto.getTipoUsuario()).isNull();
    }
}

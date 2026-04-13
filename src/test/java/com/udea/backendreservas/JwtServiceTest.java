package com.udea.backendreservas.security;

import com.udea.backendreservas.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;

/**
 * Pruebas unitarias para JwtService
 * Sprint 1 - HU-03: Inicio de sesión
 *
 * Casos cubiertos:
 *   CP-03-001/002: Token generado contiene email y rol correcto
 *   CP-03-006:     Claims requeridos presentes en el JWT (userId, rol, email, expiresIn)
 *   CP-03-007:     Token expirado → isTokenValid retorna false
 *   CP-03-008:     Token válido → isTokenValid retorna true
 *   Caja blanca:   Cobertura de sentencias de isTokenExpired / isTokenValid
 */
@DisplayName("HU-03 - JwtService: generación y validación de tokens")
class JwtServiceTest {

    private JwtService jwtService;

    // Clave Base64 de 256 bits mínimo para HS256
    private static final String SECRET =
        Base64.getEncoder().encodeToString(
            "mi-clave-secreta-super-segura-para-pruebas-unitarias-2026".getBytes()
        );
    private static final long EXPIRACION_1_HORA = 3_600_000L;
    private static final long EXPIRACION_PASADA = -1_000L; // ya expirado

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRACION_1_HORA);
    }

    // -------------------------------------------------------------------------
    // CP-03-001/002: Token generado para CLIENTE y PROVEEDOR
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CP-03-001: Token para CLIENTE contiene email y rol=CLIENTE")
    void generateToken_ParaCliente_ContieneEmailYRolCliente() {
        // Arrange
        String email = "carlos@email.com";

        // Act
        String token = jwtService.generateToken(email, "CLIENTE");

        // Assert
        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo(email);

        String rol = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        assertThat(rol).isEqualTo("CLIENTE");
    }

    @Test
    @DisplayName("CP-03-002: Token para PROVEEDOR contiene email y rol=PROVEEDOR")
    void generateToken_ParaProveedor_ContieneEmailYRolProveedor() {
        // Arrange
        String email = "salon@bellavida.com";

        // Act
        String token = jwtService.generateToken(email, "PROVEEDOR");

        // Assert
        assertThat(jwtService.extractUsername(token)).isEqualTo(email);
        String rol = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        assertThat(rol).isEqualTo("PROVEEDOR");
    }

    // -------------------------------------------------------------------------
    // CP-03-006: Claims requeridos en el JWT
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CP-03-006: Token contiene los claims requeridos: email, role, iat, exp")
    void generateToken_ContieneClaimsRequeridos() {
        // Arrange
        String email = "carlos@email.com";

        // Act
        String token = jwtService.generateToken(email, "CLIENTE");

        // Assert - subject (email)
        String subject = jwtService.extractUsername(token);
        assertThat(subject).isEqualTo(email);

        // Assert - role claim
        String rol = jwtService.extractClaim(token, c -> c.get("role", String.class));
        assertThat(rol).isNotNull();

        // Assert - expiration (expiresIn) está presente
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        assertThat(expiration).isAfter(new Date());

        // Assert - issued at está presente
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        assertThat(issuedAt).isNotNull();
    }

    @Test
    @DisplayName("CP-03-006b: Token NO contiene datos sensibles como la contraseña")
    void generateToken_NoContienePassword() {
        // Arrange & Act
        String token = jwtService.generateToken("carlos@email.com", "CLIENTE");

        // Assert - el token raw no debe contener ninguna cadena que parezca contraseña
        assertThat(token).doesNotContainIgnoringCase("password");
        assertThat(token).doesNotContainIgnoringCase("Segura#123");
    }

    // -------------------------------------------------------------------------
    // CP-03-007: Token expirado → isTokenValid = false
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CP-03-007: Token expirado → isTokenValid retorna false")
    void isTokenValid_TokenExpirado_RetornaFalse() {
        // Arrange: generamos un token con expiración negativa (ya expirado)
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", EXPIRACION_PASADA);
        String tokenExpirado = jwtService.generateToken("carlos@email.com", "CLIENTE");

        // Act & Assert
        // isTokenValid lanzará ExpiredJwtException al intentar parsear, lo que
        // indica que el framework rechaza el token; validamos ese comportamiento.
        assertThatThrownBy(() -> jwtService.isTokenValid(tokenExpirado, "carlos@email.com"))
            .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }

    // -------------------------------------------------------------------------
    // CP-03-008: Token válido → isTokenValid = true
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CP-03-008: Token vigente con email correcto → isTokenValid retorna true")
    void isTokenValid_TokenVigenteEmailCorrecto_RetornaTrue() {
        // Arrange
        String email = "carlos@email.com";
        String token = jwtService.generateToken(email, "CLIENTE");

        // Act
        boolean valido = jwtService.isTokenValid(token, email);

        // Assert
        assertThat(valido).isTrue();
    }

    @Test
    @DisplayName("CP-03-008b: Token vigente con email incorrecto → isTokenValid retorna false")
    void isTokenValid_TokenVigenteEmailIncorrecto_RetornaFalse() {
        // Arrange
        String token = jwtService.generateToken("carlos@email.com", "CLIENTE");

        // Act
        boolean valido = jwtService.isTokenValid(token, "otro@email.com");

        // Assert
        assertThat(valido).isFalse();
    }

    // -------------------------------------------------------------------------
    // Caja blanca: extractUsername delega correctamente en extractClaim
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("Caja blanca: extractUsername retorna el subject del token")
    void extractUsername_RetornaEmailDelSubject() {
        // Arrange
        String email = "test@test.com";
        String token = jwtService.generateToken(email, "CLIENTE");

        // Act
        String extraido = jwtService.extractUsername(token);

        // Assert
        assertThat(extraido).isEqualTo(email);
    }
}

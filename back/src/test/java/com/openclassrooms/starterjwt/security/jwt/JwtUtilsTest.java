package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    private final String jwtSecret = "testSecretKey"; // Secret utilisé pour les tests
    private final int jwtExpirationMs = 3600000; // 1 heure en millisecondes

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        jwtUtils = new JwtUtils();

        // Utilisation de la réflexion pour définir les valeurs des champs privés
        setPrivateField(jwtUtils, "jwtSecret", jwtSecret);
        setPrivateField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    /**
     * Définit une valeur dans un champ privé en utilisant la réflexion.
     *
     * @param target    L'objet cible.
     * @param fieldName Le nom du champ à modifier.
     * @param value     La valeur à affecter au champ.
     * @throws Exception En cas d'erreur de réflexion.
     */
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Permet d'accéder aux champs privés
        field.set(target, value);
    }

    @Test
    void testGenerateJwtToken() {
        // Mock des UserDetails
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        // Mock de l'authentication
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Génération du token JWT
        String token = jwtUtils.generateJwtToken(authentication);

        // Vérifications
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUserNameFromJwtToken() {
        // Génération manuelle d'un token
        String username = "test@example.com";
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Extraction du username depuis le token
        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        // Vérifications
        assertEquals(username, extractedUsername);
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        // Génération d'un token valide
        String token = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Validation du token
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testValidateJwtToken_InvalidSignature() {
        // Génération d'un token avec une signature différente
        String token = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, "differentSecretKey")
                .compact();

        // Validation du token
        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testValidateJwtToken_MalformedToken() {
        // Création d'un token mal formé
        String token = "malformed.token.part";

        // Validation du token
        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testValidateJwtToken_ExpiredToken() {
        // Génération d'un token expiré
        String token = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() - 1000)) // Token déjà expiré
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Validation du token
        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testValidateJwtToken_EmptyToken() {
        // Validation d'un token vide
        assertFalse(jwtUtils.validateJwtToken(""));
    }

    @Test
    void testValidateJwtToken_NullToken() {
        // Validation d'un token null
        assertFalse(jwtUtils.validateJwtToken(null));
    }
}

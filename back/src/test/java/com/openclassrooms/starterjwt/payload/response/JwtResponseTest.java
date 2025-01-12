package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JwtResponseTest {

    @Test
    void testConstructorAndGetters() {
        // Création de l'objet JwtResponse
        JwtResponse jwtResponse = new JwtResponse(
                "testToken",
                1L,
                "test@example.com",
                "John",
                "Doe",
                true
        );

        // Vérifications des champs via les getters
        assertEquals("testToken", jwtResponse.getToken());
        assertEquals(1L, jwtResponse.getId());
        assertEquals("test@example.com", jwtResponse.getUsername());
        assertEquals("John", jwtResponse.getFirstName());
        assertEquals("Doe", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
        assertEquals("Bearer", jwtResponse.getType()); // Vérifier la valeur par défaut
    }

    @Test
    void testSetters() {
        // Création de l'objet JwtResponse avec des valeurs initiales
        JwtResponse jwtResponse = new JwtResponse(
                "initialToken",
                1L,
                "initial@example.com",
                "Jane",
                "Doe",
                false
        );

        // Modification des champs via les setters
        jwtResponse.setToken("newToken");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("new@example.com");
        jwtResponse.setFirstName("Updated");
        jwtResponse.setLastName("User");
        jwtResponse.setAdmin(true);

        // Vérifications des nouveaux champs
        assertEquals("newToken", jwtResponse.getToken());
        assertEquals(2L, jwtResponse.getId());
        assertEquals("new@example.com", jwtResponse.getUsername());
        assertEquals("Updated", jwtResponse.getFirstName());
        assertEquals("User", jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
    }
}

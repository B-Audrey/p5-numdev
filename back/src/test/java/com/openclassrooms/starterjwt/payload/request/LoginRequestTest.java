package com.openclassrooms.starterjwt.payload.request;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    void testGettersAndSetters() {
        // Création de l'objet LoginRequest
        LoginRequest loginRequest = new LoginRequest();

        // Modification des champs via les setters
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        // Vérification des champs via les getters
        assertEquals("test@example.com", loginRequest.getEmail());
        assertEquals("password123", loginRequest.getPassword());
    }

    @Test
    void testDefaultValues() {
        // Création de l'objet LoginRequest sans initialiser les champs
        LoginRequest loginRequest = new LoginRequest();

        // Vérification des champs par défaut
        assertNull(loginRequest.getEmail());
        assertNull(loginRequest.getPassword());
    }
}

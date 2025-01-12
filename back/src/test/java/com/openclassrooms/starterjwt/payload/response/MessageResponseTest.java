package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MessageResponseTest {

    @Test
    void testConstructorAndGetter() {
        // Création de l'objet MessageResponse
        MessageResponse messageResponse = new MessageResponse("Success");

        // Vérification que le message est correctement initialisé
        assertEquals("Success", messageResponse.getMessage());
    }

    @Test
    void testSetter() {
        // Création de l'objet MessageResponse avec une valeur initiale
        MessageResponse messageResponse = new MessageResponse("Initial message");

        // Modification du message via le setter
        messageResponse.setMessage("Updated message");

        // Vérification que le message a été mis à jour
        assertEquals("Updated message", messageResponse.getMessage());
    }
}

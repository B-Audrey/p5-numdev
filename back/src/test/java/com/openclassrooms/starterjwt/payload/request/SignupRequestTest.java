package com.openclassrooms.starterjwt.payload.request;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidSignupRequest() {
        // Création d'un objet SignupRequest valide
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Valider l'objet
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Vérification qu'il n'y a pas de violations
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        // Création d'un objet SignupRequest avec un email invalide
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("invalid-email");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Valider l'objet
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Vérification des violations
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void testBlankFields() {
        // Création d'un objet SignupRequest avec des champs vides
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("");
        signupRequest.setFirstName(" ");
        signupRequest.setLastName(null);
        signupRequest.setPassword("   ");

        // Valider l'objet
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Vérification des violations
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordTooShort() {
        // Création d'un objet SignupRequest avec un mot de passe trop court
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("123"); // Trop court

        // Valider l'objet
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);

        // Vérification des violations
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testGettersAndSetters() {
        // Création de l'objet SignupRequest
        SignupRequest signupRequest = new SignupRequest();

        // Modification des champs via les setters
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Vérification des champs via les getters
        assertEquals("test@example.com", signupRequest.getEmail());
        assertEquals("John", signupRequest.getFirstName());
        assertEquals("Doe", signupRequest.getLastName());
        assertEquals("password123", signupRequest.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        // Création de deux objets SignupRequest identiques
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");
        request1.setFirstName("John");
        request1.setLastName("Doe");
        request1.setPassword("password123");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("test@example.com");
        request2.setFirstName("John");
        request2.setLastName("Doe");
        request2.setPassword("password123");

        // Vérifier que les deux objets sont égaux
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testNotEquals() {
        // Création de deux objets SignupRequest différents
        SignupRequest request1 = new SignupRequest();
        request1.setEmail("test@example.com");

        SignupRequest request2 = new SignupRequest();
        request2.setEmail("different@example.com");

        // Vérifier que les deux objets ne sont pas égaux
        assertNotEquals(request1, request2);
    }

    @Test
    void testToString() {
        // Création de l'objet SignupRequest
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        // Vérification que toString contient les champs
        String result = signupRequest.toString();
        assertTrue(result.contains("test@example.com"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        assertTrue(result.contains("password123"));
    }
}

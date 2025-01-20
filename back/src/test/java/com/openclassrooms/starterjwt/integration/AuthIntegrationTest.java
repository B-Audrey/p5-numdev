package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class AuthIntegrationTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Création d'un utilisateur existant pour tester l'authentification
        User existingUser = new User("test@example.com", "Doe", "John", passwordEncoder.encode("password123"), false);
        userRepository.save(existingUser);
    }

    @Test
    void testRegisterUser_Success() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(MessageResponse.class, response.getBody());
        assertEquals("User registered successfully!", ((MessageResponse) response.getBody()).getMessage());

        assertTrue(userRepository.existsByEmail("newuser@example.com"));
    }

    @Test
    void testRegisterUser_EmailAlreadyTaken() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com"); // Email déjà pris
        signupRequest.setFirstName("Jane");
        signupRequest.setLastName("Doe");
        signupRequest.setPassword("password123");

        ResponseEntity<?> response = authController.registerUser(signupRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertInstanceOf(MessageResponse.class, response.getBody());
        assertEquals("Error: Email is already taken!", ((MessageResponse) response.getBody()).getMessage());
    }

    @Test
    void testLoginUser_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertInstanceOf(JwtResponse.class, response.getBody());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertNotNull(jwtResponse.getToken());
        assertEquals("test@example.com", jwtResponse.getUsername());
        assertEquals("Doe", jwtResponse.getLastName());
        assertEquals("John", jwtResponse.getFirstName());
    }

    @Test
    void testLoginUser_WrongPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        Exception exception = assertThrows(Exception.class, () -> authController.authenticateUser(loginRequest));
        assertNotNull(exception);
    }

    @Test
    void testLoginUser_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@example.com");
        loginRequest.setPassword("password123");

        Exception exception = assertThrows(Exception.class, () -> authController.authenticateUser(loginRequest));
        assertNotNull(exception);
    }
}

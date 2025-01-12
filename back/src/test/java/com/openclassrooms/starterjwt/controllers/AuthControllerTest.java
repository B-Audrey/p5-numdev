package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import java.util.Optional;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        // Mock input
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        // Mock dependencies
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "John", "Doe", false, "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(
                authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User("test@example.com",
                                                                                             "Doe",
                                                                                             "John",
                                                                                             "password",
                                                                                             false)));

        // Test method
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assertions
        assertNotNull(response);
        assertInstanceOf(JwtResponse.class, response.getBody());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("token", jwtResponse.getToken());
        assertEquals("test@example.com", jwtResponse.getUsername());
    }

    @Test
    void testAuthenticateUser_Failure() {
        // Mock input
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("wrongpassword");

        // Mock behavior
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Test method and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class,
                                                  () -> authController.authenticateUser(loginRequest));
        assertEquals("Authentication failed", exception.getMessage());
    }

    @Test
    void testRegisterUser_Success() {
        // Mock input
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setFirstName("New");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password");

        // Mock behavior
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");

        // Test method
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assertions
        assertNotNull(response);
        assertInstanceOf(MessageResponse.class, response.getBody());

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());

        // Verify user save
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Mock input
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("existinguser@example.com");

        // Mock behavior
        when(userRepository.existsByEmail("existinguser@example.com")).thenReturn(true);

        // Test method
        ResponseEntity<?> response = authController.registerUser(signupRequest);

        // Assertions
        assertNotNull(response);
        assertInstanceOf(MessageResponse.class, response.getBody());

        MessageResponse messageResponse = (MessageResponse) response.getBody();
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());

        // Verify save was not called
        verify(userRepository, never()).save(any(User.class));
    }
}

package com.openclassrooms.starterjwt.security.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

class UserDetailsImplServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Mock data
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Test method
        UserDetails result = userDetailsService.loadUserByUsername("test@example.com");

        // Assertions
        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertEquals("John", ((UserDetailsImpl) result).getFirstName());
        assertEquals("Doe", ((UserDetailsImpl) result).getLastName());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock behavior
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Test method & Assertions
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("notfound@example.com")
        );

        assertEquals("User Not Found with email: notfound@example.com", exception.getMessage());
    }

    @Test
    void testToString_DefaultImplementation() {
        // Build UserDetailsImpl object
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        // Call toString()
        String result = userDetails.toString();

        // Assertions
        assertNotNull(result);
        assertTrue(result.contains("UserDetailsImpl"));
        assertTrue(result.contains("@"));
    }

}

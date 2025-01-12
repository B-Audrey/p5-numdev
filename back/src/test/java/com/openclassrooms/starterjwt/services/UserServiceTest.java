package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_UserFound() {
        // Mock data
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Test method
        User result = userService.findById(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void testFindById_UserNotFound() {
        // Mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Test method
        User result = userService.findById(1L);

        // Assertions
        assertNull(result);
    }

    @Test
    void testDelete_UserDeleted() {
        // Mock behavior
        doNothing().when(userRepository).deleteById(1L);

        // Test method
        userService.delete(1L);

        // Verify interaction
        verify(userRepository, times(1)).deleteById(1L);
    }
}

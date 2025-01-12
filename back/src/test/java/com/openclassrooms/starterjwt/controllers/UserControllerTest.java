package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Date;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testFindById_UserFound() {
        // Mock data
        User user = new User(1L, "email@dev.fr", "firstName", "lastName", "password", true, LocalDateTime.now(), LocalDateTime.now());
        UserDto userDto = new UserDto(1L, "email@dev.fr", "firstName", "lastName", true, "password" , LocalDateTime.now(), LocalDateTime.now());
                                      when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Test method
        ResponseEntity<?> response = userController.findById("1");

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userDto, response.getBody());
    }

    @Test
    void testFindById_UserNotFound() {
        // Mock data
        when(userService.findById(1L)).thenReturn(null);

        // Test method
        ResponseEntity<?> response = userController.findById("1");

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindById_InvalidId() {
        // Test method
        ResponseEntity<?> response = userController.findById("abc");

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testSave_UserFoundAndAuthorized() {
        // Mock data
        User user = new User(1L, "email@dev.fr", "firstName", "lastName", "password", true, LocalDateTime.now(), LocalDateTime.now());
        user.setEmail("user@example.com");
        when(userService.findById(1L)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("user@example.com");

        // Test method
        ResponseEntity<?> response = userController.save("1");

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        // Verify deletion
        verify(userService, times(1)).delete(1L);
    }

    @Test
    void testSave_UserFoundButUnauthorized() {
        // Mock data
        User user = new User();
        user.setEmail("user@example.com");
        when(userService.findById(1L)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("otheruser@example.com");

        // Test method
        ResponseEntity<?> response = userController.save("1");

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());

        // Verify no deletion
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void testSave_UserNotFound() {
        // Mock data
        when(userService.findById(1L)).thenReturn(null);

        // Test method
        ResponseEntity<?> response = userController.save("1");

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        // Verify no deletion
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void testSave_InvalidId() {
        // Test method
        ResponseEntity<?> response = userController.save("abc");

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        // Verify no deletion
        verify(userService, never()).delete(anyLong());
    }
}

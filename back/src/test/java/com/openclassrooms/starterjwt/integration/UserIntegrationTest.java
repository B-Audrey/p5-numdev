package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// Réinitialise le contexte après chaque test
@Transactional
public class UserIntegrationTest {

    @Test
    void contextLoads() {
        // Vérifie que le contexte se charge correctement
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    User user1;

    @BeforeEach
    void setUp() {
        // Initialiser les données dans la base
        User user1 = new User("user1@example.com", "Doe", "John", "password123", false);
        this.user1 = userRepository.save(user1);
    }

    @Test
    void testFindById_UserExists() {
        ResponseEntity<?> response = userController.findById(String.valueOf(user1.getId()));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserDto userDto = (UserDto) response.getBody();
        assertNotNull(userDto);
        assertEquals("user1@example.com", userDto.getEmail());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("John", userDto.getFirstName());
    }

    @Test
    void testFindById_UserDoesNotExists() {
        ResponseEntity<?> response = userController.findById(String.valueOf(8));

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    void testFindById_BadRequest() {
        ResponseEntity<?> response = userController.findById(String.valueOf(false));

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDeleteUser_Success() {
        setAuthenticatedUser(user1);

        ResponseEntity<?> response = userController.save(String.valueOf(user1.getId()));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertFalse(userRepository.existsById(user1.getId()));
    }

    @Test
    void testDeleteUser_NotFound() {
        setAuthenticatedUser(user1);

        ResponseEntity<?> response = userController.save(String.valueOf(8));

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }



    private void setAuthenticatedUser(User user) {
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user1.getEmail())
            .password(user1.getPassword())
            .roles("USER")
            .build();

    SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
    );
}
}

package com.openclassrooms.starterjwt.security.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class UserDetailsImplTest {

    @Test
    void testBuilderAndGetters() {
        // Build UserDetailsImpl object
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .build();

        // Verify fields
        assertEquals(1L, userDetails.getId());
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
        assertTrue(userDetails.getAdmin());
    }

    @Test
    void testAccountStatusMethods() {
        // Build UserDetailsImpl object
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        // Verify account status methods
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        // Build two identical UserDetailsImpl objects
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        // Build a different UserDetailsImpl object
        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(2L)
                .username("other@example.com")
                .password("password456")
                .build();

        // Verify equals and hashCode
        assertEquals(user1, user2);
        assertNotEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1, user3);
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void testGetAuthorities() {
        // Build UserDetailsImpl object
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        // Verify authorities
        assert (userDetails.getAuthorities()).isEmpty(); // no roles defined so we have an array but empty
    }

    @Test
    void testEquals_NullObject() {
        // Création d'un objet UserDetailsImpl
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        // Vérification
        assertNotEquals(null, user); // Comparé à null
    }

    @Test
    void testEquals_DifferentId() {
        // Création de deux objets UserDetailsImpl avec des IDs différents
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();

        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(2L)
                .username("test@example.com")
                .password("password123")
                .build();

        // Vérification
        assertNotEquals(user1, user2); // IDs différents
    }

    @Test
    void testToString() {
        // Création de l'objet avec le builder
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .admin(true)
                .build();

        // Appel explicite à toString()
        String result = userDetails.toString(); // méthode non surchagée

        // Vérifications
        assertNotNull(result); // Vérifier que toString ne retourne pas null
        assertTrue(result.contains("UserDetailsImpl")); // Vérifier que la classe est mentionnée
    }


}

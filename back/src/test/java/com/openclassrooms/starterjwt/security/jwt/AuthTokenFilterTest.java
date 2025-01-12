package com.openclassrooms.starterjwt.security.jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.Field;

class AuthTokenFilterTest {

    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authTokenFilter = new AuthTokenFilter();

        // Utiliser la réflexion pour injecter les dépendances privées
        setPrivateField(authTokenFilter, "jwtUtils", jwtUtils);
        setPrivateField(authTokenFilter, "userDetailsService", userDetailsService);

        // Nettoyer le contexte de sécurité avant chaque test
        SecurityContextHolder.clearContext();
    }

    /**
     * Définit une valeur dans un champ privé en utilisant la réflexion.
     *
     * @param target    L'objet cible.
     * @param fieldName Le nom du champ à modifier.
     * @param value     La valeur à affecter au champ.
     * @throws Exception En cas d'erreur de réflexion.
     */
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true); // Permet d'accéder aux champs privés
        field.set(target, value);
    }

    @Test
    void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
        // Mock request avec un JWT valide
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid.jwt.token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mock JWT validation et user details
        when(jwtUtils.validateJwtToken("valid.jwt.token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid.jwt.token")).thenReturn("test@example.com");

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1L)
                .username("test@example.com")
                .password("password123")
                .build();
        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);

        // Exécuter le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que SecurityContext est peuplé
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals("test@example.com", authentication.getName());
        assertEquals(userDetails, authentication.getPrincipal());

        // Vérifier que le filtre continue
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidJwt() throws ServletException, IOException {
        // Mock request avec un JWT invalide
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid.jwt.token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mock JWT validation
        when(jwtUtils.validateJwtToken("invalid.jwt.token")).thenReturn(false);

        // Exécuter le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que SecurityContext n'est pas peuplé
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Vérifier que le filtre continue
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoJwt() throws ServletException, IOException {
        // Mock request sans en-tête Authorization
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Exécuter le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que SecurityContext n'est pas peuplé
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Vérifier que le filtre continue
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_Exception() throws ServletException, IOException {
        // Mock request avec un JWT valide
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid.jwt.token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mock JWT validation et exception lors du chargement des détails utilisateur
        when(jwtUtils.validateJwtToken("valid.jwt.token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid.jwt.token")).thenReturn("test@example.com");
        when(userDetailsService.loadUserByUsername("test@example.com")).thenThrow(new RuntimeException("Test Exception"));

        // Exécuter le filtre
        authTokenFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que SecurityContext n'est pas peuplé
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Vérifier que le filtre continue
        verify(filterChain, times(1)).doFilter(request, response);
    }
}

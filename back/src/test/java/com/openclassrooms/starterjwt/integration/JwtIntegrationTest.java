package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.AuthTokenFilter;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtIntegrationTest {

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidJwtToken_Filter() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer valid.jwt.token");

        when(jwtUtils.validateJwtToken("valid.jwt.token")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("valid.jwt.token")).thenReturn("user@example.com");
        UserDetails userDetails = new User("user@example.com", "password", Collections.emptyList());
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);

        authTokenFilter.doFilter(request, response, filterChain);

        assertNotNull(authTokenFilter);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testInvalidJwtToken_Filter() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer invalid.jwt.token");

        when(jwtUtils.validateJwtToken("invalid.jwt.token")).thenReturn(false);

        authTokenFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testAuthEntryPointJwt() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = mock(AuthenticationException.class);

        when(authException.getMessage()).thenReturn("Unauthorized access");

        authEntryPointJwt.commence(request, response, authException);

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("Unauthorized"));
    }

}

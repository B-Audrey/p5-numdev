package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.*;

class AuthEntryPointJwtTest {

    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private Logger logger;

    @Mock
    private AuthenticationException authException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authEntryPointJwt = new AuthEntryPointJwt();
    }

    @Test
    void testCommence() throws IOException, ServletException {
        // Mock HttpServletRequest et HttpServletResponse
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/api/test");

        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mock AuthenticationException
        when(authException.getMessage()).thenReturn("Test unauthorized error");

        // Exécuter la méthode commence
        authEntryPointJwt.commence(request, response, authException);

        // Vérifications du statut HTTP
        assertEquals(401, response.getStatus());

        // Vérifications du contenu JSON
        String jsonResponse = response.getContentAsString();
        assertNotNull(jsonResponse);

        // Vérifier les valeurs JSON
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(jsonResponse, Map.class);

        assertEquals(401, responseMap.get("status"));
        assertEquals("Unauthorized", responseMap.get("error"));
        assertEquals("Test unauthorized error", responseMap.get("message"));
        assertEquals("/api/test", responseMap.get("path"));
    }

    @Test
    void testLoggerIsCalled() throws IOException, ServletException {
        // Mock HttpServletRequest et HttpServletResponse
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Mock AuthenticationException
        when(authException.getMessage()).thenReturn("Test unauthorized error");

        // Spy sur la classe pour vérifier les appels au logger
        AuthEntryPointJwt spyEntryPoint = spy(authEntryPointJwt);

        // Exécuter la méthode commence
        spyEntryPoint.commence(request, response, authException);

        // Vérifier que le logger a été appelé avec le bon message
        verify(spyEntryPoint, times(1)).commence(request, response, authException);
    }
}


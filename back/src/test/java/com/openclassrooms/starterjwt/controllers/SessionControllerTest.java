package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_SessionFound() {
        // Mock data
        Session session = new Session(1L, "Test", new Date(), "Description", new Teacher(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        SessionDto sessionDto = new SessionDto( 1L, "Test", new Date(), 1L, "Description", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Test method
        ResponseEntity<?> response = sessionController.findById("1");

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void testFindById_SessionNotFound() {
        // Mock data
        when(sessionService.getById(1L)).thenReturn(null);

        // Test method
        ResponseEntity<?> response = sessionController.findById("1");

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindById_InvalidId() {
        // Test method
        ResponseEntity<?> response = sessionController.findById("abc");

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testFindAll() {
        // Mock data
        Session session1 = new Session(1L, "Test", new Date(), "Description", new Teacher(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        Session session2 = new Session(2L, "Test", new Date(), "Description", new Teacher(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        List<Session> sessions = Arrays.asList(session1, session2);
        SessionDto sessionDto1 = new SessionDto(1L, "Test", new Date(), 1L, "Description", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        SessionDto sessionDto2 = new SessionDto(2L, "Test", new Date(), 1L, "Description", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(Arrays.asList(sessionDto1, sessionDto2));

        // Test method
        ResponseEntity<?> response = sessionController.findAll();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(Arrays.asList(sessionDto1, sessionDto2), response.getBody());
    }

    @Test
    void testCreate() {
        // Mock data
        SessionDto sessionDto = new SessionDto(1L, "Test", new Date(), 1L, "Description", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        Session session = new Session(1L, "Test", new Date(), "Description", new Teacher(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Test method
        ResponseEntity<?> response = sessionController.create(sessionDto);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void testUpdate_ValidId() {
        // Mock data
        SessionDto sessionDto = new SessionDto(1L, "Test", new Date(), 1L, "DescriptionToUpdate", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        Session session = new Session(1L, "Test", new Date(), "DescriptionToUpdate", new Teacher(), new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now());
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Test method
        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void testUpdate_InvalidId() {
        // Test method
        ResponseEntity<?> response = sessionController.update("abc", new SessionDto());

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDelete_ValidId() {
        // Mock data
        Session session = new Session();
        when(sessionService.getById(1L)).thenReturn(session);

        // Test method
        ResponseEntity<?> response = sessionController.save("1");

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDelete_InvalidId() {
        // Test method
        ResponseEntity<?> response = sessionController.save("abc");

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDelete_NotFoundId() {
        // Test method
        ResponseEntity<?> response = sessionController.save("1");

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testParticipate_ValidIds() {
        // Test method
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testParticipate_InvalidIds() {
        // Test method
        ResponseEntity<?> response = sessionController.participate("abc", "1");

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testNoLongerParticipate_Success() {
        // Test method
        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        // Verify interaction with service
        verify(sessionService, times(1)).noLongerParticipate(1L, 2L);
    }

    @Test
    void testNoLongerParticipate_InvalidIds() {
        // Test method
        ResponseEntity<?> response = sessionController.noLongerParticipate("abc", "2");

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());

        // Verify no interaction with service
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }

}

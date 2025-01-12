package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Mock data
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        // Test method
        Session result = sessionService.create(session);

        // Assertions
        assertNotNull(result);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testDelete() {
        // Test method
        sessionService.delete(1L);

        // Verify
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        // Mock data
        List<Session> sessions = Arrays.asList(new Session(), new Session());
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Test method
        List<Session> result = sessionService.findAll();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetById_SessionFound() {
        // Mock data
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Test method
        Session result = sessionService.getById(1L);

        // Assertions
        assertNotNull(result);
    }

    @Test
    void testGetById_SessionNotFound() {
        // Mock behavior
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Test method
        Session result = sessionService.getById(1L);

        // Assertions
        assertNull(result);
    }

    @Test
    void testUpdate() {
        // Mock data
        Session session = new Session();
        session.setId(1L);
        when(sessionRepository.save(session)).thenReturn(session);

        // Test method
        Session result = sessionService.update(1L, session);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testParticipate_Success() {
        // Mock data
        Session session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(2L);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Test method
        sessionService.participate(1L, 2L);

        // Verify
        verify(sessionRepository, times(1)).save(session);
        assertEquals(1, session.getUsers().size());
    }


    @Test
    void testParticipate_UserAlreadyParticipating() {
        // Mock data
        User user = new User();
        user.setId(2L);

        Session session = new Session();
        session.setId(1L);
        session.setUsers(Collections.singletonList(user));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        // Test method & Assertions
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 2L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testParticipate_SessionOrUserNotFound() {
        // Mock behavior
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Test method & Assertions
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 2L));
    }

    @Test
    void testNoLongerParticipate_Success() {
        // Mock data
        User user = new User();
        user.setId(2L);

        Session session = new Session();
        session.setId(1L);
        session.setUsers(Collections.singletonList(user));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Test method
        sessionService.noLongerParticipate(1L, 2L);

        // Verify
        verify(sessionRepository, times(1)).save(session);
        assertTrue(session.getUsers().isEmpty());
    }

    @Test
    void testNoLongerParticipate_UserNotParticipating() {
        // Mock data
        Session session = new Session();
        session.setId(1L);
        session.setUsers(Collections.emptyList());

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Test method & Assertions
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 2L));
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void testNoLongerParticipate_SessionNotFound() {
        // Mock behavior
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Test method & Assertions
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 2L));
    }
}

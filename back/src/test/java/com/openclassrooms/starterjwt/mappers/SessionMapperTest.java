package com.openclassrooms.starterjwt.mappers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToEntity_WithValidData() {
        // Mock data
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Yoga Class");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(2L, 3L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(2L)).thenReturn(user1);
        when(userService.findById(3L)).thenReturn(user2);

        // Test method
        Session result = sessionMapper.toEntity(sessionDto);

        // Assertions
        assertNotNull(result);
        assertEquals("Yoga Class", result.getDescription());
        assertEquals(teacher, result.getTeacher());
        assertEquals(2, result.getUsers().size());
        assertTrue(result.getUsers().contains(user1));
        assertTrue(result.getUsers().contains(user2));
    }

    @Test
    void testToDto_WithValidData() {
        // Mock data
        Session session = new Session();
        session.setDescription("Yoga Class");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        User user1 = new User();
        user1.setId(2L);
        User user2 = new User();
        user2.setId(3L);
        session.setUsers(Arrays.asList(user1, user2));

        // Test method
        SessionDto result = sessionMapper.toDto(session);

        // Assertions
        assertNotNull(result);
        assertEquals("Yoga Class", result.getDescription());
        assertEquals(1L, result.getTeacher_id());
        assertEquals(2, result.getUsers().size());
        assertTrue(result.getUsers().contains(2L));
        assertTrue(result.getUsers().contains(3L));
    }

    @Test
    void testToEntity_WithNullUsers() {
        // Mock data
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Yoga Class");
        sessionDto.setTeacher_id(null);
        sessionDto.setUsers(null);

        // Test method
        Session result = sessionMapper.toEntity(sessionDto);

        // Assertions
        assertNotNull(result);
        assertNull(result.getTeacher());
        assertNotNull(result.getUsers());
        assertTrue(result.getUsers().isEmpty());
    }

    @Test
    void testToDto_WithNullUsers() {
        // Mock data
        Session session = new Session();
        session.setDescription("Yoga Class");
        session.setTeacher(null);
        session.setUsers(null);

        // Test method
        SessionDto result = sessionMapper.toDto(session);

        // Assertions
        assertNotNull(result);
        assertNull(result.getTeacher_id());
        assertNotNull(result.getUsers());
        assertTrue(result.getUsers().isEmpty());
    }

    @Test
    void testToEntity_NullList() {
        // Mapping d'une liste null
        List<Session> sessions = sessionMapper.toEntity((List<SessionDto>) null);

        // Vérification
        assertNull(sessions);
    }

    @Test
    void testToEntity_EmptyList() {
        // Mapping d'une liste vide
        List<SessionDto> dtoList = new ArrayList<>();
        List<Session> sessions = sessionMapper.toEntity(dtoList);

        // Vérification
        assertNotNull(sessions);
        assertTrue(sessions.isEmpty());
    }

    @Test
    void testToEntity_ListWithElements() {
        // Création de la liste de SessionDto
        List<SessionDto> dtoList = new ArrayList<>();
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setDescription("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setDescription("Session 2");

        dtoList.add(sessionDto1);
        dtoList.add(sessionDto2);

        // Mapping vers une liste d'entités Session
        List<Session> sessions = sessionMapper.toEntity(dtoList);

        // Vérification
        assertNotNull(sessions);
        assertEquals(2, sessions.size());

        // Vérification du premier élément
        assertEquals(1L, sessions.get(0).getId());
        assertEquals("Session 1", sessions.get(0).getDescription());

        // Vérification du second élément
        assertEquals(2L, sessions.get(1).getId());
        assertEquals("Session 2", sessions.get(1).getDescription());
    }

    @Test
    void testToDto_NullList() {
        // Mapping d'une liste null
        List<SessionDto> dtoList = sessionMapper.toDto((List<Session>) null);

        // Vérification
        assertNull(dtoList);
    }

    @Test
    void testToDto_EmptyList() {
        // Mapping d'une liste vide
        List<Session> entityList = new ArrayList<>();
        List<SessionDto> dtoList = sessionMapper.toDto(entityList);

        // Vérification
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void testToDto_ListWithElements() {
        // Création de la liste d'entités Session
        List<Session> entityList = new ArrayList<>();
        Session session1 = new Session();
        session1.setId(1L);
        session1.setDescription("Session 1");

        Session session2 = new Session();
        session2.setId(2L);
        session2.setDescription("Session 2");

        entityList.add(session1);
        entityList.add(session2);

        // Mapping vers une liste de DTOs SessionDto
        List<SessionDto> dtoList = sessionMapper.toDto(entityList);

        // Vérification
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        // Vérification du premier élément
        assertEquals(1L, dtoList.get(0).getId());
        assertEquals("Session 1", dtoList.get(0).getDescription());

        // Vérification du second élément
        assertEquals(2L, dtoList.get(1).getId());
        assertEquals("Session 2", dtoList.get(1).getDescription());
    }
}
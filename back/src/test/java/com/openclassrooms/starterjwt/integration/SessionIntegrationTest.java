package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class SessionIntegrationTest {

    @Autowired
    private SessionController sessionController;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    private Session session1;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        // Création d'un enseignant pour les sessions
        teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher = teacherRepository.save(teacher);

        // Création d'un utilisateur
        user = new User( "user@example.com", "Doe", "Jane", "password123", false);
        user = userRepository.save(user);

        // Création d'une session pour les tests
        session1 = new Session();
        session1.setName("Test Session");
        session1.setDate(new Date());
        session1.setDescription("Test Description");
        session1.setTeacher(teacher);
        session1.setUsers( new ArrayList<>() );
        session1 = sessionRepository.save(session1);

        // Vérifier que les ID sont bien générés
        assertNotNull(session1.getId());
        assertNotNull(user.getId());
    }

    @Test
    void testFindById_SessionExists() {
        ResponseEntity<?> response = sessionController.findById(String.valueOf(session1.getId()));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testFindById_SessionDoesNotExist() {
        ResponseEntity<?> response = sessionController.findById("999");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindById_BadRequest() {
        ResponseEntity<?> response = sessionController.findById("invalid");

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testFindAllSessions() {
        ResponseEntity<?> response = sessionController.findAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCreateSession_Success() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("New Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("New Description");
        sessionDto.setTeacher_id(teacher.getId());

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateSession_Success() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(teacher.getId());

        ResponseEntity<?> response = sessionController.update(String.valueOf(session1.getId()), sessionDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateSession_BadRequest() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setDescription("Updated Description");
        sessionDto.setTeacher_id(teacher.getId());

        ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDeleteSession_Success() {
        ResponseEntity<?> response = sessionController.save(String.valueOf(session1.getId()));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(sessionRepository.existsById(session1.getId()));
    }

    @Test
    void testDeleteSession_NotFound() {
        ResponseEntity<?> response = sessionController.save("999");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testParticipate_Success() {
        ResponseEntity<?> response = sessionController.participate(String.valueOf(session1.getId()), String.valueOf(user.getId()));
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        // Vérification que l'utilisateur est bien ajouté à la session
        session1 = sessionRepository.findById(session1.getId()).orElseThrow(null);
        assertTrue(session1.getUsers().contains(user));
    }

    @Test
    void testParticipate_BadRequest() {
        ResponseEntity<?> response = sessionController.participate("invalid", "invalid");
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testDoNotParticipate_Success() {
        // Ajouter l'utilisateur à la session avant de tester le retrait
        sessionController.participate(String.valueOf(session1.getId()), String.valueOf(user.getId()));

        ResponseEntity<?> response = sessionController.noLongerParticipate(String.valueOf(session1.getId()), String.valueOf(user.getId()));
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        // Vérification que l'utilisateur est bien retiré de la session
        session1 = sessionRepository.findById(session1.getId()).orElseThrow(null);
        assertFalse(session1.getUsers().contains(user));
    }

    @Test
    void testDoNotParticipate_BadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "invalid");
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }
}

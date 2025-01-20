package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// Réinitialise le contexte après chaque test
@Transactional
public class TeacherIntegrationTest {

    @Test
    void contextLoads() {
        // Vérifie que le contexte se charge correctement
    }

    @Autowired
    private TeacherController teacherController;

    @Autowired
    private TeacherRepository teacherRepository;

    Teacher teacher1;
    Teacher teacher2;

    @BeforeEach
    void setUp() {
        // Initialiser les données dans la base
        Teacher teacher = new Teacher( 1L, "Doe", "John", null, null);
        this.teacher1 = teacherRepository.save(teacher);
        Teacher teacher2 = new Teacher(2L, "Doe", "Jane", null, null);
        this.teacher2 = teacherRepository.save(teacher2);
    }


    @Test
    void testFindById_TeacherExists() {
        ResponseEntity<?> response = teacherController.findById(String.valueOf(teacher1.getId()));

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        TeacherDto teacherDto = (TeacherDto) response.getBody();
        assertNotNull(teacherDto);
        assertEquals("Doe", teacherDto.getLastName());
        assertEquals("John", teacherDto.getFirstName());
    }

    @Test
    void testFindById_TeacherDoesNotExists() {
        ResponseEntity<?> response = teacherController.findById(String.valueOf(8));

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindById_BadRequest() {
        ResponseEntity<?> response = teacherController.findById(String.valueOf(false));

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void getAllTeachers() {
        ResponseEntity<?> response = teacherController.findAll();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}

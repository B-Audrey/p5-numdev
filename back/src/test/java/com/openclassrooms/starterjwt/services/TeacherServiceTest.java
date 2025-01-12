package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_TeachersFound() {
        // Mock data
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("Teacher");
        teacher1.setLastName("One");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Teacher");
        teacher2.setLastName("Two");

        List<Teacher> mockTeachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(mockTeachers);

        // Test method
        List<Teacher> result = teacherService.findAll();

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("One", result.get(0).getLastName());
        assertEquals("Teacher", result.get(0).getFirstName());
        assertEquals("Two", result.get(1).getLastName());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Teacher", result.get(1).getFirstName());
        assertEquals(2L, result.get(1).getId());

    }

    @Test
    void testFindAll_NoTeachersFound() {
        // Mock data
        when(teacherRepository.findAll()).thenReturn(Collections.emptyList());

        // Test method
        List<Teacher> result = teacherService.findAll();

        // Assertions
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindById_TeacherFound() {
        // Mock data
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Teacher");
        teacher.setLastName("One");
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Test method
        Teacher result = teacherService.findById(1L);

        // Assertions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("One", result.getLastName());
        assertEquals("Teacher", result.getFirstName());
    }

    @Test
    void testFindById_TeacherNotFound() {
        // Mock behavior
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // Test method
        Teacher result = teacherService.findById(1L);

        // Assertions
        assertNull(result);
    }
}

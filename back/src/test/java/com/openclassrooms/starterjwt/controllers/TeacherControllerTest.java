package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_TeacherFound() {
        // Mock data
        Teacher teacher = new Teacher(1L, "firstName", "lastName", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto teacherDto = new TeacherDto( 1L, "firstName", "lastName", LocalDateTime.now(), LocalDateTime.now());
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Test method
        ResponseEntity<?> response = teacherController.findById("1");

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teacherDto, response.getBody());
    }

    @Test
    void testFindById_TeacherNotFound() {
        // Mock data
        when(teacherService.findById(1L)).thenReturn(null);

        // Test method
        ResponseEntity<?> response = teacherController.findById("1");

        // Assertions
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testFindById_InvalidId() {
        // Test method
        ResponseEntity<?> response = teacherController.findById("abc");

        // Assertions
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testFindAll_TeachersFound() {
        // Mock data
        Teacher teacher1 = new Teacher(1L, "firstName", "lastName", LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher2 = new Teacher(2L, "firstName", "lastName", LocalDateTime.now(), LocalDateTime.now());
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        TeacherDto teacher1Dto = new TeacherDto( 1L, "firstName", "lastName", LocalDateTime.now(), LocalDateTime.now());
        TeacherDto teacher2Dto = new TeacherDto( 2L, "firstName", "lastName", LocalDateTime.now(), LocalDateTime.now());
        List<TeacherDto> teacherDtos = Arrays.asList(teacher1Dto, teacher2Dto);
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        // Test method
        ResponseEntity<?> response = teacherController.findAll();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(teacherDtos, response.getBody());
    }

    @Test
    void testFindAll_NoTeachers() {
        // Mock data
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(teacherMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Test method
        ResponseEntity<?> response = teacherController.findAll();

        // Assertions
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(((List<?>) response.getBody()).isEmpty());
    }
}

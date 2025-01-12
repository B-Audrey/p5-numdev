package com.openclassrooms.starterjwt.mappers;

import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class TeacherMapperImplTest {

    private final TeacherMapper teacherMapper = new TeacherMapperImpl();

    @Test
    void testToEntity_Null() {
        // Mapping d'un DTO null
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);

        // Vérification
        assertNull(teacher);
    }

    @Test
    void testToEntity_WithData() {
        // Création d'un TeacherDto
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        // Mapping vers Teacher
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Vérifications
        assertNotNull(teacher);
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    void testToDto_Null() {
        // Mapping d'une entité null
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);

        // Vérification
        assertNull(teacherDto);
    }

    @Test
    void testToDto_WithData() {
        // Création d'une entité Teacher
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Mapping vers TeacherDto
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Vérifications
        assertNotNull(teacherDto);
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
    }

    @Test
    void testToEntity_List_Null() {
        // Mapping d'une liste null
        List<Teacher> teachers = teacherMapper.toEntity((List<TeacherDto>) null);

        // Vérification
        assertNull(teachers);
    }

    @Test
    void testToEntity_List_Empty() {
        // Mapping d'une liste vide
        List<TeacherDto> dtoList = new ArrayList<>();
        List<Teacher> teachers = teacherMapper.toEntity(dtoList);

        // Vérification
        assertNotNull(teachers);
        assertTrue(teachers.isEmpty());
    }

    @Test
    void testToEntity_List_WithData() {
        // Création d'une liste de TeacherDto
        List<TeacherDto> dtoList = new ArrayList<>();
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Smith");

        dtoList.add(teacherDto1);
        dtoList.add(teacherDto2);

        // Mapping vers une liste de Teacher
        List<Teacher> teachers = teacherMapper.toEntity(dtoList);

        // Vérifications
        assertNotNull(teachers);
        assertEquals(2, teachers.size());
        assertEquals(1L, teachers.get(0).getId());
        assertEquals("Jane", teachers.get(1).getFirstName());
    }

    @Test
    void testToDto_List_Null() {
        // Mapping d'une liste null
        List<TeacherDto> dtoList = teacherMapper.toDto((List<Teacher>) null);

        // Vérification
        assertNull(dtoList);
    }

    @Test
    void testToDto_List_Empty() {
        // Mapping d'une liste vide
        List<Teacher> entityList = new ArrayList<>();
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        // Vérification
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void testToDto_List_WithData() {
        // Création d'une liste de Teacher
        List<Teacher> entityList = new ArrayList<>();
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        entityList.add(teacher1);
        entityList.add(teacher2);

        // Mapping vers une liste de TeacherDto
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        // Vérifications
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());
        assertEquals(1L, dtoList.get(0).getId());
        assertEquals("Smith", dtoList.get(1).getLastName());
    }
}

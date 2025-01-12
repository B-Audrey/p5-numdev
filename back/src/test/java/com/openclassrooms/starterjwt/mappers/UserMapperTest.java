package com.openclassrooms.starterjwt.mappers;

import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testEntityToDto() {
        // Création d'une entité User
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");

        // Mapping vers UserDto
        UserDto userDto = userMapper.toDto(user);

        // Vérifications
        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
    }

    @Test
    void testDtoToEntity() {
        // Création d'un DTO UserDto
        UserDto userDto = new UserDto();
        userDto.setId(2L);
        userDto.setEmail("dto@example.com");
        userDto.setFirstName("Jane");
        userDto.setLastName("Smith");
        userDto.setPassword("defaultPassword");

        // Mapping vers User
        User user = userMapper.toEntity(userDto);

        // Vérifications
        assertNotNull(user);
        assertEquals(2L, user.getId());
        assertEquals("dto@example.com", user.getEmail());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void testEntityToDto_NullEntity() {
        // Mapping d'une entité null
        UserDto userDto = userMapper.toDto((User) null);

        // Vérification
        assertNull(userDto);
    }

    @Test
    void testDtoToEntity_NullDto() {
        // Mapping d'un DTO null
        User user = userMapper.toEntity((UserDto) null);

        // Vérification
        assertNull(user);
    }

    @Test
    void testToEntity_NullList() {
        // Mapping d'une liste null
        List<User> users = userMapper.toEntity((List<UserDto>) null);

        // Vérification
        assertNull(users);
    }

    @Test
    void testToEntity_EmptyList() {
        // Mapping d'une liste vide
        List<UserDto> dtoList = new ArrayList<>();
        List<User> users = userMapper.toEntity(dtoList);

        // Vérification
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void testToEntity_ListWithElements() {
        // Création de la liste de UserDto
        List<UserDto> dtoList = new ArrayList<>();
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("user1@example.com");
        userDto1.setFirstName("John");
        userDto1.setLastName("Doe");
        userDto1.setPassword("defaultPassword");


        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("user2@example.com");
        userDto2.setFirstName("Jane");
        userDto2.setLastName("Smith");
        userDto2.setPassword("defaultPassword");


        dtoList.add(userDto1);
        dtoList.add(userDto2);

        // Mapping vers une liste d'entités User
        List<User> users = userMapper.toEntity(dtoList);

        // Vérification
        assertNotNull(users);
        assertEquals(2, users.size());

        // Vérification du premier élément
        assertEquals(1L, users.get(0).getId());
        assertEquals("user1@example.com", users.get(0).getEmail());
        assertEquals("John", users.get(0).getFirstName());
        assertEquals("Doe", users.get(0).getLastName());

        // Vérification du second élément
        assertEquals(2L, users.get(1).getId());
        assertEquals("user2@example.com", users.get(1).getEmail());
        assertEquals("Jane", users.get(1).getFirstName());
        assertEquals("Smith", users.get(1).getLastName());
    }

    @Test
    void testToDto_NullList() {
        // Mapping d'une liste null
        List<UserDto> dtoList = userMapper.toDto((List<User>) null);

        // Vérification
        assertNull(dtoList);
    }

    @Test
    void testToDto_EmptyList() {
        // Mapping d'une liste vide
        List<User> entityList = new ArrayList<>();
        List<UserDto> dtoList = userMapper.toDto(entityList);

        // Vérification
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }

    @Test
    void testToDto_ListWithElements() {
        // Création de la liste d'entités User
        List<User> entityList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setAdmin(false);

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setAdmin(true);

        entityList.add(user1);
        entityList.add(user2);

        // Mapping vers une liste de DTOs UserDto
        List<UserDto> dtoList = userMapper.toDto(entityList);

        // Vérification
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        // Vérification du premier élément
        assertEquals(1L, dtoList.get(0).getId());
        assertEquals("user1@example.com", dtoList.get(0).getEmail());
        assertEquals("John", dtoList.get(0).getFirstName());
        assertEquals("Doe", dtoList.get(0).getLastName());

        // Vérification du second élément
        assertEquals(2L, dtoList.get(1).getId());
        assertEquals("user2@example.com", dtoList.get(1).getEmail());
        assertEquals("Jane", dtoList.get(1).getFirstName());
        assertEquals("Smith", dtoList.get(1).getLastName());
    }
}
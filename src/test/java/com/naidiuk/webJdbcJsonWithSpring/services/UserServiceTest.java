package com.naidiuk.webJdbcJsonWithSpring.services;

import com.naidiuk.webJdbcJsonWithSpring.dto.UserDto;
import com.naidiuk.webJdbcJsonWithSpring.entity.User;
import com.naidiuk.webJdbcJsonWithSpring.errors.UserNotFoundException;
import com.naidiuk.webJdbcJsonWithSpring.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testSave() {
        //prepare
        UserDto userDto = createUserAndTransformToDto();

        //when
        UserDto expectedUser = userService.save(userDto);

        //then
        assertNull(userDto.getId());
        assertNotNull(expectedUser.getId());
    }

    @Test
    void testUpdate() {
        //prepare
        UserDto updatedUserDto = createUserAndTransformToDto();
        updatedUserDto.setId(2);

        //when
        userService.update(updatedUserDto);

        //then
        assertEquals("Elon", updatedUserDto.getName());
        assertEquals("Mask", updatedUserDto.getSurname());
        assertEquals(3243242, updatedUserDto.getSalary());
        assertEquals(21, updatedUserDto.getWorkExperienceYears());
    }

    @Test
    void testUpdateWithWrongId() {
        //prepare
        UserDto updatedUserDto = createUserAndTransformToDto();
        updatedUserDto.setId(114);

        //then
        assertThrows(UserNotFoundException.class, () -> userService.update(updatedUserDto));
    }

    @Test
    void testFindById() {
        //when
        UserDto userDto = userService.findById(4);

        //then
        assertEquals(4, userDto.getId());
    }

    @Test
    void testFindByIdWithWrongId() {
        assertThrows(UserNotFoundException.class, () -> userService.findById(322));
    }

    private UserDto createUserAndTransformToDto() {
        User user = new User();
        user.setName("Elon");
        user.setSurname("Mask");
        user.setSalary(3243242);
        user.setWorkExperienceYears(21);

        return UserMapper.transformToDto(user);
    }
}
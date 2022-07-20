package com.naidiuk.webJdbcJsonWithSpring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naidiuk.webJdbcJsonWithSpring.controllers.responses.ErrorResponse;
import com.naidiuk.webJdbcJsonWithSpring.dto.UserDto;
import com.naidiuk.webJdbcJsonWithSpring.entity.User;
import com.naidiuk.webJdbcJsonWithSpring.errors.UserNotFoundException;
import com.naidiuk.webJdbcJsonWithSpring.mappers.UserMapper;
import com.naidiuk.webJdbcJsonWithSpring.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void save() throws Exception {
        //prepare
        UserDto userDto = createUserAndTransformToDto();

        ArgumentMatchers.any(UserDto.class);

        when(userService.save(userDto)).thenReturn(userDto);

        String requestBody = objectMapper.writeValueAsString(userDto);

        //then
        mockMvc.perform(post("/users")
                        .contentType("application/json;charset=UTF-8")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Elon"))
                .andExpect(jsonPath("$.surname").value("Mask"))
                .andExpect(jsonPath("$.salary").value(3243242))
                .andExpect(jsonPath("$.workExperienceYears").value(21));
    }

    @Test
    void update() throws Exception {
        //prepare
        UserDto userDto = createUserAndTransformToDto();
        userDto.setId(4);

        ArgumentMatchers.any(UserDto.class);

        when(userService.update(userDto)).thenReturn(userDto);

        String requestBody = objectMapper.writeValueAsString(userDto);

        //then
        mockMvc.perform(put("/users")
                        .contentType("application/json;charset=UTF-8")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Elon"))
                .andExpect(jsonPath("$.surname").value("Mask"))
                .andExpect(jsonPath("$.salary").value(3243242))
                .andExpect(jsonPath("$.workExperienceYears").value(21));

    }

    @Test
    void testUpdateWithWrongId() throws Exception {
        //prepare
        UserDto userDto = createUserAndTransformToDto();
        userDto.setId(99);

        String requestBody = objectMapper.writeValueAsString(userDto);

//        ArgumentMatchers.any(UserDto.class);
        when(userService.update(userDto)).thenThrow(new UserNotFoundException("User with id = 99 doesn't exist."));


        //then
        mockMvc.perform(put("/users")
                        .contentType("application/json;charset=UTF-8")
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("произошла ошибка"))
                .andExpect(jsonPath("$.message").value("User with id = 99 doesn't exist."));
    }

    @Test
    void testGetById() throws Exception {
        //prepare
        UserDto userDto = createUserAndTransformToDto();
        userDto.setId(4);

        when(userService.findById(4)).thenReturn(userDto);

        //then
        mockMvc.perform(get("/users/{id}", 4))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("Elon"))
                .andExpect(jsonPath("$.surname").value("Mask"))
                .andExpect(jsonPath("$.salary").value(3243242))
                .andExpect(jsonPath("$.workExperienceYears").value(21));
    }

    @Test
    void testGetByIdWithWrongId() throws Exception {
        //prepare
        when(userService.findById(99))
                .thenThrow(new UserNotFoundException("User with id = 99 doesn't exist."));

        //then
        mockMvc.perform(get("/users/{id}", 99))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("произошла ошибка"))
                .andExpect(jsonPath("$.message").value("User with id = 99 doesn't exist."));
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
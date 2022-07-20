package com.naidiuk.webJdbcJsonWithSpring.mappers;

import com.naidiuk.webJdbcJsonWithSpring.dto.UserDto;
import com.naidiuk.webJdbcJsonWithSpring.entity.User;

public class UserMapper {
    public static UserDto transformToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setSalary(user.getSalary());
        userDto.setWorkExperienceYears(user.getWorkExperienceYears());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User transformToDao(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setSalary(userDto.getSalary());
        user.setWorkExperienceYears(userDto.getWorkExperienceYears());
        user.setEmail(userDto.getEmail());
        return user;
    }
}

package com.naidiuk.webJdbcJsonWithSpring.services;

import com.naidiuk.webJdbcJsonWithSpring.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);
    void upload(MultipartFile file);
    void download();
    UserDto delete(Integer id);
    UserDto update(UserDto updatedUserDto);
    UserDto findById(Integer id);
    List<UserDto> findAll();
    List<UserDto> findByNameStartsWithO();
    UserDto findWithMaxId();
    UserDto calculateYearsUntilRetirement(Integer id);
}

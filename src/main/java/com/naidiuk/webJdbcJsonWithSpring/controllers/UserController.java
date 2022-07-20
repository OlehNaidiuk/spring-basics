package com.naidiuk.webJdbcJsonWithSpring.controllers;

import com.naidiuk.webJdbcJsonWithSpring.controllers.responses.ErrorResponse;
import com.naidiuk.webJdbcJsonWithSpring.dto.UserDto;
import com.naidiuk.webJdbcJsonWithSpring.errors.EmailValidationException;
import com.naidiuk.webJdbcJsonWithSpring.errors.UserNotFoundException;
import com.naidiuk.webJdbcJsonWithSpring.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final String ERROR_MESSAGE = "произошла ошибка";

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserDto> usersDto = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(usersDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            UserDto userDto = userService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (UserNotFoundException exception) {
            return errorResponse(ERROR_MESSAGE, exception);
        }
    }

    @GetMapping("/max")
    public ResponseEntity<?> getWithMaxId() {
        UserDto userWithMaxId = userService.findWithMaxId();
        return ResponseEntity.status(HttpStatus.OK).body(userWithMaxId);
    }

    @GetMapping("/calculate-retirement/{id}")
    public ResponseEntity<?> getByIdWithYearsUntilRetirement(@PathVariable Integer id) {
        try {
            UserDto userDto = userService.calculateYearsUntilRetirement(id);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (UserNotFoundException exception) {
            return errorResponse(ERROR_MESSAGE, exception);
        }
    }

    @GetMapping("/name-starts-with-o")
    public ResponseEntity<?> getByNameStartsWithO() {
        List<UserDto> usersDto = userService.findByNameStartsWithO();
        return ResponseEntity.status(HttpStatus.OK).body(usersDto);
    }

    @GetMapping("/download")
    public void downloadToExcelFile() {
        userService.download();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UserDto updatedUserDto) {
        try {
            UserDto userDto = userService.update(updatedUserDto);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (UserNotFoundException | EmailValidationException exception) {
            return errorResponse(ERROR_MESSAGE, exception);
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UserDto userDto) {
        try {
            UserDto savedUserDto = userService.save(userDto);
            return ResponseEntity.status(HttpStatus.OK).body(savedUserDto);
        } catch (EmailValidationException e) {
            return errorResponse(ERROR_MESSAGE, e);
        }
    }

    @PostMapping("/upload")
    public void uploadFromExcelFile(@RequestParam("file")MultipartFile file) {
        userService.upload(file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            UserDto deletedUserDto = userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(deletedUserDto);
        } catch (UserNotFoundException exception) {
            return errorResponse(ERROR_MESSAGE, exception);
        }
    }

    private ResponseEntity<?> errorResponse(String errorMessage, Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(errorMessage, e.getMessage()));
    }
}

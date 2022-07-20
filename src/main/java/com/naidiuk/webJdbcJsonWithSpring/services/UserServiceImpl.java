package com.naidiuk.webJdbcJsonWithSpring.services;

import com.naidiuk.webJdbcJsonWithSpring.dto.UserDto;
import com.naidiuk.webJdbcJsonWithSpring.entity.User;
import com.naidiuk.webJdbcJsonWithSpring.errors.EmailValidationException;
import com.naidiuk.webJdbcJsonWithSpring.errors.UserNotFoundException;
import com.naidiuk.webJdbcJsonWithSpring.mappers.UserMapper;
import com.naidiuk.webJdbcJsonWithSpring.repositories.UserRepository;
import com.naidiuk.webJdbcJsonWithSpring.util.EmailValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ExcelService excelService;
    private static final int YEARS_OF_RETIREMENT = 30;

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        validateEmail(userDto.getEmail());
        User user = UserMapper.transformToDao(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.transformToDto(savedUser);
    }

    @Override
    public void importFrom(MultipartFile file) {
        List<User> users = excelService.read(file);
        userRepository.saveAll(users);
    }

    @Override
    public Resource export() {
        List<User> users = userRepository.findAll();
        File fileWithUsers = excelService.write(users);
        if (fileWithUsers.exists()) {
            Path filePath = Paths.get(fileWithUsers.getAbsolutePath());
            try {
                return new UrlResource(filePath.toUri());
            } catch (MalformedURLException e) {
                throw new RuntimeException("Resource not found.");
            }
        } else {
            throw new RuntimeException("File not found.");
        }

    }

    @Override
    @Transactional
    public UserDto delete(Integer id) {
        UserDto userDto = findById(id);
        userRepository.deleteById(id);
        return userDto;
    }

    @Override
    @Transactional
    public UserDto update(UserDto updatedUserDto) {
        UserDto userDto = findById(updatedUserDto.getId());

        validateEmail(updatedUserDto.getEmail());

        User updatedUser = UserMapper.transformToDao(userDto);

        updatedUser.setName(updatedUserDto.getName());
        updatedUser.setSurname(updatedUserDto.getSurname());
        updatedUser.setSalary(updatedUserDto.getSalary());
        updatedUser.setWorkExperienceYears(updatedUserDto.getWorkExperienceYears());
        updatedUser.setEmail(updatedUserDto.getEmail());

        userRepository.save(updatedUser);

        return updatedUserDto;
    }

    @Override
    public UserDto findById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() ->
                new UserNotFoundException("User with id = " + id + " doesn't exist."));
        return UserMapper.transformToDto(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(UserMapper::transformToDto)
                    .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findByNameStartsWithO() {
        //stream от списка users
        List<User> users = userRepository.findAll();
        return users.stream()
                    .filter(user -> (user.getName() != null && user.getName().startsWith("O")))
                    .map(UserMapper::transformToDto)
                    .collect(Collectors.toList());
    }


    @Override
    public UserDto findWithMaxId() {
        User user = userRepository.findWithMaxId();
        return UserMapper.transformToDto(user);
    }

    @Override
    public UserDto calculateYearsUntilRetirement(Integer id) {
        UserDto userDto = findById(id);
        userDto.setYearsUntilRetirement(YEARS_OF_RETIREMENT - userDto.getWorkExperienceYears());
        return userDto;
    }

    private void validateEmail(String email) {
        if (email == null) {
            throw new EmailValidationException("Please enter your email.");
        }

        boolean emailIsValidate = EmailValidatorUtil.validate(email);

        if (!emailIsValidate) {
            throw new EmailValidationException("You entered invalid email.");
        }
    }
}

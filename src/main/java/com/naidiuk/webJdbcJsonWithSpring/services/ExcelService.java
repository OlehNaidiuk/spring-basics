package com.naidiuk.webJdbcJsonWithSpring.services;

import com.naidiuk.webJdbcJsonWithSpring.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface ExcelService {
    List<User> read(MultipartFile file);
    File write(List<User> users);
}

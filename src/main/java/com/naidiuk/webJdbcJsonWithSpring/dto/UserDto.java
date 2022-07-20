package com.naidiuk.webJdbcJsonWithSpring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserDto {
    private Integer id;
    private String name;
    private String surname;
    private int salary;
    private int workExperienceYears;
    private int yearsUntilRetirement;
    private String email;
}

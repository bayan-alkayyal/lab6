package com.example.lab6.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {

    @NotEmpty(message = "id can not be empty")
    @Size(min = 3 , message = "id must be more than 2 characters")
    private String id ;

    @NotEmpty(message = "name can not be empty")
    @Size(min = 5 , message = "name must be more than 4 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String name ;

    @Email
    private String email ;

    @Pattern(regexp = "^05\\d{8}$")
    @Size(min = 10 , max = 10 , message = "phone number must consist of 10 digit")
    private String phoneNumber ;

    @NotNull(message = "age can not be empty")
    /// must be number ?
    @Min(26)
    private int age ;

    @NotEmpty(message = "position can not be empty")
    @Pattern(regexp = "^(supervisor | coordinator)$" , message = "position should be : supervisor or coordinator")
    private String position ;


    private boolean onLeave = false ;

    @JsonFormat(pattern = "YYYY-MM-dd")
    @PastOrPresent
    @NotNull(message = "hire date can not be empty")
    private LocalDate hireDate ;

    @NotNull(message = "annual leave can not be empty")
    @PositiveOrZero(message = "annual leave should be positive number or zero")
    private int annualLeave ;


}

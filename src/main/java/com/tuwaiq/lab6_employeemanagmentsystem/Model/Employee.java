package com.tuwaiq.lab6_employeemanagmentsystem.Model;

import Api.ApiResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
    @NotEmpty(message = "Sorry, your ID can't be empty, please try again.")
    @Size(min = 2,message = "Sorry, your ID must be at least 2 characters or more, please try again.")
    private String id;
    @NotEmpty(message = "Sorry, your Name can't be empty, please try again.")
    @Size(min = 4,message = "Sorry, your Name must be at least 4 characters or more, please try again.")
    @Pattern(regexp = "^[a-zA-Z]+$",message = "Sorry, your Name must only contain alphabet characters (no numbers allowed), please try again.")
    private String name;
    @Email(message = "Sorry, your Email must be a valid email formate (containing @domain), please try again.")
    private String email;
    @Pattern(regexp = "^(?=05)[0-9]{10,10}$",message = "Sorry, your Phone number must start with 05 and contain 10 numbers, please try again.")
    private String phoneNumber;
    @NotNull(message = "Sorry, your Age can't be empty, please try again.")
    @Positive(message = "Sorry, your Age must be a positive number, please try again.")
    @Min(value = 25,message = "Sorry, your Age can't be less than 25 years old, please try again.")
    private int age;
    @NotEmpty(message = "Sorry, your Position can't be empty, please try again.")
    @Pattern(regexp = "Supervisor|Coordinator", message = "Sorry, your Position can only be 'Supervisor' or 'Coordinator', please try again.")
    private String position;
    @AssertFalse(message = "Sorry, you can't be On leave when registering")
    private Boolean onLeave;
    @NotNull(message = "Sorry your Hire date can't be empty, please try again")
    @PastOrPresent(message = "Sorry, your Hire date can only be from the past or present, please try again.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    @NotNull(message = "Sorry, your Annual leave can't be empty, please try again.")
    @PositiveOrZero(message = "Sorry, your Annual leave can only be positive or zero, please try again.")
    private int annualLeave;
}

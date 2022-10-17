package com.retrospecsoptometrists.service.authentication.dtos;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateProfileDto implements Serializable {

    private static final long serialVersionUID = 3746783924711L;

    @NotBlank(message = "Email Address cannot be missing")
    @Email(message = "Email Address must be correctly formulated")
    private String emailAddress;

    @NotBlank(message = "Password cannot be missing")
    private String password;

    @NotBlank(message = "Employee ID cannot be missing")
    private String employeeId;

    @NotBlank(message = "First Name cannot be missing")
    private String firstName;

    @NotBlank(message = "Last Name cannot be missing")
    private String lastName;

    @NotBlank(message = "Phone Number cannot be missing")
    @Size(min = 5, max = 32, message = "Phone Number must be correctly formulated")
    private String phoneNumber;

    @NotNull(message = "Enabling status cannont be missing")
    private Boolean enabled;

    @NotNull(message = "Organisation ID cannot be missing")
    private Long organisationalId;

    @NotNull(message = "Branch ID cannot be missing")
    private String branchId;

    @NotBlank(message = "The role cannot be missing")
    private String role;
}

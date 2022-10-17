package com.retrospecsoptometrists.service.authentication.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SelfUpdateRequestDto implements Serializable {

    private static final long serialVersionUID = 3256444672984L;

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

}

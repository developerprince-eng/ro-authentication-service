package com.retrospecsoptometrists.service.authentication.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 7364783332221L;

    @NotBlank(message = "The username cannot be missing")
    private String username;

    @NotBlank(message = "The password cannot be missing")
    private String password;

}

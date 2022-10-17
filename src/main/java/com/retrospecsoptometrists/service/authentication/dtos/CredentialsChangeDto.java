package com.retrospecsoptometrists.service.authentication.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CredentialsChangeDto implements Serializable {

    private static final long serialVersionUID = 7812367482737L;

    @NotBlank(message = "The old password cannot be missing")
    private String oldPassword;

    @NotBlank(message = "The new password cannot be missing")
    private String newPassword;
}

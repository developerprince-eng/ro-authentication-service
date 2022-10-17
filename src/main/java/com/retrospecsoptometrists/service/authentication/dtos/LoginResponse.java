package com.retrospecsoptometrists.service.authentication.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 6734671295340L;

    private String token;

    private Object accessTicket;
}

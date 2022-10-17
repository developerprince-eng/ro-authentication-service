package com.retrospecsoptometrists.service.authentication.exceptions;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ErrorMessage implements Serializable {
    private static final long serialVersionUID = 7364729127821L;

    private ZonedDateTime eventTime;

    private String error;

    private String errorDescription;

    private HttpStatus errorCode;
}

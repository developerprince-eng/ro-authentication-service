package com.retrospecsoptometrists.service.authentication.controllers;

import javax.validation.Valid;

import com.retrospecsoptometrists.service.authentication.dtos.LoginRequest;
import com.retrospecsoptometrists.service.authentication.services.AuthenticationManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
@CrossOrigin(origins = "${origin.user}", allowedHeaders = {"Authorization", "Origin"})
public class AuthenticationController {

    private final AuthenticationManagementService authenticationManagementService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationManagementService.authenticateUser(loginRequest);
    }

    // Forgot Password (/forgot/credentials)
}

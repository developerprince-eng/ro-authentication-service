package com.retrospecsoptometrists.service.authentication.services;

import java.util.Base64;

import com.retrospecsoptometrists.service.authentication.dtos.LoginRequest;
import com.retrospecsoptometrists.service.authentication.dtos.LoginResponse;
import com.retrospecsoptometrists.service.authentication.utilities.JwtManipulationUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationManagementService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtManipulationUtility jwtUtility;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtility.generateJwtToken(authentication);
        String[] tokenComponents = token.split(BaseServicesResource.TOKEN_SPLITTER);
        Base64.Decoder base64Decoder = Base64.getUrlDecoder();
        String accessTicketStr = new String(base64Decoder.decode(tokenComponents[BaseServicesResource.TOKEN_PAYLOAD]));

        return ResponseEntity.ok(LoginResponse.builder()
                .token(token)
                .accessTicket(BaseServicesResource.gsonMapper.fromJson(accessTicketStr, Object.class))
                .build());
    }
}

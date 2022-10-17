package com.retrospecsoptometrists.service.authentication.services;

import java.security.SecureRandom;

import com.google.gson.Gson;

import org.springframework.stereotype.Service;

@Service
public class BaseServicesResource {

    private static final int SECURITY_CODE_LENGTH = 9;

    private static final int LOWER_BOUND = 65;

    private static final int UPPER_BOUND = 91;

    public static final String BEARER = "Bearer ";

    public static final String HEADER = "Authorization";

    public static final Gson gsonMapper = new Gson();

    public static final String TOKEN_SPLITTER = "\\.";

    public static final int TOKEN_PAYLOAD = 1;

    public static final String generateSecurityCode() {
        SecureRandom random = new SecureRandom();
        return random.ints(LOWER_BOUND, UPPER_BOUND)
                .filter(i -> Character.isAlphabetic(i) || Character.isDigit(i))
                .limit(SECURITY_CODE_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}

package com.retrospecsoptometrists.service.authentication.controllers;

import javax.validation.Valid;

import com.retrospecsoptometrists.service.authentication.dtos.CredentialsChangeDto;
import com.retrospecsoptometrists.service.authentication.dtos.SelfUpdateRequestDto;
import com.retrospecsoptometrists.service.authentication.dtos.UserProfileDto;
import com.retrospecsoptometrists.service.authentication.dtos.UserUpdateProfileDto;
import com.retrospecsoptometrists.service.authentication.dtos.ViewRequestDto;
import com.retrospecsoptometrists.service.authentication.security.AuthenticatedUser;
import com.retrospecsoptometrists.service.authentication.services.UserManagementService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
// @CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    // Send emails to the affected user email with callBack URL + Token + security
    // code. Security code to be required as Request Param

    private final UserManagementService userManagementService;

    @GetMapping(path = "/uservalidation")
    public ResponseEntity<?> userValidation(AuthenticatedUser user) {
        userManagementService.proceedOnlyEnabled(user);
        log.info("Successfully validated user: {}", user.getPrincipal());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createUser(AuthenticatedUser user, @Valid @RequestBody UserProfileDto profile) {
        userManagementService.proceedOnlyEnabled(user);
        userManagementService.authoriseCreateUserRequest(user, profile);
        return userManagementService.executeCreateUser(user, profile);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<?> updateUser(AuthenticatedUser user,
            @Valid @RequestBody UserUpdateProfileDto updateProfile) {
        userManagementService.proceedOnlyEnabled(user);
        userManagementService.authoriseUpdateUserRequest(user, updateProfile);
        return userManagementService.executeUpdateUser(user, updateProfile);
    }

    @PostMapping(path = "/view")
    public ResponseEntity<?> viewUsers(AuthenticatedUser user, @RequestBody ViewRequestDto viewRequest) {
        userManagementService.proceedOnlyEnabled(user);
        userManagementService.authoriseViewUserRequest(user, viewRequest);
        return userManagementService.executeViewUserProfiles(user, viewRequest);
    }

    @GetMapping(path = "/self/view")
    public ResponseEntity<?> selfViewProfile(AuthenticatedUser user) {
        userManagementService.proceedOnlyEnabled(user);
        return userManagementService.executeViewSelfProfile(user);
    }

    @PostMapping(path = "/self/update")
    public ResponseEntity<?> selfUpdateProfile(AuthenticatedUser user,
            @Valid @RequestBody SelfUpdateRequestDto updateRequest) {
        userManagementService.proceedOnlyEnabled(user);
        return userManagementService.executeSelfUpdate(user, updateRequest);
    }

    @PostMapping(path = "/self/update/credentials")
    public ResponseEntity<?> updateCredentials(AuthenticatedUser user,
            @Valid @RequestBody CredentialsChangeDto credentials) {
        userManagementService.proceedOnlyEnabled(user);
        return userManagementService.executeUpdateCredentials(user, credentials);
    }
}

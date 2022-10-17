package com.retrospecsoptometrists.service.authentication.services;

import java.time.ZonedDateTime;
import java.util.*;

import com.retrospecsoptometrists.service.authentication.dtos.CredentialsChangeDto;
import com.retrospecsoptometrists.service.authentication.dtos.SelfUpdateRequestDto;
import com.retrospecsoptometrists.service.authentication.dtos.UserProfileDto;
import com.retrospecsoptometrists.service.authentication.dtos.UserUpdateProfileDto;
import com.retrospecsoptometrists.service.authentication.dtos.ViewRequestDto;
import com.retrospecsoptometrists.service.authentication.entities.ApplicationUser;
import com.retrospecsoptometrists.service.authentication.entities.Role;
import com.retrospecsoptometrists.service.authentication.entities.RoleMap;
import com.retrospecsoptometrists.service.authentication.entities.UserProfile;
import com.retrospecsoptometrists.service.authentication.enums.RolesEnum;
import com.retrospecsoptometrists.service.authentication.exceptions.AccessDeniedException;
import com.retrospecsoptometrists.service.authentication.exceptions.ErrorMessage;
import com.retrospecsoptometrists.service.authentication.exceptions.UserDataManipulationException;
import com.retrospecsoptometrists.service.authentication.repositories.ApplicationUserRepository;
import com.retrospecsoptometrists.service.authentication.repositories.UserProfileRepository;
import com.retrospecsoptometrists.service.authentication.security.AuthenticatedUser;
import com.retrospecsoptometrists.service.authentication.utilities.UserManagementUtilities;

import com.generate.avroschema.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.Option;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserManagementService {

        @Autowired
        private Producer producer;

        private final PasswordEncoder passwordEncoder;

        private final UserManagementUtilities userUtilities;

        private final ApplicationUserRepository appuserRepository;

        private final UserProfileRepository userprofileRepository;

        public boolean authoriseCreateUserRequest(AuthenticatedUser user, UserProfileDto profile) {
                if (!userUtilities.validateCreationRights(user.getRoles(), profile.getOrganisationalId(),
                                profile.getBranchId()))
                        throw new AccessDeniedException("Operation cannot be exectuted. Insufficient access rights");

                if (appuserRepository.existsByUserProfile(new UserProfile(profile.getEmailAddress())))
                        throw new UserDataManipulationException("Requested user profile already exists");

                log.info("User {} is authorised to create the new user -> {}, Branch: {}, Organisation ID: {}",
                                user.getPrincipal(), profile.getEmailAddress(), profile.getBranchId(),
                                profile.getOrganisationalId());
                return true;
        }

        public boolean proceedOnlyEnabled(AuthenticatedUser user) {
                if (!user.getEnabled())
                        throw new AccessDeniedException("This profile is disabled. Access cannot be granted");
                return true;
        }

        public UserProfile creatUser(AuthenticatedUser user, UserProfileDto profile){
                Set<RoleMap> startingRole = new HashSet<>();
                startingRole.add(RoleMap.builder()
                        .role(new Role(RolesEnum.GENERAL.name()))
                        .branchId(profile.getBranchId())
                        .organisationalId(profile.getOrganisationalId())
                        .build());

                UserProfile userProfile = UserProfile.builder()
                        .emailAddress(profile.getEmailAddress())
                        .employeeId(profile.getEmailAddress())
                        .firstName(profile.getFirstName())
                        .lastName(profile.getLastName())
                        .phoneNumber(profile.getPhoneNumber())
                        .enabled(profile.getEnabled())
                        .rolemaps(startingRole)
                        .build();
                userProfile = userprofileRepository.save(userProfile);

                ApplicationUser appuser = ApplicationUser.builder()
                        .userProfile(userProfile)
                        .encryptedPass(passwordEncoder.encode(profile.getPassword()))
                        .securityCode(BaseServicesResource.generateSecurityCode())
                        .build();

                appuserRepository.save(appuser);
                log.info("User {} successfully created a new user -> {}, Branch: {}, Organisation ID: {}",
                        user.getPrincipal(),
                        profile.getEmailAddress(), profile.getBranchId(), profile.getOrganisationalId());

                List<com.generate.avroschema.RoleMap> roleMaps = new ArrayList<>();
                roleMaps.add(com.generate.avroschema.RoleMap.newBuilder(  )
                        .setBranchId(Optional.ofNullable( profile.getBranchId()).orElse( "" ))
                        .setOrganisationalId(Optional.ofNullable(profile.getOrganisationalId()).orElse( 0l ))
                        .setRoleId(Optional.ofNullable( RolesEnum.GENERAL.name()).orElse( "" ))
                        .build());
                producer.sendToUserCreateUpdateEventStream( User.newBuilder()
                        .setAction( Optional.ofNullable( "CREATE" ).orElse( "" ) )
                        .setEmailAddress(Optional.ofNullable(userProfile.getEmailAddress()).orElse(""))
                        .setEmployeeId( Optional.ofNullable( userProfile.getEmployeeId()).orElse( "" ))
                        .setEnabled(Optional.ofNullable(userProfile.getEnabled()).orElse(false))
                        .setFirstName(Optional.ofNullable(userProfile.getFirstName()).orElse( "" ))
                        .setLastName(Optional.ofNullable(userProfile.getLastName()).orElse( "" ))
                        .setPhoneNumber(Optional.ofNullable(userProfile.getPhoneNumber()).orElse( ""))
                        .setRole(roleMaps)
                        .build());

                return userProfile;
        }

        public ResponseEntity<?> executeCreateUser(AuthenticatedUser user, UserProfileDto profile) {
                // TODO:- Send (1) email, (2) SMS to the new user with security code and
                // callback URL + token
                UserProfile userProfile = creatUser( user, profile);
                return ResponseEntity.ok().body(userProfile);
        }

        public boolean authoriseUpdateUserRequest(AuthenticatedUser user, UserUpdateProfileDto profile) {
                if (!userUtilities.validateUpdateRights(user.getRoles(), profile.getOrganisationalId(),
                                profile.getBranchId(), RolesEnum.valueOf(profile.getRole())))
                        throw new AccessDeniedException("Operation cannot be exectuted. Insufficient access rights");

                if (!appuserRepository.existsByUserProfile(new UserProfile(profile.getEmailAddress())))
                        throw new UserDataManipulationException("Requested user profile does not exist");

                log.info("User {} is authorised to update the user -> {}, Branch: {}, Organisation ID: {}",
                                user.getPrincipal(), profile.getEmailAddress(), profile.getBranchId(),
                                profile.getOrganisationalId());
                return true;
        }

        public ResponseEntity<?> executeUpdateUser(AuthenticatedUser user, UserUpdateProfileDto profile) {
                Optional<ApplicationUser> appUser = appuserRepository
                                .findByUserProfile(new UserProfile(profile.getEmailAddress()));
                if (!appUser.isPresent())
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

                RoleMap rm = RoleMap.builder()
                                .branchId(profile.getBranchId())
                                .organisationalId(profile.getOrganisationalId())
                                .role(new Role(profile.getRole()))
                                .build();

                UserProfile updateUserProfile = appUser.get().getUserProfile();
                Set<RoleMap> roleMap = updateUserProfile.getRolemaps();
                appropriatelyAddRole(roleMap, rm);

                updateUserProfile.setEmployeeId(profile.getEmployeeId());
                updateUserProfile.setFirstName(profile.getFirstName());
                updateUserProfile.setLastName(profile.getLastName());
                updateUserProfile.setEnabled(profile.getEnabled());
                updateUserProfile.setPhoneNumber(profile.getPhoneNumber());
                updateUserProfile.setRolemaps(roleMap);
                appUser.get().setEncryptedPass(passwordEncoder.encode(profile.getPassword()));
                updateUserProfile = userprofileRepository.save(updateUserProfile);
                appuserRepository.save(appUser.get());
                // TODO:- Send (1) email, (2) SMS to warn the user that changes were done to his
                // (her) profile
                return ResponseEntity.ok().body(updateUserProfile);
        }

        private final void appropriatelyAddRole(Set<RoleMap> roleMapSet, RoleMap rm) {
                for (RoleMap rmap : roleMapSet) {
                        if (rmap.equals(rm))
                                return;
                        else if (rmap.semiEquals(rm)) {
                                rmap.setRole(new Role(rm.getRole().getRoleId()));
                                return;
                        }
                }
                roleMapSet.add(rm);
        }

        public boolean authoriseViewUserRequest(AuthenticatedUser user, ViewRequestDto viewRequest) {
                if (!userUtilities.validateCreationRights(user.getRoles(), viewRequest.getOrganisationalId(),
                                viewRequest.getBranchId()))
                        throw new AccessDeniedException("Not authorised to view requested users");

                return true;
        }

        public ResponseEntity<?> executeViewUserProfiles(AuthenticatedUser user, ViewRequestDto viewRequest) {
                log.info("User {} fetching a view of users based on the request object => {}", user.getPrincipal(),
                                viewRequest);
                List<UserProfile> results = fetchAsSystemAdmin(user, viewRequest);
                if (!Objects.isNull(results) && !results.isEmpty())
                        return ResponseEntity.ok().body(results);

                results = fetchAsOrganisationAdmin(user, viewRequest);
                if (!Objects.isNull(results) && !results.isEmpty())
                        return ResponseEntity.ok().body(results);

                results = fetchAsBranchManager(user, viewRequest);
                if (!Objects.isNull(results) && !results.isEmpty())
                        return ResponseEntity.ok().body(results);

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(ErrorMessage.builder()
                                                .error("The requested profile(s) were not found")
                                                .errorDescription("Data not found")
                                                .errorCode(HttpStatus.NOT_FOUND)
                                                .eventTime(ZonedDateTime.now())
                                                .build());
        }

        private final List<UserProfile> fetchAsSystemAdmin(AuthenticatedUser user, ViewRequestDto viewRequest) {
                if (userUtilities.isSystemAdmin(user)) {
                        if (Objects.isNull(viewRequest.getOrganisationalId())) {
                                return userprofileRepository.findAll();
                        } else if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        Objects.isNull(viewRequest.getBranchId())) {
                                return userprofileRepository
                                                .fetchByOrganisationalId(viewRequest.getOrganisationalId());
                        } else if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        !Objects.isNull(viewRequest.getBranchId()) &&
                                        Objects.isNull(viewRequest.getEmailAddress())) {
                                return userprofileRepository.fetchByOrganisationalIdAndBranchId(
                                                viewRequest.getOrganisationalId(), viewRequest.getBranchId());
                        } else if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        !Objects.isNull(viewRequest.getBranchId()) &&
                                        !Objects.isNull(viewRequest.getEmailAddress())) {
                                return userprofileRepository.fetchByOrganisationalIdAndBranchIdAndEmailAddress(
                                                viewRequest.getOrganisationalId(), viewRequest.getBranchId(),
                                                viewRequest.getEmailAddress());
                        }
                }

                return null;
        }

        private final List<UserProfile> fetchAsOrganisationAdmin(AuthenticatedUser user, ViewRequestDto viewRequest) {
                if (userUtilities.isOrganisationalAdmin(user)) {
                        if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        Objects.isNull(viewRequest.getBranchId())) {
                                return userprofileRepository
                                                .fetchByOrganisationalId(viewRequest.getOrganisationalId());
                        } else if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        !Objects.isNull(viewRequest.getBranchId()) &&
                                        Objects.isNull(viewRequest.getEmailAddress())) {
                                return userprofileRepository.fetchByOrganisationalIdAndBranchId(
                                                viewRequest.getOrganisationalId(), viewRequest.getBranchId());
                        } else if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        !Objects.isNull(viewRequest.getBranchId()) &&
                                        !Objects.isNull(viewRequest.getEmailAddress())) {
                                return userprofileRepository.fetchByOrganisationalIdAndBranchIdAndEmailAddress(
                                                viewRequest.getOrganisationalId(), viewRequest.getBranchId(),
                                                viewRequest.getEmailAddress());
                        }
                }
                return null;
        }

        private final List<UserProfile> fetchAsBranchManager(AuthenticatedUser user, ViewRequestDto viewRequest) {
                if (userUtilities.isBranchManager(user)) {
                        if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        !Objects.isNull(viewRequest.getBranchId()) &&
                                        Objects.isNull(viewRequest.getEmailAddress())) {
                                return userprofileRepository.fetchByOrganisationalIdAndBranchId(
                                                viewRequest.getOrganisationalId(), viewRequest.getBranchId());
                        } else if (!Objects.isNull(viewRequest.getOrganisationalId()) &&
                                        !Objects.isNull(viewRequest.getBranchId()) &&
                                        !Objects.isNull(viewRequest.getEmailAddress())) {
                                return userprofileRepository.fetchByOrganisationalIdAndBranchIdAndEmailAddress(
                                                viewRequest.getOrganisationalId(), viewRequest.getBranchId(),
                                                viewRequest.getEmailAddress());
                        }
                }
                return null;
        }

        public ResponseEntity<?> executeViewSelfProfile(AuthenticatedUser user) {
                log.info("User {} self-requesting the profile", user.getPrincipal());
                return ResponseEntity.ok()
                                .body(userprofileRepository.findByEmailAddress(user.getEmailAddress()));
        }

        public ResponseEntity<?> executeSelfUpdate(AuthenticatedUser user, SelfUpdateRequestDto updateRequest) {
                UserProfile fullprofile = userprofileRepository.findByEmailAddress(user.getEmailAddress());
                if (Objects.isNull(fullprofile))
                        throw new AccessDeniedException(
                                        "Profile mismatch between the logged in user and the profile to be updated");

                log.info("User {} self-updating the profile", user.getPrincipal());
                fullprofile.setEmployeeId(updateRequest.getEmployeeId());
                fullprofile.setFirstName(updateRequest.getFirstName());
                fullprofile.setLastName(updateRequest.getLastName());
                fullprofile.setPhoneNumber(updateRequest.getPhoneNumber());
                fullprofile.setEnabled(updateRequest.getEnabled());
                fullprofile = userprofileRepository.save(fullprofile);
                return ResponseEntity.ok().body(fullprofile);
        }

        public ResponseEntity<?> executeUpdateCredentials(AuthenticatedUser user, CredentialsChangeDto credentials) {
                Optional<ApplicationUser> optionalAppUser = appuserRepository
                                .findByUserProfile(new UserProfile(user.getEmailAddress()));
                ApplicationUser appUser = optionalAppUser.get();
                if (Objects.isNull(appUser)
                                || !passwordEncoder.matches(credentials.getOldPassword(), appUser.getEncryptedPass()))
                        throw new AccessDeniedException("Invalid credentials");

                log.info("User {} updating credentials", user.getPrincipal());
                appUser.setEncryptedPass(passwordEncoder.encode(credentials.getNewPassword()));
                appuserRepository.save(appUser);
                return ResponseEntity.ok().body("Successfully updated the credentials");
        }
}

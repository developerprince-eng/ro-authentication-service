package com.retrospecsoptometrists.service.authentication.services;

import com.retrospecsoptometrists.service.authentication.entities.UserProfile;
import com.retrospecsoptometrists.service.authentication.repositories.UserProfileRepository;
import com.generate.avroschema.RoleMap;
import com.generate.avroschema.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableScheduling
@Service
@Slf4j
@RequiredArgsConstructor
public class Scheduler {

    @Autowired
    private Producer producer;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private static final int EXTRACTION_DELAY = 5 * 60 * 1000; // 2 Minutes


    @Scheduled(fixedDelay = EXTRACTION_DELAY)
    public void updateUsers(){
        log.info("-------------USERS UPDATE INITIATING----------------");
        log.info("==> Started Updating users to user event stream ...");
        List<UserProfile> userProfiles = userProfileRepository.findAll();

        userProfiles.stream().forEach( userProfile -> {
            List<RoleMap> roleMaps = new ArrayList<>();
            userProfile.getRolemaps().forEach( roleMap -> {
                roleMaps.add( RoleMap.newBuilder()
                        .setBranchId(Optional.ofNullable(roleMap.getBranchId()).orElse( "" ))
                        .setOrganisationalId(Optional.ofNullable(roleMap.getOrganisationalId()).orElse( 0L ))
                        .setRoleId(Optional.ofNullable(roleMap.getRole().getRoleId()).orElse( "" ))
                        .build());
            });
            producer.sendToUserCreateUpdateEventStream( User.newBuilder()
                    .setEmployeeId(Optional.ofNullable(userProfile.getEmployeeId()).orElse( ""))
                    .setAction("UPDATE")
                    .setEmailAddress(Optional.ofNullable(userProfile.getEmailAddress()).orElse( "" ))
                    .setEnabled(Optional.ofNullable(userProfile.getEnabled()).orElse(false))
                    .setFirstName(Optional.ofNullable(userProfile.getFirstName()).orElse(""))
                    .setLastName(Optional.ofNullable(userProfile.getLastName()).orElse( "" ))
                    .setPhoneNumber(Optional.ofNullable(userProfile.getPhoneNumber()).orElse(""))
                    .setRole(Optional.ofNullable(roleMaps).orElse(new ArrayList<>()))
                    .build());
        });
    }
}

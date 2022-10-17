package com.retrospecsoptometrists.service.authentication.repositories;

import java.util.Optional;

import com.retrospecsoptometrists.service.authentication.entities.ApplicationUser;
import com.retrospecsoptometrists.service.authentication.entities.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    boolean existsByUserProfile(UserProfile userProfile);

    Optional<ApplicationUser> findByUserProfile(UserProfile userProfile);

}

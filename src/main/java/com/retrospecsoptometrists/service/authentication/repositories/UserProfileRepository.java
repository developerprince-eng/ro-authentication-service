package com.retrospecsoptometrists.service.authentication.repositories;

import java.util.List;

import com.retrospecsoptometrists.service.authentication.entities.UserProfile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    UserProfile findByEmailAddress(String emailAddress);

    @Transactional
    @Query("select prof from UserProfile prof "
            + "left join fetch prof.rolemaps rmap "
            + "where "
            + "   rmap.organisationalId = :organisationalId")
    List<UserProfile> fetchByOrganisationalId(Long organisationalId);

    @Transactional
    @Query("select prof from UserProfile prof "
            + "left join fetch prof.rolemaps rmap "
            + "where "
            + "   rmap.organisationalId = :organisationalId "
            + "and "
            + "   rmap.branchId = :branchId")
    List<UserProfile> fetchByOrganisationalIdAndBranchId(Long organisationalId, String branchId);

    @Transactional
    @Query("select prof from UserProfile prof "
            + "left join fetch prof.rolemaps rmap "
            + "where "
            + "   rmap.organisationalId = :organisationalId "
            + "and "
            + "   rmap.branchId = :branchId "
            + "and "
            + "   prof.emailAddress = :emailAddress")
    List<UserProfile> fetchByOrganisationalIdAndBranchIdAndEmailAddress(Long organisationalId, String branchId,
            String emailAddress);
}

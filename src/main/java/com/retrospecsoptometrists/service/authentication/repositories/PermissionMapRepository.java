package com.retrospecsoptometrists.service.authentication.repositories;

import com.retrospecsoptometrists.service.authentication.entities.PermissionMap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionMapRepository extends JpaRepository<PermissionMap, Long> {

}

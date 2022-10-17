package com.retrospecsoptometrists.service.authentication.repositories;

import com.retrospecsoptometrists.service.authentication.entities.Permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}

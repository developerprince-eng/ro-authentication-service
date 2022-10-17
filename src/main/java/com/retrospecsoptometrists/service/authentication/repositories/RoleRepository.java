package com.retrospecsoptometrists.service.authentication.repositories;

import com.retrospecsoptometrists.service.authentication.entities.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}

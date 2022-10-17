package com.retrospecsoptometrists.service.authentication.repositories;

import com.retrospecsoptometrists.service.authentication.entities.RoleMap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapRepository extends JpaRepository<RoleMap, Long> {

}

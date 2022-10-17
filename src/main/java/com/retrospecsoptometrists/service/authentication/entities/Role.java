package com.retrospecsoptometrists.service.authentication.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 5129047836753L;

    @Id
    @Column(name = "role_id", columnDefinition = "VARCHAR(16)")
    private String roleId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Role(String roleId) {
        this.roleId = roleId;
    }
}

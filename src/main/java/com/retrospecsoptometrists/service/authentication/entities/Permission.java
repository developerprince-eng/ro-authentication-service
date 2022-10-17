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
@Table(name = "permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 4637825861364L;

    @Id
    @Column(name = "permission_id", columnDefinition = "VARCHAR(16)")
    private String permissionId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public Permission(String permissionId) {
        this.permissionId = permissionId;
    }
}

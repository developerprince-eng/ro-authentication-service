package com.retrospecsoptometrists.service.authentication.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "permission_map", uniqueConstraints = @UniqueConstraint(name = "UniqueRolePermissionCombination", columnNames = {
        "role_id", "permission_id" }))
public class PermissionMap implements Serializable {

    private static final long serialVersionUID = 8243278486521L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "permission_map_id", columnDefinition = "BIGSERIAL")
    private Long permissionMapId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "role_id_fk"), referencedColumnName = "role_id")
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(name = "permission_id", foreignKey = @ForeignKey(name = "permission_id_fk"), referencedColumnName = "permission_id")
    private Permission permission;

    public PermissionMap(Long permissionMapId) {
        this.permissionMapId = permissionMapId;
    }
}

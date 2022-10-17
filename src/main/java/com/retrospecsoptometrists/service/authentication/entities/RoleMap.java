package com.retrospecsoptometrists.service.authentication.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "role_map")
public class RoleMap implements Serializable {

    private static final long serialVersionUID = 146327837835L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_map_id", columnDefinition = "BIGSERIAL")
    private Long roleMapId;

    @Column(name = "organisational_id", columnDefinition = "BIGINT")
    private Long organisationalId;

    @Column(name = "branch_id", columnDefinition = "VARCHAR(32)")
    private String branchId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "role_id_fk"), referencedColumnName = "role_id")
    private Role role;

    @JsonIgnore
    @ManyToMany(mappedBy = "rolemaps")
    private Set<UserProfile> userProfiles;

    public RoleMap(Long roleMapId) {
        this.roleMapId = roleMapId;
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj))
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        final RoleMap other = (RoleMap) obj;

        if (Objects.isNull(this.getBranchId()) || Objects.isNull(other.getBranchId()) ||
                Objects.isNull(this.getOrganisationalId()) || Objects.isNull(other.getOrganisationalId()) ||
                Objects.isNull(this.getRole()) || Objects.isNull(other.getRole()))
            return false;

        if (Objects.isNull(this.getRole().getRoleId()) || Objects.isNull(other.getRole().getRoleId()))
            return false;

        if (this.getBranchId().equals(other.getBranchId()) &&
                this.getOrganisationalId().equals(other.getOrganisationalId()) &&
                this.getRole().getRoleId().equals(other.getRole().getRoleId()))
            return true;

        return false;
    }

    public boolean semiEquals(Object obj) {
        if (Objects.isNull(obj))
            return false;

        if (this.getClass() != obj.getClass())
            return false;

        final RoleMap other = (RoleMap) obj;

        if (Objects.isNull(this.getBranchId()) || Objects.isNull(other.getBranchId()) ||
                Objects.isNull(this.getOrganisationalId()) || Objects.isNull(other.getOrganisationalId()) ||
                Objects.isNull(this.getRole()) || Objects.isNull(other.getRole()))
            return false;

        if (Objects.isNull(this.getRole().getRoleId()) || Objects.isNull(other.getRole().getRoleId()))
            return false;

        if (this.getBranchId().equals(other.getBranchId()) &&
                this.getOrganisationalId().equals(other.getOrganisationalId()))
            return true;

        return false;
    }
}

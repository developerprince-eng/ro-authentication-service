package com.retrospecsoptometrists.service.authentication.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
@Table(name = "user_profile")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 2673467354823L;

    @Id
    @Column(name = "email_address", columnDefinition = "VARCHAR(255)")
    private String emailAddress;

    @Column(name = "employee_id", columnDefinition = "VARCHAR(32)")
    private String employeeId;

    @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "VARCHAR(255)")
    private String lastName;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(32)")
    private String phoneNumber;

    @Column(name = "enabled", columnDefinition = "BOOLEAN")
    private Boolean enabled;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "userprofile_rolemap", joinColumns = {
            @JoinColumn(name = "email_address") }, inverseJoinColumns = { @JoinColumn(name = "role_map_id") })
    private Set<RoleMap> rolemaps;

    public UserProfile(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}

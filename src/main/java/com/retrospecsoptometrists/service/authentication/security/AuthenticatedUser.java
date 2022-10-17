package com.retrospecsoptometrists.service.authentication.security;

import java.util.Collection;
import java.util.List;

import com.retrospecsoptometrists.service.authentication.dtos.RoleSummary;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AuthenticatedUser extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 8747348958344L;

    private String employeeId;

    private String emailAddress;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private Boolean enabled;

    private List<RoleSummary> roles;

    public AuthenticatedUser(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return roles;
    }

    @Override
    public Object getPrincipal() {
        return emailAddress;
    }
}

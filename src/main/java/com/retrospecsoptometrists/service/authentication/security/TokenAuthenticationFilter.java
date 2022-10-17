package com.retrospecsoptometrists.service.authentication.security;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.retrospecsoptometrists.service.authentication.dtos.RoleSummary;
import com.retrospecsoptometrists.service.authentication.entities.UserProfile;
import com.retrospecsoptometrists.service.authentication.repositories.UserProfileRepository;
import com.retrospecsoptometrists.service.authentication.services.BaseServicesResource;
import com.retrospecsoptometrists.service.authentication.utilities.JwtManipulationUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtManipulationUtility jwtUtility;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserProfileRepository userprofileRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (!Objects.isNull(token) && jwtUtility.validateJwtToken(token)) {
                String username = jwtUtility.getUserNameFromJwtToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UserProfile profile = userprofileRepository.findByEmailAddress(username);
                if (Objects.isNull(profile))
                    throw new AuthException("Failed to confirm the requesting user");

                List<RoleSummary> roleSummary = profile.getRolemaps().stream()
                        .map(rolemap -> new RoleSummary(rolemap.getRole().getRoleId(),
                                rolemap.getBranchId(),
                                "" + rolemap.getOrganisationalId()))
                        .collect(Collectors.toList());

                AuthenticatedUser authenticatedUser = new AuthenticatedUser(userDetails.getAuthorities());
                authenticatedUser.setAuthenticated(true);
                authenticatedUser.setDetails(profile.getEmailAddress());
                authenticatedUser.setEmailAddress(profile.getEmailAddress());
                authenticatedUser.setEmployeeId(profile.getEmployeeId());
                authenticatedUser.setFirstName(profile.getFirstName());
                authenticatedUser.setLastName(profile.getLastName());
                authenticatedUser.setPhoneNumber(profile.getPhoneNumber());
                authenticatedUser.setEnabled(profile.getEnabled());
                authenticatedUser.setRoles(roleSummary);

                SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            }
        } catch (Exception ex) {
            log.error("Cannot set user authentication: {}", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(BaseServicesResource.HEADER);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BaseServicesResource.BEARER)) {
            return headerAuth.substring(BaseServicesResource.BEARER.length());
        }

        return null;
    }
}

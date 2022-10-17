package com.retrospecsoptometrists.service.authentication.utilities;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.retrospecsoptometrists.service.authentication.dtos.RoleSummary;
import com.retrospecsoptometrists.service.authentication.entities.UserProfile;
import com.retrospecsoptometrists.service.authentication.repositories.UserProfileRepository;
import com.retrospecsoptometrists.service.authentication.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtManipulationUtility {

    @Autowired
    private UserProfileRepository userprofileRepository;

    @Value("${application.security.secret}")
    private String secret;

    @Value("${application.security.expiry}")
    private Integer exipration;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        UserProfile profile = userprofileRepository.findByEmailAddress(userPrincipal.getUsername());
        if (!Objects.isNull(profile) && !Objects.isNull(profile.getRolemaps())) {
            List<RoleSummary> roleSummary = profile.getRolemaps().stream()
                    .map(rolemap -> new RoleSummary(rolemap.getRole().getRoleId(), rolemap.getBranchId(),
                            "" + rolemap.getOrganisationalId()))
                    .collect(Collectors.toList());
            return Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .claim("username", profile.getEmailAddress())
                    .claim("employeeId", profile.getEmployeeId())
                    .claim("firstName", profile.getFirstName())
                    .claim("lastName", profile.getLastName())
                    .claim("phoneNumber", profile.getPhoneNumber())
                    .claim("enabled", profile.getEnabled())
                    .claim("roles", roleSummary)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + exipration))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();

        } else {
            return Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + exipration))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        }
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("JWT token is expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("JWT token is unsupported: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }

        return false;
    }
}

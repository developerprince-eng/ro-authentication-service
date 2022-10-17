package com.retrospecsoptometrists.service.authentication.utilities;

import java.util.List;
import java.util.Objects;

import com.retrospecsoptometrists.service.authentication.dtos.RoleSummary;
import com.retrospecsoptometrists.service.authentication.enums.RolesEnum;
import com.retrospecsoptometrists.service.authentication.exceptions.AccessDeniedException;
import com.retrospecsoptometrists.service.authentication.security.AuthenticatedUser;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserManagementUtilities {

    private static final String INVALID_ROLE_ERROR = "Invalid role specified => {}";

    public boolean validateCreationRights(List<RoleSummary> roles, Long organisationalId, String branchId) {
        try {
            for (RoleSummary role : roles) {
                RolesEnum roleInstance = RolesEnum.valueOf(role.getRoleId());
                switch (roleInstance) {
                    case SYS_ADMIN:
                        return true;

                    case ADMIN:
                        if (!Objects.isNull(role.getOrganisationalId())
                                && role.getOrganisationalId().equals("" + organisationalId))
                            return true;
                        break;

                    case MANAGER:
                        if (!Objects.isNull(role.getOrganisationalId()) &&
                                !Objects.isNull(role.getBranchId()) &&
                                role.getOrganisationalId().equals("" + organisationalId) &&
                                role.getBranchId().equals(branchId))
                            return true;
                        break;

                    case GENERAL:
                        break;

                    default:
                        ;
                }
            }
        } catch (IllegalArgumentException ex) {
            log.error(INVALID_ROLE_ERROR, ex.getLocalizedMessage());
        }
        return false;
    }

    public boolean validateUpdateRights(List<RoleSummary> updaterRoles, Long organisationalId, String branchId,
            RolesEnum newUserRole) {
        try {
            for (RoleSummary updaterRole : updaterRoles) {
                RolesEnum roleInstance = RolesEnum.valueOf(updaterRole.getRoleId());
                switch (roleInstance) {
                    case SYS_ADMIN:
                        return true;

                    case ADMIN:
                        if (!Objects.isNull(updaterRole.getOrganisationalId())
                                && updaterRole.getOrganisationalId().equals("" + organisationalId))
                            return isOrgAdminAuthorised(newUserRole);
                        break;

                    case MANAGER:
                        if (!Objects.isNull(updaterRole.getOrganisationalId()) &&
                                !Objects.isNull(updaterRole.getBranchId()) &&
                                updaterRole.getOrganisationalId().equals("" + organisationalId) &&
                                updaterRole.getBranchId().equals(branchId))
                            return isManagerAuthorised(newUserRole);
                        break;

                    case GENERAL:
                        break;

                    default:
                        ;
                }
            }
        } catch (IllegalArgumentException ex) {
            log.error(INVALID_ROLE_ERROR, ex.getLocalizedMessage());
        }
        throw new AccessDeniedException("The user has no rights to update other user profiles");
    }

    private final boolean isOrgAdminAuthorised(RolesEnum newUserRole) {
        if (!newUserRole.equals(RolesEnum.SYS_ADMIN))
            return true;

        return false;
    }

    private final boolean isManagerAuthorised(RolesEnum newUserRole) {
        if (newUserRole.equals(RolesEnum.MANAGER) || newUserRole.equals(RolesEnum.GENERAL))
            return true;

        return false;
    }

    public boolean isSystemAdmin(AuthenticatedUser user) {
        try {
            for (RoleSummary role : user.getRoles()) {
                RolesEnum roleInstance = RolesEnum.valueOf(role.getRoleId());
                switch (roleInstance) {
                    case SYS_ADMIN:
                        return true;
                    case ADMIN:
                        return false;
                    case MANAGER:
                        return false;
                    case GENERAL:
                        return false;
                    default:
                        ;
                }
            }
        } catch (IllegalArgumentException ex) {
            log.error(INVALID_ROLE_ERROR, ex.getLocalizedMessage());
        }

        return false;
    }

    public boolean isOrganisationalAdmin(AuthenticatedUser user) {
        try {
            for (RoleSummary role : user.getRoles()) {
                RolesEnum roleInstance = RolesEnum.valueOf(role.getRoleId());
                switch (roleInstance) {
                    case SYS_ADMIN:
                        return false;
                    case ADMIN:
                        return true;
                    case MANAGER:
                        return false;
                    case GENERAL:
                        return false;
                    default:
                        ;
                }
            }
        } catch (IllegalArgumentException ex) {
            log.error(INVALID_ROLE_ERROR, ex.getLocalizedMessage());
        }

        return false;
    }

    public boolean isBranchManager(AuthenticatedUser user) {
        try {
            for (RoleSummary role : user.getRoles()) {
                RolesEnum roleInstance = RolesEnum.valueOf(role.getRoleId());
                switch (roleInstance) {
                    case SYS_ADMIN:
                        return false;
                    case ADMIN:
                        return false;
                    case MANAGER:
                        return true;
                    case GENERAL:
                        return false;
                    default:
                        ;
                }
            }
        } catch (IllegalArgumentException ex) {
            log.error(INVALID_ROLE_ERROR, ex.getLocalizedMessage());
        }

        return false;
    }
}

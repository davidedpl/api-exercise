package usersmanagement.domain.security;

/**
 * Defines the permissions validator for this domain.
 */
public interface UserPermissionsValidator {
    void validate(UserPermission action, UserSecurityContext ctx) throws SecurityException;
}

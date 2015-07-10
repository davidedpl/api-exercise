package usersmanagement.domain.security;

public interface SecurityValidator {

    void validate(UserPermission action, UserSecurityContext ctx) throws SecurityException;
}

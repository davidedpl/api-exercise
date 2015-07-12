package usersmanagement.domain.security;

import usersmanagement.domain.UserType;

import java.util.Optional;

/**
 * Contains information about the authenticated user making the current request.
 */
public final class UserAuthenticationAttributes {

    private final String currentUsername;
    private final UserType currentUserType;

    public UserAuthenticationAttributes() {
        this(null, null);
    }

    public UserAuthenticationAttributes(UserType currentUserType) {
        this(null, currentUserType);
    }

    public UserAuthenticationAttributes(String currentUsername, UserType currentUserType) {
        this.currentUsername = currentUsername;
        this.currentUserType = currentUserType;
    }

    public Optional<String> getCurrentUsername() {
        return Optional.ofNullable(currentUsername);
    }
    public Optional<UserType> getCurrentUserType() {
        return Optional.ofNullable(currentUserType);
    }
}

package usersmanagement.domain.security;

import usersmanagement.domain.UserType;

import java.util.Optional;

/**
 * Contains information related to the operating user and the target of current operation.
 */
public final class UserSecurityContext {

    private final UserAuthenticationAttributes authenticationAttributes;
    private final String targetUsername;
    private final UserType targetUserType;

    private UserSecurityContext(UserSecurityContextBuilder builder) {
        this.authenticationAttributes = builder.authenticationAttributes;
        this.targetUsername = builder.targetUsername;
        this.targetUserType = builder.targetUserType;
    }

    public Optional<String> getCurrentUsername() {
        return authenticationAttributes.getCurrentUsername();
    }
    public Optional<UserType> getCurrentUserType() {
        return authenticationAttributes.getCurrentUserType();
    }

    public Optional<String> getTargetUsername() {
        return Optional.ofNullable(targetUsername);
    }

    public Optional<UserType> getTargetUserType() {
        return Optional.ofNullable(targetUserType);
    }

    public static class UserSecurityContextBuilder {
        private String targetUsername;
        private UserType targetUserType;
        private final UserAuthenticationAttributes authenticationAttributes;

        public UserSecurityContextBuilder(UserAuthenticationAttributes authenticationAttributes) {
            if (authenticationAttributes == null) {
                throw new NullPointerException();
            }
            this.authenticationAttributes = authenticationAttributes;
        }

        public UserSecurityContextBuilder withTargetUsername(String targetUsername) {
            this.targetUsername = targetUsername;
            return this;
        }

        public UserSecurityContextBuilder withTargetUserType(UserType targetUserType) {
            this.targetUserType = targetUserType;
            return this;
        }

        public UserSecurityContext build() {
            return new UserSecurityContext(this);
        }
    }
}

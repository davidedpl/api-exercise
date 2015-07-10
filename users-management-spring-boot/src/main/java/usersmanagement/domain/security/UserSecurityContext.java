package usersmanagement.domain.security;

import usersmanagement.domain.UserType;

import java.util.Optional;

/**
 * Contains information related to the operating user and the target of current operation.
 */
public final class UserSecurityContext {

    private final String currentUsername;
    private final String targetUsername;
    private final UserType targetUserType;

    private UserSecurityContext(UserSecurityContextBuilder builder) {
        this.currentUsername = builder.currentUsername;
        this.targetUsername = builder.targetUsername;
        this.targetUserType = builder.targetUserType;
    }

    public Optional<String> getCurrentUsername() {
        return Optional.ofNullable(currentUsername);
    }

    public Optional<String> getTargetUsername() {
        return Optional.ofNullable(targetUsername);
    }

    public Optional<UserType> getTargetUserType() {
        return Optional.ofNullable(targetUserType);
    }

    public static class UserSecurityContextBuilder {
        private String currentUsername;
        private String targetUsername;
        private UserType targetUserType;

        public UserSecurityContextBuilder withCurrentUserName(String currentUsername) {
            this.currentUsername = currentUsername;
            return this;
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

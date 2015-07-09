package usersmanagement.domain.security;

import usersmanagement.domain.UserType;

import java.util.Optional;

public final class UserSecurityContext {

    private final String username;
    private final String targetUsername;
    private final UserType targetUserType;

    private UserSecurityContext(UserSecurityContextBuilder builder) {
        this.username = builder.username;
        this.targetUsername = builder.targetUsername;
        this.targetUserType = builder.targetUserType;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getTargetUsername() {
        return Optional.ofNullable(targetUsername);
    }

    public Optional<UserType> getTargetUserType() {
        return Optional.ofNullable(targetUserType);
    }

    public static class UserSecurityContextBuilder {
        private String username;
        private String targetUsername;
        private UserType targetUserType;


        public UserSecurityContextBuilder withUserName(String username) {
            this.username = username;
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

package usersmanagement.domain;

import usersmanagement.domain.security.PermissionHolder;
import usersmanagement.domain.security.UserSecurityContext;

/**
 * Describes the different types of users.
 */
public enum UserType {

    Subscriber((action, ctx) -> {
        switch (action) {
            case CREATE:
            case READ:
                return ctx.getUsername().map(name -> name.equals(ctx.getTargetUsername().orElse(null)))
                        .orElse(false);
            default:
                return false;
        }
    }),
    Administrator(new PermissionHolder() {
        @Override
        public boolean hasPermission(UserAction action, UserSecurityContext ctx) {
            switch (action) {
                case CREATE:
                case READ:
                case UPDATE:
                    return ctx.getTargetUserType().map(type -> type.equals(Subscriber)).orElse(false);
                default:
                    return false;
            }
        }
    }),
    SuperUser((action, ctx) -> {
        switch (action) {
            case CREATE:
            case READ:
            case UPDATE:
            case DELETE:
                return true;
            default:
                return false;
        }
    });

    private final PermissionHolder permissionHolder;

    UserType(PermissionHolder permissionHolder) {
        this.permissionHolder = permissionHolder;
    }

    public PermissionHolder getPermissions() {
        return permissionHolder;
    }

}

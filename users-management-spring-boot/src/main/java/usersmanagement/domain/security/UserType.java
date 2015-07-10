package usersmanagement.domain.security;

/**
 * Describes the different types of users and define their permissions.
 */
public enum UserType implements SecurityValidator {

    Subscriber((action, ctx) -> {
        switch (action) {
            case CREATE:
            case READ:
                return ctx.getCurrentUsername().map(name -> name.equals(ctx.getTargetUsername().orElse(null)))
                        .orElse(false);
            default:
                return false;
        }
    }),
    Administrator((action, ctx) -> {
        switch (action) {
            case CREATE:
            case READ:
            case UPDATE:
                return ctx.getTargetUserType().map(type -> type == Subscriber || type == null).orElse(false);
            default:
                return false;
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

    public void validate(UserPermission action, UserSecurityContext ctx) {
        if (!permissionHolder.hasPermission(action, ctx)) {
            throw new SecurityException("Operation not permitted");
        }
    }
}

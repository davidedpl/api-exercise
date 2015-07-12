package usersmanagement.domain.security;

import org.springframework.stereotype.Component;
import usersmanagement.domain.UserType;

/**
 * Authorizes users' operations with a logic based on user-types.
 */
@Component
public class TypeBasedUserPermissionValidator implements UserPermissionsValidator {
    @Override
    public void validate(UserPermission action, UserSecurityContext ctx) throws SecurityException {
        if (!hasPermission(
                ctx.getCurrentUserType().orElseThrow(SecurityException::new), action, ctx)) {
            throw new SecurityException("Operation not permitted");
        }
    }

    private boolean hasPermission(UserType type, UserPermission action, UserSecurityContext ctx) {
        switch (type) {
            case Subscriber:
                switch (action) {
                    case CREATE:
                    case READ:
                        return ctx.getCurrentUsername()
                                .map(name -> name.equals(ctx.getTargetUsername().orElse(null)))
                                .orElse(false);
                    default:
                        return false;
                }
            case Administrator:
                switch (action) {
                    case CREATE:
                    case READ:
                    case UPDATE:
                        return ctx.getTargetUserType().map(t -> t == UserType.Subscriber).orElse(false);
                    default:
                        return false;
                }
            case SuperUser:
                switch (action) {
                    case CREATE:
                    case READ:
                    case UPDATE:
                    case DELETE:
                        return ctx.getTargetUserType().map(t -> t != UserType.SuperUser).orElse(false);
                    default:
                        return false;
                }
            default:
                throw new SecurityException("Unsupported user-type");
        }
    }

}

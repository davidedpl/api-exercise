package usersmanagement.domain.security;

import usersmanagement.domain.UserAction;

public interface PermissionHolder {
    public boolean hasPermission(UserAction action, UserSecurityContext ctx);
}

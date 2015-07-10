package usersmanagement.domain.security;

/**
 * Describe a security element containing the permissions for a specific role
 */
interface PermissionHolder {
    public boolean hasPermission(UserPermission action, UserSecurityContext ctx);
}

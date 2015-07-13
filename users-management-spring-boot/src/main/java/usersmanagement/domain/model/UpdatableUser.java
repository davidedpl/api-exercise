package usersmanagement.domain.model;

/**
 * Describes a user that can be updated.
 */
public interface UpdatableUser extends User {
    /**
     * Create a new user that has the same attributes of the current one, updated
     * with the content provided by the helper.
     * @param helper provides the updated information
     * @return a new user with the updated which is the result of the updated infromation and
     * the unchanged ones
     */
    User update(UserUpdateHelper helper);
}

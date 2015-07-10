package usersmanagement.domain.exceptions;

/**
 * Exception used when trying to perform an action on user that doesn't exist.
 */
public class UserNotFoundException extends UserException {
    public UserNotFoundException(String username) {
        super("User " + username + " not found");
    }
}

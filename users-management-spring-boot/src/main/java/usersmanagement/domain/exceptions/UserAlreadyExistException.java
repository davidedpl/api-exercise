package usersmanagement.domain.exceptions;

/**
 * Exception used when trying to create a user that already exists.
 */
public class UserAlreadyExistException extends UserException {
    public UserAlreadyExistException(String username) {
        super(username);
    }
}

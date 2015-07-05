package usersmanagement.domain.exceptions;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(String username) {
        super(username);
    }
}

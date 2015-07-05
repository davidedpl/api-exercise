package usersmanagement.domain.exceptions;

public class UserAlreadyExistException extends UserException {
    public UserAlreadyExistException(String username) {
        super(username);
    }
}

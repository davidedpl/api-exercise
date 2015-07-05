package usersmanagement.domain.exceptions;

/**
 * Basic Exception to use as super class for User related exceptions that require common features.
 */
public abstract class UserException extends RuntimeException {

    private final String username;

    public UserException(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

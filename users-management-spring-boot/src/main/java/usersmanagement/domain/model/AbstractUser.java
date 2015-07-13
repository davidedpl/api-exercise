package usersmanagement.domain.model;

import org.apache.commons.validator.routines.EmailValidator;

import javax.validation.ValidationException;
import java.time.LocalDate;

/**
 * Support implementation for a generic user.
 */
abstract class AbstractUser implements UpdatableUser {

    private static final String USERNAME_VALIDATION_REGEX = "^[a-zA-Z0-9]+$";

    private final String title;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String email;
    private final String password;
    private final String username;

    AbstractUser(String lastName, String firstName, String password, String email, String title,
                        String username, LocalDate dateOfBirth) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ValidationException("Email: " + email);
        }
        this.title = title;
        this.username = username;
        if (!username.matches(USERNAME_VALIDATION_REGEX)) {
            throw new ValidationException("Username: " + username);
        }
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public abstract UserType getType();

    @Override
    public final String getTitle() {
        return title;
    }

    @Override
    public final String getFirstName() {
        return firstName;
    }

    @Override
    public final String getLastName() {
        return lastName;
    }

    @Override
    public final LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public final String getEmail() {
        return email;
    }

    @Override
    public final String getPassword() {
        return password;
    }

    @Override
    public final String getUsername() {
        return username;
    }

}

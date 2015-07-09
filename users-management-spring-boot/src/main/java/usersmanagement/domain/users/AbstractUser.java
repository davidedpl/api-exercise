package usersmanagement.domain.users;

import usersmanagement.domain.User;
import usersmanagement.domain.UserType;

import java.time.LocalDate;

/**
 * Support implementation for a generic user.
 */
abstract class AbstractUser implements User {

    private final String title;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final String email; // TODO validate email
    private final char[] password; // TODO hash in SHA1
    private final String username; // TODO explain -- add validation (letters and numbers only)

    AbstractUser(String lastName, String firstName, char[] password, String email, String title,
                        String username, LocalDate dateOfBirth) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.title = title;
        this.username = username;
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
    public final char[] getPassword() {
        return password;
    }

    @Override
    public final String getUsername() {
        return username;
    }
}

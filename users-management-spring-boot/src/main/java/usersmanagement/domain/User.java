package usersmanagement.domain;

import java.time.LocalDate;

/**
 * Describes a generic type of user.
 */
public interface User {

    UserType getType();

    String getTitle();

    String getFirstName();

    String getLastName();

    LocalDate getDateOfBirth();

    String getEmail();

    char[] getPassword();

    String getUsername();
}

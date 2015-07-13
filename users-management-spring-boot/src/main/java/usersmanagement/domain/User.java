package usersmanagement.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;

/**
 * Describes a generic type of user.
 */
public interface User {

    UserType getType();

    String getTitle();

    String getFirstName();

    String getLastName();

    @JsonSerialize(using = ToStringSerializer.class)
    LocalDate getDateOfBirth();

    String getEmail();

    char[] getPassword();

    String getUsername();

}

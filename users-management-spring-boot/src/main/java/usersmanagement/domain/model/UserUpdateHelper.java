package usersmanagement.domain.model;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Commodity class that contains all the optional information that can be used to update a User.
 */
public class UserUpdateHelper {

    private final Optional<String> title;
    private final Optional<String> firstName;
    private final Optional<String> lastName;
    private final Optional<LocalDate> dateOfBirth;
    private final Optional<String> email;
    private final Optional<String> password;
    private final Optional<Address> homeAddress;
    private final Optional<Address> billingAddress;

    public static UserUpdateHelper emptyHelper() {
        return new UserUpdateHelper(Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }

    public static UserUpdateHelper fromUser(User user) {
        return new UserUpdateHelper(
                Optional.ofNullable(user.getTitle()),
                Optional.ofNullable(user.getFirstName()),
                Optional.ofNullable(user.getLastName()),
                Optional.ofNullable(user.getDateOfBirth()),
                Optional.ofNullable(user.getEmail()),
                Optional.ofNullable(user.getPassword()),
                Optional.ofNullable(user instanceof Addressable ? ((Addressable) user).getHomeAddress() : null),
                Optional.ofNullable(user instanceof Addressable ? ((Addressable) user).getBillingAddress() : null));
    }

    public UserUpdateHelper(Optional<String> title,
                            Optional<String> firstName,
                            Optional<String> lastName,
                            Optional<LocalDate> dateOfBirth,
                            Optional<String> email,
                            Optional<String> password,
                            Optional<Address> homeAddress,
                            Optional<Address> billingAddress) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.homeAddress = homeAddress;
        this.billingAddress = billingAddress;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<LocalDate> getDateOfBirth() {
        return dateOfBirth;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getPassword() {
        return password;
    }

    public Optional<Address> getHomeAddress() {
        return homeAddress;
    }

    public Optional<Address> getBillingAddress() {
        return billingAddress;
    }
}

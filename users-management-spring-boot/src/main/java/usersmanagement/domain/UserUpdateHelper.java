package usersmanagement.domain;

import usersmanagement.domain.users.Users;

import java.time.LocalDate;
import java.util.Optional;

public class UserUpdateHelper {

    private final Optional<String> title;
    private final Optional<String> firstName;
    private final Optional<String> lastName;
    private final Optional<LocalDate> dateOfBirth;
    private final Optional<String> email;
    private final Optional<char[]> password;

    private final Optional<Address> homeAddress;
    private final Optional<Address> billingAddress;

    public UserUpdateHelper(Optional<String> title,
                            Optional<String> firstName,
                            Optional<String> lastName,
                            Optional<LocalDate> dateOfBirth,
                            Optional<String> email,
                            Optional<char[]> password,
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

    public User updateUser(User originalUser) {
        switch (originalUser.getType()) {
            case Subscriber:
                return Users.getSubscriber(
                        title.orElse(originalUser.getTitle()),
                        lastName.orElse(originalUser.getFirstName()),
                        firstName.orElse(originalUser.getLastName()),
                        dateOfBirth.orElse(originalUser.getDateOfBirth()),
                        email.orElse(originalUser.getEmail()),
                        password.orElse(originalUser.getPassword()),
                        originalUser.getUsername(),
                        homeAddress.orElse(((Addressable)originalUser).getHomeAddress()),
                        billingAddress.orElse(((Addressable)originalUser).getBillingAddress())
                );
            case Administrator:
                return Users.getAdmin(
                        title.orElse(originalUser.getTitle()),
                        lastName.orElse(originalUser.getFirstName()),
                        firstName.orElse(originalUser.getLastName()),
                        dateOfBirth.orElse(originalUser.getDateOfBirth()),
                        email.orElse(originalUser.getEmail()),
                        password.orElse(originalUser.getPassword()),
                        originalUser.getUsername()
                );
            default:
                throw new UnsupportedOperationException("Unable to update user of type: " + originalUser.getType());
        }
    }

}

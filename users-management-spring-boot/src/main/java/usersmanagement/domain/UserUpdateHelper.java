package usersmanagement.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator()
    public UserUpdateHelper(
            @JsonProperty(value = "title", required = false) String title,
            @JsonProperty(value = "firstName", required = false) String firstName,
            @JsonProperty(value = "lastName", required = false) String lastName,
            @JsonProperty(value = "dateOfBirth", required = false) String dateOfBirth,
            @JsonProperty(value = "email", required = false) String email,
            @JsonProperty(value = "password", required = false) String password,
            @JsonProperty(value = "homeAddress", required = false) AddressHelper homeAddress,
            @JsonProperty(value = "billingAddress", required = false) AddressHelper billingAddress) {
        this.title = Optional.ofNullable(title);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.dateOfBirth = Optional.ofNullable(dateOfBirth).map(LocalDate::parse);
        this.email = Optional.ofNullable(email);
        this.password = Optional.ofNullable(password).map(String::toCharArray);
        this.homeAddress = Optional.ofNullable(homeAddress).map(AddressHelper::toAddress);
        this.billingAddress = Optional.ofNullable(billingAddress).map(AddressHelper::toAddress);
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

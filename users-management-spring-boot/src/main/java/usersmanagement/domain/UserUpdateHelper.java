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
        return new UserUpdateHelper(user.getTitle(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getPassword(),
                user instanceof Addressable ? ((Addressable) user).getHomeAddress() : null,
                user instanceof Addressable ? ((Addressable) user).getBillingAddress() : null);
    }

    public static UserUpdateHelper fromAddressableUser(AddressableUser user) {
        return new UserUpdateHelper(user.getTitle(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getPassword(),
                user.getHomeAddress(),
                user.getBillingAddress());
    }

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

    public UserUpdateHelper(String title,
                            String firstName,
                            String lastName,
                            LocalDate dateOfBirth,
                            String email,
                            char[] password,
                            Address homeAddress,
                            Address billingAddress) {
        this.title = Optional.ofNullable(title);
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.dateOfBirth = Optional.ofNullable(dateOfBirth);
        this.email = Optional.ofNullable(email);
        this.password = Optional.ofNullable(password);
        this.homeAddress = Optional.ofNullable(homeAddress);
        this.billingAddress = Optional.ofNullable(billingAddress);
    }

    public User updateUser(User originalUser) {
        switch (originalUser.getType()) {
            case Subscriber:
                return Users.getSubscriber(
                        title.orElse(originalUser.getTitle()),
                        lastName.orElse(originalUser.getLastName()),
                        firstName.orElse(originalUser.getFirstName()),
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

    public Optional<char[]> getPassword() {
        return password;
    }

    public Optional<Address> getHomeAddress() {
        return homeAddress;
    }

    public Optional<Address> getBillingAddress() {
        return billingAddress;
    }
}

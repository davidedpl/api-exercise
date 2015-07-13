package usersmanagement.domain.model;

import java.time.LocalDate;

/**
 * Provides static factories for different types of users.
 */
public class Users {

    private Users() { }

    public static AddressableUser getSubscriber(String title, String lastName, String firstName, LocalDate dateOfBirth,
                                                String email, String password, String username,
                                                Address homeAddress, Address billingAddress) {
        return new AbstractAddressableUser(lastName, firstName, password, email,
                title, username, dateOfBirth, homeAddress, billingAddress) {
            @Override
            public User update(UserUpdateHelper helper) {
                return Users.getSubscriber(
                        helper.getTitle().orElse(getTitle()),
                        helper.getLastName().orElse(getLastName()),
                        helper.getFirstName().orElse(getFirstName()),
                        helper.getDateOfBirth().orElse(getDateOfBirth()),
                        helper.getEmail().orElse(getEmail()),
                        helper.getPassword().orElse(getPassword()),
                        getUsername(),
                        helper.getHomeAddress().orElse(getHomeAddress()),
                        helper.getBillingAddress().orElse(getBillingAddress())
                );
            }

            @Override
            public UserType getType() {
                return UserType.Subscriber;
            }
        };
    }

    public static User getAdmin(String title, String lastName, String firstName, LocalDate dateOfBirth,
                                String email, String password, String username) {
        return new AbstractUser(lastName, firstName, password, email, title, username, dateOfBirth) {
            @Override
            public User update(UserUpdateHelper helper) {
                return Users.getAdmin(
                        helper.getTitle().orElse(getTitle()),
                        helper.getLastName().orElse(getFirstName()),
                        helper.getFirstName().orElse(getLastName()),
                        helper.getDateOfBirth().orElse(getDateOfBirth()),
                        helper.getEmail().orElse(getEmail()),
                        helper.getPassword().orElse(getPassword()),
                        getUsername()
                );
            }

            @Override
            public UserType getType() {
                return UserType.Administrator;
            }
        };
    }

}

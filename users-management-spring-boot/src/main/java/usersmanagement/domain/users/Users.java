package usersmanagement.domain.users;

import usersmanagement.domain.Address;
import usersmanagement.domain.AddressableUser;
import usersmanagement.domain.User;
import usersmanagement.domain.UserType;

import java.time.LocalDate;

/**
 * Provides static factories for different types of users.
 */
public class Users {

    // we don't want to instantiate this class
    private Users() {
        throw new AssertionError();
    }

    public static AddressableUser getSubscriber(String title, String lastName, String firstName, LocalDate dateOfBirth,
                                                String email, char[] password, String username,
                                                Address homeAddress, Address billingAddress) {
        return new AbstractAddressableUser(lastName, firstName, password, email,
                title, username, dateOfBirth, homeAddress, billingAddress) {
            @Override
            public UserType getType() {
                return UserType.Subscriber;
            }
        };
    }

    public static User getAdmin(String title, String lastName, String firstName, LocalDate dateOfBirth,
                                String email, char[] password, String username) {
        return new AbstractUser(lastName, firstName, password, email, title, username, dateOfBirth) {
            @Override
            public UserType getType() {
                return UserType.Administrator;
            }
        };
    }

//    public static User getSuperUser(String title, String lastName, String firstName, LocalDate dateOfBirth,
//                                    String email, char[] password, String username) {
//        return new AbstractUser(lastName, firstName, password, email, title, username, dateOfBirth) {
//            @Override
//            public UserType getType() {
//                return UserType.SuperUser;
//            }
//        };
//    }
}
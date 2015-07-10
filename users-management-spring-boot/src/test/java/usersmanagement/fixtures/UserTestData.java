package usersmanagement.fixtures;

import usersmanagement.domain.Address;
import usersmanagement.domain.User;
import usersmanagement.domain.users.Users;

import java.time.LocalDate;

public class UserTestData {

    public static User subscriberUser1() {
        return Users.getSubscriber(
                "Mr",
                "John",
                "Doe",
                LocalDate.now(),
                "john@doe.co.uk",
                new char[]{'p', 'w', 'd'},
                "johndoe",
                address1(),
                address2()
        );
    }

    public static User subscriberUser2() {
        return Users.getSubscriber(
                "Ms",
                "Jane",
                "Doe",
                LocalDate.now(),
                "jane@doe.co.uk",
                new char[]{'p', 'w', 'd'},
                "janedoe",
                address1(),
                address2()
        );
    }

    public static User adminUser() {
        return Users.getAdmin(
                "Lord",
                "Admin",
                "Istrator",
                LocalDate.now(),
                "admin@users.co.uk",
                new char[]{'a', 'd', 'm', 'i', 'n'},
                "admin"
        );
    }

    public static Address address1() {
        return new Address.AddressBuilder("1 High Road", "GB").withAddressLine2("London").build();
    }

    public static Address address2() {
        return new Address.AddressBuilder("2 Main Street", "GB").withAddressLine2("London").build();
    }
}

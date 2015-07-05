package usersmanagement.fixtures;

import usersmanagement.domain.Address;
import usersmanagement.domain.User;
import usersmanagement.domain.UserType;

import java.time.LocalDate;

public class UserTestData {

    public static User subscriberUser1() {
        return new User(
                UserType.Subscriber,
                "Mr",
                "John",
                "Doe",
                LocalDate.now(),
                "john@doe.co.uk",
                new char[] {'p','w','d'},
                "johndoe",
                address1(),
                address2()
        );
    }

    public static User subscriberUser2() {
        return new User(
                UserType.Subscriber,
                "Ms",
                "Jane",
                "Doe",
                LocalDate.now(),
                "jane@doe.co.uk",
                new char[] {'p','w','d'},
                "janedoe",
                address1(),
                address2()
        );
    }

    public static Address address1() {
        return new Address.AddressBuilder("1 High Road", "GB").withAddressLine2("London").build();
    }

    public static Address address2() {
        return new Address.AddressBuilder("2 Main Street", "GB").withAddressLine2("London").build();
    }
}

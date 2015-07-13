package usersmanagement.fixtures;

import usersmanagement.domain.Address;
import usersmanagement.domain.AddressableUser;
import usersmanagement.domain.User;
import usersmanagement.domain.user.Users;

import java.time.LocalDate;

public class UserTestData {

    public static AddressableUser subscriberUser1() {
        return Users.getSubscriber(
                "Mr",
                "Doe",
                "John",
                LocalDate.parse("2015-01-05"),
                "john@doe.co.uk",
                "pwd",
                "johndoe",
                addressFull(),
                addressBasic()
        );
    }

    public static AddressableUser subscriberUser2() {
        return Users.getSubscriber(
                "Ms",
                "Clark",
                "Jane",
                LocalDate.parse("2015-08-07"),
                "jane@clark.co.uk",
                "123",
                "janeclark",
                addressBasic(),
                addressFull()
        );
    }

    public static User adminUser() {
        return Users.getAdmin(
                "Lord",
                "Istrator",
                "Admin",
                LocalDate.parse("2018-08-08"),
                "admin@users.co.uk",
                "admin",
                "admin"
        );
    }

    public static User adminUser2() {
        return Users.getAdmin(
                "Sir",
                "Green",
                "Ross",
                LocalDate.parse("1980-08-08"),
                "admin2@users.co.uk",
                "admin2",
                "admin2"
        );
    }

    public static Address addressFull() {
        return new Address.AddressBuilder("1 High Road", "GB").withAddressLine2("London").withPostCode("SW1 2RF").build();
    }

    public static Address addressBasic() {
        return new Address.AddressBuilder("2 Main Street", "GB").build();
    }

}

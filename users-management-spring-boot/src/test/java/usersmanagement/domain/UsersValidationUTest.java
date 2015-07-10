package usersmanagement.domain;

import org.junit.Test;
import usersmanagement.domain.utils.Users;
import usersmanagement.fixtures.UserTestData;

import javax.validation.ValidationException;
import java.time.LocalDate;

public class UsersValidationUTest {

    private final String invalidEmail = "john.doe.co.uk";

    @Test(expected = ValidationException.class)
    public void userWithInvalidEmail() {
        Users.getSubscriber(
                "Mr",
                "John",
                "Doe",
                LocalDate.parse("2015-01-05"),
                invalidEmail,
                new char[]{'p', 'w', 'd'},
                "johndoe",
                UserTestData.addressFull(),
                UserTestData.addressBasic()
        );
    }
}

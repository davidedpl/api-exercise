package usersmanagement.domain;

import org.junit.Test;
import usersmanagement.domain.user.Users;
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
                "pwd",
                "johndoe",
                UserTestData.addressFull(),
                UserTestData.addressBasic()
        );
    }
}

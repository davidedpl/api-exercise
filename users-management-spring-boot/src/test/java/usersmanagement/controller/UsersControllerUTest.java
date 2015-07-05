package usersmanagement.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.fixtures.UserTestData;

@RunWith(MockitoJUnitRunner.class)
public class UsersControllerUTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UsersController userController;

    User testUser = UserTestData.subscriberUser1();
    User otherUser = UserTestData.subscriberUser2();

    @Test
    public void subscriberCanRetrieveItself() {
        userController.getSingleUser(testUser.getType(), testUser.getUsername(), testUser.getUsername());
    }

    @Test
    public void subscriberCanNotRetrieveAnotherUser() {
        try {
            userController.getSingleUser(testUser.getType(), testUser.getUsername(), otherUser.getUsername());
            Assert.fail("SecurityException expected");
        } catch (SecurityException e) {
            // test success
        }
    }

    @Test
    public void subscriberCanCreateItself() {
        userController.register(testUser.getType(), testUser.getUsername(), testUser);
    }

    @Test
    public void subscriberCanNotRegisterAnotherUser() {
        try {
            userController.register(testUser.getType(), testUser.getUsername(), otherUser);
            Assert.fail("SecurityException expected");
        } catch (SecurityException e) {
            // test success
        }
    }

    // TODO test calls to repository
}

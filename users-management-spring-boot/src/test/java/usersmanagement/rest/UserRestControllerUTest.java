package usersmanagement.rest;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.fixtures.UserTestData;
import usersmanagement.rest.v1.UserRestController;

@RunWith(MockitoJUnitRunner.class)
public class UserRestControllerUTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserRestController userController;

    User testUser = UserTestData.subscriberUser1();
    User otherUser = UserTestData.subscriberUser2();

//    @Test
//    public void subscriberCanRetrieveItself() {
//        userController.readUser(testUser.getCurrentUsername(), testUser.getType(), testUser.getCurrentUsername());
//        verify(userRepository).retrieve(testUser.getCurrentUsername());
//        verifyNoMoreInteractions(userRepository);
//    }
//
//    @Test
//    public void subscriberCanNotRetrieveAnotherUser() {
//        try {
//            userController.readUser(testUser.getCurrentUsername(), testUser.getType(), otherUser.getCurrentUsername());
//            Assert.fail("SecurityException expected");
//        } catch (SecurityException e) {
//            verifyZeroInteractions(userRepository);
//        }
//    }

//    @Test
//    public void subscriberCanCreateItself() {
//        userController.register(testUser.getType(), testUser.getCurrentUsername(), testUser);
//        verify(userRepository).create(testUser);
//        verifyNoMoreInteractions(userRepository);
//    }
//
//    @Test
//    public void subscriberCanNotRegisterAnotherUser() {
//        try {
//            userController.register(testUser.getType(), testUser.getCurrentUsername(), otherUser);
//            Assert.fail("SecurityException expected");
//        } catch (SecurityException e) {
//            verifyZeroInteractions(userRepository);
//        }
//    }

}

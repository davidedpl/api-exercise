package usersmanagement.repository;

import org.junit.Assert;
import org.junit.Test;
import usersmanagement.domain.exceptions.UserNotFoundException;

import static org.junit.Assert.assertEquals;

public class UserRepositoryInMemory_DeleteUTest extends UserRepositoryInMemoryUTestCommon {

    @Test
    public void deleteExistingUser_UserNoMorePresent() {
        userRepository.delete(createdUser.getUsername());
        assertUserNotPresent(createdUser.getUsername());
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteNotExistingUser_UserNotFoundException() {
        userRepository.delete(notExistingUser.getUsername());
    }

    @Test
    public void deleteNotExistingUser_UserNotFoundExceptionWithUsername() {
        try {
            userRepository.delete(notExistingUser.getUsername());
            Assert.fail("UserNotFoundException was expected");
        } catch (UserNotFoundException e) {
            assertEquals(notExistingUser.getUsername(), e.getUsername());
        }
    }

}

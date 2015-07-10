package usersmanagement.infrastructure;

import org.junit.Assert;
import org.junit.Test;
import usersmanagement.domain.AddressableUser;
import usersmanagement.domain.exceptions.UserAlreadyExistException;

import static org.junit.Assert.assertEquals;

public class UserRepositoryInMemory_CreateUTest extends UserRepositoryInMemoryUTestCommon {

    @Test
    public void createUserThatNotExists_Success() {
        // only assess creation is executed without errors
        userRepository.create(notExistingUser);
    }

    @Test(expected = UserAlreadyExistException.class)
    public void createExistingUser_UserAlreadyExistsException() {
        userRepository.create(createdUser);
    }

    @Test
    public void createExistingUser_UserAlreadyExistsExceptionWithUsername() {
        AddressableUser createdUser = createSubscriber1();
        try {
            userRepository.create(createdUser);
            Assert.fail("UserAlreadyExistException was expected");
        } catch (UserAlreadyExistException e) {
            assertEquals(createdUser.getUsername(), e.getUsername());
        }
    }

}

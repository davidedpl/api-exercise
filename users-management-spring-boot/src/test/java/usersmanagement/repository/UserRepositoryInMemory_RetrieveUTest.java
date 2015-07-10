package usersmanagement.repository;

import org.junit.Test;
import usersmanagement.domain.User;

public class UserRepositoryInMemory_RetrieveUTest extends UserRepositoryInMemoryUTestCommon {

    @Test
    public void retrieveExistingUser_Success() {
        User retrievedUser = userRepository.retrieve(createdUser.getUsername()).get();
        assertSameUser(createdUser, retrievedUser);
    }

    @Test
    public void retrieveNotExistingUser_UserNotFoundException() {
        assertUserNotPresent(notExistingUser.getUsername());
    }

    // TODO retrive in range

}

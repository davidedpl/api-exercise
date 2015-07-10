package usersmanagement.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import usersmanagement.domain.User;
import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.fixtures.UserTestData;

import static org.junit.Assert.assertEquals;

public class UserRepositoryInMemoryUTest {

    private UserRepositoryInMemory userRepository;

    @Before
    public void init() {
        userRepository = new UserRepositoryInMemory();
    }

    @Test
    public void createUserThatNotExists_Success() {
        User userToCreate = UserTestData.subscriberUser1();
        userRepository.create(userToCreate);
        User retrievedUser = userRepository.retrieve(userToCreate.getUsername()).get();
        assertEquals(userToCreate.getUsername(), retrievedUser.getUsername());
        // TODO complete
    }

    @Test
    public void createExistingUser_UserAlreadyExistsException() {
        User userToCreate = UserTestData.subscriberUser1();
        userRepository.create(userToCreate);
        try {
            userRepository.create(userToCreate);
            Assert.fail("UserAlreadyExistException was expected");
        } catch (UserAlreadyExistException e) {
            assertEquals(userToCreate.getUsername(), e.getUsername());
        }
    }

    @Test
    public void retrieveExistingUser_Success() {
        User userToRetrieve = UserTestData.subscriberUser1();
        userRepository.create(userToRetrieve);
        User retrievedUser = userRepository.retrieve(userToRetrieve.getUsername()).get();
        assertEquals(userToRetrieve.getUsername(), retrievedUser.getUsername());
        // TODO complete
    }

    @Test
    public void retrieveNotExistingUser_UserNotFoundException() {
        User userToRetrieve = UserTestData.subscriberUser1();
        try {
            userRepository.retrieve(userToRetrieve.getUsername());
            Assert.fail("UserNotFoundException was expected");
        } catch (UserNotFoundException e) {
            assertEquals(userToRetrieve.getUsername(), e.getUsername());
        }
    }

    @Test
    public void deleteExistingUser_Success() {
        User userToDelete = UserTestData.subscriberUser1();
        userRepository.create(userToDelete);
        userRepository.delete(userToDelete.getUsername());
        try {
            userRepository.retrieve(userToDelete.getUsername());
            Assert.fail("UserNotFoundException was expected");
        } catch (UserNotFoundException e) {
            assertEquals(userToDelete.getUsername(), e.getUsername());
        }
    }

    @Test
    public void deleteNotExistingUser_UserNotFoundException() {
        User userToDelete = UserTestData.subscriberUser1();
        try {
            userRepository.delete(userToDelete.getUsername());
            Assert.fail("UserNotFoundException was expected");
        } catch (UserNotFoundException e) {
            assertEquals(userToDelete.getUsername(), e.getUsername());
        }
    }

    // TODO test update

//    @Test
//    public void updateSubscriber() {
//        String updatedName = "AAA";
//        User originalUser = UserTestData.subscriberUser1();
//        userRepository.create(originalUser);
//        UserUpdateHelper helper = new UserUpdateHelper(
//                null, updatedName, null, null, null, null, null, null
//        );
//        userRepository.update(originalUser.getCurrentUsername(), helper);
//        User updatedUser = userRepository.retrieve(originalUser.getCurrentUsername()).get();
//        assertEquals(updatedName, updatedUser.getFirstName());
//    }

    // TOOD test retrieve range

}

package usersmanagement.repository;

import org.junit.Assert;
import org.junit.Test;
import usersmanagement.domain.AddressableUser;
import usersmanagement.domain.User;
import usersmanagement.domain.UserUpdateHelper;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.fixtures.UserTestData;

import static org.junit.Assert.assertEquals;

public class UserRepositoryInMemory_UpdateUTest extends UserRepositoryInMemoryUTestCommon {

    @Test
    public void updateUser_Success() {
        UserUpdateHelper updateHelper = UserUpdateHelper.fromAddressableUser(notExistingUser);
        userRepository.update(createdUser.getUsername(), updateHelper);
        assertUpdateWithAddress(createdUser, updateHelper);
    }

    @Test
    public void updateAdmin_Success() {
        UserUpdateHelper updateHelper = UserUpdateHelper.fromUser(UserTestData.adminUser2());
        userRepository.update(createdAdmin.getUsername(), updateHelper);
        User updatedUser = userRepository.retrieve(createdAdmin.getUsername()).get();
        assertUpdate(updatedUser, createdAdmin, updateHelper);
    }

    @Test
    public void updateEmptyDoesntChangeAnything() {
        UserUpdateHelper updateHelper = UserUpdateHelper.emptyHelper();
        userRepository.update(createdUser.getUsername(), updateHelper);
        assertUpdateWithAddress(createdUser, updateHelper);
    }

    @Test(expected = UserNotFoundException.class)
    public void updateNotExistingUser_UserNotFoundException() {
        userRepository.update(notExistingUser.getUsername(), UserUpdateHelper.emptyHelper());
    }

    @Test
    public void updateNotExistingUser_UserNotFoundExceptionWithUsername() {
        try {
            userRepository.update(notExistingUser.getUsername(), UserUpdateHelper.emptyHelper());
            Assert.fail("UserNotFoundException was expected");
        } catch (UserNotFoundException e) {
            assertEquals(notExistingUser.getUsername(), e.getUsername());
        }
    }

    private void assertUpdateWithAddress(AddressableUser originalUser, UserUpdateHelper updateHelper) {
        AddressableUser updatedUser = (AddressableUser) userRepository.retrieve(createdUser.getUsername()).get();
        assertUpdate(updatedUser, originalUser, updateHelper);
        assertEquals(
                updateHelper.getHomeAddress().orElse(originalUser.getHomeAddress()),
                updatedUser.getHomeAddress());
        assertEquals(
                updateHelper.getBillingAddress().orElse(originalUser.getBillingAddress()),
                updatedUser.getBillingAddress());
    }

    private void assertUpdate(User updatedUser, User originalUser, UserUpdateHelper updateHelper) {
        assertEquals(
                updateHelper.getTitle().orElse(originalUser.getTitle()), updatedUser.getTitle());
        assertEquals(
                updateHelper.getFirstName().orElse(originalUser.getFirstName()), updatedUser.getFirstName());
        assertEquals(
                updateHelper.getLastName().orElse(originalUser.getLastName()), updatedUser.getLastName());
        assertEquals(
                updateHelper.getDateOfBirth().orElse(originalUser.getDateOfBirth()),
                updatedUser.getDateOfBirth());
        assertEquals(
                updateHelper.getEmail().orElse(originalUser.getEmail()), updatedUser.getEmail());
        assertEquals(
                updateHelper.getPassword().orElse(originalUser.getPassword()), updatedUser.getPassword());

        // unchanged attributes
        assertEquals(originalUser.getUsername(), updatedUser.getUsername());
        assertEquals(originalUser.getType(), updatedUser.getType());
    }

}

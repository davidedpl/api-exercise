package usersmanagement.infrastructure;

import org.junit.Before;
import org.junit.Test;
import usersmanagement.domain.model.Addressable;
import usersmanagement.domain.model.AddressableUser;
import usersmanagement.domain.model.User;
import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.model.UserUpdateHelper;
import usersmanagement.fixtures.UserTestData;
import usersmanagement.infrastructure.repository.UserRepositoryInMemory;

import static org.junit.Assert.assertEquals;

public class UserRepositoryInMemoryUTest {

    protected UserRepositoryInMemory userRepository;
    protected static final AddressableUser notExistingUser = UserTestData.subscriberUser2();
    protected AddressableUser createdUser;
    protected User createdAdmin;

    @Before
    public void init() {
        userRepository = new UserRepositoryInMemory();
        createdUser = createSubscriber1();
        createdAdmin = createAdmin();
    }

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
    public void deleteExistingUser_UserNoMorePresent() {
        userRepository.delete(createdUser.getUsername());
        assertUserNotPresent(createdUser.getUsername());
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteNotExistingUser_UserNotFoundException() {
        userRepository.delete(notExistingUser.getUsername());
    }

    @Test
    public void retrieveExistingUser_Success() {
        User retrievedUser = userRepository.retrieve(createdUser.getUsername()).get();
        assertSameUser(createdUser, retrievedUser);
    }

    @Test
    public void retrieveNotExistingUser_UserNotFoundException() {
        assertUserNotPresent(notExistingUser.getUsername());
    }

    @Test
    public void retrieveAllWithResults() {
        assertEquals(2, userRepository.retrieveAll().size());
    }

    @Test
    public void retrieveAllWithoutResults() {
        initWithEmptyRepository();
        assertEquals(0, userRepository.retrieveAll().size());
    }

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

    private void initWithEmptyRepository() {
        userRepository = new UserRepositoryInMemory();
    }

    protected AddressableUser createSubscriber1() {
        AddressableUser user = UserTestData.subscriberUser1();
        userRepository.create(user);
        return user;
    }

    protected User createAdmin() {
        User user = UserTestData.adminUser();
        userRepository.create(user);
        return user;
    }

    protected void assertSameUser(User expected, User actual) {
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        if (expected instanceof Addressable) {
            assertEquals(((Addressable)expected).getHomeAddress(), ((Addressable) actual).getHomeAddress());
            assertEquals(((Addressable)expected).getBillingAddress(), ((Addressable) actual).getBillingAddress());
        }
    }

    protected void assertUserNotPresent(String username) {
        assertEquals(false, userRepository.retrieve(username).isPresent());
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

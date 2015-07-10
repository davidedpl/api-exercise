package usersmanagement.repository;

import org.junit.Before;
import usersmanagement.domain.Addressable;
import usersmanagement.domain.AddressableUser;
import usersmanagement.domain.User;
import usersmanagement.fixtures.UserTestData;

import static org.junit.Assert.assertEquals;

public abstract class UserRepositoryInMemoryUTestCommon {

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
}

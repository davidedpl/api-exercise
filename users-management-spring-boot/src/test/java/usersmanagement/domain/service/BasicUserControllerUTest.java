package usersmanagement.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.security.UserPermissionsValidator;
import usersmanagement.domain.utils.UserUpdateHelper;
import usersmanagement.fixtures.UserTestData;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BasicUserControllerUTest {

    @Mock private UserRepository userRepository;
    private BasicUserController userService;

    private static final UserAuthenticationAttributes EMPTY_AUTH_ATTRIBUTES = new UserAuthenticationAttributes();
    private static final String ANY_USER_NAME = "test";
    private static final User USER_THAT_EXISTS = UserTestData.subscriberUser1();
//    private static final User USER_THAT_NOT_EXISTS = UserTestData.subscriberUser1();
    private static final UserUpdateHelper ANY_USER_UPDATE_HELPER = UserUpdateHelper.emptyHelper();

    // read user tests

    @Test
    public void read_UserExists_Authorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationSuccess());
        User retrievedUser = userService.readUser(EMPTY_AUTH_ATTRIBUTES, USER_THAT_EXISTS.getUsername());
        assertEquals(USER_THAT_EXISTS, retrievedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void read_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.readUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME);
    }

    @Test(expected = SecurityException.class)
    public void read_UserExists_NotAuthorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationFails());
        userService.readUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME);
        // TODO use catchexception and verify interactions
    }

    @Test(expected = SecurityException.class)
    public void read_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        userService.readUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME);
        // TODO use catchexception and verify interactions
    }


    // register user tests

    @Test(expected = UserAlreadyExistException.class)
    public void register_UserExists_Authorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationSuccess());
        userService.registerUser(EMPTY_AUTH_ATTRIBUTES, USER_THAT_EXISTS);
    }

    @Test
    public void register_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.registerUser(EMPTY_AUTH_ATTRIBUTES, USER_THAT_EXISTS);
    }

    @Test(expected = SecurityException.class)
    public void register_UserExists_NotAuthorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationFails());
        userService.registerUser(EMPTY_AUTH_ATTRIBUTES, USER_THAT_EXISTS);
    }

    @Test(expected = SecurityException.class)
    public void register_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        userService.registerUser(EMPTY_AUTH_ATTRIBUTES, USER_THAT_EXISTS);
    }


    // update user tests

    @Test
    public void update_UserExists_Authorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationSuccess());
        userService.updateUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME, ANY_USER_UPDATE_HELPER);
    }

    @Test(expected = UserNotFoundException.class)
    public void update_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.updateUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME, ANY_USER_UPDATE_HELPER);
    }

    @Test(expected = SecurityException.class)
    public void update_UserExists_NotAuthorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationFails());
        userService.updateUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME, ANY_USER_UPDATE_HELPER);
    }

    @Test(expected = SecurityException.class)
    public void update_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        userService.updateUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME, ANY_USER_UPDATE_HELPER);
    }



    // delete user tests

    @Test
    public void delete_UserExists_Authorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationSuccess());
        userService.deleteUser(EMPTY_AUTH_ATTRIBUTES, USER_THAT_EXISTS.getUsername());
        verify(userRepository, only()).delete(USER_THAT_EXISTS.getUsername());
    }

    @Test(expected = UserNotFoundException.class)
    public void delete_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.deleteUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME);
    }

    @Test(expected = SecurityException.class)
    public void delete_UserExists_NotAuthorized() {
        given(userExists(USER_THAT_EXISTS), permissionValidationFails());
        userService.deleteUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME);
    }

    @Test(expected = SecurityException.class)
    public void delete_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        userService.deleteUser(EMPTY_AUTH_ATTRIBUTES, ANY_USER_NAME);
    }



    private void given(UserRepository userRepository, UserPermissionsValidator userPermissionsValidator) {
        userService = new BasicUserController(userRepository, userPermissionsValidator);
    }

    private UserRepository userExists(User user) {
        doThrow(new UserAlreadyExistException(user.getUsername())).when(userRepository).create(user);
        when(userRepository.retrieve(anyString())).thenReturn(Optional.of(user));
        when(userRepository.retrieveRange(anyInt(), anyInt())).thenReturn(Collections.singletonList(user));
        doNothing().when(userRepository).update(eq(user.getUsername()), any(UserUpdateHelper.class));
        doNothing().when(userRepository).delete(user.getUsername());
        return userRepository;
    }

    private UserRepository userDoesntExist() {
        return new UserRepository() {
            @Override
            public void create(User user) {
                // operation executed without errors
            }
            @Override
            public Optional<User> retrieve(String username) {
                return Optional.empty();
            }
            @Override
            public List<User> retrieveRange(int offset, int limit) {
                return Collections.emptyList();
            }
            @Override
            public void update(String username, UserUpdateHelper updateHelper) {
                throw new UserNotFoundException(username);
            }
            @Override
            public void delete(String username) {
                throw new UserNotFoundException(username);
            }
        };
//                when(userRepository.create(user)).thenThrow(new UserAlreadyExistException(user.getUsername()));
//        when(userRepository.retrieve(anyString())).thenReturn(Optional.empty());
//        when(userRepository.retrieveRange(anyInt(), anyInt())).thenReturn(Collections.EMPTY_LIST);
//        when(userRepository.update(user.getUsername(), any(UserUpdateHelper.class)));
//        return userRepository;
    }

    private UserPermissionsValidator permissionValidationSuccess() {
        return (action, ctx) -> {
            // do nothing to confirm validation
        };
    }

    private UserPermissionsValidator permissionValidationFails() {
        return (action, ctx) -> {
            throw new SecurityException();
        };
    }
}

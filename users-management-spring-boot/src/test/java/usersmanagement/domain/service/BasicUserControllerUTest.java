package usersmanagement.domain.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BasicUserControllerUTest {

    private UserRepository userRepository;
    private BasicUserController userService;

    private static final UserAuthenticationAttributes SOME_AUTH_ATTRIBUTES = new UserAuthenticationAttributes();
    private static final User USER_THAT_EXISTS = UserTestData.subscriberUser1();
    private static final String SOME_USER_NAME = "someusername";
    private static final UserUpdateHelper SOME_USER_UPDATE_HELPER = UserUpdateHelper.emptyHelper();

    // read user tests

    @Test
    public void read_UserExists_Authorized() {
        given(userExists(), permissionValidationSuccess());
        User retrievedUser = userService.readUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME);
        assertEquals(USER_THAT_EXISTS, retrievedUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void read_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.readUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME);
    }

    @Test(expected = SecurityException.class)
    public void read_UserExists_NotAuthorized() {
        given(userExists(), permissionValidationFails());
        userService.readUser(SOME_AUTH_ATTRIBUTES, USER_THAT_EXISTS.getUsername());
    }

    @Test(expected = SecurityException.class)
    public void read_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        userService.readUser(SOME_AUTH_ATTRIBUTES, USER_THAT_EXISTS.getUsername());
    }


    // register user tests

    @Test(expected = UserAlreadyExistException.class)
    public void register_UserExists_Authorized() {
        given(userExists(), permissionValidationSuccess());
        userService.registerUser(SOME_AUTH_ATTRIBUTES, USER_THAT_EXISTS);
    }

    @Test
    public void register_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.registerUser(SOME_AUTH_ATTRIBUTES, USER_THAT_EXISTS);
        verify(userRepository).create(USER_THAT_EXISTS);
    }

    @Test
    public void register_UserExists_NotAuthorized() {
        given(userExists(), permissionValidationFails());
        expectedSecurityExceptionOnCreation();
    }

    @Test
    public void register_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        expectedSecurityExceptionOnCreation();
    }


    // update user tests

    @Test
    public void update_UserExists_Authorized() {
        given(userExists(), permissionValidationSuccess());
        userService.updateUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME, SOME_USER_UPDATE_HELPER);
        verify(userRepository).update(SOME_USER_NAME, SOME_USER_UPDATE_HELPER);
    }

    @Test(expected = UserNotFoundException.class)
    public void update_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.updateUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME, SOME_USER_UPDATE_HELPER);
    }

    @Test
    public void update_UserExists_NotAuthorized() {
        given(userExists(), permissionValidationFails());
        expectedSecurityExceptionOnUpdate();
    }

    @Test
    public void update_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        expectedSecurityExceptionOnUpdate();
    }


    // delete user tests

    @Test
    public void delete_UserExists_Authorized() {
        given(userExists(), permissionValidationSuccess());
        userService.deleteUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME);
        verify(userRepository).delete(SOME_USER_NAME);
    }

    @Test(expected = UserNotFoundException.class)
    public void delete_UserNotExists_Authorized() {
        given(userDoesntExist(), permissionValidationSuccess());
        userService.deleteUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME);
    }

    @Test
    public void delete_UserExists_NotAuthorized() {
        given(userExists(), permissionValidationFails());
        expectedSecurityExceptionOnDelete();
    }

    @Test
    public void delete_UserNotExists_NotAuthorized() {
        given(userDoesntExist(), permissionValidationFails());
        expectedSecurityExceptionOnDelete();
    }


    private void given(Supplier<UserRepository> userRepositorySupplier,
                       UserPermissionsValidator userPermissionsValidator) {
        userRepository = userRepositorySupplier.get();
        userService = new BasicUserController(userRepository, userPermissionsValidator);
    }

    private Supplier<UserRepository> userExists() {
        return () -> {
            UserRepository userRepository = Mockito.mock(UserRepository.class);
            doThrow(new UserAlreadyExistException(SOME_USER_NAME)).when(userRepository).create(USER_THAT_EXISTS);
            when(userRepository.retrieve(anyString())).thenReturn(Optional.of(USER_THAT_EXISTS));
            when(userRepository.retrieveRange(anyInt(), anyInt())).thenReturn(Collections.singletonList(USER_THAT_EXISTS));
            doNothing().when(userRepository).update(anyString(), any(UserUpdateHelper.class));
            doNothing().when(userRepository).delete(anyString());
            return userRepository;
        };
    }

    private Supplier<UserRepository> userDoesntExist() {
        return () -> {
            UserRepository userRepository = Mockito.mock(UserRepository.class);
            doNothing().when(userRepository).create(any(User.class));
            when(userRepository.retrieve(anyString())).thenReturn(Optional.empty());
            when(userRepository.retrieveRange(anyInt(), anyInt())).thenReturn(Collections.EMPTY_LIST);
            doThrow(new UserNotFoundException(SOME_USER_NAME)).when(userRepository)
                    .update(anyString(), any(UserUpdateHelper.class));
            doThrow(new UserNotFoundException(SOME_USER_NAME)).when(userRepository).delete(anyString());
            return userRepository;
        };
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

    private void expectedSecurityExceptionOnCreation() {
        try {
            userService.registerUser(SOME_AUTH_ATTRIBUTES, USER_THAT_EXISTS);
            fail("Expected SecurityException");
        } catch (SecurityException e) {
            verify(userRepository, never()).create(any(User.class));
        }
    }

    private void expectedSecurityExceptionOnUpdate() {
        try {
            userService.updateUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME, SOME_USER_UPDATE_HELPER);
            fail("Expected SecurityException");
        } catch (SecurityException e) {
            verify(userRepository, never()).update(anyString(), any(UserUpdateHelper.class));
        }
    }
    private void expectedSecurityExceptionOnDelete() {
        try {
            userService.deleteUser(SOME_AUTH_ATTRIBUTES, SOME_USER_NAME);
            fail("Expected SecurityException");
        } catch (SecurityException e) {
            verify(userRepository, never()).delete(anyString());
        }
    }
}
package usersmanagement.domain.controller;

import org.springframework.stereotype.Service;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.security.UserPermission;
import usersmanagement.domain.security.UserPermissionsValidator;
import usersmanagement.domain.security.UserSecurityContext.UserSecurityContextBuilder;
import usersmanagement.domain.user.UserUpdateHelper;

import javax.inject.Inject;
import java.util.*;

/**
 * This implementation validate user's permission and, if successfully authorized, continue
 * executing the operation on the Repository.
 */
@Service
class BasicUserController implements UserController {

    private final UserRepository userRepository;
    private final UserPermissionsValidator userPermissionsValidator;

    @Inject
    public BasicUserController(UserRepository userRepository, UserPermissionsValidator userPermissionsValidator) {
        this.userRepository = userRepository;
        this.userPermissionsValidator = userPermissionsValidator;
    }

    @Override
    public Collection<User> readAll(UserAuthenticationAttributes authenticationAttributes) {
        Collection<User> result = new ArrayList<>();
        Collection<User> users = userRepository.retrieveAll();
        if (users.size() > 0) {
            UserSecurityContextBuilder ctxBuilder = new UserSecurityContextBuilder(authenticationAttributes);
            for (User user : users) {
                ctxBuilder.withTargetUsername(user.getUsername());
                ctxBuilder.withTargetUserType(user.getType());
                try {
                    userPermissionsValidator.validate(UserPermission.READ, ctxBuilder.build());
                    result.add(user);
                } catch (SecurityException e) {
                    // exception ignored and used to skip filter only
                }
            }
        }
        return result;
    }

    @Override
    public User readUser(UserAuthenticationAttributes authenticationAttributes, String username) {
        Optional<User> user = userRepository.retrieve(username);
        userPermissionsValidator.validate(UserPermission.READ,
                new UserSecurityContextBuilder(authenticationAttributes)
                        .withTargetUsername(username)
                        .withTargetUserType(user.map(User::getType).orElse(null)).build());
        return user.orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public void registerUser(UserAuthenticationAttributes authenticationAttributes, User userToRegister) {
        userPermissionsValidator.validate(UserPermission.CREATE,
                new UserSecurityContextBuilder(authenticationAttributes)
                        .withTargetUsername(userToRegister.getUsername())
                        .withTargetUserType(userToRegister.getType()).build());
        userRepository.create(userToRegister);
    }

    @Override
    public void updateUser(UserAuthenticationAttributes authenticationAttributes,
                           String username, UserUpdateHelper updateHelper) {
        Optional<User> originalUser = userRepository.retrieve(username);
        userPermissionsValidator.validate(UserPermission.CREATE,
                new UserSecurityContextBuilder(authenticationAttributes)
                        .withTargetUserType(originalUser.map(u -> u.getType()).orElse(null)).build());
        userRepository.update(username, updateHelper);
    }

    @Override
    public void deleteUser(UserAuthenticationAttributes authenticationAttributes, String username) {
        userPermissionsValidator.validate(UserPermission.DELETE,
                new UserSecurityContextBuilder(authenticationAttributes).build());
        userRepository.delete(username);
    }

}

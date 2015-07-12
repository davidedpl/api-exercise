package usersmanagement.domain.service;

import org.springframework.stereotype.Service;
import usersmanagement.domain.security.UserPermissionsValidator;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.utils.UserUpdateHelper;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.security.UserPermission;
import usersmanagement.domain.security.UserSecurityContext;

import javax.inject.Inject;
import java.util.Optional;

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
    public User readUser(UserAuthenticationAttributes authenticationAttributes, String username) {
        Optional<User> user = userRepository.retrieve(username);
        userPermissionsValidator.validate(UserPermission.READ,
                new UserSecurityContext.UserSecurityContextBuilder(authenticationAttributes)
                        .withTargetUsername(username)
                        .withTargetUserType(user.map(User::getType).orElse(null)).build());
        return user.orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public void registerUser(UserAuthenticationAttributes authenticationAttributes, User userToRegister) {
        userPermissionsValidator.validate(UserPermission.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder(authenticationAttributes)
                        .withTargetUsername(userToRegister.getUsername())
                        .withTargetUserType(userToRegister.getType()).build());
        userRepository.create(userToRegister);
    }

    @Override
    public void updateUser(UserAuthenticationAttributes authenticationAttributes,
                           String username, UserUpdateHelper updateHelper) {
        Optional<User> originalUser = userRepository.retrieve(username);
        userPermissionsValidator.validate(UserPermission.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder(authenticationAttributes)
                        .withTargetUserType(originalUser.map(u -> u.getType()).orElse(null)).build());
        userRepository.update(username, updateHelper);
    }

    @Override
    public void deleteUser(UserAuthenticationAttributes authenticationAttributes, String username) {
        userPermissionsValidator.validate(UserPermission.DELETE,
                new UserSecurityContext.UserSecurityContextBuilder(authenticationAttributes).build());
        userRepository.delete(username);
    }

}

package usersmanagement.domain;

import org.springframework.stereotype.Service;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.SecurityValidator;
import usersmanagement.domain.security.UserPermission;
import usersmanagement.domain.security.UserSecurityContext;
import usersmanagement.domain.security.UserType;
import usersmanagement.domain.utils.UserUpdateHelper;

import javax.inject.Inject;
import java.util.Optional;

@Service
class BasicUserController implements UserController {

    private final UserRepository userRepository;

    @Inject
    public BasicUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User readUser(String clientUserName, UserType clientUserRole, String username) {
        Optional<User> user = userRepository.retrieve(username);
        getSecurityValidator(clientUserRole).validate(UserPermission.READ,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withCurrentUserName(clientUserName)
                        .withTargetUsername(username)
                        .withTargetUserType(user.map(User::getType).orElse(null)).build());
        return user.orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public void registerUser(String clientUserName, UserType clientUserRole, User userToRegister) {
        getSecurityValidator(clientUserRole).validate(UserPermission.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withCurrentUserName(clientUserName)
                        .withTargetUsername(userToRegister.getUsername())
                        .withTargetUserType(userToRegister.getType()).build());
        userRepository.create(userToRegister);
    }

    @Override
    public void updateUser(UserType clientUserRole, String username, UserUpdateHelper updateHelper) {
        Optional<User> originalUser = userRepository.retrieve(username);
        getSecurityValidator(clientUserRole).validate(UserPermission.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withTargetUserType(originalUser.map(u -> u.getType()).orElse(null)).build());
        userRepository.update(username, updateHelper);
    }

    @Override
    public void deleteUser(UserType clientUserRole, String username) {
        getSecurityValidator(clientUserRole).validate(UserPermission.DELETE,
                new UserSecurityContext.UserSecurityContextBuilder().build());
        userRepository.delete(username);
    }

    private static SecurityValidator getSecurityValidator(UserType type) {
        if (type == null) {
            throw new SecurityException("UserType is null");
        }
        return type;
    }

}

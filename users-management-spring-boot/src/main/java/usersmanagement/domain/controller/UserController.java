package usersmanagement.domain.controller;

import usersmanagement.domain.User;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.utils.UserUpdateHelper;

import java.util.List;

/**
 * Defines operations that can be executed on a User.
 * Implementations of this class will provide the appropriate business-logic for each operation and
 * will take care of security authorizations.
 */
public interface UserController {

    List<User> readAll(UserAuthenticationAttributes authenticationAttributes);

    User readUser(UserAuthenticationAttributes authenticationAttributes, String username);

    void registerUser(UserAuthenticationAttributes authenticationAttributes, User userToRegister);

    void updateUser(UserAuthenticationAttributes authenticationAttributes, String username,
                    UserUpdateHelper updateHelper);

    void deleteUser(UserAuthenticationAttributes authenticationAttributes, String username);
}

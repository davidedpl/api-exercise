package usersmanagement.domain.controller;

import usersmanagement.domain.model.User;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.model.UserUpdateHelper;

import java.util.Collection;

/**
 * Defines operations that can be executed on a User.
 * Implementations of this class will provide the appropriate business-logic for each operation and
 * will take care of security authorizations.
 */
public interface UserController {

    Collection<User> readAll(UserAuthenticationAttributes authenticationAttributes);

    User readUser(UserAuthenticationAttributes authenticationAttributes, String username);

    void registerUser(UserAuthenticationAttributes authenticationAttributes, User userToRegister);

    void updateUser(UserAuthenticationAttributes authenticationAttributes, String username,
                    UserUpdateHelper updateHelper);

    void deleteUser(UserAuthenticationAttributes authenticationAttributes, String username);
}

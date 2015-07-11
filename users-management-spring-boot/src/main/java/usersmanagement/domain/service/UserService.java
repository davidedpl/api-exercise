package usersmanagement.domain.service;

import usersmanagement.domain.User;
import usersmanagement.domain.security.UserType;
import usersmanagement.domain.utils.UserUpdateHelper;

public interface UserService {
    User readUser(String clientUserName, UserType clientUserRole, String username);

    void registerUser(String clientUserName, UserType clientUserRole, User userToRegister);

    void updateUser(UserType clientUserRole, String username, UserUpdateHelper updateHelper);

    void deleteUser(UserType clientUserRole, String username);
}

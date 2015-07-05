package usersmanagement.controller;

import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.UserType;

import javax.inject.Inject;

public class UsersController {

    private final UserRepository userRepository;

    @Inject
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserType requiringUserType, String requiringUsername, User user) {
        // TODO validate the user
        securityCheck(requiringUserType, requiringUsername, user.getUsername(), "register");
        userRepository.create(user);
    }

    public User getSingleUser(UserType requiringUserType, String requiringUsername, String username) {
        securityCheck(requiringUserType, requiringUsername, username, "retrieve");
        return userRepository.retrieve(username);
    }

    private void securityCheck(UserType requiringUserType, String requiringUsername, String username, String action) {
        if (requiringUserType == UserType.Subscriber) {
            if (!requiringUsername.equals(username)) {
                throw new SecurityException("Subscriber " + requiringUsername + " tried to " + action
                        + " user " + username);
            }
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

}

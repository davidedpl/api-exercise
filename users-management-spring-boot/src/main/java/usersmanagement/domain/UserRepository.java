package usersmanagement.domain;

import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Map;

public interface UserRepository {

    /**
     * Add the provided user to the repository.
     * @param user
     * @throws UserAlreadyExistException if a user with the same username has already been registered
     */
    void create(User user);

    /**
     * Retrieve the user with the provided username.
     * @param username
     * @throws UserNotFoundException if the user with the given username doesn't exist
     */
    User retrieve(String username);

    /**
     * Retrieve all the users in the specified range.
     * @param offset first user in the range (starts from 0)
     * @param limit max users in the range
     * @return
     */
    List<User> retrieveRange(int offset, int limit);

    /**
     * Update the user with the provided username.
     * @param username
     * @throws UserNotFoundException if the user with the given username doesn't exist
     */
    void update(String username, Map<String, Object> props);

    /**
     * Remove the user with the provided username from the repository.
     * @param username
     * @throws UserNotFoundException if the user with the given username doesn't exist
     */
    void delete(String username);

}
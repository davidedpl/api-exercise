package usersmanagement.domain.repository;

import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.model.User;
import usersmanagement.domain.model.UserUpdateHelper;

import java.util.Collection;
import java.util.Optional;

/**
 * Describe a generic repository for users.
 */
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
     */
    Optional<User> retrieve(String username);

    /**
     * Retrieve all the users in the database.
     */
    Collection<User> retrieveAll();

    /**
     * Update the user with the provided username.
     * @param username
     * @param updateHelper contains the updated values
     * @throws UserNotFoundException if the user with the given username doesn't exist
     */
    void update(String username, UserUpdateHelper updateHelper);

    /**
     * Remove the user with the provided username from the repository.
     * @param username
     * @throws UserNotFoundException if the user with the given username doesn't exist
     */
    void delete(String username);

}

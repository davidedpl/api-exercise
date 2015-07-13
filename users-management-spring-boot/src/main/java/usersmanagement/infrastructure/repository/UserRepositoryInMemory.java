package usersmanagement.infrastructure.repository;

import org.springframework.stereotype.Repository;
import usersmanagement.domain.model.UpdatableUser;
import usersmanagement.domain.model.User;
import usersmanagement.domain.repository.UserRepository;
import usersmanagement.domain.model.UserUpdateHelper;
import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

@Repository
public class UserRepositoryInMemory implements UserRepository {

    private final NavigableMap<String, User> users = new ConcurrentSkipListMap<String, User>();

    @Override
    public void create(User user) {
        if (users.putIfAbsent(user.getUsername(), user) != null) {
            throw new UserAlreadyExistException(user.getUsername());
        }
    }

    @Override
    public Optional<User> retrieve(String username) {
        return Optional.ofNullable(readUser(username));
    }

    @Override
    public Collection<User> retrieveAll() {
        return users.values();
    }

    @Override
    public void update(String username, UserUpdateHelper updateHelper) {
        User originalUser = readUser(username);
        if (originalUser == null) {
            throw new UserNotFoundException(username);
        }
        if (!(originalUser instanceof UpdatableUser)) {
            throw new UnsupportedOperationException("User " + username + " doesn't support the update");
        }
        User updatedUser = ((UpdatableUser) originalUser).update(updateHelper);
        users.put(username, updatedUser);
    }

    @Override
    public void delete(String username) {
        if (users.remove(username) == null) {
            throw new UserNotFoundException(username);
        }
    }

    private User readUser(String username) {
        return users.get(username);
    }

}

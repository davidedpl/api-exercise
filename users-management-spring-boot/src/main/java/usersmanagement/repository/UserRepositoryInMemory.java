package usersmanagement.repository;

import org.springframework.stereotype.Repository;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.UserUpdateHelper;
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
    public List<User> retrieveRange(int offset, int limit) {
        List<User> result = new ArrayList<>();
        Iterator<User> it = users.values().iterator();
        for (int i = 0; i < offset + limit && it.hasNext(); i++) {
            User user = it.next();
            if (i >= offset) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public void update(String username, UserUpdateHelper updateHelper) {
        User originalUser = readUser(username);
        if (originalUser == null) {
            throw new UserNotFoundException(username);
        }
        User updatedUser = updateHelper.updateUser(originalUser);
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

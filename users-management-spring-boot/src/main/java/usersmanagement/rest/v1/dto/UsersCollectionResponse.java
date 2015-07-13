package usersmanagement.rest.v1.dto;

import usersmanagement.domain.model.User;

import javax.ws.rs.core.UriInfo;
import java.util.*;

public class UsersCollectionResponse {

    private final Map<String, Collection<UserResponse>> _embedded = new HashMap<>();

    public UsersCollectionResponse(Collection<User> users, UriInfo uriInfo) {
        Collection<UserResponse> userResponses = new ArrayList<>(users.size());
        for (User user : users) {
            userResponses.add(UserResponse.fromUser(user, uriInfo));
        }
        _embedded.put("users", userResponses);
    }

    public Map<String, Collection<UserResponse>> get_embedded() {
        return _embedded;
    }
}

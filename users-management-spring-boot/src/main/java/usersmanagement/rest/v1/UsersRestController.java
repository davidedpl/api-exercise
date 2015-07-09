package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.UserType;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersRestController {

    private final UserRepository userRepository;

    @Inject
    public UsersRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GET
    @Path("/{username}")
    public String readUser(
            @PathParam("username") String username,
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole) {

//        checkAuthorization(clientUserRole, clientUserName, username, "retrieve");

        // role Super
        // role Admin
        // role Subscriber and target.equals(username)



        User user = userRepository.retrieve(username);
        return user.getUsername();
    }

    @POST
    public CreateUser registerUser(JsonNode createUser) {
        return new CreateUser(createUser.get("username").asText());
    }



    public void register(UserType requiringUserType, String requiringUsername, User user) {
        checkAuthorization(requiringUserType, requiringUsername, user.getUsername(), "register");
        userRepository.create(user);
    }


    // asses that the requiring user is enabled to perform the required action on the target user
    // throws a SecurityException if the permission is not valid
    private void checkAuthorization(UserType requiringUserType, String requiringUsername,
                                    String targetUsername, String action) {
        if (requiringUserType == UserType.Subscriber) {
            if (!requiringUsername.equals(targetUsername)) {
                throw new SecurityException("Subscriber " + requiringUsername + " tried to " + action
                        + " user " + targetUsername);
            }
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

}

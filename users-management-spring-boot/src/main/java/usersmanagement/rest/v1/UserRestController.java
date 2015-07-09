package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.UserType;
import usersmanagement.rest.v1.assembler.CreateUserAssembler;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Component
@Path(UserRestController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {

    public final static String PATH = "/v1/users";

    private final UserRepository userRepository;
    private final CreateUserAssembler createUserAssembler;
    private UriInfo uriInfo;

    @Inject
    public UserRestController(UserRepository userRepository, CreateUserAssembler createUserAssembler) {
        this.userRepository = userRepository;
        this.createUserAssembler = createUserAssembler;
    }

    @Context
    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @GET
    @Path("{username}")
    public Response readUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username) {

//        checkAuthorization(clientUserRole, clientUserName, username, "retrieve");
        // role Super
        // role Admin
        // role Subscriber and target.equals(username)

        return Response.ok(userRepository.retrieve(username)).build();
    }

    @POST
    public Response registerUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole,
            JsonNode createUser) {
        User userToRegister = createUserAssembler.assemble(createUser);
//        checkAuthorization(clientUserRole, clientUserName, userToRegister.getUsername(), "register");
        userRepository.create(userToRegister);
        URI userUri = uriInfo.getBaseUriBuilder().path(PATH + "/" + userToRegister.getUsername()).build();
        return Response.created(userUri).build();
    }

    @PUT
    @Path("{username}")
    public Response updateUser(
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username,
            JsonNode updateUser) {
        // TODO
        return Response.ok().build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username) {
        if (UserType.SuperUser != clientUserRole) {
            throw new SecurityException("Operation not permitted");
        }
        userRepository.delete(username);
        return Response.ok().build();
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

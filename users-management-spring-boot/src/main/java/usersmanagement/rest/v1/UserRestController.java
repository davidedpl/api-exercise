package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.UserController;
import usersmanagement.domain.security.UserType;
import usersmanagement.domain.utils.UserUpdateHelper;
import usersmanagement.rest.v1.assembler.CreateUserAssembler;
import usersmanagement.rest.v1.assembler.UserUpdateHelperAssembler;

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

    private final CreateUserAssembler createUserAssembler;
    private final UserUpdateHelperAssembler userUpdateHelperAssembler;
    private final UserController userController;

    @Inject
    public UserRestController(CreateUserAssembler createUserAssembler,
                              UserUpdateHelperAssembler userUpdateHelperAssembler, UserController userController) {
        this.createUserAssembler = createUserAssembler;
        this.userUpdateHelperAssembler = userUpdateHelperAssembler;
        this.userController = userController;
    }

    @GET
    @Path("{username}")
    public Response readUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username) {
        return Response.ok(userController.readUser(clientUserName, clientUserRole, username)).build();
    }

    @POST
    public Response registerUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole,
            @Context UriInfo uriInfo,
            JsonNode createUser) {
        User userToRegister = createUserAssembler.assemble(createUser);
        userController.registerUser(clientUserName, clientUserRole, userToRegister);
        URI userUri = uriInfo.getBaseUriBuilder().path(PATH + "/" + userToRegister.getUsername()).build();
        return Response.created(userUri).build();
    }

    @PUT
    @Path("{username}")
    public Response updateUser(
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username,
            JsonNode updateUser) {
        UserUpdateHelper updateHelper = userUpdateHelperAssembler.assemble(updateUser);
        userController.updateUser(clientUserRole, username, updateHelper);
        return Response.ok(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username) {
        userController.deleteUser(clientUserRole, username);
        return Response.ok(Response.Status.NO_CONTENT).build();
    }

}

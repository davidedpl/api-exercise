package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.*;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.PermissionHolder;
import usersmanagement.domain.security.UserSecurityContext;
import usersmanagement.rest.v1.assembler.CreateUserAssembler;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

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

        Optional<User> user = userRepository.retrieve(username);

        validatePermission(clientUserRole.getPermissions(),
                UserAction.READ,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withUserName(clientUserName)
                        .withTargetUsername(username)
                        .withTargetUserType(user.map(u -> u.getType()).orElse(null))
                        .build());

        return Response.ok(user.orElseThrow(() -> new UserNotFoundException(username))).build();
    }

    @POST
    public Response registerUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole,
            JsonNode createUser) {

        User userToRegister = createUserAssembler.assemble(createUser);

        validatePermission(clientUserRole.getPermissions(),
                UserAction.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withUserName(clientUserName)
                        .withTargetUsername(userToRegister.getUsername())
                        .withTargetUserType(userToRegister.getType())
                        .build());

        userRepository.create(userToRegister);
        URI userUri = uriInfo.getBaseUriBuilder().path(PATH + "/" + userToRegister.getUsername()).build();
        return Response.created(userUri).build();
    }

    @PUT
    @Path("{username}")
    public Response updateUser(
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username,
            UserUpdateHelper updateUser) {

        Optional<User> originalUser = userRepository.retrieve(username);

        validatePermission(clientUserRole.getPermissions(),
                UserAction.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withTargetUserType(originalUser.map(u -> u.getType()).orElse(null))
                        .build());

        userRepository.update(username, updateUser);

        return Response.ok(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username) {

        validatePermission(clientUserRole.getPermissions(),
                UserAction.DELETE,
                new UserSecurityContext.UserSecurityContextBuilder().build());

        userRepository.delete(username);
        return Response.ok(Response.Status.NO_CONTENT).build();
    }

    private void validatePermission(PermissionHolder permissionHolder,
                                    UserAction action, UserSecurityContext ctx) {
        if (!permissionHolder.hasPermission(action, ctx)) {
            throw new SecurityException("Operation not permitted");
        }
    }

}

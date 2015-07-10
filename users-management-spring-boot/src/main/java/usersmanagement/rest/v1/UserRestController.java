package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import usersmanagement.domain.User;
import usersmanagement.domain.UserRepository;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.SecurityValidatorFactory;
import usersmanagement.domain.security.UserPermission;
import usersmanagement.domain.security.UserSecurityContext;
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
import java.util.Optional;

@Component
@Path(UserRestController.PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {

    public final static String PATH = "/v1/users";

    private final UserRepository userRepository;
    private final CreateUserAssembler createUserAssembler;
    private final UserUpdateHelperAssembler userUpdateHelperAssembler;
    private final SecurityValidatorFactory securityValidatorFactory;

    @Inject
    public UserRestController(UserRepository userRepository, CreateUserAssembler createUserAssembler,
                              UserUpdateHelperAssembler userUpdateHelperAssembler, SecurityValidatorFactory securityValidatorFactory) {
        this.userRepository = userRepository;
        this.createUserAssembler = createUserAssembler;
        this.userUpdateHelperAssembler = userUpdateHelperAssembler;
        this.securityValidatorFactory = securityValidatorFactory;
    }

    @GET
    @Path("{username}")
    public Response readUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username) {

        Optional<User> user = userRepository.retrieve(username);

        securityValidatorFactory.createFor(clientUserRole).validate(UserPermission.READ,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withCurrentUserName(clientUserName)
                        .withTargetUsername(username)
                        .withTargetUserType(user.map(User::getType).orElse(null)).build());

        return Response.ok(user.orElseThrow(() -> new UserNotFoundException(username))).build();
    }

    @POST
    public Response registerUser(
            @HeaderParam("username") String clientUserName,
            @HeaderParam("role") UserType clientUserRole,
            @Context UriInfo uriInfo,
            JsonNode createUser) {

        User userToRegister = createUserAssembler.assemble(createUser);

        securityValidatorFactory.createFor(clientUserRole).validate(UserPermission.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withCurrentUserName(clientUserName)
                        .withTargetUsername(userToRegister.getUsername())
                        .withTargetUserType(userToRegister.getType()).build());

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

        UserUpdateHelper updateHelper = userUpdateHelperAssembler.assemble(updateUser);

        Optional<User> originalUser = userRepository.retrieve(username);

        securityValidatorFactory.createFor(clientUserRole).validate(UserPermission.CREATE,
                new UserSecurityContext.UserSecurityContextBuilder()
                        .withTargetUserType(originalUser.map(u -> u.getType()).orElse(null)).build());

        userRepository.update(username, updateHelper);

        return Response.ok(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(
            @HeaderParam("role") UserType clientUserRole,
            @PathParam("username") String username) {

        securityValidatorFactory.createFor(clientUserRole).validate(UserPermission.DELETE,
                new UserSecurityContext.UserSecurityContextBuilder().build());

        userRepository.delete(username);
        return Response.ok(Response.Status.NO_CONTENT).build();
    }

}

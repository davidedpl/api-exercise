package usersmanagement.rest.v1;

import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import usersmanagement.config.JerseyConfig;
import usersmanagement.domain.User;
import usersmanagement.domain.UserType;
import usersmanagement.domain.controller.UserController;
import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.utils.UserUpdateHelper;
import usersmanagement.fixtures.UserTestData;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserRestResourceJerseyTest extends JerseyTest {

    private static final User FOUND_USER = UserTestData.subscriberUser1();
    private static final String FOUND_USER_NAME = FOUND_USER.getUsername();
    private static final User NOT_FOUND_USER = UserTestData.subscriberUser2();
    private static final String NOT_FOUND_USER_NAME = NOT_FOUND_USER .getUsername();
    private static final UserType AUTHORIZED_USER_TYPE = UserType.SuperUser;
    private static final UserType NOT_AUTHORIZED_USER_TYPE = UserType.SuperUser;

    private static final String BASE_URI = "http://localhost:9998";
    private static final String BASE_RESOURCE_PATH = BASE_URI + UserRestResource.PATH;

    private Client client;
    private Response response;

    public static class StubbedUserServiceFactory implements Factory<UserController> {
        @Override
        public UserController provide() {
            return new UserController() {
                @Override
                public List<User> readAll(UserAuthenticationAttributes authenticationAttributes) {
                    return Collections.singletonList(FOUND_USER);
                }

                @Override
                public User readUser(UserAuthenticationAttributes authenticationAttributes, String username) {
                    authorize(authenticationAttributes);
                    if (!username.equals(FOUND_USER_NAME)) {
                        throw new UserNotFoundException(username);
                    }
                    return FOUND_USER;
                }

                @Override
                public void registerUser(UserAuthenticationAttributes authenticationAttributes, User userToRegister) {
                    authorize(authenticationAttributes);
                    if (userToRegister.getUsername().equals(FOUND_USER_NAME)) {
                        throw new UserAlreadyExistException(userToRegister.getUsername());
                    }
                }

                @Override
                public void updateUser(UserAuthenticationAttributes authenticationAttributes, String username, UserUpdateHelper updateHelper) {
                    authorize(authenticationAttributes);
                    if (!username.equals(FOUND_USER_NAME)) {
                        throw new UserNotFoundException(username);
                    }
                }

                @Override
                public void deleteUser(UserAuthenticationAttributes authenticationAttributes, String username) {
                    authorize(authenticationAttributes);
                    if (!username.equals(FOUND_USER_NAME)) {
                        throw new UserNotFoundException(username);
                    }
                }

                private void authorize(UserAuthenticationAttributes authenticationAttributes) {
                    if (authenticationAttributes.getCurrentUserType().orElse(null) != AUTHORIZED_USER_TYPE) {
                        throw new SecurityException("Test UserType not authorized");
                    }
                }
            };
        }

        @Override
        public void dispose(UserController t) {}
    }

    @Override
    public Application configure() {
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(StubbedUserServiceFactory.class).to(UserController.class);
            }
        };
        ResourceConfig config = new ResourceConfig(UserRestResource.class);
        config.register(binder);


        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                JerseyConfig.class);
        config.property("contextConfig", ctx);

        return config;
    }

    @Before
    public void init() {
        client = ClientBuilder.newClient();
    }

    @After
    public void cleanup() {
        response.close();
        client.close();
    }

    @Test
    public void readSuccessful() {
        response = client.target(BASE_RESOURCE_PATH + "/" + FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        System.out.print(response.readEntity(String.class));
        // TODO check response content
    }

    @Test
    public void readNotFound() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void readForbidden() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
                .header("type", NOT_AUTHORIZED_USER_TYPE.toString())
                .get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    // read all
    // TODO

    // register
//    @Test
//    public void registerSuccessful() {
//        response = client.target(BASE_RESOURCE_PATH)
//                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
//                .header("type", AUTHORIZED_USER_TYPE.toString())
//                .post(NOT_FOUND_USER);
//        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
//    }

    public void registerValidationError() {
        // TODO some invalid attribute
    }

//    @Test
//    public void registerConflict() {
//        response = client.target(BASE_RESOURCE_PATH)
//                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
//                .header("type", AUTHORIZED_USER_TYPE.toString())
//                .post(FOUND_USER);
//        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
//    }
//
//    @Test
//    public void registerForbidden() {
//        response = client.target(BASE_RESOURCE_PATH)
//                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
//                .header("type", NOT_AUTHORIZED_USER_TYPE.toString())
//                .post(FOUND_USER);
//        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
//    }

    //update

//    @Test
//    public void updateSuccessful() {
//        response = client.target(BASE_RESOURCE_PATH + "/" + FOUND_USER_NAME)
//                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
//                .header("type", AUTHORIZED_USER_TYPE.toString())
//                .put(...);
//        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
//    }

    @Test
    public void updateValidationError() {
        // try to update unmodifiable fields
        // TODO
    }


//    @Test
//    public void updateNotFound() {
//        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
//                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
//                .header("type", AUTHORIZED_USER_TYPE.toString())
//                .put(...);
//        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//    }
//
//    @Test
//    public void updateForbidden() {
//        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
//                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
//                .header("type", NOT_AUTHORIZED_USER_TYPE.toString())
//                .put(...);
//        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
//    }


    // delete

    @Test
    public void deleteSuccessful() {
        response = client.target(BASE_RESOURCE_PATH + "/" + FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteNotFound() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .delete();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteForbidden() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(RepresentationFactory.HAL_JSON)
                .header("type", NOT_AUTHORIZED_USER_TYPE.toString())
                .delete();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

}

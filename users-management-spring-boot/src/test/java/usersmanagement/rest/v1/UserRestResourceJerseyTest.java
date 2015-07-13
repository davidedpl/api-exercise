package usersmanagement.rest.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.StringUtils;
import usersmanagement.JerseyConfig;
import usersmanagement.domain.model.Address;
import usersmanagement.domain.model.AddressableUser;
import usersmanagement.domain.model.User;
import usersmanagement.domain.model.UserType;
import usersmanagement.domain.controller.UserController;
import usersmanagement.domain.exceptions.UserAlreadyExistException;
import usersmanagement.domain.exceptions.UserNotFoundException;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.model.UserUpdateHelper;
import usersmanagement.fixtures.UserTestData;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserRestResourceJerseyTest extends JerseyTest {

    private static final AddressableUser FOUND_USER = UserTestData.subscriberUser1();
    private static final User NOT_ADDRESSABLE_FOUND_USER = UserTestData.adminUser();
    private static final String FOUND_USER_NAME = FOUND_USER.getUsername();
    private static final AddressableUser NOT_FOUND_USER = UserTestData.subscriberUser2();
    private static final User NOT_ADDRESSABLE_NOT_FOUND_USER = UserTestData.adminUser2();
    private static final String NOT_FOUND_USER_NAME = NOT_FOUND_USER .getUsername();
    private static final UserType AUTHORIZED_USER_TYPE = UserType.SuperUser;
    private static final UserType NOT_AUTHORIZED_USER_TYPE = UserType.Subscriber;

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
                    if (!isFound(username)) {
                        throw new UserNotFoundException(username);
                    }
                    return FOUND_USER;
                }

                @Override
                public void registerUser(UserAuthenticationAttributes authenticationAttributes, User userToRegister) {
                    authorize(authenticationAttributes);
                    if (isFound(userToRegister.getUsername())) {
                        throw new UserAlreadyExistException(userToRegister.getUsername());
                    }
                }

                @Override
                public void updateUser(UserAuthenticationAttributes authenticationAttributes, String username, UserUpdateHelper updateHelper) {
                    authorize(authenticationAttributes);
                    if (!isFound(username)) {
                        throw new UserNotFoundException(username);
                    }
                }

                @Override
                public void deleteUser(UserAuthenticationAttributes authenticationAttributes, String username) {
                    authorize(authenticationAttributes);
                    if (!isFound(username)) {
                        throw new UserNotFoundException(username);
                    }
                }

                private boolean isFound(String username) {
                    return username.equals(FOUND_USER_NAME)
                            || username.equals(NOT_ADDRESSABLE_FOUND_USER.getUsername());
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
        String userPath = BASE_RESOURCE_PATH + "/" + FOUND_USER_NAME;
        response = client.target(userPath)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        JsonNode retrievedUser = response.readEntity(JsonNode.class);
        assertFoundUser(retrievedUser, userPath);
    }

    @Test
    public void readNotFound() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void readForbidden() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", NOT_AUTHORIZED_USER_TYPE.toString())
                .get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    // read all
    @Test
    public void readAllWithResults() {
        response = client.target(BASE_RESOURCE_PATH)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        JsonNode retrievedUser = response.readEntity(JsonNode.class).get("_embedded").withArray("users").get(0);
        assertFoundUser(retrievedUser, BASE_RESOURCE_PATH + "/" + FOUND_USER_NAME);
    }

    // register
    @Test
    public void registerSuccessful() {
        ObjectNode userNode = JsonNodeFactory.instance.objectNode();
        userNode.put("title", NOT_FOUND_USER.getTitle());
        userNode.put("firstName", NOT_FOUND_USER.getFirstName());
        userNode.put("lastName", NOT_FOUND_USER.getLastName());
        userNode.put("dateOfBirth", NOT_FOUND_USER.getDateOfBirth().toString());
        userNode.put("email", NOT_FOUND_USER.getEmail());
        userNode.put("username", NOT_FOUND_USER.getUsername());
        userNode.put("type", NOT_FOUND_USER.getType().toString());
        userNode.put("password", NOT_FOUND_USER.getPassword());

        ObjectNode homeAddressNode = JsonNodeFactory.instance.objectNode();
        homeAddressNode.put("addressLine1", NOT_FOUND_USER.getHomeAddress().getAddressLine1());
        if (!StringUtils.isEmpty(NOT_FOUND_USER.getHomeAddress().getAddressLine2())) {
            homeAddressNode.put("addressLine2", NOT_FOUND_USER.getHomeAddress().getAddressLine2());
        }
        if (!StringUtils.isEmpty(NOT_FOUND_USER.getHomeAddress().getPostCode())) {
            homeAddressNode.put("postCode", NOT_FOUND_USER.getHomeAddress().getPostCode());
        }
        homeAddressNode.put("country", NOT_FOUND_USER.getHomeAddress().getCountry());
        userNode.put("homeAddress", homeAddressNode);

        ObjectNode billingAddressNode = JsonNodeFactory.instance.objectNode();
        billingAddressNode.put("addressLine1", NOT_FOUND_USER.getBillingAddress().getAddressLine1());
        if (!StringUtils.isEmpty(NOT_FOUND_USER.getBillingAddress().getAddressLine2())) {
            billingAddressNode.put("addressLine2", NOT_FOUND_USER.getBillingAddress().getAddressLine2());
        }
        if (!StringUtils.isEmpty(NOT_FOUND_USER.getBillingAddress().getPostCode())) {
            billingAddressNode.put("postCode", NOT_FOUND_USER.getBillingAddress().getPostCode());
        }
        billingAddressNode.put("country", NOT_FOUND_USER.getBillingAddress().getCountry());
        userNode.put("billingAddress", billingAddressNode);

        String userPath = BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME;
        response = client.target(BASE_RESOURCE_PATH)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .post(Entity.json(userNode));
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(userPath, response.getHeaderString("Location"));
    }

    @Test
    public void registerEmailValidationError() {
        ObjectNode userNode = JsonNodeFactory.instance.objectNode();
        userNode.put("title", FOUND_USER.getTitle());
        userNode.put("firstName", FOUND_USER.getFirstName());
        userNode.put("lastName", FOUND_USER.getLastName());
        userNode.put("dateOfBirth", FOUND_USER.getDateOfBirth().toString());
        userNode.put("email", "invalid_email");
        userNode.put("username", FOUND_USER.getUsername());
        userNode.put("type", FOUND_USER.getType().toString());
        userNode.put("password", FOUND_USER.getPassword());

        response = client.target(BASE_RESOURCE_PATH)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .post(Entity.json(userNode));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    public void registerConflict() {
        response = client.target(BASE_RESOURCE_PATH)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .post(Entity.json(testNode(NOT_ADDRESSABLE_FOUND_USER)));
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    public void registerForbidden() {
        response = client.target(BASE_RESOURCE_PATH)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", NOT_AUTHORIZED_USER_TYPE.toString())
                .post(Entity.json(testNode(NOT_ADDRESSABLE_FOUND_USER)));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }


    // delete

    @Test
    public void deleteSuccessful() {
        response = client.target(BASE_RESOURCE_PATH + "/" + FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteNotFound() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", AUTHORIZED_USER_TYPE.toString())
                .delete();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void deleteForbidden() {
        response = client.target(BASE_RESOURCE_PATH + "/" + NOT_FOUND_USER_NAME)
                .request(MediaType.APPLICATION_JSON).accept(UserRestResource.HAL_JSON)
                .header("type", NOT_AUTHORIZED_USER_TYPE.toString())
                .delete();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }


    private ObjectNode testNode(User user) {
        ObjectNode userNode = JsonNodeFactory.instance.objectNode();
        userNode.put("title", user.getTitle());
        userNode.put("firstName", user.getFirstName());
        userNode.put("lastName", user.getLastName());
        userNode.put("dateOfBirth", user.getDateOfBirth().toString());
        userNode.put("email", user.getEmail());
        userNode.put("username", user.getUsername());
        userNode.put("type", user.getType().toString());
        userNode.put("password", user.getPassword());
        return userNode;
    }

    private void assertFoundUser(JsonNode retrievedUser, String userPath) {
        assertEquals(userPath, retrievedUser.get("_links").get("self").get("href").asText());
        assertEquals(FOUND_USER.getTitle(), retrievedUser.get("title").asText());
        assertEquals(FOUND_USER.getFirstName(), retrievedUser.get("firstName").asText());
        assertEquals(FOUND_USER.getLastName(), retrievedUser.get("lastName").asText());
        assertEquals(FOUND_USER.getDateOfBirth().toString(), retrievedUser.get("dateOfBirth").asText());
        assertEquals(FOUND_USER.getEmail(), retrievedUser.get("email").asText());
        assertEquals(FOUND_USER.getUsername(), retrievedUser.get("username").asText());
        assertEquals(FOUND_USER.getType().toString(), retrievedUser.get("type").asText());

        Address expectedHomeAddress = FOUND_USER.getHomeAddress();

        JsonNode actualHomeAddress = retrievedUser.get("homeAddress");
        assertEquals(expectedHomeAddress.getAddressLine1(), actualHomeAddress.get("addressLine1").asText());
        if (!StringUtils.isEmpty(expectedHomeAddress.getAddressLine2())) {
            assertEquals(expectedHomeAddress.getAddressLine2(), actualHomeAddress.get("addressLine2").asText());
        }
        if (!StringUtils.isEmpty(expectedHomeAddress.getPostCode())) {
            assertEquals(expectedHomeAddress.getPostCode(), actualHomeAddress.get("postCode").asText());
        }
        assertEquals(expectedHomeAddress.getCountry(), actualHomeAddress.get("country").asText());

        Address expectedBillingAddress = FOUND_USER.getBillingAddress();

        JsonNode actualBillingAddress = retrievedUser.get("billingAddress");
        assertEquals(expectedBillingAddress.getAddressLine1(), actualBillingAddress.get("addressLine1").asText());
        if (!StringUtils.isEmpty(expectedBillingAddress.getAddressLine2())) {
            assertEquals(expectedBillingAddress.getAddressLine2(), actualBillingAddress.get("addressLine2").asText());
        }
        if (!StringUtils.isEmpty(expectedBillingAddress.getPostCode())) {
            assertEquals(expectedBillingAddress.getPostCode(), actualBillingAddress.get("postCode").asText());
        }
        assertEquals(expectedBillingAddress.getCountry(), actualBillingAddress.get("country").asText());
    }

}

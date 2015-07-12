package usersmanagement.rest;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import usersmanagement.config.JerseyConfig;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.service.UserController;
import usersmanagement.fixtures.UserTestData;
import usersmanagement.rest.v1.UserRestResource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRestResourceJerseyTest extends JerseyTest {

    public static class MockUserServiceFactory implements Factory<UserController> {
        @Override
        public UserController provide() {
            final UserController mockedController = mock(UserController.class);
            when(mockedController.readUser(any(UserAuthenticationAttributes.class), anyString()))
                    .thenReturn(UserTestData.subscriberUser1());
            return mockedController;
        }

        @Override
        public void dispose(UserController t) {}
    }

    @Override
    public Application configure() {
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(MockUserServiceFactory.class).to(UserController.class);
            }
        };
        ResourceConfig config = new ResourceConfig(UserRestResource.class);
        config.register(binder);


        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
//                usersmanagement.Application.class,
                JerseyConfig.class);
        config.property("contextConfig", ctx);

        return config;
    }


    @Test
    public void testMockedGreetingService() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("http://localhost:9998/v1/users/pino")
//                .queryParam("name", "peeskillet")
                .request(MediaType.APPLICATION_JSON).get();
        assertEquals(200, response.getStatus());

//        String msg = response.readEntity(String.class);
//        assertEquals("Hello peeskillet", msg);
//        System.out.println("Message: " + msg);

        response.close();
        client.close();
    }

    @Test
    public void readUserSuccessful() {

    }
}

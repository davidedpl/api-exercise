package usersmanagement.rest;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import usersmanagement.JerseyConfig;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.service.UserService;
import usersmanagement.fixtures.UserTestData;
import usersmanagement.rest.v1.UserRestController;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

public class UserRestControllerJerseyTest extends JerseyTest {

    public static class MockUserServiceFactory implements Factory<UserService> {
        @Override
        public UserService provide() {
            final UserService mockedService = Mockito.mock(UserService.class);
            Mockito.when(mockedService.readUser(any(UserAuthenticationAttributes.class), anyString()))
                    .thenReturn(UserTestData.subscriberUser1());

//                    .thenAnswer(new Answer<String>() {
//                        @Override
//                        public String answer(InvocationOnMock invocation)
//                                throws Throwable {
//                            String name = (String) invocation.getArguments()[0];
//                            return "Hello " + name;
//                        }
//
//                    });
            return mockedService;
        }

        @Override
        public void dispose(UserService t) {}
    }

    @Override
    public Application configure() {
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(MockUserServiceFactory.class).to(UserService.class);
            }
        };
        ResourceConfig config = new ResourceConfig(UserRestController.class);
        config.register(binder);


        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
//                usersmanagement.Application.class,
                JerseyConfig.class);
        config.property("contextConfig", ctx);

//        config.property(TestProperties.RECORD_LOG_LEVEL, Level.INFO);

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

package usersmanagement;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import usersmanagement.rest.v1.UsersRestController;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(UsersRestController.class);
    }
}

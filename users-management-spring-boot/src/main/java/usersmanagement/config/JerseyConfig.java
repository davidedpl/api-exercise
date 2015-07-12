package usersmanagement.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import usersmanagement.rest.v1.assembler.AddressAssembler;
import usersmanagement.rest.v1.assembler.CreateUserAssembler;
import usersmanagement.rest.v1.assembler.UserUpdateHelperAssembler;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        String currPackage = this.getClass().getPackage().toString();
        String restPackage = currPackage.substring(0, currPackage.lastIndexOf('.')) + ".rest";
        packages(true, restPackage);
    }

    @Bean
    public CreateUserAssembler createUserAssembler() {
        return new CreateUserAssembler(addressAssembler());
    }

    @Bean
    public AddressAssembler addressAssembler() {
        return new AddressAssembler();
    }

    @Bean
    public UserUpdateHelperAssembler userUpdateHelperAssembler() {
        return new UserUpdateHelperAssembler(addressAssembler());
    }
}
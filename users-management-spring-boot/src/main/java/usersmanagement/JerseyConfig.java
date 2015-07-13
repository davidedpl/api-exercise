package usersmanagement;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import usersmanagement.rest.v1.assembler.AddressAssembler;
import usersmanagement.rest.v1.assembler.CreateUserAssembler;
import usersmanagement.rest.v1.assembler.UserUpdateHelperAssembler;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages(true, this.getClass().getPackage().toString());
    }

    // Bean configuration is required here to support JerseyTest
    @Bean
    public CreateUserAssembler createUserAssembler() {
        return new CreateUserAssembler(addressAssembler(), passwordEncoder());
    }

    @Bean
    public AddressAssembler addressAssembler() {
        return new AddressAssembler();
    }

    @Bean
    public UserUpdateHelperAssembler userUpdateHelperAssembler() {
        return new UserUpdateHelperAssembler(addressAssembler());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

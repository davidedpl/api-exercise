package usersmanagement.domain.security;

import org.springframework.stereotype.Component;

@Component
public class SecurityValidatorFactory {

    public SecurityValidator createFor(UserType type) {
        if (type == null) {
            throw new SecurityException("UserType is null");
        }
        return type;
    }

}

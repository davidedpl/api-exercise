package usersmanagement.rest.v1;

public final class CreateUser {

   private final String username;

    public CreateUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

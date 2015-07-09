package usersmanagement.domain;

/**
 * Describes the different types of users.
 */
public enum UserType {

    Subscriber("Subscriber"),
    Administrator("Administrator"),
    SuperUser("Super User");

    /** String representation of the user-type */
    private final String representation;

    UserType(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }

}

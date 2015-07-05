package usersmanagement.domain;

public enum UserType {

    Subscriber("Subscriber"),
    Administrator("Administrator"),
    SuperUser("Super User");

    /** String representation of the user-type */
    final String representation;

    UserType(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return representation;
    }
}

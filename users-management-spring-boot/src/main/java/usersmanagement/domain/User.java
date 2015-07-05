package usersmanagement.domain;

import java.time.LocalDate;

public class User {

    private final UserType type;
    private final String title;
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth; // TODO verify is working properly
    private final String email; // TODO validate email
    private final char[] password; // TODO hash in SHA1
    private final String username; // TODO explain -- add validation (letters and numbers only)

    // TODO consider using a subclass for different User types
    private final Address homeAddress; // TODO Subscriber only
    private final Address billingAddress; // TODO Subscriber only


    public User(UserType type, String title, String firstName, String lastName, LocalDate dateOfBirth, String email,
                char[] password, String username, Address homeAddress, Address billingAddress) {
        this.type = type;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.username = username;
        this.homeAddress = homeAddress;
        this.billingAddress = billingAddress;
    }

    public UserType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public char[] getPassword() {
        return password;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public String getUsername() {
        return username;
    }
}

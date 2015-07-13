package usersmanagement.domain.model;

import java.time.LocalDate;

/**
 * Support implementation of a user with address details.
 */
abstract class AbstractAddressableUser extends AbstractUser implements AddressableUser {

    private final Address homeAddress;
    private final Address billingAddress;

    AbstractAddressableUser(String lastName, String firstName, String password, String email,
                                   String title, String username, LocalDate dateOfBirth,
                                   Address homeAddress, Address billingAddress) {
        super(lastName, firstName, password, email, title, username, dateOfBirth);
        this.homeAddress = homeAddress;
        this.billingAddress = billingAddress;
    }

    @Override
    public final Address getHomeAddress() {
        return homeAddress;
    }

    @Override
    public final Address getBillingAddress() {
        return billingAddress;
    }
}

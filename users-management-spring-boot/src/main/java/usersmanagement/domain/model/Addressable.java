package usersmanagement.domain.model;

/**
 * Provides access to home and billing address
 */
public interface Addressable {

    Address getHomeAddress();

    Address getBillingAddress();
}

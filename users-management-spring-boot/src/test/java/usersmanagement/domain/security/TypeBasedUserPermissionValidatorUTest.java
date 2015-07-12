package usersmanagement.domain.security;

import org.junit.Assert;
import org.junit.Test;

import static usersmanagement.domain.security.SecurityTestData.*;

public class TypeBasedUserPermissionValidatorUTest {

    private TypeBasedUserPermissionValidator permissionValidator = new TypeBasedUserPermissionValidator();

    // Subscriber

    @Test
    public void subscriberCanRegisterHimSelf() {
        permissionValidator.validate(UserPermission.CREATE, ctxSubscriberOnHimSelf);
        // no errors expected
    }

    @Test
    public void subscriberCanRetrieveHimSelf() {
        permissionValidator.validate(UserPermission.READ, ctxSubscriberOnHimSelf);
        // no errors expected
    }

    @Test(expected = SecurityException.class)
    public void subscriberCantRegisterOtherSubscriber() {
        permissionValidator.validate(UserPermission.CREATE, ctxSubscriberOnOther);
    }

    @Test(expected = SecurityException.class)
    public void subscriberCantRetrieveOtherSubscriber() {
        permissionValidator.validate(UserPermission.READ, ctxSubscriberOnOther);
    }

    @Test(expected = SecurityException.class)
    public void subscriberCantUpdateAnyUser() {
        permissionValidator.validate(UserPermission.UPDATE, ctxSubscriberOnAny);
    }

    @Test(expected = SecurityException.class)
    public void subscriberCantDeleteAnyUser() {
        permissionValidator.validate(UserPermission.DELETE, ctxSubscriberOnAny);
    }


    // Administrator

    @Test
    public void adminCanRegisterSubscribers() {
        permissionValidator.validate(UserPermission.CREATE, ctxAdminOnSubscriber);
        // no errors expected
    }

    @Test
    public void adminCanRetrieveSubscribers() {
        permissionValidator.validate(UserPermission.READ, ctxAdminOnSubscriber);
        // no errors expected
    }

    @Test
    public void adminCanUpdateSubscribers() {
        permissionValidator.validate(UserPermission.UPDATE, ctxAdminOnSubscriber);
        // no errors expected
    }

    @Test(expected = SecurityException.class)
    public void adminCantRegisterNonSubscribers() {
        permissionValidator.validate(UserPermission.CREATE, ctxAdminOnNonSubscriber);
    }

    @Test(expected = SecurityException.class)
    public void adminCantRetrieveNonSubscribers() {
        permissionValidator.validate(UserPermission.READ, ctxAdminOnNonSubscriber);
    }

    @Test(expected = SecurityException.class)
    public void adminCantUpdateNonSubscribers() {
        permissionValidator.validate(UserPermission.UPDATE, ctxAdminOnNonSubscriber);
    }

    @Test(expected = SecurityException.class)
    public void adminCantDeleteAnyUser() {
        permissionValidator.validate(UserPermission.DELETE, ctxAdminOnAny);
    }


    // SuperUser

    @Test
    public void superCanRegisterSubscribers() {
        permissionValidator.validate(UserPermission.CREATE, ctxSuperOnSubscriber);
        // no errors expected
    }

    @Test
    public void superCanRegisterAdmin() {
        permissionValidator.validate(UserPermission.CREATE, ctxSuperOnAdmin);
        // no errors expected
    }

    @Test
    public void superCanRetrieveSubscribers() {
        permissionValidator.validate(UserPermission.READ, ctxSuperOnSubscriber);
        // no errors expected
    }

    @Test
    public void superCanRetrieveAdmin() {
        permissionValidator.validate(UserPermission.READ, ctxSuperOnAdmin);
        // no errors expected
    }

    @Test
    public void superCanUpdateSubscribers() {
        permissionValidator.validate(UserPermission.UPDATE, ctxSuperOnSubscriber);
        // no errors expected
    }

    @Test
    public void superCanUpdateAdmin() {
        permissionValidator.validate(UserPermission.UPDATE, ctxSuperOnAdmin);
        // no errors expected
    }

    @Test
    public void superCanDeleteSubscribers() {
        permissionValidator.validate(UserPermission.DELETE, ctxSuperOnSubscriber);
        // no errors expected
    }

    @Test
    public void superCanDeleteAdmin() {
        permissionValidator.validate(UserPermission.DELETE, ctxSuperOnAdmin);
        // no errors expected
    }

    @Test(expected = SecurityException.class)
    public void superCantRegisterSuper() {
        permissionValidator.validate(UserPermission.CREATE, ctxSuperOnSuper);
    }

    @Test(expected = SecurityException.class)
    public void superCantRetrieveSuper() {
        permissionValidator.validate(UserPermission.READ, ctxSuperOnSuper);
    }

    @Test(expected = SecurityException.class)
    public void superCantUpdateSuper() {
        permissionValidator.validate(UserPermission.UPDATE, ctxSuperOnSuper);
    }

    @Test(expected = SecurityException.class)
    public void superCantDeleteSuper() {
        permissionValidator.validate(UserPermission.DELETE, ctxSuperOnSuper);
    }


    // Unknown

    @Test
    public void unknownTypeFails() {
        for (UserPermission perm : UserPermission.values()) {
            try {
                permissionValidator.validate(perm, SecurityTestData.ctxWithNullType);
                Assert.fail("Expected SecurityException");
            } catch (SecurityException e) {
                // expected
            }
        }
    }

}

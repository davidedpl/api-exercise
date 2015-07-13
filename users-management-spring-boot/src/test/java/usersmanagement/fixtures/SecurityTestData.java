package usersmanagement.fixtures;

import usersmanagement.domain.model.UserType;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.security.UserSecurityContext;
import usersmanagement.domain.security.UserSecurityContext.UserSecurityContextBuilder;

public class SecurityTestData {

    private final static String SUBSCRIBER_AUTHENTICATED_NAME = "TestSubscriber";
    private final static String SUBSCRIBER_OTHER_NAME = "TestSubscriber_Other";


    // useful UserSecurityContextBuilders
    private static final UserAuthenticationAttributes attrSubscriber =
            new UserAuthenticationAttributes(SUBSCRIBER_AUTHENTICATED_NAME, UserType.Subscriber);

    private static final UserAuthenticationAttributes attrAdmin =
            new UserAuthenticationAttributes(UserType.Administrator);

    private static final UserAuthenticationAttributes attrSuper =
            new UserAuthenticationAttributes(UserType.SuperUser);


    // applying auth attributes to different targets

    public static final UserSecurityContext ctxWithNullType =
            new UserSecurityContextBuilder(null, new UserAuthenticationAttributes()).build();


    public static final UserSecurityContext ctxSubscriberOnHimSelf =
            new UserSecurityContextBuilder(
                    UserType.Subscriber, attrSubscriber).withTargetUsername(SUBSCRIBER_AUTHENTICATED_NAME).build();

    public static final UserSecurityContext ctxSubscriberOnOther =
            new UserSecurityContextBuilder(
                    UserType.Administrator, attrSubscriber).withTargetUsername(SUBSCRIBER_OTHER_NAME).build();

    public static final UserSecurityContext ctxSubscriberOnAny =
            new UserSecurityContextBuilder(null, attrSubscriber).build();


    public static final UserSecurityContext ctxAdminOnSubscriber =
            new UserSecurityContextBuilder(UserType.Subscriber, attrAdmin).build();

    public static final UserSecurityContext ctxAdminOnNonSubscriber =
            new UserSecurityContextBuilder(UserType.Administrator, attrAdmin).build();

    public static final UserSecurityContext ctxAdminOnAny =
            new UserSecurityContextBuilder(null, attrAdmin).build();


    public static final UserSecurityContext ctxSuperOnSubscriber =
            new UserSecurityContextBuilder(UserType.Subscriber, attrSuper).build();

    public static final UserSecurityContext ctxSuperOnAdmin =
            new UserSecurityContextBuilder(UserType.Administrator, attrSuper).build();

    public static final UserSecurityContext ctxSuperOnSuper =
            new UserSecurityContextBuilder(UserType.SuperUser, attrSuper).build();
}

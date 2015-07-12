package usersmanagement.fixtures;

import usersmanagement.domain.UserType;
import usersmanagement.domain.security.UserAuthenticationAttributes;
import usersmanagement.domain.security.UserSecurityContext;
import usersmanagement.domain.security.UserSecurityContext.UserSecurityContextBuilder;

public class SecurityTestData {

    private final static String SUBSCRIBER_AUTHENTICATED_NAME = "TestSubscriber";
    private final static String SUBSCRIBER_OTHER_NAME = "TestSubscriber_Other";


    // useful UserSecurityContextBuilders
    private static final UserSecurityContextBuilder ctxSubscriber = new UserSecurityContextBuilder(
            new UserAuthenticationAttributes(SUBSCRIBER_AUTHENTICATED_NAME, UserType.Subscriber));

    private static final UserSecurityContextBuilder ctxAdmin = new UserSecurityContextBuilder(
            new UserAuthenticationAttributes(UserType.Administrator));

    private static final UserSecurityContextBuilder ctxSuper = new UserSecurityContextBuilder(
            new UserAuthenticationAttributes(UserType.SuperUser));


    // applying auth attributes to different targets

    public static final UserSecurityContext ctxWithNullType =
            new UserSecurityContextBuilder(new UserAuthenticationAttributes()).build();


    public static final UserSecurityContext ctxSubscriberOnHimSelf =
            ctxSubscriber.withTargetUsername(SUBSCRIBER_AUTHENTICATED_NAME).build();

    public static final UserSecurityContext ctxSubscriberOnOther =
            ctxSubscriber.withTargetUsername(SUBSCRIBER_OTHER_NAME).build();

    public static final UserSecurityContext ctxSubscriberOnAny = ctxSubscriber.build();


    public static final UserSecurityContext ctxAdminOnSubscriber =
            ctxAdmin.withTargetUserType(UserType.Subscriber).build();

    public static final UserSecurityContext ctxAdminOnNonSubscriber =
            ctxAdmin.withTargetUserType(UserType.Administrator).build();

    public static final UserSecurityContext ctxAdminOnAny = ctxAdmin.build();


    public static final UserSecurityContext ctxSuperOnSubscriber =
            ctxSuper.withTargetUserType(UserType.Subscriber).build();

    public static final UserSecurityContext ctxSuperOnAdmin =
            ctxSuper.withTargetUserType(UserType.Administrator).build();

    public static final UserSecurityContext ctxSuperOnSuper =
            ctxSuper.withTargetUserType(UserType.SuperUser).build();
}

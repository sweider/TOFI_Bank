package by.bsuir.sweider_b.banksystem.shared.services.authentication;

/**
 * Created by sweid on 03.01.2016.
 */
public interface ICustomerAuthenticationService {
    String SERVICE_NAME = "AuthenticationService";
    AuthenticationResult authenticateCustomer(final String login, final String password) throws AuthenticationException;
}

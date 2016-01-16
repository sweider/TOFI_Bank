package by.bsuir.sweider_b.banksystem.shared.services.authentication;

import java.rmi.RemoteException;

/**
 * Created by sweid on 15.01.2016.
 */
public interface IAdminsAuthenticationService {
    String SERVICE_NAME = "AdminsAuthenticationService";
    AuthenticationResult authenticateAdmin(final String login, final String password) throws AuthenticationException, RemoteException;
}

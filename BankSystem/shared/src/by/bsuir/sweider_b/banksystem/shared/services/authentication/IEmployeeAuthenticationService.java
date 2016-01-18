package by.bsuir.sweider_b.banksystem.shared.services.authentication;

import java.rmi.RemoteException;

/**
 * Created by sweid on 18.01.2016.
 */
public interface IEmployeeAuthenticationService {
    String SERVICE_NAME = "EmployeeAuthenticationService";
    AuthenticationResult authenticateOperator(final String login, final String password) throws AuthenticationException, RemoteException;
}

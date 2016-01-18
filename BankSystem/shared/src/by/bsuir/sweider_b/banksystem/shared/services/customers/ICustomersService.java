package by.bsuir.sweider_b.banksystem.shared.services.customers;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sweid on 18.01.2016.
 */
public interface ICustomersService {
    String SERVICE_NAME = "ICustomersService";
    void addNewCustomer(String sessionId, CustomerDO newCustomer) throws RemoteException, CustomerCreationException;

    void updateCustomer(String sessionId, int customerId, CustomerDO newParams) throws RemoteException, CustomerUpdatingException;

    List<CustomerDO> showCustomers() throws RemoteException;

    void changePasswordForCustomer(String sessionId, int employeeId, String newPassword) throws RemoteException, CustomerUpdatingException;

    CustomerDO getCustomerByPassportNumber(String sessionId, String passportNumber) throws RemoteException, CustomerNotFoundException;

    List<CustomerDO> getCustomersByFio(String sessionId, String name, String lastName, String surName) throws RemoteException, CustomerNotFoundException;
}

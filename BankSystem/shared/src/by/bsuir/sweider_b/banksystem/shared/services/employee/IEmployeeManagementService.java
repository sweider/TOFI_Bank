package by.bsuir.sweider_b.banksystem.shared.services.employee;

import java.rmi.RemoteException;

/**
 * Created by sweid on 16.01.2016.
 */
public interface IEmployeeManagementService{
    String SERVICE_NAME = "EmployeeManagementService";
    void createEmployee(String sessionId, NewEmployeeData data) throws RemoteException, EmployeeCreationException;
}

package by.bsuir.sweider_b.banksystem.shared.services.employee;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sweid on 16.01.2016.
 */
public interface IEmployeeManagementService{
    String SERVICE_NAME = "EmployeeManagementService";

    void createEmployee(String sessionId, NewEmployeeDO data) throws RemoteException, EmployeeCreationException;

    List<EmployeeShowDO> getEmployees() throws RemoteException;

    void changePasswordForEmployee(String sessionId, int employeeId, String newPwd) throws RemoteException, UpdatingException;

    void updateEmployeeData(String sessionId, EmployeeShowDO data) throws RemoteException, UpdatingException;

    void setActiveState(String sessionId, int employeeId, boolean state) throws RemoteException, UpdatingException;
}

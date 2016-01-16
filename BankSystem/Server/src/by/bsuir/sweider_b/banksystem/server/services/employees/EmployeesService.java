package by.bsuir.sweider_b.banksystem.server.services.employees;

import by.bsuir.sweider_b.banksystem.server.domain.employee.Employee;
import by.bsuir.sweider_b.banksystem.shared.services.employee.EmployeeCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.employee.IEmployeeManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.NewEmployeeData;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.Optional;

@Service
public class EmployeesService implements IEmployeeManagementService {
    @Autowired
    private BCryptPasswordEncoder ENCODER;


    public void changePassword(String sessionId){}

    public void createCreditApplication(String sessionId){}


    @Override
    public void createEmployee(String sessionId, NewEmployeeData data) throws RemoteException, EmployeeCreationException {
        Optional<Employee> optEmployee =  Employee.filter().aliasses(new AbstractMap.SimpleEntry("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", data.getLogin()), Restrictions.eq("role", data.getRole()))
                .first();
        if(optEmployee.isPresent()) throw new EmployeeCreationException("Данный логин уже используется");
        Employee newEmployee = new Employee(data.getLogin(), ENCODER.encode(data.getPassword()), data.getRole());
        if(!newEmployee.save()){
            throw new EmployeeCreationException("При сохранении нового пользователя произошла непредвиденная ошибка, попробуйте еще раз.");
        }
    }
}
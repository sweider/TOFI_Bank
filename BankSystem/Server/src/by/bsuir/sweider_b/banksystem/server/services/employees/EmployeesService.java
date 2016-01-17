package by.bsuir.sweider_b.banksystem.server.services.employees;

import by.bsuir.sweider_b.banksystem.server.domain.employee.Employee;
import by.bsuir.sweider_b.banksystem.shared.services.employee.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeesService implements IEmployeeManagementService {
    @Autowired
    private BCryptPasswordEncoder ENCODER;

    @Override
    public void createEmployee(String sessionId, NewEmployeeDO data) throws RemoteException, EmployeeCreationException {
        Optional<Employee> optEmployee =  Employee.filter().aliasses(new AbstractMap.SimpleEntry("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", data.getLogin()), Restrictions.eq("role", data.getRole()))
                .first();
        if(optEmployee.isPresent()) throw new EmployeeCreationException("Данный логин уже используется");
        Employee newEmployee = new Employee(data.getLogin(), ENCODER.encode(data.getPassword()), data.getRole(),
                data.getPassportNbmr(), data.getName(), data.getLastName(), data.getSurName());
        if(!newEmployee.save()){
            throw new EmployeeCreationException("При сохранении нового пользователя произошла непредвиденная ошибка, попробуйте еще раз.");
        }
    }

    @Override
    public List<EmployeeShowDO> getEmployees() throws RemoteException {
        List<EmployeeShowDO> employees = Employee.filter().where(Restrictions.eq("isActive", true)).get().stream()
                .map( employee -> new EmployeeShowDO(employee.getId(), employee.getLogin(), employee.getRole(),
                        employee.getPassportData().getName(), employee.getPassportData().getLastname(),
                        employee.getPassportData().getSurname(), employee.getPassportData().getPassportNumber()))
                .collect(Collectors.toList());
        return employees;
    }

    @Override
    public void changePasswordForEmployee(String sessionId, int employeeId, String newPwd) throws RemoteException, UpdatingException {
        Employee employee =  Employee.find(employeeId).orElseThrow(() -> new UpdatingException("Пользователь не найден!"));
        employee.getUserCredentials().setPassword(ENCODER.encode(newPwd));
        if(!employee.save()){
            throw new UpdatingException("При обновлении пароля произошла непредвиденная ошибка, попробуйте еще раз.");
        }
    }

    @Override
    public void updateEmployeeData(String sessionId, EmployeeShowDO data) throws RemoteException, UpdatingException {
        Employee employee = Employee.find(data.getId()).orElseThrow(() -> new UpdatingException("Пользователь не найден!"));
        employee.setRole(data.getRole());
        employee.getUserCredentials().setLogin(data.getLogin());
        employee.getPassportData().setName(data.getName());
        employee.getPassportData().setLastname(data.getLastName());
        employee.getPassportData().setSurname(data.getSurName());
        employee.getPassportData().setPassportNumber(data.getPassportNmbr());
        if(!employee.save()){
            throw new UpdatingException("При обновлении пользователя произошла непредвиденная ошибка, попробуйте еще раз.");
        }
    }

    @Override
    public void setActiveState(String sessionId, int employeeId, boolean state) throws RemoteException, UpdatingException {
        Employee employee = Employee.find(employeeId).orElseThrow(() -> new UpdatingException("Пользователь не найден!"));
        employee.setActive(state);
        if(!employee.save()){
            throw new UpdatingException("При обновлении пользователя произошла непредвиденная ошибка, попробуйте еще раз.");
        }
    }
}
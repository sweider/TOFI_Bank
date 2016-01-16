package by.bsuir.sweider_b.banksystem.server.services.authentication;

import by.bsuir.sweider_b.banksystem.server.domain.customer.Customer;
import by.bsuir.sweider_b.banksystem.server.domain.employee.Employee;
import by.bsuir.sweider_b.banksystem.server.services.sessions.SessionToUserService;
import by.bsuir.sweider_b.banksystem.server.services.sessions.SessionsExpirationService;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

/**
 * Created by sweid on 06.12.2015.
 */

@Service
public class AuthenticationService  implements ICustomerAuthenticationService, IAdminsAuthenticationService {
    @Autowired
    private BCryptPasswordEncoder ENCODER;

    @Autowired
    private SessionsExpirationService sessionsExpirationService;

    @Autowired
    private SessionToUserService sessionToUserService;

    public AuthenticationResult authenticateCustomer(final String login, final String password) throws AuthenticationException {
        String encodedPwd = ENCODER.encode(password);
        Optional<Customer> optCustomer = Customer.filter()
                                                    .aliasses(new SimpleEntry<>("userCredentials", "uc"))
                                                    .where(Restrictions.eq("uc.login", login), Restrictions.eq("uc.password", encodedPwd))
                                                    .first();
        Customer customer = optCustomer.orElseThrow(AuthenticationException::new);
        String sessionId = ENCODER.encode("customer_" + login + encodedPwd);
        this.sessionsExpirationService.putSession(sessionId);
        this.sessionToUserService.putCustomer(sessionId, customer);

        UserDetails userDetails = new UserDetails(customer.getCustomerInfo().getPassportData().getName());
        return new AuthenticationResult(sessionId,userDetails);
    }

    public AuthenticationResult authenticateEmployee(final String login, final String password) throws AuthenticationException {
        String encodedPwd = ENCODER.encode(password);
        Optional<Employee> optEmployee = Employee.filter()
                .aliasses(new SimpleEntry<>("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", login), Restrictions.eq("uc.password", encodedPwd))
                .first();
        Employee employee = optEmployee.orElseThrow(AuthenticationException::new);
        String sessionId = ENCODER.encode("employee_" + login + encodedPwd);
        this.sessionsExpirationService.putSession(sessionId);
        this.sessionToUserService.putEmployee(sessionId, employee);
        return new AuthenticationResult(sessionId, new UserDetails("Employee"));
    }

    @Override
    public AuthenticationResult authenticateAdmin(String login, String password) throws AuthenticationException {
        String encodedPwd = ENCODER.encode(password);
        Optional<Employee> optEmployee = Employee.filter()
                .aliasses(new SimpleEntry<>("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", login), Restrictions.eq("role", EmployeeRole.ADMIN))
                .first();
        Employee employee = optEmployee.orElseThrow(AuthenticationException::new);
        if(ENCODER.matches(password, employee.getPassword())) {
            String sessionId = ENCODER.encode("employee_" + login + encodedPwd);
            this.sessionsExpirationService.putSession(sessionId);
            this.sessionToUserService.putEmployee(sessionId, employee);
            return new AuthenticationResult(sessionId, new UserDetails(employee.getLogin()));
        }
        else{
            throw new AuthenticationException();
        }
    }
}

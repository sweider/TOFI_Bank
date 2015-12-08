package by.bsuir.sweider_b.services.authentication;

import by.bsuir.sweider_b.domain.customer.Customer;
import by.bsuir.sweider_b.domain.employee.Employee;
import by.bsuir.sweider_b.domain.user.UserCredentials;
import by.bsuir.sweider_b.services.sessions.SessionToUserService;
import by.bsuir.sweider_b.services.sessions.SessionsExpirationService;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

/**
 * Created by sweid on 06.12.2015.
 */

@Service
public class AuthenticationService {
    @Autowired
    private BCryptPasswordEncoder ENCODER;

    @Autowired
    private SessionsExpirationService sessionsExpirationService;

    @Autowired
    private SessionToUserService sessionToUserService;

    public AuthenticationResult authenticateCustomer(UserCredentials credentials) throws AuthenticationException {
        String encodedPwd = ENCODER.encode(credentials.getPassword());
        Optional<Customer> optCustomer = Customer.filter()
                                                    .aliasses(new SimpleEntry<>("userCredentials", "uc"))
                                                    .where(Restrictions.eq("uc.login", credentials.getLogin()), Restrictions.eq("uc.password", encodedPwd))
                                                    .first();
        Customer customer = optCustomer.orElseThrow(AuthenticationException::new);
        String sessionId = ENCODER.encode("customer_" + credentials.getLogin() + encodedPwd);
        this.sessionsExpirationService.putSession(sessionId);
        this.sessionToUserService.putCustomer(sessionId, customer);

        UserDetails userDetails = new UserDetails(customer.getCustomerInfo().getPassportData().getName());
        return new AuthenticationResult(sessionId,userDetails);
    }

    public AuthenticationResult authenticateEmployee(UserCredentials credentials) throws AuthenticationException {
        String encodedPwd = ENCODER.encode(credentials.getPassword());
        Optional<Employee> optEmployee = Employee.filter()
                .aliasses(new SimpleEntry<String, String>("userCredentials","uc"))
                .where(Restrictions.eq("uc.login", credentials.getLogin()), Restrictions.eq("uc.password", encodedPwd))
                .first();
        Employee employee = optEmployee.orElseThrow(AuthenticationException::new);
        String sessionId = ENCODER.encode("employee_" + credentials.getLogin() + encodedPwd);
        this.sessionsExpirationService.putSession(sessionId);
        this.sessionToUserService.putEmployee(sessionId, employee);
        return new AuthenticationResult(sessionId, new UserDetails("Employee"));
    }

}

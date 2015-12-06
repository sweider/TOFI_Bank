package by.bsuir.sweider_b.services.authentication;

import by.bsuir.sweider_b.domain.customer.Customer;
import by.bsuir.sweider_b.domain.user.UserCredentials;
import by.bsuir.sweider_b.services.sessions.SessionToCustomerService;
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
    private final static BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private SessionsExpirationService sessionsExpirationService;

    @Autowired
    private SessionToCustomerService sessionToCustomerService;

    public UserDetails authenticateCustomer(UserCredentials credentials) throws AuthenticationException {
        String encodedPwd = ENCODER.encode(credentials.getPassword());
        Optional<Customer> optCustomer = Optional.ofNullable(
                Customer.filter()
                    .aliasses(new SimpleEntry<>("userCredentials", "uc"))
                    .where(Restrictions.eq("uc.login", credentials.getLogin()), Restrictions.eq("uc.password", encodedPwd))
                    .first()
        );
        Customer customer = optCustomer.orElseThrow(() -> new AuthenticationException("Недействительная пара 'Логин':'Пароль'"));

        String sessionId = ENCODER.encode("customer_" + credentials.getLogin() + encodedPwd);
        this.sessionsExpirationService.putSession(sessionId);
        this.sessionToCustomerService.putCustomer(sessionId, customer);

        UserDetails userDetails = new UserDetails(customer.getCustomerInfo().getPassportData().getName());
        return userDetails;
    }

}

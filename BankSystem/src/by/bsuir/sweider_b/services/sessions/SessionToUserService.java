package by.bsuir.sweider_b.services.sessions;

import by.bsuir.sweider_b.domain.customer.Customer;
import by.bsuir.sweider_b.domain.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by sweid on 06.12.2015.
 */
@Service
public class SessionToUserService {
    private final HashMap<String, Customer> customersStorage;
    private final HashMap<String, Employee> employeeStorage;

    @Autowired
    private SessionsExpirationService sessionsExpirationService;

    public SessionToUserService() {
        this.customersStorage = new HashMap<>();
        this.employeeStorage = new HashMap<>();
    }

    public void putCustomer(String sessionId, Customer customer){
        this.customersStorage.put(sessionId, customer);
    }

    public void putEmployee(String sessionId, Employee employee){
        this.employeeStorage.put(sessionId,employee);
    }

    public Optional<Customer> getCustomer(String sessionId) throws SessionExpiredException {
        return Optional.ofNullable((Customer) this.getUser(sessionId, this.customersStorage));
    }

    public Optional<Employee> getEmployee(String sessionId) throws SessionExpiredException {
        return Optional.ofNullable((Employee) this.getUser(sessionId, this.employeeStorage));
    }

    private Object getUser(String sessionId, HashMap storage) throws SessionExpiredException {
        if(this.sessionsExpirationService.checkSession(sessionId)){
            return Optional.ofNullable(storage.get(sessionId));
        }
        else{
            throw new SessionExpiredException();
        }
    }


    @PostConstruct
    private void init(){
        this.sessionsExpirationService.addSessionsExpiredListener(stream -> stream.forEach(this.customersStorage::remove));
    }



}

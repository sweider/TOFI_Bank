package by.bsuir.sweider_b.services.sessions;

import by.bsuir.sweider_b.domain.customer.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by sweid on 06.12.2015.
 */
@Service
public class SessionToCustomerService {
    private final HashMap<String, Customer> storage;

    @Autowired
    private SessionsExpirationService sessionsExpirationService;

    public SessionToCustomerService() {
        this.storage = new HashMap<>();
    }

    public void putCustomer(String sessionId, Customer customer){
        this.storage.put(sessionId, customer);
    }

    public Customer getCustomer(String sessionId) throws SessionExpiredException {
        if(this.sessionsExpirationService.checkSession(sessionId)){
            return this.storage.get(sessionId);
        }
        else{
            throw new SessionExpiredException();
        }
    }


    @PostConstruct
    private void init(){
        this.sessionsExpirationService.addSessionsExpiredListener(stream -> stream.forEach(sessionId -> this.storage.remove(sessionId)));
    }



}

package by.bsuir.sweider_b.banksystem.server.services;

import by.bsuir.sweider_b.banksystem.server.domain.customer.Customer;
import by.bsuir.sweider_b.banksystem.server.domain.employee.Employee;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Optional;

/**
 * Created by sweid on 18.01.2016.
 */
@Service
public class InitializationService  {
    private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(InitializationService.class);

    @Autowired
    private BCryptPasswordEncoder ENCODER;

    public void injectDefaultValuesInDB(){
        this.injectDefaultOperator();
        this.injectDefaultCustomer();
    }

    private void injectDefaultOperator() {
        Optional<Employee> optEmployee = Employee.filter()
                .aliasses(new AbstractMap.SimpleEntry<>("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", "operator"))
                .first();
        if(!optEmployee.isPresent()){
            String operator_pwd = ENCODER.encode("operator");
            if(new Employee("operator", operator_pwd, EmployeeRole.OPERATOR, "-", "operator", "operator","operator").save()){
                LOGGER.info("Injected default operator");
            }
            else {
                LOGGER.error("Failed to inject default operator");
            }
        }
    }

    private void injectDefaultCustomer(){
        Optional<Customer> optCustomer = Customer.filter()
                .aliasses(new AbstractMap.SimpleEntry<>("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", "customer"))
                .first();
        if(!optCustomer.isPresent()){
            String customer_pwd = ENCODER.encode("customer");
            if(new Customer("3030594A028PB8", "Александр", "Владимирович","Лиходиевский", "customer", customer_pwd,
                    "Минск", "Могилевская", "16", "72","+375256796369").save()){
                LOGGER.info("Injected default operator");
            }
            else {
                LOGGER.error("Failed to inject default operator");
            }
        }
    }
}

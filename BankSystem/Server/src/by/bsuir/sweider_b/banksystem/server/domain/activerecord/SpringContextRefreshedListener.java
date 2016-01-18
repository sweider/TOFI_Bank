package by.bsuir.sweider_b.banksystem.server.domain.activerecord;

import by.bsuir.sweider_b.banksystem.server.domain.employee.Employee;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by sweid on 06.12.2015.
 */
@Component
public class SpringContextRefreshedListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(SpringContextRefreshedListener.class);

    @Autowired
    private BCryptPasswordEncoder ENCODER;

    @EventListener
    void contextRefreshedEvent(ContextRefreshedEvent contextEvent) {
        ApplicationContext ctx = contextEvent.getApplicationContext();
        try {
            this.injectSessionFactoryToActiveRecord(ctx);
            this.injectDefaultAdmin(ctx);
            this.injectDefaultOperator(ctx);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            LOGGER.error("Failed to set session factory to active record.", ex);
        }
    }

    private void injectDefaultAdmin(ApplicationContext ctx) {
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        Optional<Employee>  optEmployee = Employee.filter()
                .aliasses(new AbstractMap.SimpleEntry<>("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", "default_admin"))
                .first();
        if(!optEmployee.isPresent()){
            String default_admin_pwd = encoder.encode("default_admin");
            if(new Employee("default_admin", default_admin_pwd, EmployeeRole.ADMIN, "-", "default_admin", "default_admin","default_admin").save()){
                LOGGER.info("Injected default admin");
            }
            else {
                LOGGER.error("Failed to inject default admin");
            }
        }
    }

    private void injectSessionFactoryToActiveRecord(ApplicationContext ctx) throws NoSuchFieldException, IllegalAccessException, BeansException, IllegalArgumentException, SecurityException {
        SessionFactory sf = ctx.getBean(SessionFactory.class);
        synchronized (ActiveRecord.class){
            Field field = ActiveRecord.class.getDeclaredField("sessionFactory");
            field.setAccessible(true);
            field.set(null, sf);
            field.setAccessible(false);
        }
    }

    private void injectDefaultOperator(ApplicationContext ctx) {
        BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        Optional<Employee>  optEmployee = Employee.filter()
                .aliasses(new AbstractMap.SimpleEntry<>("userCredentials", "uc"))
                .where(Restrictions.eq("uc.login", "operator"))
                .first();
        if(!optEmployee.isPresent()){
            String perator_pwd = encoder.encode("operator");
            if(new Employee("operator", perator_pwd, EmployeeRole.OPERATOR, "-", "operator", "operator","operator").save()){
                LOGGER.info("Injected default operator");
            }
            else {
                LOGGER.error("Failed to inject default operator");
            }
        }
    }
}
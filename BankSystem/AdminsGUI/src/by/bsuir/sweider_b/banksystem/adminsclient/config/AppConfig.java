package by.bsuir.sweider_b.banksystem.adminsclient.config;

import by.bsuir.sweider_b.banksystem.shared.services.authentication.IAdminsAuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.ICustomerAuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.credits.ICreditManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.IEmployeeManagementService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * Created by sweid on 03.01.2016.
 */
@Configuration
@ComponentScan({"by.bsuir.sweider_b.banksystem.adminsclient", "META-INF"})
public class AppConfig {

    @Bean
    public RmiProxyFactoryBean getAuthenticationService(){
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://127.0.0.1:1999/" + IAdminsAuthenticationService.SERVICE_NAME);
        factory.setServiceInterface(IAdminsAuthenticationService.class);
        return factory;
    }

    @Bean(name = "adminsAuth")
    public IAdminsAuthenticationService getAuth(){
        return (IAdminsAuthenticationService) getAuthenticationService().getObject();
    }





    @Bean
    public RmiProxyFactoryBean getEmployeeService(){
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://127.0.0.1:1999/" + IEmployeeManagementService.SERVICE_NAME);
        factory.setServiceInterface(IEmployeeManagementService.class);
        return factory;
    }

    @Bean(name = "employeeManager")
    public IEmployeeManagementService getEmployeeManager(){
        return (IEmployeeManagementService) getEmployeeService().getObject();
    }

    @Bean
    public RmiProxyFactoryBean getCreditsManagementService(){
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://127.0.0.1:1999/" + ICreditManagementService.SERVICE_NAME);
        factory.setServiceInterface(ICreditManagementService.class);
        return factory;
    }

    @Bean(name = "creditsManager")
    public ICreditManagementService getCreditsManager(){
        return (ICreditManagementService) getCreditsManagementService().getObject();
    }
}

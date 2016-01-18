package by.bsuir.sweider_b.banksystem.operatorgui.config;

import by.bsuir.sweider_b.banksystem.shared.services.authentication.IAdminsAuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.IEmployeeAuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.creditaplications.ICreditAplicatonService;
import by.bsuir.sweider_b.banksystem.shared.services.credits.ICreditManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.customers.ICustomersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * Created by sweid on 03.01.2016.
 */
@Configuration
@ComponentScan({"by.bsuir.sweider_b.banksystem.operatorgui", "META-INF"})
public class AppConfig {

    @Bean
    public RmiProxyFactoryBean getAuthenticationService(){
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://127.0.0.1:1999/" + IEmployeeAuthenticationService.SERVICE_NAME);
        factory.setServiceInterface(IEmployeeAuthenticationService.class);
        return factory;
    }

    @Bean(name = "employeeAuth")
    public IEmployeeAuthenticationService getAuth(){
        return (IEmployeeAuthenticationService) getAuthenticationService().getObject();
    }

    @Bean
    public RmiProxyFactoryBean getCustomersServiceProxy(){
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://127.0.0.1:1999/" + ICustomersService.SERVICE_NAME);
        factory.setServiceInterface(ICustomersService.class);
        return factory;
    }

    @Bean(name = "customersService")
    public ICustomersService getCustomersService(){
        return (ICustomersService) getCustomersServiceProxy().getObject();
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

    @Bean
    public RmiProxyFactoryBean getCreditsApplicationManagementService(){
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://127.0.0.1:1999/" + ICreditAplicatonService.SERVICE_NAME);
        factory.setServiceInterface(ICreditAplicatonService.class);
        return factory;
    }

    @Bean(name = "creditsApplicationService")
    public ICreditAplicatonService getCreditsApplicationService(){
        return (ICreditAplicatonService) getCreditsApplicationManagementService().getObject();
    }


}

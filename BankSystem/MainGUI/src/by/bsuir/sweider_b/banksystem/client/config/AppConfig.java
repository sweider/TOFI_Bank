package by.bsuir.sweider_b.banksystem.client.config;

import by.bsuir.sweider_b.banksystem.shared.services.authentication.ICustomerAuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * Created by sweid on 03.01.2016.
 */
@Configuration
@ComponentScan({"by.bsuir.sweider_b.banksystem.client", "META-INF"})
public class AppConfig {

    @Bean
    public RmiProxyFactoryBean getAuthenticationService(){
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceUrl("rmi://127.0.0.1:1999/" + ICustomerAuthenticationService.SERVICE_NAME);
        factory.setServiceInterface(ICustomerAuthenticationService.class);
        return factory;
    }

    @Bean(name = "authService")
    public ICustomerAuthenticationService getAuth(){
        return (ICustomerAuthenticationService) getAuthenticationService().getObject();
    }
}

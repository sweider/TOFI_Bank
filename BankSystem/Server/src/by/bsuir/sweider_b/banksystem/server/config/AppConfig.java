package by.bsuir.sweider_b.banksystem.server.config;

import by.bsuir.sweider_b.banksystem.server.services.authentication.AuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.ICustomerAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.rmi.RmiServiceExporter;

import javax.management.remote.rmi.RMIConnection;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by sweid on 06.12.2015.
 */
@Configuration
@Import({HibernateConfig.class,SecurityConfig.class})
@ComponentScan({"by.bsuir.sweider_b.banksystem.server"})
public class AppConfig {

    @Bean
    @Autowired
    public RmiServiceExporter getAuthenticationServiceExporter(AuthenticationService auth) throws RemoteException {
        RmiServiceExporter rmiExporter = new RmiServiceExporter();
        rmiExporter.setServiceName(ICustomerAuthenticationService.SERVICE_NAME);
        rmiExporter.setService(auth);
        rmiExporter.setServiceInterface(ICustomerAuthenticationService.class);
        rmiExporter.setAlwaysCreateRegistry(true);
        Registry r = LocateRegistry.createRegistry(1999);
        rmiExporter.setRegistry(r);
        return rmiExporter;
    }
}

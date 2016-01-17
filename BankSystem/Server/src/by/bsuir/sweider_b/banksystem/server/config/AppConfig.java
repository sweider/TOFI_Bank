package by.bsuir.sweider_b.banksystem.server.config;

import by.bsuir.sweider_b.banksystem.server.services.authentication.AuthenticationService;
import by.bsuir.sweider_b.banksystem.server.services.credits.CreditsManagementService;
import by.bsuir.sweider_b.banksystem.server.services.employees.EmployeesService;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.IAdminsAuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.ICustomerAuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.credits.ICreditManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.IEmployeeManagementService;
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

    @Bean(name = "clientAuth")
    @Autowired
    public RmiServiceExporter getAuthenticationServiceExporter(AuthenticationService auth) throws RemoteException {
        RmiServiceExporter rmiExporter = new RmiServiceExporter();
        rmiExporter.setServiceName(ICustomerAuthenticationService.SERVICE_NAME);
        rmiExporter.setService(auth);
        rmiExporter.setServiceInterface(ICustomerAuthenticationService.class);
        rmiExporter.setAlwaysCreateRegistry(false);
        rmiExporter.setRegistryPort(1999);
        return rmiExporter;
    }

    @Bean(name ="adminAuth")
    @Autowired
    public RmiServiceExporter exportAdminsAuthetication(AuthenticationService auth) throws RemoteException {
        RmiServiceExporter rmiExporter = new RmiServiceExporter();
        rmiExporter.setServiceName(IAdminsAuthenticationService.SERVICE_NAME);
        rmiExporter.setService(auth);
        rmiExporter.setServiceInterface(IAdminsAuthenticationService.class);
        rmiExporter.setAlwaysCreateRegistry(false);
        rmiExporter.setRegistryPort(1999);
        return rmiExporter;
    }

    @Bean(name = "employeeManager")
    @Autowired
    public RmiServiceExporter exportEmployeeManager(EmployeesService service){
        RmiServiceExporter rmiExporter = new RmiServiceExporter();
        rmiExporter.setServiceName(IEmployeeManagementService.SERVICE_NAME);
        rmiExporter.setService(service);
        rmiExporter.setServiceInterface(IEmployeeManagementService.class);
        rmiExporter.setAlwaysCreateRegistry(false);
        rmiExporter.setRegistryPort(1999);
        return rmiExporter;
    }

    @Bean(name = "creditsManager")
    @Autowired
    public RmiServiceExporter exportCreditsManager(CreditsManagementService service){
        RmiServiceExporter rmiExporter = new RmiServiceExporter();
        rmiExporter.setServiceName(ICreditManagementService.SERVICE_NAME);
        rmiExporter.setService(service);
        rmiExporter.setServiceInterface(ICreditManagementService.class);
        rmiExporter.setAlwaysCreateRegistry(false);
        rmiExporter.setRegistryPort(1999);
        return rmiExporter;
    }
}

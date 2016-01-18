package by.bsuir.sweider_b.banksystem.shared.services.creditaplications;

import java.rmi.RemoteException;

/**
 * Created by sweid on 18.01.2016.
 */
public interface ICreditAplicatonService {
    String SERVICE_NAME = "ICreditAplicationsService";

    void registrateNewCreditApplication(String sessionId, CreditApplicationDO aplication) throws RemoteException, CreditApplicationCreationException;
}

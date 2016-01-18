package by.bsuir.sweider_b.banksystem.shared.services.credits;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by sweid on 17.01.2016.
 */
public interface ICreditManagementService {
    String SERVICE_NAME = "CreditsManagementService";
    void createCreditKind(String sessionId, String title, String description, int length, long minSum,
                          long maxSum, int percents) throws RemoteException, CreditCreationException;

    void updateCreditKind(String sessionId, int creditId, String newDescription) throws RemoteException, CreditUpdateException;

    List<CreditKindDO> getCreditKinds(String sessionId, boolean active) throws RemoteException;

    void changeCreditKindActiveState(String sessionId, int creditId, boolean newState) throws RemoteException, CreditUpdateException;
}

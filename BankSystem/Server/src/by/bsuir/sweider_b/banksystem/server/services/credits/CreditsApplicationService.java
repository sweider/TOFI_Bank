package by.bsuir.sweider_b.banksystem.server.services.credits;

import by.bsuir.sweider_b.banksystem.server.domain.credit.CreditKind;
import by.bsuir.sweider_b.banksystem.server.domain.creditapplication.CreditApplication;
import by.bsuir.sweider_b.banksystem.server.domain.customer.Customer;
import by.bsuir.sweider_b.banksystem.shared.services.creditaplications.CreditApplicationCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.creditaplications.CreditApplicationDO;
import by.bsuir.sweider_b.banksystem.shared.services.creditaplications.ICreditAplicatonService;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;

/**
 * Created by sweid on 18.01.2016.
 */
@Service
public class CreditsApplicationService implements ICreditAplicatonService{
    @Override
    public void registrateNewCreditApplication(String sessionId, CreditApplicationDO aplication) throws RemoteException, CreditApplicationCreationException {
        CreditKind creditKind = CreditKind.find(aplication.getCreditKindId()).orElseThrow(() -> new CreditApplicationCreationException("Не найден запрошенный кредитный план!"));
        Customer customer = Customer.find(aplication.getCustomerId()).orElseThrow(() -> new CreditApplicationCreationException("Не найден указанный пользователь!"));
        if(!new CreditApplication(aplication.getSum(), aplication.getIncome(), creditKind, customer).save()){
            throw new CreditApplicationCreationException("Произошла непредвиденная ошибка при сохранении заявки! Побробуйте еще раз.");
        }
    }
}

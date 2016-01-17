package by.bsuir.sweider_b.banksystem.server.services.credits;

import by.bsuir.sweider_b.banksystem.server.domain.credit.CreditKind;
import by.bsuir.sweider_b.banksystem.server.domain.credit.CreditObligation;
import by.bsuir.sweider_b.banksystem.shared.services.credits.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sweid on 17.01.2016.
 */
@Service
public class CreditsManagementService implements ICreditManagementService {

    @Override
    public void createCredit(String sessionId, String title, String description, int length, long minSum,
                             long maxSum, boolean prepayment, PaymentType type) throws RemoteException, CreditCreationException {
        if(CreditKind.filter().where(Restrictions.eq("name", title)).first().isPresent()) throw new CreditCreationException("Кредит с таки названием уже существует!");
        CreditKind newCredit = new CreditKind(title, description, length, minSum,maxSum, prepayment, type);
        if(!newCredit.save()){
            throw new CreditCreationException("Произошла непредвиденная ошибка при создании кредита, попробуйте еще раз!");
        }
    }

    @Override
    public void updateCredit(String sessionId, int creditId, String newDescription) throws RemoteException, CreditUpdateException {
        CreditKind credit = CreditKind.find(creditId).orElseThrow(() -> new CreditUpdateException("Кредит не найден!"));
        credit.setDescription(newDescription);
        if(!credit.save()){
            throw new CreditUpdateException("Произошла непредвиненная ошибка, попробуйте еще раз");
        }
    }

    @Override
    public List<CreditShowObject> getCredits(String sessionId, boolean active) throws RemoteException {
        List<CreditKind> creditKinds = CreditKind.filter().where(Restrictions.eq("isActive", active)).get();
        return creditKinds.stream().map(
                credit -> new CreditShowObject(credit.getId(), credit.getName(), credit.getDescription(),credit.getDurationInMonth(),
                credit.getMinMoneyAmount(), credit.getMaxMonewAmount(),
                        CreditObligation.filter()
                                        .aliasses(new AbstractMap.SimpleEntry("creditKind","ck"))
                                        .where(Restrictions.eq("ck.id", credit.getId()))
                                        .get()
                                        .size())
                ).collect(Collectors.toList());
    }

    @Override
    public void changeCreditActiveState(String sessionId, int creditId, boolean newState) throws RemoteException, CreditUpdateException {
        CreditKind credit = CreditKind.find(creditId).orElseThrow( () -> new CreditUpdateException("Кредит не найден!"));
        credit.setActive(newState);
        if(!credit.save()){
            throw new CreditUpdateException("При обновлении кредита произошла непредвиденная ошибка, попробуйте еще раз");
        }
    }

}

package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers.workwithcustomer;

import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerDO;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 18.01.2016.
 */
@Component
public class WorkWithCustomerRootPane extends BorderPane {

    private CustomerDO currentCustomer;

    private UserChoosingPane userChoosingPane;

    private NewCreditApplicationPane newCreditApplicationPane;

    @Autowired
    public WorkWithCustomerRootPane(UserChoosingPane userChoosingPane, NewCreditApplicationPane newCreditApplicationPane){
        this.userChoosingPane = userChoosingPane;
        this.newCreditApplicationPane = newCreditApplicationPane;
        this.setLeft(this.userChoosingPane);
    }

    protected void activateCreditChoosingPartForCustomer(CustomerDO customer){
        this.currentCustomer = customer;
        this.newCreditApplicationPane.refresh();
        this.setLeft(this.newCreditApplicationPane);
    }

    protected void goBackToUserChoosing(){
        this.currentCustomer = null;
        this.setLeft(this.userChoosingPane);
    }

    protected CustomerDO getCurrentCustomer(){
        return this.currentCustomer;
    }

    protected void finishCreditApplicationCreation(){
        this.userChoosingPane.refresh();
        this.setLeft(this.userChoosingPane);
    }
}

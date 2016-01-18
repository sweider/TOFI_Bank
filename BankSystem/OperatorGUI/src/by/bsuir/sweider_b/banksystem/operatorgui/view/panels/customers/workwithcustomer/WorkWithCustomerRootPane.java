package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers.workwithcustomer;

import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 18.01.2016.
 */
@Component
public class WorkWithCustomerRootPane extends BorderPane {

    private UserChoosingPane userChoosingPane;

    @Autowired
    public WorkWithCustomerRootPane(UserChoosingPane userChoosingPane){
        this.userChoosingPane = userChoosingPane;
        this.setLeft(this.userChoosingPane);
    }
}

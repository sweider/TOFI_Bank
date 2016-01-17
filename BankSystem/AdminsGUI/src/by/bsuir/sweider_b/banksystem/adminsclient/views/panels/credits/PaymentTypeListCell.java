package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.credits;

import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import by.bsuir.sweider_b.banksystem.shared.services.credits.PaymentType;
import javafx.scene.control.ListCell;

/**
 * Created by sweid on 17.01.2016.
 */
public class PaymentTypeListCell extends ListCell<PaymentType> {
    @Override protected void updateItem(PaymentType item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getUfName());
        }
    }
}
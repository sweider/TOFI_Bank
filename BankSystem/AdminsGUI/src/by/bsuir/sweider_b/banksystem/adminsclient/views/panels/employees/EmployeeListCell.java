package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees;

import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import javafx.scene.control.ListCell;

/**
 * Created by sweid on 17.01.2016.
 */
public class EmployeeListCell extends ListCell<EmployeeRole> {
    @Override protected void updateItem(EmployeeRole item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getUserFriendlyName());
        }
    }
}
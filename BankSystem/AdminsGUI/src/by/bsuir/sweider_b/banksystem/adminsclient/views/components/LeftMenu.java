package by.bsuir.sweider_b.banksystem.adminsclient.views.components;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.Delegate;
import javafx.scene.control.Accordion;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by sweid on 14.01.2016.
 */
@Component
public class LeftMenu extends Accordion {

    public LeftMenu() {
        this.getStyleClass().add("left-menu");
        HashMap<String, Delegate> test = new HashMap<>();
        test.put("Создать пользователя", AdministrationApp.RUNNING_INSTANCE::activateNewUserPage);


        LeftMenuAccordionPane pane = new LeftMenuAccordionPane("Управление\nпользователями", test);
        MenuItem users = new MenuItem("Управление\nпользователями");
        users.setOnMouseClicked(event -> AdministrationApp.RUNNING_INSTANCE.activateNewUserPage());

        MenuItem credits = new MenuItem("Управление\nкредитами");
        credits.setOnMouseClicked(event -> AdministrationApp.RUNNING_INSTANCE.activateCreditsManagementsPage());


        this.getPanes().addAll(pane);
    }
}

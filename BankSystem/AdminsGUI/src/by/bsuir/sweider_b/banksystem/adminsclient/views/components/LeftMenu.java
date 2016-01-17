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
        LeftMenuAccordionPane pane = getUsersPane();
        MenuItem users = new MenuItem("Управление\nпользователями");
        users.setOnMouseClicked(event -> AdministrationApp.RUNNING_INSTANCE.activateNewUserPage());

        MenuItem credits = new MenuItem("Управление\nкредитами");
        credits.setOnMouseClicked(event -> AdministrationApp.RUNNING_INSTANCE.activateCreditsManagementsPage());


        this.getPanes().addAll(pane, getCreditsPane());
    }

    private LeftMenuAccordionPane getUsersPane() {
        HashMap<String, Delegate> test = new HashMap<>();
        test.put("Создать пользователя", AdministrationApp.RUNNING_INSTANCE::activateNewUserPage);
        test.put("Просмотреть\nпользователей", AdministrationApp.RUNNING_INSTANCE::activateShowUsersPage);
        return new LeftMenuAccordionPane("Управление\nпользователями", test);
    }

    private LeftMenuAccordionPane getCreditsPane() {
        HashMap<String, Delegate> test = new HashMap<>();
        test.put("Создать кредит", AdministrationApp.RUNNING_INSTANCE::activateNewCreditPage);
        test.put("Просмотреть\nактивные кредиты", AdministrationApp.RUNNING_INSTANCE::activateShowActiveCreditsPage);
        test.put("Просмотреть\nнеактивные кредиты", AdministrationApp.RUNNING_INSTANCE::activateShowDeactivatedCreditsPage);
        return new LeftMenuAccordionPane("Управление\nкредитами", test);
    }
}

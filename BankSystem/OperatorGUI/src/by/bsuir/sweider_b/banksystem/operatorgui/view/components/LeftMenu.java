package by.bsuir.sweider_b.banksystem.operatorgui.view.components;

import by.bsuir.sweider_b.banksystem.operatorgui.OperatorApp;
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

        this.getPanes().addAll(getUsersPane());
    }

    private LeftMenuAccordionPane getUsersPane() {
        HashMap<String, Delegate> test = new HashMap<>();
        test.put("Новый клиент", OperatorApp.RUNNING_INSTANCE::activateNewCustomerPage);
        test.put("Работа с клиентом", OperatorApp.RUNNING_INSTANCE::activateWorkWithCustomerPage);
        return new LeftMenuAccordionPane("Работа с клиентами", test);
    }

 }

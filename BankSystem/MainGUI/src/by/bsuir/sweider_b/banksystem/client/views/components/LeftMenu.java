package by.bsuir.sweider_b.banksystem.client.views.components;

import by.bsuir.sweider_b.banksystem.client.MainApp;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 14.01.2016.
 */
@Component
public class LeftMenu extends VBox{

    public LeftMenu() {
        this.getStyleClass().add("left-menu");

        MenuItem home = new MenuItem("Домашняя\nстраница");
        home.setOnMouseClicked(event -> MainApp.RUNNING_INSTANCE.activateHomePage());

        MenuItem myCred = new MenuItem("Мои кредиты");
        myCred.setOnMouseClicked(event -> MainApp.RUNNING_INSTANCE.activateMyCreditsPage());

        MenuItem newCred = new MenuItem("Новая заявка");
        newCred.setOnMouseClicked(event -> MainApp.RUNNING_INSTANCE.activateNewCreditsPage());

        MenuItem check = new MenuItem("Просмотреть\nзаявки");
        check.setOnMouseClicked(event -> MainApp.RUNNING_INSTANCE.activateActiveRequestsPage());

        this.getChildren().addAll(
                home,
                new Separator(),
                myCred,
                new Separator(),
                check,
                new Separator(),
                newCred,
                new Separator()
        );
    }
}

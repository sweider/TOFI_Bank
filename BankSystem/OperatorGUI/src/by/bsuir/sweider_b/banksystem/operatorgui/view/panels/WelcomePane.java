package by.bsuir.sweider_b.banksystem.operatorgui.view.panels;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 18.01.2016.
 */
@Component
public class WelcomePane extends VBox {

    public WelcomePane(){
        Label header = new Label("Добро пожаловать в операторский модуль банка!");
        header.getStyleClass().add("page-header");
        header.setWrapText(true);

        Label text = new Label("Для работы воспользуйтесь соответствующими ссылками на левой панели.");
        text.getStyleClass().add("welcome-page_text");
        text.setWrapText(true);
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.getChildren().addAll(header, text);
    }
}

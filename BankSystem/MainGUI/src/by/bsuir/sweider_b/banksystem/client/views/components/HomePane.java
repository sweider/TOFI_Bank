package by.bsuir.sweider_b.banksystem.client.views.components;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 14.01.2016.
 */
@Component
public class HomePane extends Pane {
    public HomePane(){
        Label label = new Label("I am Home pane");
        this.getChildren().add(label);
    }
}

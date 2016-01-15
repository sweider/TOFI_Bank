package by.bsuir.sweider_b.banksystem.client.views.components;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 14.01.2016.
 */
@Component
public class MyCreditsPane extends FlowPane {
    public MyCreditsPane() {
        this.getStyleClass().add("my-credits");
        this.setVgap(4);
        this.setHgap(4);
        this.getChildren().addAll(new CreditView(), new CreditView(), new CreditView());
    }
}

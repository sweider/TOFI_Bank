package by.bsuir.sweider_b.banksystem.client.views.components;

import javafx.scene.layout.FlowPane;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 14.01.2016.
 */
@Component
public class NewRequestPane extends FlowPane {

    public NewRequestPane(){
        this.getStyleClass().add("new-credit");
        this.setVgap(4);
        this.setHgap(4);
        refreshData();
    }

    public void refreshData() {
        this.getChildren().addAll(new CreditView(), new CreditView(), new CreditView());
    }

}

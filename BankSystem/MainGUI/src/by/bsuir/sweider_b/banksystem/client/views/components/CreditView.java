package by.bsuir.sweider_b.banksystem.client.views.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Created by sweid on 15.01.2016.
 */
public class CreditView extends BorderPane {
    private static int counter = 1;
    private Label topLabel;
    private VBox topContainer;

    public CreditView(){
        this.setUpTop();
        this.setUpCreditDescription();

        FlowPane botContainer = new FlowPane();
        botContainer.setOrientation(Orientation.HORIZONTAL);
        botContainer.setAlignment(Pos.CENTER_RIGHT);
        botContainer.setPadding(new Insets(5,5,5,5));
        Button getBtn = new Button("Оформить заявку");
        botContainer.getChildren().add(getBtn);
        this.setBottom(botContainer);

        this.getStyleClass().add("credit-view");
    }

    private void setUpCreditDescription() {
        ListView<String> centerView = new ListView<>();
        centerView.setEditable(false);
        centerView.setDisable(true);
        ObservableList<String> data = FXCollections.observableArrayList();
        data.addAll(
                "Условия кредита:",
                "  - от 1 года до 3х лет",
                "  - до 50 млн. рублей",
                "  - досрочное погашение",
                "  - ежемесячная уплата процентов"
        );
        centerView.setItems(data);
        centerView.getStyleClass().add("credit-view_list-view");

        this.setCenter(centerView);
    }

    private VBox setUpTop() {
        topLabel = new Label("Лучший онлайн12 121 12 12121212 1212 12 12 12  кредит №" + counter++);
        topLabel.setWrapText(true);
        topLabel.getStyleClass().add("credit-view_top-label");
        topContainer = new VBox(topLabel);
        topContainer.getStyleClass().add("credit-view_top-container");
        this.setTop(topContainer);
        return topContainer;
    }
}

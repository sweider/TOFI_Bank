package by.bsuir.sweider_b.banksystem.client.views.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by sweid on 14.01.2016.
 */
public class MenuItem extends HBox{
    public MenuItem(){}

    public MenuItem(String text){
        this.initialize(text);
    }

    private void initialize(String text){
        Label label = new Label(text);
        label.getStyleClass().add("left-menu-item_label");
        this.setMinHeight(30);
        this.getStyleClass().add("left-menu-item");
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().add(label);
        this.setMargin(label, new Insets(0,10,0,10));
    }


}

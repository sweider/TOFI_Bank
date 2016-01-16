package by.bsuir.sweider_b.banksystem.adminsclient.views.panels;

import by.bsuir.sweider_b.banksystem.adminsclient.views.components.LeftMenu;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sweid on 16.01.2016.
 */
@Component
public class RootPane extends BorderPane {
    @Autowired
    public RootPane(LeftMenu leftMenu){
        this.setMinSize(800, 600);
        this.setPrefSize(1360, 768);
        this.setLeft(leftMenu);
    }
}

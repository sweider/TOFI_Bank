package by.bsuir.sweider_b.banksystem.client.views.authentication;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * Created by sweid on 03.01.2016.
 */
public class AuthenticationFormController {
    @FXML
    private TextField login;
    @FXML
    private TextField password;


    @FXML
    private void initialize() {
    }

    @FXML
    private void onAuthenticationBtnClick(MouseEvent event){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Hello, bro");
        alert.showAndWait();
    }
}

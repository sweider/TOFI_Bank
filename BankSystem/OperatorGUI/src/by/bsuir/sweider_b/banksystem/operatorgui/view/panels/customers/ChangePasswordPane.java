package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers;

import by.bsuir.sweider_b.banksystem.operatorgui.OperatorApp;
import by.bsuir.sweider_b.banksystem.operatorgui.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerUpdatingException;
import by.bsuir.sweider_b.banksystem.shared.services.customers.ICustomersService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.UpdatingException;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by sweid on 17.01.2016.
 */
@Component
public class ChangePasswordPane extends VBox{
    @Resource(name = "customersService")
    private ICustomersService customersService;

    @Autowired
    private CurrentSessionHolder sessionHolder;

    private ArrayList<String> validationErrors;
    private PasswordField pwdField;
    private PasswordField confField;

    private int employeeId;
    private final Button createBtn;

    public ChangePasswordPane(){
        this.validationErrors = new ArrayList<>();

        Label header = new Label("Изменение пароля");
        header.getStyleClass().add("page-header");

        createBtn = new Button("Изменить");
        createBtn.setOnMouseClicked(event ->  sendChangePwdRequest());

        this.setSpacing(5);
        this.setPadding(new Insets(10,10,10,10));

        this.getChildren().addAll(
                header,
                getPwdGroup(),
                getConfGroup(),
                createBtn
        );
    }

    private VBox getConfGroup() {
        Label confLabel = new Label("Подтверждение пароля");
        this.confField = new PasswordField();
        this.confField.setPromptText("Введите пароль еще раз");
        return new VBox(confLabel, this.confField);
    }

    private VBox getPwdGroup() {
        Label pwdLabel = new Label("Пароль");
        this.pwdField = new PasswordField();
        this.pwdField.setPromptText("Введите пароль");
        return new VBox(pwdLabel, this.pwdField);
    }

    private void sendChangePwdRequest(){
        this.blockControls(true);
        if(this.isDataValid()){
            try {
                this.customersService.changePasswordForCustomer(sessionHolder.getSessionId(),this.employeeId, this.pwdField.getText());
                this.showSuccessMsg();
                this.clearState();
                OperatorApp.APP_CONTEXT.getBean(ShowCustomersPanel.class).activateMainPane();
            } catch (RemoteException e) {
                OperatorApp.showRmiExceptionWarning();
                e.printStackTrace();
            } catch (CustomerUpdatingException e) {
                showUpdatingErrorAlert(e);
            }
        }
        else{
            showFormFillingErrorsAlert();
        }
        this.blockControls(false);
    }

    private void showSuccessMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Завершено");
        alert.setHeaderText("Успех!");
        alert.setContentText("Пароль успешно изменен");
        alert.showAndWait();
    }

    private void showUpdatingErrorAlert(CustomerUpdatingException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось изменить пароль клиента");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void showFormFillingErrorsAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("При заполнении формы были допущены ошибки");
        String errorMsg = this.validationErrors.stream().reduce((s, s2) ->  s2 + "\n" + s ).get();
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    private boolean isDataValid() {
        validationErrors.clear();
        if(this.pwdField.getText().isEmpty()){
            validationErrors.add("Пароль не должен быть пустым!");
        }
        else {
            if(this.pwdField.getText().length() < 6){
                validationErrors.add("Пароль должен быть не менее 6 символов!");
            }
            if (!this.pwdField.getText().equals(this.confField.getText())) {
                validationErrors.add("Введенные пароли не совпадают!");
            }
        }
        return this.validationErrors.isEmpty();
    }

    private void clearState(){
        this.validationErrors.clear();
        this.pwdField.clear();
        this.confField.clear();
    }

    private void blockControls(boolean value){
        this.pwdField.setDisable(value);
        this.confField.setDisable(value);
        this.createBtn.setDisable(value);
    }

    public void setEmployeeId(int id){
        this.employeeId = id;
    }
}

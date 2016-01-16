package by.bsuir.sweider_b.banksystem.adminsclient.views.panels;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import by.bsuir.sweider_b.banksystem.shared.services.employee.EmployeeCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.employee.IEmployeeManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.NewEmployeeData;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by sweid on 16.01.2016.
 */
@Component
public class NewUserPane extends BorderPane {
    @Resource(name = "employeeManager")
    private IEmployeeManagementService employeeManagementService;

    @Autowired
    private CurrentSessionHolder sessionHolder;

    private TextField loginField;
    private PasswordField pwdField;
    private PasswordField confField;
    private ComboBox<EmployeeRole> roleField;
    private final Label header;
    private ArrayList<String> validationErrors;

    public NewUserPane(){
        this.validationErrors = new ArrayList<>();
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(5,5,5,30));
        vbox.getStyleClass().add("new-user-form");
        vbox.setSpacing(5);

        header = new Label("Новый пользователь");
        header.getStyleClass().add("new-user-form_header");

        VBox loginGroup = getLoginGroup();

        VBox pwdGroup = getPwdGroup();

        VBox confGroup = getConfGroup();
        
        VBox roleGroup = getRoleGroup();

        Button createBtn = new Button("Создать");
        createBtn.setOnMouseClicked(event ->  sendCreateRequest());

        vbox.getChildren().addAll(
                header,
                loginGroup,
                pwdGroup,
                confGroup,
                roleGroup,
                createBtn
        );
        vbox.setMaxWidth(500);

        this.setLeft(vbox);
    }

    private VBox getRoleGroup() {
        Label label = new Label("Должность");
        this.roleField = new ComboBox<>();
        this.roleField.setCellFactory(p -> new EmployeeListCell());
        this.roleField.setButtonCell(new EmployeeListCell());
        this.roleField.getItems().addAll(EmployeeRole.values());
        this.roleField.setValue(EmployeeRole.OPERATOR);
        this.roleField.prefWidthProperty().bind(header.widthProperty());
        this.roleField.setPrefHeight(30);
        return new VBox(label, this.roleField);
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

    private VBox getLoginGroup() {
        Label loginLbl = new Label("Логин");
        this.loginField = new TextField();
        this.loginField.setPromptText("Введите логин");
        return new VBox(loginLbl, this.loginField);
    }

    private static class EmployeeListCell extends ListCell<EmployeeRole>{
        @Override protected void updateItem(EmployeeRole item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
                setText(item.getUserFriendlyName());
            }
        }
    }

    private void sendCreateRequest(){
        if(this.isDataValid()){
            NewEmployeeData data = new NewEmployeeData(this.loginField.getText(), this.pwdField.getText(), this.roleField.getValue());
            try {
                this.employeeManagementService.createEmployee(sessionHolder.getSessionId(), data);
                showSuccessMsg();
                this.clearState();
            } catch (RemoteException e) {
                AdministrationApp.showRmiExceptionWarning();
                e.printStackTrace();
            } catch (EmployeeCreationException e) {
                showCreationErrorAlert(e);
            }
        }
        else{
            showFormFillingErrorsAlert();
        }
    }

    private void showSuccessMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Завершено");
        alert.setHeaderText("Успех!");
        alert.setContentText("Пользователь успешно создан");
        alert.showAndWait();
    }

    private void showCreationErrorAlert(EmployeeCreationException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось создать пользователя");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void showFormFillingErrorsAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("При заполнении формы были допущены ошибки");
        String errorMsg = this.validationErrors.stream().reduce((s, s2) ->  s2 + s + "\n").get();
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    private boolean isDataValid() {
        validationErrors.clear();
        if(!this.pwdField.getText().equals(this.confField.getText())){
            validationErrors.add("Введенные пароли не совпадают!");
        }

        if(this.pwdField.getText().isEmpty() || this.loginField.getText().isEmpty()){
            validationErrors.add("Все поля должны быть заполнены!");
        }
        return this.validationErrors.isEmpty();
    }

    private void clearState(){
        this.validationErrors.clear();
        this.loginField.clear();
        this.pwdField.clear();
        this.confField.clear();
        this.roleField.setValue(EmployeeRole.OPERATOR);
    }
}

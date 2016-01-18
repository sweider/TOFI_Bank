package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import by.bsuir.sweider_b.banksystem.shared.services.employee.EmployeeCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.employee.IEmployeeManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.NewEmployeeDO;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Label header;
    private ArrayList<String> validationErrors;
    private TextField nameField;
    private TextField lastNameField;
    private TextField surNameField;
    private TextField passportField;
    private Button createBtn;

    public NewUserPane(){
        this.validationErrors = new ArrayList<>();
        VBox vbox = getEmployeeForm();

        this.setLeft(vbox);
    }

    private VBox getEmployeeForm() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(5,5,5,30));
        vbox.getStyleClass().add("new-user-form");
        vbox.setSpacing(5);

        header = new Label("Новый пользователь");
        header.getStyleClass().add("new-user-form_header");

        VBox pwdGroup = getPwdGroup();

        VBox confGroup = getConfGroup();

        createBtn = new Button("Создать");
        createBtn.setDefaultButton(true);
        createBtn.setOnMouseClicked(event ->  sendCreateRequest());

        vbox.getChildren().addAll(
                header,
                getLoginGroup(),
                pwdGroup,
                confGroup,
                getRoleGroup(),
                getNameGroup(),
                getLastNameGroup(),
                getSurNameGroup(),
                getPassportGroup(),
                createBtn
        );
        vbox.setMaxWidth(500);
        return vbox;
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


    private VBox getNameGroup() {
        Label loginLbl = new Label("Имя");
        this.nameField = new TextField();
        this.nameField.setPromptText("Введите имя");
        return new VBox(loginLbl, this.nameField);
    }

    private VBox getLastNameGroup() {
        Label loginLbl = new Label("Фамилия");
        this.lastNameField = new TextField();
        this.lastNameField.setPromptText("Введите фамилию");
        return new VBox(loginLbl, this.lastNameField);
    }

    private VBox getSurNameGroup() {
        Label loginLbl = new Label("Отчество");
        this.surNameField = new TextField();
        this.surNameField.setPromptText("Введите отчество");
        return new VBox(loginLbl, this.surNameField);
    }

    private VBox getPassportGroup() {
        Label loginLbl = new Label("Номер паспорта");
        this.passportField = new TextField();
        this.passportField.setPromptText("Введите номер паспорта");
        return new VBox(loginLbl, this.passportField);
    }


    private void sendCreateRequest(){
        this.blockControls(true);
        if(this.isDataValid()){
            NewEmployeeDO data = new NewEmployeeDO(this.loginField.getText(), this.pwdField.getText(), this.roleField.getValue(),
                    this.passportField.getText(), this.nameField.getText(), this.surNameField.getText(), this.lastNameField.getText());
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
        this.blockControls(false);
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
        this.nameField.clear();
        this.lastNameField.clear();
        this.surNameField.clear();
        this.passportField.clear();
    }

    private void blockControls(boolean value){
        this.loginField.setDisable(value);
        this.pwdField.setDisable(value);
        this.confField.setDisable(value);
        this.roleField.setDisable(value);
        this.nameField.setDisable(value);
        this.lastNameField.setDisable(value);
        this.surNameField.setDisable(value);
        this.passportField.setDisable(value);
        this.createBtn.setDisable(value);
    }
}

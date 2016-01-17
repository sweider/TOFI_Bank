package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import by.bsuir.sweider_b.banksystem.shared.services.employee.EmployeeShowDO;
import by.bsuir.sweider_b.banksystem.shared.services.employee.IEmployeeManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.UpdatingException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
public class ChangeEmployeeDataPane extends VBox {
    @Resource(name = "employeeManager")
    private IEmployeeManagementService employeeManagementService;

    @Autowired
    private CurrentSessionHolder sessionHolder;

    private ArrayList<String> validationErrors;
    private TextField nameField;
    private TextField lastNameField;
    private TextField surNameField;
    private TextField passportField;
    private TextField loginField;
    private ComboBox<EmployeeRole> roleField;

    private EmployeeShowDO employee;
    private Button changeBtn;
    private final Label header;

    public ChangeEmployeeDataPane() {
        this.validationErrors = new ArrayList<>();

        header = new Label("Изменение данных");
        header.getStyleClass().add("page-header");

        this.setSpacing(5);
        this.setPadding(new Insets(10, 10, 10, 10));

        this.getChildren().addAll(
                header,
                getLoginGroup(),
                getRoleGroup(),
                getNameGroup(),
                getLastNameGroup(),
                getSurNameGroup(),
                getPassportGroup(),
                getButtonsGroup()
        );
    }

    private HBox getButtonsGroup() {
        changeBtn = new Button("Изменить");
        changeBtn.setDefaultButton(true);
        changeBtn.setOnMouseClicked(event -> sendChangeDataRequest());

        Button backBtn = new Button("Назад");
        backBtn.setOnMouseClicked(event -> returnToShowPane());


        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(changeBtn, backBtn);
        return  hbox;
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

    private void sendChangeDataRequest() {
        this.blockControls(true);
        if (this.isDataValid()) {
            try {
                EmployeeShowDO updateData = new EmployeeShowDO(this.employee.getId(), this.loginField.getText(),
                        this.roleField.getValue(), this.nameField.getText(), this.lastNameField.getText(),
                        this.surNameField.getText(), this.passportField.getText());
                this.employeeManagementService.updateEmployeeData(sessionHolder.getSessionId(),  updateData);
                this.showSuccessMsg();
                this.clearState();
                AdministrationApp.APP_CONTEXT.getBean(ShowUsersPanel.class).updateValueFor(this.employee, updateData);
                this.returnToShowPane();
            } catch (RemoteException e) {
                AdministrationApp.showRmiExceptionWarning();
                e.printStackTrace();
            } catch (UpdatingException e) {
                showUpdatingErrorAlert(e);
            }
        } else {
            showFormFillingErrorsAlert();
        }
        this.blockControls(false);
    }

    private void returnToShowPane() {
        AdministrationApp.APP_CONTEXT.getBean(ShowUsersPanel.class).activateMainPane();
    }

    private void showSuccessMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Завершено");
        alert.setHeaderText("Успех!");
        alert.setContentText("Данные успешно изменены");
        alert.showAndWait();
    }

    private void showUpdatingErrorAlert(UpdatingException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось изменить данные пользователя");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void showFormFillingErrorsAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("При заполнении формы были допущены ошибки");
        String errorMsg = this.validationErrors.stream().reduce((s, s2) -> s2 + s + "\n").get();
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    private boolean isDataValid() {
        validationErrors.clear();
        new Alert(Alert.AlertType.WARNING, "Валидация формы не реализована!");
        return this.validationErrors.isEmpty();
    }

    private void clearState() {
        this.validationErrors.clear();
        this.loginField.clear();
        this.nameField.clear();
        this.surNameField.clear();
        this.lastNameField.clear();
        this.roleField.setValue(EmployeeRole.OPERATOR);
    }

    private void blockControls(boolean value) {
        this.loginField.setDisable(value);
        this.nameField.setDisable(value);
        this.surNameField.setDisable(value);
        this.lastNameField.setDisable(value);
        this.roleField.setDisable(value);
        this.changeBtn.setDisable(value);
    }

    public void setEmployee(EmployeeShowDO data) {
        this.employee = data;

        this.loginField.setText(this.employee.getLogin());
        this.nameField.setText(this.employee.getName());
        this.lastNameField.setText(this.employee.getLastName());
        this.surNameField.setText(this.employee.getSurName());
        this.roleField.setValue(this.employee.getRole());
        this.passportField.setText(this.employee.getPassportNmbr());
    }
}

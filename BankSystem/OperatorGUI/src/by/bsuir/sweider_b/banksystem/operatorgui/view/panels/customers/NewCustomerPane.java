package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers;

import by.bsuir.sweider_b.banksystem.operatorgui.OperatorApp;
import by.bsuir.sweider_b.banksystem.operatorgui.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.operatorgui.view.components.RussianTextOnlyField;
import by.bsuir.sweider_b.banksystem.shared.Patterns;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerDO;
import by.bsuir.sweider_b.banksystem.shared.services.customers.ICustomersService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by sweid on 16.01.2016.
 */
@Component
public class NewCustomerPane extends BorderPane {
    @Resource(name = "customersService")
    private ICustomersService customersService;

    @Autowired
    private CurrentSessionHolder sessionHolder;

    private Label header;
    private ArrayList<String> validationErrors;

    private TextField loginField;
    private PasswordField pwdField;
    private PasswordField confField;
    private RussianTextOnlyField nameField;
    private RussianTextOnlyField lastNameField;
    private RussianTextOnlyField surNameField;
    private TextField passportField;
    private RussianTextOnlyField cityField;
    private RussianTextOnlyField streetField;
    private TextField buildingField;
    private TextField roomField;
    private TextField phoneNumberField;
    private Button createBtn;



    public NewCustomerPane(){
        this.validationErrors = new ArrayList<>();
        VBox vbox = getCustomersForm();

        this.setLeft(vbox);
    }

    private VBox getCustomersForm() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(5,5,5,30));
        vbox.getStyleClass().add("new-user-form");
        vbox.setSpacing(5);

        header = new Label("Новый клиент");
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
                getNameGroup(),
                getLastNameGroup(),
                getSurNameGroup(),
                getPassportGroup(),
                getCityGroup(),
                getStreetGroup(),
                getBuildingGroup(),
                getRoomGroup(),
                getPhoneGroup(),
                createBtn
        );
        vbox.setMaxWidth(500);
        return vbox;
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
    private VBox getPhoneGroup() {
        Label loginLbl = new Label("Номер телефона");
        this.phoneNumberField = new TextField();
        this.phoneNumberField.setPromptText("Введите номер телефона");
        return new VBox(loginLbl, this.phoneNumberField);
    }

    private VBox getLoginGroup() {
        Label loginLbl = new Label("Логин");
        this.loginField = new TextField();
        this.loginField.setPromptText("Введите логин");
        return new VBox(loginLbl, this.loginField);
    }


    private VBox getNameGroup() {
        Label loginLbl = new Label("Имя");
        this.nameField = new RussianTextOnlyField();
        this.nameField.setPromptText("Введите имя");
        return new VBox(loginLbl, this.nameField);
    }

    private VBox getLastNameGroup() {
        Label loginLbl = new Label("Фамилия");
        this.lastNameField = new RussianTextOnlyField();
        this.lastNameField.setPromptText("Введите фамилию");
        return new VBox(loginLbl, this.lastNameField);
    }

    private VBox getSurNameGroup() {
        Label loginLbl = new Label("Отчество");
        this.surNameField = new RussianTextOnlyField();
        this.surNameField.setPromptText("Введите отчество");
        return new VBox(loginLbl, this.surNameField);
    }

    private VBox getPassportGroup() {
        Label loginLbl = new Label("Номер паспорта");
        this.passportField = new TextField();
        this.passportField.setPromptText("Введите номер паспорта");
        return new VBox(loginLbl, this.passportField);
    }

    private VBox getCityGroup() {
        Label loginLbl = new Label("Город");
        this.cityField = new RussianTextOnlyField();
        this.cityField.setPromptText("Введите название города");
        return new VBox(loginLbl, this.cityField);
    }

    private VBox getStreetGroup() {
        Label loginLbl = new Label("Улица");
        this.streetField = new RussianTextOnlyField();
        this.streetField.setPromptText("Введите название улицы");
        return new VBox(loginLbl, this.streetField);
    }

    private VBox getBuildingGroup() {
        Label loginLbl = new Label("Дом");
        this.buildingField = new TextField();
        this.buildingField.setPromptText("Введите номер дома");
        return new VBox(loginLbl, this.buildingField);
    }

    private VBox getRoomGroup() {
        Label loginLbl = new Label("Квартира");
        this.roomField = new TextField();
        this.roomField.setPromptText("Введите номер квартиры");
        return new VBox(loginLbl, this.roomField);
    }


    private void sendCreateRequest(){
        this.blockControls(true);
        if(this.isDataValid()){
            CustomerDO data = new CustomerDO(this.loginField.getText(),
                    this.pwdField.getText(),
                    this.nameField.getText(),
                    this.lastNameField.getText(),
                    this.surNameField.getText(),
                    this.passportField.getText(),
                    this.cityField.getText(),
                    this.streetField.getText(),
                    this.buildingField.getText(),
                    this.roomField.getText(),
                    this.phoneNumberField.getText());
            try {
                this.customersService.addNewCustomer(sessionHolder.getSessionId(), data);
                showSuccessMsg();
                this.clearState();
            } catch (RemoteException e) {
                OperatorApp.showRmiExceptionWarning();
                e.printStackTrace();
            } catch (CustomerCreationException e) {
                e.printStackTrace();
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
        alert.setContentText("Клиент успешно создан");
        alert.showAndWait();
    }

    private void showCreationErrorAlert(CustomerCreationException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось создать клиента");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void showFormFillingErrorsAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("При заполнении формы были допущены ошибки");
        String errorMsg = this.validationErrors.stream().reduce((s, s2) ->  s2 + "\r\n"+ s).get();
        alert.setContentText(errorMsg);
        alert.showAndWait();
    }

    private boolean isDataValid() {
        validationErrors.clear();
        if(this.pwdField.getText().isEmpty()
                || this.loginField.getText().isEmpty()
                || this.nameField.getText().isEmpty()
                || this.surNameField.getText().isEmpty()
                || this.lastNameField.getText().isEmpty()
                || this.passportField.getText().isEmpty()
                || this.cityField.getText().isEmpty()
                || this.streetField.getText().isEmpty()
                || this.buildingField.getText().isEmpty()
                || this.roomField.getText().isEmpty()
                || this.phoneNumberField.getText().isEmpty()){
            validationErrors.add("Все поля должны быть заполнены!");
        }
        else {
            if (!this.pwdField.getText().equals(this.confField.getText())) {
                validationErrors.add("Введенные пароли не совпадают!");
            }
            if(this.loginField.getText().length() < 5){
                validationErrors.add("Логин должен быть не менее 5 символов.");
            }
            if(this.pwdField.getText().length() < 6){
                validationErrors.add("Пароль должен быть не менее 6 символов.");
            }
            if(this.nameField.getText().length() < 2){
                validationErrors.add("Имя должно быть не менее 2 символов.");
            }
            if(this.lastNameField.getText().length() < 4){
                validationErrors.add("Фамилия должна быть не менее 4 символов.");
            }
            if(this.surNameField.getText().length() < 4){
                validationErrors.add("Отчество должно быть не менее 4 символов.");
            }
            if(this.cityField.getText().length() < 3){
                validationErrors.add("Название города должно быть не менее 3 символов.");
            }
            if(this.streetField.getText().length() < 3){
                validationErrors.add("Название улицы должно быть не менее 3 символов.");
            }
            if(!Patterns.phoneRegexp.matcher(this.phoneNumberField.getText()).matches()){
                validationErrors.add("Номер телефона должен быть в формате +375XXXXXXXXX");
            }
            if(!Patterns.passportNumberPattern.matcher(this.passportField.getText()).matches()){
                validationErrors.add("Неверно указан формат номера паспорта!");
            }
        }
        return this.validationErrors.isEmpty();
    }

    private void clearState(){
        this.validationErrors.clear();
        this.loginField.clear();
        this.pwdField.clear();
        this.confField.clear();
        this.nameField.clear();
        this.lastNameField.clear();
        this.surNameField.clear();
        this.passportField.clear();
        this.cityField.clear();
        this.streetField.clear();
        this.buildingField.clear();
        this.roomField.clear();
        this.phoneNumberField.clear();
    }

    private void blockControls(boolean value){
        this.createBtn.setDisable(value);
        this.loginField.setDisable(value);
        this.pwdField.setDisable(value);
        this.confField.setDisable(value);
        this.nameField.setDisable(value);
        this.lastNameField.setDisable(value);
        this.surNameField.setDisable(value);
        this.passportField.setDisable(value);
        this.cityField.setDisable(value);
        this.streetField.setDisable(value);
        this.buildingField.setDisable(value);
        this.roomField.setDisable(value);
        this.phoneNumberField.setDisable(value);
    }
}

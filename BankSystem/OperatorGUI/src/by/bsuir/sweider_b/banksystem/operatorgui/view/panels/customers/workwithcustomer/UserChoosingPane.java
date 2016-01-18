package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers.workwithcustomer;

import by.bsuir.sweider_b.banksystem.operatorgui.OperatorApp;
import by.bsuir.sweider_b.banksystem.operatorgui.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers.TableViewForCustomers;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerDO;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerNotFoundException;
import by.bsuir.sweider_b.banksystem.shared.services.customers.ICustomersService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sweid on 18.01.2016.
 */
@Component
public class UserChoosingPane extends VBox {

    @Resource(name = "customersService")
    private ICustomersService customersService;

    @Autowired
    private CurrentSessionHolder sessionHolder;



    private TextField passportNumberField;

    private TextField nameField;
    private TextField lastNameField;
    private TextField surnameField;

    private RadioButton passportRadio;
    private RadioButton fioRadio;

    private ToggleGroup toggle;
    private final Button searchBtn;
    private ArrayList<String> validationErrors;

    private VBox userShowNode;
    private TableViewForCustomers customersShowTable;


    public UserChoosingPane(){
        this.validationErrors = new ArrayList<>();
        configureToggle();

        Label header = new Label("Поиск пользователя");
        header.getStyleClass().add("page-header");

        searchBtn = new Button("Поиск");
        searchBtn.setDefaultButton(true);
        searchBtn.setOnMouseClicked(event -> sendFindCustomerRequest());

        this.setPadding(new Insets(15));
        this.setSpacing(5);
        this.getChildren().addAll(header, getPassportDataGroup(),getFIODataGroup(), searchBtn);

        this.toggle.selectToggle(this.passportRadio);

        this.createUserShowNode();
        //this.activatePassportBLock();
    }

    private void sendFindCustomerRequest() {
        if(this.userShowNode!=null) this.getChildren().remove(this.userShowNode);
        if(this.toggle.getSelectedToggle().equals(this.passportRadio)){
            sendFindCustomerByPassportRequest();
        }
        else{
            sendFindCustomerByFioRequest();
        }
    }

    private void sendFindCustomerByPassportRequest() {
        this.searchBtn.setDisable(true);
        this.passportNumberField.setDisable(true);

        if(isPassportVariantValid()){
            try {
                CustomerDO customer = this.customersService.getCustomerByPassportNumber(this.sessionHolder.getSessionId(), this.passportNumberField.getText());
                this.showUserNode(new ArrayList<CustomerDO>(){{ add(customer);}});
            } catch (RemoteException e) {
                e.printStackTrace();
                OperatorApp.showRmiExceptionWarning();
            } catch (CustomerNotFoundException e) {
                e.printStackTrace();
                showCustomersNotFoundAlert();
            }
        }

        this.passportNumberField.setDisable(false);
        this.searchBtn.setDisable(false);
    }

    private void sendFindCustomerByFioRequest() {
        this.searchBtn.setDisable(true);
        this.nameField.setDisable(true);
        this.surnameField.setDisable(true);
        this.lastNameField.setDisable(true);
        if(isFioVariantValid()){
            try {
                List<CustomerDO> customers = this.customersService.getCustomersByFio(this.sessionHolder.getSessionId(),
                        this.nameField.getText(),
                        this.lastNameField.getText(),
                        this.surnameField.getText()
                );
                this.showUserNode(customers);
            } catch (RemoteException e) {
                e.printStackTrace();
                OperatorApp.showRmiExceptionWarning();
            } catch (CustomerNotFoundException e) {
                e.printStackTrace();
                showCustomersNotFoundAlert();
            }
        }

        this.nameField.setDisable(false);
        this.surnameField.setDisable(false);
        this.lastNameField.setDisable(false);
        this.searchBtn.setDisable(false);
    }

    private void showUserNode(List<CustomerDO> customers) {
        this.getChildren().add(this.userShowNode);
        this.customersShowTable.setItemsToDisplay(customers);
    }

    private void createUserShowNode() {
        Button creditBtn = new Button("Открыть заявку на кредит");
        creditBtn.setDisable(true);
        //creditBtn.setOnMouseClicked();

        Button showApplicationsBtn = new Button("Просмотреть статусы заявок");
        showApplicationsBtn.setDisable(true);
        //showApplicationsBtn.setOnMouseClicked();

        Button paymentBtn = new Button("Погашение кредита");
        paymentBtn.setDisable(true);
        //paymentBtn.setOnMouseClicked();
        HBox buttons = new HBox(5,creditBtn, showApplicationsBtn, paymentBtn);

        customersShowTable = new TableViewForCustomers();
        customersShowTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            creditBtn.setDisable(newValue == null);
            showApplicationsBtn.setDisable(newValue == null);
            paymentBtn.setDisable(newValue == null);
        });

        new VBox(10, customersShowTable, buttons);

        this.userShowNode = new VBox(10, customersShowTable, buttons);
    }

    private boolean isFioVariantValid() {
        this.validationErrors.clear();
        if(this.nameField.getText().isEmpty() || this.lastNameField.getText().isEmpty() || this.lastNameField.getText().isEmpty()){
            this.validationErrors.add("Нужно заполнить все поля");
        }
        else{

        }
        return this.validationErrors.isEmpty();
    }

    private void showCustomersNotFoundAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Сообщение");
        alert.setHeaderText("Пользователи не найдены");
        alert.setContentText("По данному запросу пользователи не найдены, попробуйте изменить запрос");
        alert.showAndWait();
    }

    private boolean isPassportVariantValid() {
        this.validationErrors.clear();
        if(this.passportNumberField.getText().isEmpty()){
            this.validationErrors.add("Нужно указать номер паспорта");
        }
        else{

        }
        return this.validationErrors.isEmpty();
    }



    private void configureToggle() {
        this.toggle = new ToggleGroup();
        this.passportRadio = new RadioButton("");
        this.passportRadio.setToggleGroup(this.toggle);

        this.fioRadio = new RadioButton("");
        this.fioRadio.setToggleGroup(this.toggle);

        this.toggle.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (toggle.getSelectedToggle() != null) {
                if(new_toggle.equals(passportRadio)){
                    activatePassportBLock();
                }
                if(new_toggle.equals(fioRadio)){
                    activateFioBlock();
                }
            }
        });
    }

    private void activateFioBlock() {
        this.passportNumberField.setDisable(true);
        this.nameField.setDisable(false);
        this.lastNameField.setDisable(false);
        this.surnameField.setDisable(false);
    }

    private void activatePassportBLock() {
        this.passportNumberField.setDisable(false);
        this.nameField.setDisable(true);
        this.lastNameField.setDisable(true);
        this.surnameField.setDisable(true);
    }

    private HBox getPassportDataGroup(){
        Label paspportLabel = new Label("Номер паспорта");
        this.passportNumberField = new TextField();
        this.passportNumberField.setPromptText("Введите номер паспорта");


        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);

        hbox.getChildren().addAll(this.passportRadio, new VBox(paspportLabel, this.passportNumberField));
        return hbox;
    }

    private HBox getFIODataGroup(){
        Label nameLabel = new Label("Имя");
        this.nameField = new TextField();
        this.nameField.setPromptText("Введите имя");

        Label lastNameLabel = new Label("Фамилия");
        this.lastNameField = new TextField();
        this.lastNameField.setPromptText("Введите фамилию");


        Label surNameLable = new Label("Отчество");
        this.surnameField = new TextField();
        this.surnameField.setPromptText("Введите отчество");

        VBox controls = new VBox(5, new VBox(nameLabel, nameField), new VBox(lastNameLabel,lastNameField), new VBox(surNameLable, surnameField));


        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);

        hbox.getChildren().addAll(this.fioRadio, controls);
        return hbox;
    }
}

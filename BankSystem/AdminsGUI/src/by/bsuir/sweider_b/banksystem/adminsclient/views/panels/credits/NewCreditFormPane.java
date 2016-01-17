package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.credits;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.adminsclient.views.components.NumericTextField;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees.EmployeeListCell;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import by.bsuir.sweider_b.banksystem.shared.services.credits.CreditCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.credits.ICreditManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.credits.PaymentType;
import by.bsuir.sweider_b.banksystem.shared.services.employee.EmployeeCreationException;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
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
public class NewCreditFormPane extends BorderPane{
    @Resource(name = "creditsManager")
    private ICreditManagementService creditManagementService;

    @Autowired
    private CurrentSessionHolder sessionHolder;


    private TextField titleField;
    private TextArea descriptionField;
    private NumericTextField lengthInMonthField;
    private NumericTextField minMoneyAmountField;
    private NumericTextField maxMoneyAmountField;
    private ComboBox<PaymentType> paymentTypeComboBox;
    private CheckBox prepaymentCheckBox;

    private ArrayList<String> validationErrors;
    private final Button createBtn;
    private final Label header;


    public NewCreditFormPane(){
        VBox container = new VBox(5);
        header = new Label("Новый кредит");
        header.getStyleClass().add("page-header");

        createBtn = new Button("Создать");
        createBtn.setDefaultButton(true);
        createBtn.setOnMouseClicked(event -> sendCreateRequest());
        container.getChildren().addAll(
                header,
                getTitleGroup(),
                getDescriptionGroup(),
                getLengthGroup(),
                getMinMoneyGroup(),
                getMaxMoneyGroup(),
                getPaymentTypeGroup(),
                getPrepaymentGroup(),
                createBtn
        );

        container.setPadding(new Insets(10));
        this.setLeft(container);
    }

    private void sendCreateRequest() {
        this.blockControls(true);
        if(this.isValid()){
            try {
                this.creditManagementService.createCredit(
                        this.sessionHolder.getSessionId(),
                        this.titleField.getText(),
                        this.descriptionField.getText(),
                        Integer.parseInt(this.lengthInMonthField.getText()),
                        Integer.parseInt(this.minMoneyAmountField.getText()),
                        Integer.parseInt(this.maxMoneyAmountField.getText()),
                        this.prepaymentCheckBox.isSelected(),
                        this.paymentTypeComboBox.getValue()
                 );
                this.clearState();
                this.showSuccessMsg();
            } catch (RemoteException e) {
                e.printStackTrace();
                AdministrationApp.showRmiExceptionWarning();
            } catch (CreditCreationException e) {
                e.printStackTrace();
                this.showCreationErrorAlert(e);
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
        alert.setContentText("Кредит успешно создан!");
        alert.showAndWait();
    }

    private void showCreationErrorAlert(CreditCreationException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось создать кредит");
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

    private boolean isValid(){
        this.validationErrors.clear();
        if(titleField.getText().isEmpty() || descriptionField.getText().isEmpty() || lengthInMonthField.getText().isEmpty() ||
                minMoneyAmountField.getText().isEmpty() || maxMoneyAmountField.getText().isEmpty()){
            this.validationErrors.add("Все поля должны быть заполнены!");
        }
        else {
            if (descriptionField.getText().split(" ").length < 5) {
                this.validationErrors.add("Описание должно быть не менее 5 слов");
            }
            if(Integer.parseInt(maxMoneyAmountField.getText()) < Integer.parseInt(minMoneyAmountField.getText())){
                this.validationErrors.add("Минимальная сумма должна быть меньше либо равна максимальной");
            }
        }
        return this.validationErrors.isEmpty();
    }

    private void blockControls(boolean value){
        this.titleField.setDisable(value);
        this.descriptionField.setDisable(value);
        this.lengthInMonthField.setDisable(value);
        this.minMoneyAmountField.setDisable(value);
        this.maxMoneyAmountField.setDisable(value);
        this.prepaymentCheckBox.setDisable(value);
        this.paymentTypeComboBox.setDisable(value);
        this.createBtn.setDisable(value);
    }

    private void clearState(){
        this.titleField.clear();
        this.descriptionField.clear();
        this.lengthInMonthField.clear();
        this.minMoneyAmountField.clear();
        this.maxMoneyAmountField.clear();
        this.paymentTypeComboBox.setValue(PaymentType.PERIODLY);
        this.prepaymentCheckBox.setSelected(false);
    }


    private VBox getTitleGroup(){
        Label label = new Label("Название кредита");
        this.titleField = new TextField();
        this.titleField.setPromptText("Введите название кредита");
        return new VBox(label, this.titleField);
    }

    private VBox getPrepaymentGroup(){
        this.prepaymentCheckBox = new CheckBox("Возможность досрочного погашения");
        this.prepaymentCheckBox.setSelected(false);
        return new VBox(this.prepaymentCheckBox);
    }

    private VBox getDescriptionGroup(){
        Label label = new Label("Описание кредита");
        this.descriptionField = new TextArea();
        this.descriptionField.setPromptText("Введите описание кредита");
        this.descriptionField.setWrapText(true);
        return new VBox(label, this.descriptionField);
    }

    private VBox getLengthGroup(){
        Label label = new Label("Срок в месяцах");
        this.lengthInMonthField = new NumericTextField();
        this.lengthInMonthField.setPromptText("Введите срок");
        return new VBox(label, this.lengthInMonthField);
    }

    private VBox getMinMoneyGroup(){
        Label label = new Label("Минимальная сумма");
        this.minMoneyAmountField = new NumericTextField();
        this.minMoneyAmountField.setPromptText("Введите сумму");
        return new VBox(label, this.minMoneyAmountField);
    }

    private VBox getMaxMoneyGroup(){
        Label label = new Label("Максимальная сумма");
        this.maxMoneyAmountField = new NumericTextField();
        this.maxMoneyAmountField.setPromptText("Введите сумму");
        return new VBox(label, this.maxMoneyAmountField);
    }

    private VBox getPaymentTypeGroup() {
        Label label = new Label("Способ погашения");
        this.paymentTypeComboBox = new ComboBox<>();
        this.paymentTypeComboBox.setCellFactory(p -> new PaymentTypeListCell());
        this.paymentTypeComboBox.setButtonCell(new PaymentTypeListCell());
        this.paymentTypeComboBox.getItems().addAll(PaymentType.values());
        this.paymentTypeComboBox.setValue(PaymentType.PERIODLY);
        this.paymentTypeComboBox.prefWidthProperty().bind(this.descriptionField.widthProperty());
        this.paymentTypeComboBox.setPrefHeight(30);
        return new VBox(label, this.paymentTypeComboBox);
    }
}

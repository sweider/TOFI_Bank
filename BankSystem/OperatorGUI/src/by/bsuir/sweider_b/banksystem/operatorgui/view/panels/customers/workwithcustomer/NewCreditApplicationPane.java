package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers.workwithcustomer;

import by.bsuir.sweider_b.banksystem.operatorgui.OperatorApp;
import by.bsuir.sweider_b.banksystem.operatorgui.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.operatorgui.view.components.NumericTextField;
import by.bsuir.sweider_b.banksystem.operatorgui.view.components.TableViewForCredits;
import by.bsuir.sweider_b.banksystem.shared.services.creditaplications.CreditApplicationCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.creditaplications.CreditApplicationDO;
import by.bsuir.sweider_b.banksystem.shared.services.creditaplications.ICreditAplicatonService;
import by.bsuir.sweider_b.banksystem.shared.services.credits.CreditKindDO;
import by.bsuir.sweider_b.banksystem.shared.services.credits.ICreditManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerCreationException;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerDO;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class NewCreditApplicationPane extends VBox {

    @Autowired
    private CurrentSessionHolder sessionHolder;

    @Resource(name = "creditsApplicationService")
    private ICreditAplicatonService creditAplicatonService;

    @Resource(name = "creditsManager")
    private ICreditManagementService creditKindsService;

    private TableViewForCredits creditsTable;
    private VBox creditChoosingPart;
    private VBox formPart;

    private CreditKindDO currentlyWorkingCredit;
    private NumericTextField sumField;
    private NumericTextField incomeField;

    private ArrayList<String> validationErrors;
    private Button approveBtn;

    public NewCreditApplicationPane(){
        this.validationErrors = new ArrayList<>();

        Button backButton = new Button("Вернуться к выбору пользователя");
        backButton.setOnMouseClicked(event -> OperatorApp.APP_CONTEXT.getBean(WorkWithCustomerRootPane.class).goBackToUserChoosing());
        this.initializeCreditChoosingTable();
        this.initializeForm();

        this.getChildren().addAll(backButton, this.creditChoosingPart);
        this.setSpacing(10);
        this.setPadding(new Insets(15));
    }

    private void initializeCreditChoosingTable(){
        this.creditsTable = new TableViewForCredits();
        Button choose = new Button("Выбрать");
        choose.setDefaultButton(true);
        choose.setOnMouseClicked(event -> {
            this.removeFormIfDisplaying();
            this.currentlyWorkingCredit = this.creditsTable.getSelectionModel().getSelectedItem().getBaseData();
            this.reinitializeForm();
            this.showForm();
        });
        this.creditsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> choose.setDisable(newValue == null));
        this.creditChoosingPart = new VBox(5, this.creditsTable, choose);
    }

    private void initializeForm(){
        Label sum = new Label("Сумма кредита");
        sumField = new NumericTextField();
        sumField.setPromptText("Введите сумму");

        Label income = new Label("Ежемесячный доход");
        incomeField = new NumericTextField();
        incomeField.setPromptText("Укажите ежемесячный доход");

        approveBtn = new Button("Оформить заявку");
        approveBtn.setDefaultButton(true);
        approveBtn.setOnMouseClicked(event -> createCreditApplication());

        this.formPart = new VBox(5, new VBox(sum, sumField), new VBox(income, incomeField), approveBtn);
    }

    private void createCreditApplication() {
        this.approveBtn.setDisable(true);
        if(this.validateForm()){
            WorkWithCustomerRootPane rootPane = OperatorApp.APP_CONTEXT.getBean(WorkWithCustomerRootPane.class);
            CustomerDO customer = rootPane.getCurrentCustomer();
            try {
                this.creditAplicatonService.registrateNewCreditApplication(this.sessionHolder.getSessionId(),
                        new CreditApplicationDO(customer.getId(),
                                this.currentlyWorkingCredit.getId(),
                                Long.parseLong(this.sumField.getText()),
                                Long.parseLong(this.incomeField.getText())));
                this.showSuccessMsg();
                rootPane.finishCreditApplicationCreation();
            } catch (RemoteException e) {
                e.printStackTrace();
                OperatorApp.showRmiExceptionWarning();
            } catch (CreditApplicationCreationException e) {
                e.printStackTrace();
                this.showCreationErrorAlert(e);
            }
        }
        else{
            this.showFormFillingErrorsAlert();
        }

        this.approveBtn.setDisable(false);
    }

    private void showSuccessMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Завершено");
        alert.setHeaderText("Успех!");
        alert.setContentText("Заявка успешно зарегистрирована! Ожидайте решения комиссии.");
        alert.showAndWait();
    }

    private void showCreationErrorAlert(CreditApplicationCreationException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось зарегистрировать заявку");
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

    private void removeFormIfDisplaying(){
        this.getChildren().remove(this.formPart);
    }

    private void reinitializeForm(){
        this.sumField.clear();
        this.incomeField.clear();
    }

    private void showForm(){
        this.getChildren().add(this.formPart);
    }

    private boolean validateForm(){
        this.validationErrors.clear();

        if(this.sumField.getText().isEmpty() || this.incomeField.getText().isEmpty()){
            this.validationErrors.add("Все поля должны быть заполнены.");
        }
        else{
            int summ = Integer.parseInt(this.sumField.getText());
            if(summ < this.currentlyWorkingCredit.getMin()){
                this.validationErrors.add("Сумма не должна быть меньше " + this.currentlyWorkingCredit.getMin());
            }
            else if(summ > this.currentlyWorkingCredit.getMax()){
                this.validationErrors.add("Сумма не должна быть более " + this.currentlyWorkingCredit.getMax());
            }
        }
        return this.validationErrors.isEmpty();
    }

    protected void refresh(){
        this.reinitializeForm();
        this.removeFormIfDisplaying();
        try {
            List<CreditKindDO> creditKinds =  this.creditKindsService.getCreditKinds(sessionHolder.getSessionId(), true);
            this.creditsTable.setItemsToDisplay(creditKinds);
        } catch (RemoteException e) {
            e.printStackTrace();
            OperatorApp.showRmiExceptionWarning();
        }
    }
}

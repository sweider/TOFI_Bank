package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.credits;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.shared.services.credits.CreditShowObject;
import by.bsuir.sweider_b.banksystem.shared.services.credits.CreditUpdateException;
import by.bsuir.sweider_b.banksystem.shared.services.credits.ICreditManagementService;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
public class EditDescriptionPane extends VBox{
    @Resource(name = "creditsManager")
    private ICreditManagementService creditManagementService;
    @Autowired
    private CurrentSessionHolder sessionHolder;

    private TextArea descriptionArea;
    private final Button updateBtn;

    private ArrayList<String> validationErrors;
    private CreditShowObject data;

    public EditDescriptionPane(){
        this.validationErrors = new ArrayList<>();
        Label header = new Label("Имзенить описание");
        header.getStyleClass().add("page-header");

        updateBtn = new Button("Обновить");
        updateBtn.setDefaultButton(true);
        updateBtn.setOnMouseClicked(event -> sendUpdateRequest());

        Button backBtn = new Button("Назад");
        backBtn.setOnMouseClicked(event -> returnToShow());



        this.setSpacing(5);
        this.getChildren().addAll(header, getDescGroup(), new HBox(10, updateBtn, backBtn));
    }

    private void returnToShow() {
        AdministrationApp.APP_CONTEXT.getBean(ShowCreditsPane.class).activateMainPane();
    }

    private void sendUpdateRequest() {
        this.blockControls(true);
        if(this.isValid()){
            try {
                this.creditManagementService.updateCredit(this.sessionHolder.getSessionId(), this.data.getId(), this.descriptionArea.getText());
                this.data.setDescription(this.descriptionArea.getText());
                AdministrationApp.APP_CONTEXT.getBean(ShowCreditsPane.class).updateCredit(this.data);
                this.showSuccessMsg();
                this.returnToShow();
            } catch (RemoteException e) {
                e.printStackTrace();
                AdministrationApp.showRmiExceptionWarning();
            } catch (CreditUpdateException e) {
                e.printStackTrace();
                this.showUpdatingErrorAlert(e);
            }
        }
        else{
            this.showFormFillingErrorsAlert();
        }
        this.blockControls(false);
    }

    private void showSuccessMsg() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Завершено");
        alert.setHeaderText("Успех!");
        alert.setContentText("Данные успешно изменены");
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

    private void showUpdatingErrorAlert(CreditUpdateException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Не удалось обновить описание");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }


    private boolean isValid(){
        this.validationErrors.clear();
        if(this.descriptionArea.getText().isEmpty()){
            this.validationErrors.add("Поле должно быть заполнено");
        }
        else{
            if(this.descriptionArea.getText().split(" ").length < 5){
                this.validationErrors.add("Описание должно быть не менее 5 слов.");
            }
        }
        return this.validationErrors.isEmpty();
    }


    public void setData(CreditShowObject data){
        this.data = data;
        this.descriptionArea.setText(data.getDescription());
    }

    private VBox getDescGroup(){
        Label descriptionLabel = new Label("Описание кредита");
        this.descriptionArea = new TextArea();
        this.descriptionArea.setPromptText("Введите описание");
        this.descriptionArea.setWrapText(true);
        return new VBox(descriptionLabel, descriptionArea);
    }

    private void blockControls(boolean value){
        this.descriptionArea.setDisable(value);
        this.updateBtn.setDisable(value);
    }
}

package by.bsuir.sweider_b.banksystem.adminsclient;
/**
 * Created by sweid on 15.01.2016.
 */

import by.bsuir.sweider_b.banksystem.adminsclient.config.AppConfig;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.credits.CreditsManagementPane;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.RootPane;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.credits.NewCreditFormPane;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.credits.ShowCreditsPane;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees.NewUserPane;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees.ShowUsersPanel;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.AuthenticationResult;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.Optional;

public class AdministrationApp extends Application {
    public static AdministrationApp RUNNING_INSTANCE;
    public static AnnotationConfigApplicationContext APP_CONTEXT;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        RUNNING_INSTANCE = this;
        this.initializeSpring();
        this.authenticate();
        this.launchApp(primaryStage);
    }

    private void initializeSpring() {
        try{
            APP_CONTEXT = new AnnotationConfigApplicationContext(AppConfig.class);
        }
        catch(Throwable ex){
            ex.printStackTrace();
            while(ex.getCause() != null){
                ex = ex.getCause();
            }
            if(ex instanceof java.net.ConnectException){
                this.showErrorAlert();
                System.exit(-1);
            }
        }
    }

    private void authenticate() {
        Optional<AuthenticationResult> result = AuthenticationDialog.authenticate(APP_CONTEXT);
        if(result.isPresent()){
            APP_CONTEXT.getBean(CurrentSessionHolder.class).processAuthenticationResult(result.get());
        }
        else{
            System.exit(0);
        }
    }

    private void launchApp(Stage primaryStage) throws java.io.IOException {
        primaryStage.setTitle("ВАШ БАНК");
        Scene stage = new Scene(APP_CONTEXT.getBean(RootPane.class));
        stage.getStylesheets().add(this.getClass().getResource("/custom.css").toString());
        primaryStage.setScene(stage);
        primaryStage.show();
    }

    public void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Сервер недоступен.");
        alert.setTitle("Нет связи с сервером");
        alert.setContentText("Не удалось установить связь с сервером. К сожалению, приложение не может использоваться в этом случае.");
        alert.showAndWait();
    }

    public void activateNewUserPage() {
        NewUserPane userPage = APP_CONTEXT.getBean(NewUserPane.class);
        APP_CONTEXT.getBean(RootPane.class).setCenter(userPage);
    }

    public void activateCreditsManagementsPage() {
        CreditsManagementPane creditsPage = APP_CONTEXT.getBean(CreditsManagementPane.class);
        APP_CONTEXT.getBean(RootPane.class).setCenter(creditsPage);
    }

    public void activateShowUsersPage() {
        ShowUsersPanel panel = APP_CONTEXT.getBean(ShowUsersPanel.class);
        panel.updateData();
        APP_CONTEXT.getBean(RootPane.class).setCenter(panel);
    }

    public static void showRmiExceptionWarning(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Операция не выполнена");
        alert.setTitle("Ошибка сети");
        alert.setContentText("Во время выполнения запроса произошла сетевая ошибка. Запрос не был выполнен.");
        alert.showAndWait();
    }


    public void activateNewCreditPage() {
        RootPane root = APP_CONTEXT.getBean(RootPane.class);
        root.setCenter(APP_CONTEXT.getBean(NewCreditFormPane.class));
    }

    public void activateShowActiveCreditsPage() {
        RootPane root = APP_CONTEXT.getBean(RootPane.class);
        ShowCreditsPane showPane = APP_CONTEXT.getBean(ShowCreditsPane.class);
        showPane.updateData(true);
        root.setCenter(showPane);
    }

    public void activateShowDeactivatedCreditsPage() {
        RootPane root = APP_CONTEXT.getBean(RootPane.class);
        ShowCreditsPane showPane = APP_CONTEXT.getBean(ShowCreditsPane.class);
        showPane.updateData(false);
        root.setCenter(showPane);
    }
}

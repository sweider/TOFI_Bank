package by.bsuir.sweider_b.banksystem.client; /**
 * Created by sweid on 03.01.2016.
 */

import by.bsuir.sweider_b.banksystem.client.config.AppConfig;
import by.bsuir.sweider_b.banksystem.client.controllers.AuthorizationService;
import by.bsuir.sweider_b.banksystem.client.views.components.*;
import by.bsuir.sweider_b.banksystem.client.views.authentication.AuthenticationDialog;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.AuthenticationResult;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MainApp extends Application {
    public static AnnotationConfigApplicationContext APP_CONTEXT;
    public static MainApp RUNNING_INSTANCE;

    private AuthorizationService authService;

    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        RUNNING_INSTANCE = this;
        this.initializeSpring();
        this.initializeServices();
        this.authenticate();
        initializeApp(primaryStage);
    }

    private void initializeServices() {
        this.authService = APP_CONTEXT.getBean(AuthorizationService.class);
    }


    private void authenticate() {
        Optional<AuthenticationResult> result = AuthenticationDialog.authenticate(APP_CONTEXT);
        if(result.isPresent()){
            result.get();

        }
        else{
            System.exit(0);
        }
    }

    private void initializeSpring() {
        try{
           APP_CONTEXT = new AnnotationConfigApplicationContext(AppConfig.class);
        }
        catch(Throwable ex){
            while(ex.getCause() != null){
                ex = ex.getCause();
            }
            if(ex instanceof java.net.ConnectException){
                this.showErrorAlert();
                System.exit(-1);
            }
            else{
                ex.printStackTrace();
            }
        }
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Сервер недоступен.");
        alert.setTitle("Нет связи с сервером");
        alert.setContentText("Не удалось установить связь с сервером. К сожалению, приложение не может использоваться в этом случае.");
        alert.showAndWait();
    }

    private void initializeApp(Stage primaryStage) throws java.io.IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/by/bsuir/sweider_b/banksystem/client/views/authentication/Authentication.fxml"));
        primaryStage.setTitle("ВАШ БАНК");
        Scene stage = new Scene(APP_CONTEXT.getBean(RootPane.class));
        stage.getStylesheets().add(this.getClass().getResource("/custom.css").toString());
        primaryStage.setScene(stage);
        primaryStage.show();
    }

    public void activateHomePage(){
        this.activatePage(HomePane.class);
    }


    public void activateMyCreditsPage() {
        this.activatePage(MyCreditsPane.class);
    }

    public void activateActiveRequestsPage() {
        this.activatePage(ActiveRequestsPane.class);
    }

    public void activateNewCreditsPage() {
        this.activatePage(NewRequestPane.class);
    }

    private void activatePage(Class paneClass){
        APP_CONTEXT.getBean(RootPane.class).setCenter((Node) APP_CONTEXT.getBean(paneClass));
    }
}

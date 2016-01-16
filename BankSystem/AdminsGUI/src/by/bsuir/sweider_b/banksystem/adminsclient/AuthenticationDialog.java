package by.bsuir.sweider_b.banksystem.adminsclient;

import by.bsuir.sweider_b.banksystem.shared.services.authentication.AuthenticationException;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.AuthenticationResult;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.IAdminsAuthenticationService;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.ICustomerAuthenticationService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.springframework.context.ApplicationContext;

import java.rmi.RemoteException;
import java.util.Optional;

/**
 * Created by sweid on 03.01.2016.
 */
public class AuthenticationDialog extends Dialog<AuthenticationResult> {


    private boolean isAuthenticationFailed;
    private IAdminsAuthenticationService authenticationService;

    private AuthenticationResult authenticationResult;
    private final TextField username;
    private final PasswordField passwordField;

    public AuthenticationDialog(ApplicationContext ctx) {
        this.authenticationService = (IAdminsAuthenticationService) ctx.getBean("adminsAuth");
        this.setTitle("Требуется авторизация");
        this.setHeaderText("Пожалуйста, авторизуйтесь");

        this.setUpImage();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        username = new TextField();
        username.setPromptText("Введите логин");
        username.setMinWidth(200);
        passwordField = new PasswordField();
        passwordField.setPromptText("Введите пароль");

        grid.add(new Label("Логин:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(passwordField, 1, 1);

        ButtonType loginButtonType = new ButtonType("Авторизоваться", ButtonBar.ButtonData.OK_DONE);
        ButtonType ruCancelButtonType = new ButtonType("Выход", ButtonBar.ButtonData.CANCEL_CLOSE);
        this.getDialogPane().getButtonTypes().addAll(loginButtonType, ruCancelButtonType);

        Node loginButton = this.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        this.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());
        this.setResultConverter(dialogButton -> {
            if(dialogButton == loginButtonType) {
                try {
                    this.authenticationResult = this.authenticationService.authenticateAdmin(username.getText(), passwordField.getText());
                    return this.authenticationResult;
                } catch (AuthenticationException e) {
                    this.showAlert();
                    this.isAuthenticationFailed = true;
                } catch (RemoteException e) {
                    e.printStackTrace();
                    AdministrationApp.showRmiExceptionWarning();
                }
            }
            return null;
        });
    }

    private void setUpImage() {
        ImageView graphic = new ImageView(this.getClass().getResource("/login.png").toString());
        graphic.setFitWidth(64);
        graphic.setFitHeight(64);
        this.setGraphic(graphic);
    }


    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Ошибка авторизации.");
        alert.setTitle("Не удалось авторизовать");
        alert.setContentText("Введенные данные невалидны. Пожалуйста, попробуйте еще раз.");
        alert.showAndWait();
    }

    public static Optional<AuthenticationResult> authenticate(ApplicationContext ctx){
        AuthenticationDialog authenticationDialog = new AuthenticationDialog(ctx);
        Optional<AuthenticationResult> result;
        do {
            authenticationDialog.prepareForReuse();
            result = authenticationDialog.showAndWait();
        }
        while(authenticationDialog.isAuthenticationFailed);
        return result;
    }

    private void prepareForReuse() {
        this.isAuthenticationFailed = false;
        this.passwordField.clear();
        this.username.clear();
    }
}

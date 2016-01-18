package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.shared.services.employee.EmployeeShowDO;
import by.bsuir.sweider_b.banksystem.shared.services.employee.IEmployeeManagementService;
import by.bsuir.sweider_b.banksystem.shared.services.employee.UpdatingException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sweid on 16.01.2016.
 */
@Component
public class ShowUsersPanel extends BorderPane {
    @Resource(name = "employeeManager")
    private IEmployeeManagementService employeeManagementService;

    @Autowired
    private CurrentSessionHolder sessionHolder;

    @Autowired
    private ChangePasswordPane changePwdPane;

    @Autowired
    ChangeEmployeeDataPane changeEmployeeDataPane;

    private List<EmployeeDataForTable> tableData;
    private TableView<EmployeeDataForTable> tableView;

    private VBox mainContent;
    private ContextMenu contextMenu;


    public ShowUsersPanel() {
        mainContent = new VBox();
        Label header = new Label("Пользователи системы");
        header.getStyleClass().add("page-header");

        this.createTableViewForData();

        mainContent.setPadding(new Insets(10,10,10,10));
        mainContent.getChildren().addAll(header, tableView);
        this.setCenter(mainContent);
    }

    public void updateData() {
        try {
            List<EmployeeShowDO> data = this.employeeManagementService.getEmployees();
            this.tableData = data.stream().map(EmployeeDataForTable::new).collect(Collectors.toList());
            this.tableView.setItems(FXCollections.observableArrayList(this.tableData));
        } catch (RemoteException e) {
            e.printStackTrace();
            AdministrationApp.showRmiExceptionWarning();
        }
    }

    private void createTableViewForData(){
        TableView<EmployeeDataForTable> table = new TableView<>();
        TableColumn<EmployeeDataForTable, String> loginColumn = new TableColumn<>("Логин");
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

        TableColumn<EmployeeDataForTable, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<EmployeeDataForTable, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<EmployeeDataForTable, String> roleColumn = new TableColumn<>("Роль");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(nameColumn, lastNameColumn, loginColumn, roleColumn);
        contextMenu = getContextMenu(table);


        table.setRowFactory(param -> {
            final TableRow<EmployeeDataForTable> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(this.contextMenu)
            );

            return row;
        });


        this.tableView = table;
    }

    private ContextMenu getContextMenu(TableView<EmployeeDataForTable> table) {
        MenuItem changeData = new MenuItem("Изменить данные");
        changeData.setOnAction(event -> this.showChangeDataForm(table.getSelectionModel().getSelectedItem().getBaseData()));

        MenuItem changePwd = new MenuItem("Изменить пароль");
        changePwd.setOnAction(event -> this.showChangePwdForm(table.getSelectionModel().getSelectedItem().getBaseData().getId()));


        MenuItem deactivate = new MenuItem("Деактивировать");
        deactivate.setOnAction(event -> this.sendDeactivationRequest(table.getSelectionModel().getSelectedItem()));

        return new ContextMenu(changeData, changePwd, deactivate);
    }

    private void showChangeDataForm(EmployeeShowDO baseData) {
        if(baseData.getLogin().equals("default_admin")){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Извините, но изменять данные администратора по-умолчанию нельзя.");
            alert.setHeaderText("Недопустимое действие");
            alert.showAndWait();
        }
        else {
            this.changeEmployeeDataPane.setEmployee(baseData);
            this.setCenter(null);
            this.setLeft(this.changeEmployeeDataPane);
        }
    }

    private void sendDeactivationRequest(EmployeeDataForTable emp){
        try {
            this.employeeManagementService.setActiveState(this.sessionHolder.getSessionId(), emp.getBaseData().getId(), false);
            this.tableView.getItems().remove(emp);
            this.showSuccessChangeStateMsg();
        } catch (RemoteException e) {
            e.printStackTrace();
            AdministrationApp.showRmiExceptionWarning();
        } catch (UpdatingException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Обновление не удалось");
            errorAlert.setContentText(e.getMessage());
            errorAlert.setTitle("Ошибка");
            errorAlert.showAndWait();
        }
    }

    private void showSuccessChangeStateMsg() {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText("Успех!");
        errorAlert.setContentText("Состояние успешно изменено!");
        errorAlert.setTitle("Завершено");
        errorAlert.showAndWait();
    }

    private void showChangePwdForm(int id){
        this.changePwdPane.setEmployeeId(id);
        this.setCenter(null);
        this.setLeft(this.changePwdPane);
    }

    public void activateMainPane(){
        this.setLeft(null);
        this.setCenter(this.mainContent);
    }

    public void updateValueFor(EmployeeShowDO old, EmployeeShowDO newValue){
        EmployeeDataForTable empl = null;
        for(EmployeeDataForTable data : this.tableView.getItems()){
            if(data.getBaseData().equals(old)){
                empl = data;
                break;
            }
        }
        int index = this.tableView.getItems().indexOf(empl);
        this.tableView.getItems().remove(index);
        this.tableView.getItems().set(index, new EmployeeDataForTable(newValue));
    }

    public static class EmployeeDataForTable{
        private SimpleStringProperty name;
        private SimpleStringProperty login;
        private SimpleStringProperty role;
        private SimpleStringProperty lastName;
        private EmployeeShowDO baseData;

        public EmployeeDataForTable(EmployeeShowDO data){
            this.login = new SimpleStringProperty(data.getLogin());
            this.role = new SimpleStringProperty(data.getRole().getUserFriendlyName());
            this.name = new SimpleStringProperty(data.getName());
            this.lastName = new SimpleStringProperty(data.getLastName());
            this.baseData = data;
        }

        public EmployeeShowDO getBaseData() {
            return baseData;
        }

        public String getLogin() {
            return login.get();
        }

        public SimpleStringProperty loginProperty() {
            return login;
        }

        public void setLogin(String login) {
            this.login.set(login);
        }

        public String getRole() {
            return role.get();
        }

        public SimpleStringProperty roleProperty() {
            return role;
        }

        public void setRole(String role) {
            this.role.set(role);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getLastName() {
            return lastName.get();
        }

        public SimpleStringProperty lastNameProperty() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName.set(lastName);
        }
    }
}

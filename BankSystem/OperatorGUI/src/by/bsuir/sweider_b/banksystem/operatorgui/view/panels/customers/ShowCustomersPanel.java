package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers;

import by.bsuir.sweider_b.banksystem.operatorgui.OperatorApp;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerDO;
import by.bsuir.sweider_b.banksystem.shared.services.customers.ICustomersService;
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
public class ShowCustomersPanel extends BorderPane {
    @Resource(name = "customersService")
    private ICustomersService customersService;

    @Autowired
    private ChangePasswordPane changePwdPane;


    private List<CustomerDataForTable> tableData;
    private TableView<CustomerDataForTable> tableView;

    private VBox mainContent;
    private ContextMenu contextMenu;


    public ShowCustomersPanel() {
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
            List<CustomerDO> data = this.customersService.showCustomers();
            this.tableData = data.stream().map(CustomerDataForTable::new).collect(Collectors.toList());
            this.tableView.setItems(FXCollections.observableArrayList(this.tableData));
        } catch (RemoteException e) {
            e.printStackTrace();
            OperatorApp.showRmiExceptionWarning();
        }
    }

    private void createTableViewForData(){
        TableView<CustomerDataForTable> table = new TableView<>();
        TableColumn<CustomerDataForTable, String> loginColumn = new TableColumn<>("Логин");
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

        TableColumn<CustomerDataForTable, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<CustomerDataForTable, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<CustomerDataForTable, String> roleColumn = new TableColumn<>("Роль");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(nameColumn, lastNameColumn, loginColumn, roleColumn);
        contextMenu = new ContextMenu();


        table.setRowFactory(param -> {
            final TableRow<CustomerDataForTable> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(this.contextMenu)
            );

            return row;
        });


        this.tableView = table;
    }


    private void sendDeactivationRequest(int employeeId){
        new Alert(Alert.AlertType.ERROR, "Данный функционал еще не реализован").showAndWait();
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

    public void updateValueFor(CustomerDO old, CustomerDO newValue){
        CustomerDataForTable empl = null;
        for(CustomerDataForTable data : this.tableView.getItems()){
            if(data.getBaseData().equals(old)){
                empl = data;
                break;
            }
        }
        int index = this.tableView.getItems().indexOf(empl);
        this.tableView.getItems().remove(index);
        this.tableView.getItems().set(index, new CustomerDataForTable(newValue));
    }

    public static class CustomerDataForTable {
        private SimpleStringProperty name;
        private SimpleStringProperty lastName;
        private SimpleStringProperty passportData;
        private CustomerDO baseData;

        public CustomerDataForTable(CustomerDO data){
            this.name = new SimpleStringProperty(data.getName());
            this.lastName = new SimpleStringProperty(data.getLastName());
            this.passportData = new SimpleStringProperty(data.getPassportNumber());
            this.baseData = data;
        }

        public CustomerDO getBaseData() {
            return baseData;
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

        public String getPassportData() {
            return passportData.get();
        }

        public SimpleStringProperty passportDataProperty() {
            return passportData;
        }
    }
}

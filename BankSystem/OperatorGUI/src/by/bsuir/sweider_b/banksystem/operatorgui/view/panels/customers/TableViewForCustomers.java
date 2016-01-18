package by.bsuir.sweider_b.banksystem.operatorgui.view.panels.customers;

import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerDO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sun.util.resources.cldr.so.CurrencyNames_so;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sweid on 18.01.2016.
 */
public class TableViewForCustomers extends TableView<TableViewForCustomers.CustomerDataForTable> {

    private ContextMenu contextMenu;

    public TableViewForCustomers(){
        TableColumn<CustomerDataForTable, String> surNameColumn = new TableColumn<>("Отчество");
        surNameColumn.setCellValueFactory(new PropertyValueFactory<>("surName"));

        TableColumn<CustomerDataForTable, String> nameColumn = new TableColumn<>("Имя");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<CustomerDataForTable, String> lastNameColumn = new TableColumn<>("Фамилия");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<CustomerDataForTable, String> passportDataColumn = new TableColumn<>("Номер паспорта");
        passportDataColumn.setCellValueFactory(new PropertyValueFactory<>("passportData"));

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.getColumns().addAll(nameColumn, surNameColumn, lastNameColumn, passportDataColumn);
        contextMenu = new ContextMenu();


        this.setRowFactory(param -> {
            final TableRow<CustomerDataForTable> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(this.contextMenu)
            );

            return row;
        });
    }

    public void setItemsToDisplay(List<CustomerDO> customers){
        this.getItems().clear();
        this.getItems().addAll(customers.stream().map(CustomerDataForTable::new).collect(Collectors.toList()));
    }

    public void setConextMenuItems(MenuItem ... items){
        this.contextMenu.getItems().clear();
        this.contextMenu.getItems().addAll(items);
    }



    public static class CustomerDataForTable {
        private SimpleStringProperty name;
        private SimpleStringProperty lastName;
        private SimpleStringProperty surName;
        private SimpleStringProperty passportData;
        private CustomerDO baseData;

        public CustomerDataForTable(CustomerDO data){
            this.name = new SimpleStringProperty(data.getName());
            this.lastName = new SimpleStringProperty(data.getLastName());
            this.passportData = new SimpleStringProperty(data.getPassportNumber());
            this.surName = new SimpleStringProperty(data.getSurName());
            this.baseData = data;
        }


        public String getSurName() {
            return surName.get();
        }

        public SimpleStringProperty surNameProperty() {
            return surName;
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

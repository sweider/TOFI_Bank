package by.bsuir.sweider_b.banksystem.adminsclient.views.panels.credits;

import by.bsuir.sweider_b.banksystem.adminsclient.AdministrationApp;
import by.bsuir.sweider_b.banksystem.adminsclient.controllers.CurrentSessionHolder;
import by.bsuir.sweider_b.banksystem.adminsclient.views.panels.employees.ChangePasswordPane;
import by.bsuir.sweider_b.banksystem.shared.services.credits.CreditKindDO;
import by.bsuir.sweider_b.banksystem.shared.services.credits.CreditUpdateException;
import by.bsuir.sweider_b.banksystem.shared.services.credits.ICreditManagementService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
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
 * Created by sweid on 17.01.2016.
 */
@Component
public class ShowCreditsPane extends BorderPane {
    @Resource(name = "creditsManager")
    private ICreditManagementService creditManagementService;

    @Autowired
    private CurrentSessionHolder sessionHolder;

    @Autowired
    private ChangePasswordPane changePwdPane;

    @Autowired
    EditDescriptionPane editDescriptionPane;

    private List<CreditDataForTable> tableData;
    private TableView<CreditDataForTable> tableView;
    private ContextMenu currentContextMenu;

    private VBox mainContent;


    public ShowCreditsPane() {
        this.currentContextMenu = new ContextMenu();
        mainContent = new VBox();
        Label header = new Label("Активные кредиты");
        header.getStyleClass().add("page-header");

        this.createTableViewForData();

        mainContent.setPadding(new Insets(10, 10, 10, 10));
        mainContent.getChildren().addAll(header, tableView);
        this.setCenter(mainContent);
    }

    public void updateData(boolean isForActive) {
        try {
            List<CreditKindDO> data = this.creditManagementService.getCreditKinds(this.sessionHolder.getSessionId(), isForActive);
            this.tableData = data.stream().map(CreditDataForTable::new).collect(Collectors.toList());
            this.tableView.setItems(FXCollections.observableArrayList(this.tableData));
            this.updateContextMenu(isForActive);
        } catch (RemoteException e) {
            e.printStackTrace();
            AdministrationApp.showRmiExceptionWarning();
        }
    }

    private void updateContextMenu(boolean isForActive) {
        this.currentContextMenu.getItems().clear();
        this.currentContextMenu.getItems().addAll(this.getMenuItems(isForActive));
    }

    private void createTableViewForData() {
        TableView<CreditDataForTable> table = new TableView<>();
        TableColumn<CreditDataForTable, String> titleColumn = new TableColumn<>("Название");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<CreditDataForTable, Long> minColumn = new TableColumn<>("Минимальная сумма");
        minColumn.setCellValueFactory(new PropertyValueFactory<>("minAmount"));

        TableColumn<CreditDataForTable, Long> maxColumn = new TableColumn<>("Максимальная сумма");
        maxColumn.setCellValueFactory(new PropertyValueFactory<>("maxAmount"));

        TableColumn<CreditDataForTable, Integer> length = new TableColumn<>("Срок");
        length.setCellValueFactory(new PropertyValueFactory<>("length"));


        TableColumn<CreditDataForTable, Integer> percents = new TableColumn<>("Ставка %");
        percents.setCellValueFactory(new PropertyValueFactory<>("percents"));

        TableColumn<CreditDataForTable, Integer> count = new TableColumn<>("Выдано");
        count.setCellValueFactory(new PropertyValueFactory<>("count"));

        table.setRowFactory(param -> {
            final TableRow<CreditDataForTable> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(this.currentContextMenu)
            );

            return row;
        });

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(titleColumn, length, minColumn, maxColumn, count);

        this.tableView = table;
    }


    private MenuItem[] getMenuItems(boolean isForActive){
        MenuItem changeData = new MenuItem("Изменить описание");
        changeData.setOnAction(event -> this.showChangeDataForm(this.tableView.getSelectionModel().getSelectedItem().getBaseData()));

        MenuItem changeState = new MenuItem(isForActive ? "Деактивировать" : "Активировать");
        changeState.setOnAction(event -> this.sendChangeStateRequest(this.tableView.getSelectionModel().getSelectedItem(), !isForActive));
        return new MenuItem[]{changeData, changeState};
    }

    public void updateCredit(CreditKindDO data){
        for(CreditDataForTable item : this.tableView.getItems()){
            if(item.getBaseData().getId() == data.getId()){
                item.initProperties(data);
                break;
            }
        }

    }

    private void showChangeDataForm(CreditKindDO baseData) {
        this.editDescriptionPane.setData(baseData);
        this.setCenter(null);
        this.setLeft(this.editDescriptionPane);
    }

    private void sendChangeStateRequest(CreditDataForTable credit, boolean newState) {
        try {
            this.creditManagementService.changeCreditKindActiveState(this.sessionHolder.getSessionId(), credit.getBaseData().getId(), newState);
            this.tableView.getItems().remove(credit);
            this.showSuccessChangeStateMsg();
        } catch (RemoteException e) {
            e.printStackTrace();
            AdministrationApp.showRmiExceptionWarning();
        } catch (CreditUpdateException e) {
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

    public void activateMainPane() {
        this.setLeft(null);
        this.setCenter(this.mainContent);
    }

    public static class CreditDataForTable {
        private SimpleStringProperty name;
        private SimpleLongProperty minAmount;
        private SimpleLongProperty maxAmount;
        private SimpleIntegerProperty length;
        private SimpleIntegerProperty count;
        private SimpleIntegerProperty percents;
        private CreditKindDO baseData;

        public CreditDataForTable(CreditKindDO data) {
            initProperties(data);
        }

        private void initProperties(CreditKindDO data) {
            this.name = new SimpleStringProperty(data.getTitle());
            this.minAmount = new SimpleLongProperty(data.getMin());
            this.length = new SimpleIntegerProperty(data.getLenght());
            this.minAmount = new SimpleLongProperty(data.getMin());
            this.maxAmount = new SimpleLongProperty(data.getMax());
            this.count = new SimpleIntegerProperty(data.getCount());
            this.percents = new SimpleIntegerProperty(data.getPercent());
            this.baseData = data;
        }

        public int getPercents() {
            return percents.get();
        }

        public SimpleIntegerProperty percentsProperty() {
            return percents;
        }

        public void updateBaseData(CreditKindDO baseData) {
            this.initProperties(baseData);

        }

        public int getCount() {
            return count.get();
        }

        public SimpleIntegerProperty countProperty() {
            return count;
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public long getMinAmount() {
            return minAmount.get();
        }

        public SimpleLongProperty minAmountProperty() {
            return minAmount;
        }

        public long getMaxAmount() {
            return maxAmount.get();
        }

        public SimpleLongProperty maxAmountProperty() {
            return maxAmount;
        }

        public int getLength() {
            return length.get();
        }

        public SimpleIntegerProperty lengthProperty() {
            return length;
        }

        public CreditKindDO getBaseData() {
            return baseData;
        }
    }
}

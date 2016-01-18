package by.bsuir.sweider_b.banksystem.operatorgui.view.components;

import by.bsuir.sweider_b.banksystem.shared.services.credits.CreditKindDO;
import by.bsuir.sweider_b.banksystem.shared.services.customers.CustomerDO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sweid on 18.01.2016.
 */
public class TableViewForCredits extends TableView<TableViewForCredits.CreditDataForTable> {

    private ContextMenu contextMenu;

    public TableViewForCredits(){
        TableColumn<CreditDataForTable, String> titleCol = new TableColumn<>("Название");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<CreditDataForTable, Long> minCol = new TableColumn<>("Мин. сумма");
        minCol.setCellValueFactory(new PropertyValueFactory<>("minMoney"));

        TableColumn<CreditDataForTable, Long> maxCol = new TableColumn<>("Макс. сумма");
        maxCol.setCellValueFactory(new PropertyValueFactory<>("maxMoney"));

        TableColumn<CreditDataForTable, Integer> lengthCol = new TableColumn<>("Срок кредитования");
        lengthCol.setCellValueFactory(new PropertyValueFactory<>("length"));

        TableColumn<CreditDataForTable, Integer> percentCol = new TableColumn<>("Процент");
        percentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.getColumns().addAll(titleCol, minCol, maxCol, lengthCol, percentCol);
        contextMenu = new ContextMenu();


        this.setRowFactory(param -> {
            final TableRow<CreditDataForTable> row = new TableRow<>();
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(this.contextMenu)
            );

            return row;
        });
    }

    public void setItemsToDisplay(List<CreditKindDO> credits){
        this.getItems().clear();
        this.getItems().addAll(credits.stream().map(CreditDataForTable::new).collect(Collectors.toList()));
    }

    public void setConextMenuItems(javafx.scene.control.MenuItem... items){
        this.contextMenu.getItems().clear();
        this.contextMenu.getItems().addAll(items);
    }



    public static class CreditDataForTable {
        private SimpleStringProperty title;
        private SimpleLongProperty minMoney;
        private SimpleLongProperty maxMoney;
        private SimpleIntegerProperty length;
        private SimpleIntegerProperty percent;
        private CreditKindDO baseData;

        public CreditDataForTable(CreditKindDO data){
            this.title = new SimpleStringProperty(data.getTitle());
            this.minMoney = new SimpleLongProperty(data.getMin());
            this.maxMoney = new SimpleLongProperty(data.getMax());
            this.length = new SimpleIntegerProperty(data.getLenght());
            this.percent = new SimpleIntegerProperty(data.getPercent());
            this.baseData = data;
        }



        public CreditKindDO getBaseData() {
            return baseData;
        }

        public String getTitle() {
            return title.get();
        }

        public SimpleStringProperty titleProperty() {
            return title;
        }

        public void setTitle(String title) {
            this.title.set(title);
        }

        public long getMinMoney() {
            return minMoney.get();
        }

        public SimpleLongProperty minMoneyProperty() {
            return minMoney;
        }

        public long getMaxMoney() {
            return maxMoney.get();
        }

        public SimpleLongProperty maxMoneyProperty() {
            return maxMoney;
        }

        public int getLength() {
            return length.get();
        }

        public SimpleIntegerProperty lengthProperty() {
            return length;
        }

        public int getPercent() {
            return percent.get();
        }

        public SimpleIntegerProperty percentProperty() {
            return percent;
        }
    }
}

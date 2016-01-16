package by.bsuir.sweider_b.banksystem.adminsclient.views.components;

import by.bsuir.sweider_b.banksystem.shared.services.authentication.Delegate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

import java.util.HashMap;

/**
 * Created by sweid on 16.01.2016.
 */
public class LeftMenuAccordionPane extends TitledPane {
    private HashMap<String, Delegate> mapping;

    public LeftMenuAccordionPane(String title, HashMap<String, Delegate> content){
        this.setText(title);
        this.mapping = content;
        ListView<String> listView = getListView(content);
        this.setContent(listView);
    }

    private ListView<String> getListView(HashMap<String, Delegate> content) {
        ListView<String> listView = new ListView<>();
        listView.getStyleClass().add("left-menu_list-view");
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(content.keySet());
        listView.setItems(list);
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mapping.get(newValue).execute();
        });
        return listView;
    }


}

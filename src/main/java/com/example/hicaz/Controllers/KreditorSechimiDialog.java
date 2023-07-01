package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Kreditor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class KreditorSechimiDialog implements Initializable {

    @FXML
    private TableView<Kreditor> KreditorSiyahiTableView;
    @FXML
    private TableColumn<Kreditor,String> nr;
    @FXML
    private TableColumn<Kreditor,String> name;

    @FXML
    private TextField search;



    private DataSource ds;

    private static ObservableList<Kreditor> kreditorObservableList = FXCollections.observableArrayList();
    private static List<Kreditor> kreditorList = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        nr.setCellValueFactory(new PropertyValueFactory<>("nr"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        initMalList();
        KreditorSiyahiTableView.setItems(kreditorObservableList);

    }

    public Kreditor selectedKreditor(){
        return KreditorSiyahiTableView.getSelectionModel().getSelectedItem();
    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = search.getText();

            Search(kreditorObservableList,name);
        }
    }
    private void Search(ObservableList<Kreditor> kreditorObservableList, String name) {
        kreditorObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < kreditorList.size(); i++) {
            if (kreditorList.get(i).getName().toLowerCase().contains(lowercaseName)) {
                kreditorObservableList.add(kreditorList.get(i));
            }
        }
    }

    public void initMalList(){
        ds.open();
        kreditorList.clear();
        kreditorObservableList.clear();
        kreditorList =ds.queryKreditor();
        for (Kreditor kreditor : kreditorList){
            kreditorObservableList.add(kreditor);

        }
        ds.close();
    }
}

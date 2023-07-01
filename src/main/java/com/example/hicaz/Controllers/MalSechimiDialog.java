package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Kreditor;
import com.example.hicaz.model.Mal;
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

public class MalSechimiDialog implements Initializable {
    @FXML
    private TableView<Mal> malSiyahiTableView;
    @FXML
    private TableColumn<Mal,String> MalNr;
    @FXML
    private TableColumn<Mal,String> Mal;
    @FXML
    private TableColumn<Mal,String> Vahid;

    @FXML
    private TextField search;


    private DataSource ds;

    private static ObservableList<Mal> malObservableList= FXCollections.observableArrayList();
    private static List<Mal> malList = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        MalNr.setCellValueFactory(new PropertyValueFactory<>("Nr"));
        Mal.setCellValueFactory(new PropertyValueFactory<>("Ad"));
        Vahid.setCellValueFactory(new PropertyValueFactory<>("Vahid"));
        initMalList();
        malSiyahiTableView.setItems(malObservableList);

    }


    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = search.getText();

            Search(malObservableList,name);
        }
    }
    private void Search(ObservableList<Mal> malObservableList, String name) {
        malObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < malList.size(); i++) {
            if (malList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                malObservableList.add(malList.get(i));
            }
        }
    }

    public Mal selectedMal(){
        return malSiyahiTableView.getSelectionModel().getSelectedItem();
    }

    public void initMalList(){
        ds.open();
        malList.clear();
        malObservableList.clear();
        malList=ds.queryAllMal();
        for (com.example.hicaz.model.Mal mal : malList){
            malObservableList.add(mal);

        }
        ds.close();
    }
}

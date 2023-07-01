package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Mal;
import com.example.hicaz.model.Medaxil;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MalSiyahiController implements Initializable {
    @FXML
    private AnchorPane malSiyahiPane;

    @FXML
    private TableView<Mal> malTableView;
    @FXML
    private TableColumn<Mal,String> Nr;
    @FXML
    private TableColumn<Mal,String> malName;
    @FXML
    private TableColumn<Mal,String> Vahid;
    @FXML
    private TableColumn<Mal,String> ortaQiymet;

    @FXML
    private TextField searchField;

    private DataSource ds;

    private Pane pane;

    private static ObservableList<Mal> malObservableList= FXCollections.observableArrayList();
    private static List<Mal> malList= new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        Nr.setCellValueFactory(new PropertyValueFactory<>("Nr"));
        malName.setCellValueFactory(new PropertyValueFactory<>("Ad"));
        Vahid.setCellValueFactory(new PropertyValueFactory<>("Vahid"));
        ortaQiymet.setCellValueFactory(new PropertyValueFactory<>("OrtaGiymet"));
        init();
    }

    public void init(){
        ds.open();
        malList.clear();
        malObservableList.clear();
        malList=ds.queryAllMal();
        for (Mal mal:malList){
            malObservableList.add(mal);
        }
        malTableView.setItems(malObservableList);
    }
    private void Search(ObservableList<Mal> malObservableList, String malName) {
        malObservableList.clear();
        String lowercaseName = malName.toLowerCase();
        for (int i = 0; i < malList.size(); i++) {
            if (malList.get(i).getAd().toLowerCase().contains(lowercaseName)) {
                malObservableList.add(malList.get(i));
            }
        }
    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = searchField.getText();

            Search(malObservableList,name);
        }
    }

    public void createYeniMal(){
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/example/hicaz/NewMal.fxml"));
            pane = loader.load();
            setNode(pane);

        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        Mal selectedItem = malTableView.getSelectionModel().getSelectedItem();
        if (selectedItem!=null){
            if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)){
                deleteItem(selectedItem);
            }
        }
    }
    public void  deleteItem(Mal mal){
        try {
            ds.open();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Malı silmək");
            alert.setHeaderText("Mal nömrəsi: " + mal.getNr()+"\n Mal adı: " + mal+ "\n Orta məbləğ: " + mal.getOrtaGiymet());
            alert.setContentText("Əminsiniz?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                ds.deleteMal(mal.getNr());
                ds.close();
                init();
                return;
            }
            ds.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setNode(Node node) {
        malSiyahiPane.getChildren().clear();
        malSiyahiPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

}

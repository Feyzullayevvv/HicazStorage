package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Mal;
import com.example.hicaz.model.MedaxilFaktura;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewMalController implements Initializable {

    @FXML
    private AnchorPane newMalPane;
    @FXML
    private TableView<Mal> newMalTableView;
    @FXML
    private TableColumn<Mal,String> malName;
    @FXML
    private TableColumn<Mal,String> malVahid;
    @FXML
    private TableColumn<Mal,String> malOrtaGiymet;

    @FXML
    private TextField malAdiTextField;
    @FXML
    private TextField vahidTextField;
    @FXML
    private TextField ortaQiymetTextField;
    @FXML
    private Button addButton;

    @FXML
    private Button bitirButton;
    private DataSource ds;
    private Pane pane;


    private static ObservableList<Mal> malObservableList= FXCollections.observableArrayList();
    private static List<Mal> malList= new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        malName.setCellValueFactory(new PropertyValueFactory<>("Ad"));
        malVahid.setCellValueFactory(new PropertyValueFactory<>("Vahid"));
        malOrtaGiymet.setCellValueFactory(new PropertyValueFactory<>("OrtaGiymet"));
        malAdiTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        vahidTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        ortaQiymetTextField.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        initList();
    }



    public void onElaveEtClicked(){
        Mal mal= new Mal();
        mal.setAd(malAdiTextField.getText());
        mal.setVahid(vahidTextField.getText());
        mal.setOrtaGiymet(Double.parseDouble(ortaQiymetTextField.getText()));
        malList.add(mal);
        initList();
    }
    public boolean areFieldsValid() {
        String qiymetText = ortaQiymetTextField.getText();

        if (qiymetText.isEmpty()) {
            return false;
        }

        try {
            double qiymetValue = Double.parseDouble(qiymetText);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void updateElaveEtButtonState() {
        if (malAdiTextField.getText().isEmpty() || vahidTextField.getText().isEmpty() || ortaQiymetTextField.getText().isEmpty() || !areFieldsValid()){
            addButton.setDisable(true);
        }else{

            addButton.setDisable(false);
        }
    }
    public void initList(){
        malObservableList.clear();
        for (Mal mal:malList){
            malObservableList.add(mal);
        }
        newMalTableView.setItems(malObservableList);
        if (malObservableList.isEmpty()) {
            bitirButton.setDisable(true);
        } else {
            bitirButton.setDisable(false);
        }
    }
    public void handleBitti(){
        ds.open();
        for (Mal mal:malObservableList){
            ds.insertMal(mal.getAd(),mal.getVahid(),mal.getOrtaGiymet());
        }

        try {
            malList.clear();
            malObservableList.clear();
            pane= FXMLLoader.load(getClass().getResource("/com/example/hicaz/MalSiyahi.fxml"));
            setNode(pane);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void handleBack(){
        try {
            malList.clear();
            malObservableList.clear();
            pane= FXMLLoader.load(getClass().getResource("/com/example/hicaz/MalSiyahi.fxml"));
            setNode(pane);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void handleDelete(){
        Mal selectedItem = newMalTableView.getSelectionModel().getSelectedItem();
        try {
            malList.remove(selectedItem);
            initList();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void setNode(Node node) {
        newMalPane.getChildren().clear();
        newMalPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }
}

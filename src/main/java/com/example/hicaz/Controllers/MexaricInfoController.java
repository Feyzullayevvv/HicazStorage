package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Mexaric;
import com.example.hicaz.model.MexaricFaktura;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MexaricInfoController implements Initializable {
    @FXML
    private AnchorPane mexaricInfoPane;

    @FXML
    private TextField tarixTextField;
    @FXML
    private TextField tehvilAlanTextField;

    @FXML
    private TableView<MexaricFaktura> mexaricInfoTableView;
    @FXML
    private TableColumn<MexaricFaktura,String> MalNr;
    @FXML
    private TableColumn<MexaricFaktura,String> Mal;
    @FXML
    private TableColumn<MexaricFaktura,String> Vahid;
    @FXML
    private TableColumn<MexaricFaktura,String> Ceki;


    private Pane pane;

    private Mexaric mexaric;
    private DataSource ds;
    private static ObservableList<MexaricFaktura> mexaricFakturaObservableList = FXCollections.observableArrayList();
    private static List<MexaricFaktura> mexaricFakturaList = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds=new DataSource();
        MalNr.setCellValueFactory(new PropertyValueFactory<>("MalNr"));
        Mal.setCellValueFactory(new PropertyValueFactory<>("Mal"));
        Vahid.setCellValueFactory(new PropertyValueFactory<>("Vahid"));
        Ceki.setCellValueFactory(new PropertyValueFactory<>("Ceki"));


        tarixTextField.setText(mexaric.getTarix());
        tehvilAlanTextField.setText(mexaric.getTehvilAlan());
        initList();
        mexaricInfoTableView.setItems(mexaricFakturaObservableList);

    }

    public void setMexaric(Mexaric mexaric){
        this.mexaric =mexaric;
    }

    private void setNode(Node node) {
        mexaricInfoPane.getChildren().clear();
        mexaricInfoPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }
    public void initList(){
        ds.open();

        mexaricFakturaList.clear();
        mexaricInfoTableView.setItems(mexaricFakturaObservableList);
        mexaricFakturaList =ds.queryMexaricItems(mexaric.getNr());
        ds.close();
        for (MexaricFaktura mexaricFaktura: mexaricFakturaList){
            mexaricFakturaObservableList.add(mexaricFaktura);
        }


    }


    public void handleBack(){
        try {
            pane= FXMLLoader.load(getClass().getResource("/com/example/hicaz/Mexaric.fxml"));
            setNode(pane);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

}

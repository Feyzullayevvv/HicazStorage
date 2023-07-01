package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.AnbarItem;
import com.example.hicaz.model.MexaricFaktura;
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
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewMexaricController implements Initializable {

    @FXML
    private TableView<MexaricFaktura> newMexaricTableView;
    @FXML
    private TableColumn<MexaricFaktura,String> MalNr;
    @FXML
    private TableColumn<MexaricFaktura,String> Mal;
    @FXML
    private TableColumn<MexaricFaktura,String> Vahid;
    @FXML
    private TableColumn<MexaricFaktura,String> Ceki;


    @FXML
    private AnchorPane newMexaricPane;

    @FXML
    private TextField malText;
    @FXML
    private TextField cekitext;

    @FXML
    private TextField vahidText;
    @FXML
    private TextField anbarText;


    @FXML
    private TextField tehvilAlanText;

    @FXML
    private Button elaveEtButton;
    @FXML
    private Button bittiButton;
    private DataSource ds;



    private Pane pane;

    private static ObservableList<MexaricFaktura> mexaricFakturaObservableList = FXCollections.observableArrayList();
    private static List<MexaricFaktura> mexaricFakturaList = new ArrayList<>();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds=new DataSource();
        MalNr.setCellValueFactory(new PropertyValueFactory<>("MalNr"));
        Mal.setCellValueFactory(new PropertyValueFactory<>("Mal"));
        Vahid.setCellValueFactory(new PropertyValueFactory<>("Vahid"));
        Ceki.setCellValueFactory(new PropertyValueFactory<>("Ceki"));

        initList();
        malText.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        cekitext.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        tehvilAlanText.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
    }


    public void createMalSiyahi(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(newMexaricPane.getScene().getWindow());
        dialog.setTitle("Mal seçmək");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/hicaz/MalSiyahiDiaglog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        MalSechimiDialog controller = fxmlLoader.getController();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result= dialog.showAndWait();
        if ((result.isPresent() && result.get()== ButtonType.OK) && controller.selectedMal()!=null){
            malText.setText(controller.selectedMal().getAd());
            vahidText.setText(controller.selectedMal().getVahid());
            ds.open();
            AnbarItem item= ds.getItem(controller.selectedMal().getAd());
            ds.close();
            anbarText.setText(String.valueOf(item.getCeki()));
        }

    }


    public void initList(){
        mexaricFakturaObservableList.clear();
        for (MexaricFaktura mexaricFaktura: mexaricFakturaList){
            mexaricFakturaObservableList.add(mexaricFaktura);
        }
        newMexaricTableView.setItems(mexaricFakturaObservableList);
        if (mexaricFakturaObservableList.isEmpty()) {
            bittiButton.setDisable(true);
        } else {
            bittiButton.setDisable(false);
        }
    }

    private void updateElaveEtButtonState() {
        if (malText.getText().isEmpty() || cekitext.getText().isEmpty()  || tehvilAlanText.getText().isEmpty() || Double.parseDouble(cekitext.getText())>Double.parseDouble(anbarText.getText())){
            elaveEtButton.setDisable(true);
        }else{
            elaveEtButton.setDisable(false);
        }
    }





    public void onElaveEtClicked(){
        MexaricFaktura mexaricFaktura= new MexaricFaktura();
        ds.open();
        com.example.hicaz.model.Mal mal=ds.getMal(malText.getText());

        mexaricFaktura.setMalNr(mal.getNr());
        mexaricFaktura.setMal(mal.getAd());
        mexaricFaktura.setVahid(mal.getVahid());
        mexaricFaktura.setCeki(Double.parseDouble(cekitext.getText()));

        mexaricFakturaList.add(mexaricFaktura);
        initList();
        malText.clear();
        cekitext.clear();
        vahidText.clear();


        ds.close();
    }

    public void handleDelete(){
        MexaricFaktura selectedItem = newMexaricTableView.getSelectionModel().getSelectedItem();
        try {
            mexaricFakturaList.remove(selectedItem);
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

    public void handleBitti(){
        ds.open();
        int n =ds.insertMexaric(tehvilAlanText.getText());
        System.out.println(tehvilAlanText.getText());
        System.out.println(n);
        if(n!=-1){
            for (MexaricFaktura mexaricFaktura: mexaricFakturaList){
                ds.insertMexaricFaktura(mexaricFaktura.getMal(), mexaricFaktura.getCeki(),n);
            }
            ds.close();
        }

        try {
            mexaricFakturaList.clear();
            mexaricFakturaObservableList.clear();
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
    private void setNode(Node node) {
        newMexaricPane.getChildren().clear();
        newMexaricPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void handleBack(){
        try {
            mexaricFakturaList.clear();
            mexaricFakturaObservableList.clear();
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

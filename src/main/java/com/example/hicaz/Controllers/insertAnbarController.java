package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.AnbarItem;
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
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class insertAnbarController implements Initializable {

    @FXML
    private AnchorPane insertAnbarPane;

    @FXML
    private TextField malText;
    @FXML
    private TextField miqdarText;
    @FXML
    private TextField vahidText;
    @FXML
    private TextField meblegText;

    @FXML
    private Button elaveEtButton;

    @FXML
    private Button bittiButton;

    @FXML
    private TableView<AnbarItem> anbarItemsTableView;

    @FXML
    private TableColumn<AnbarItem,String> Mal;
    @FXML
    private TableColumn<AnbarItem,String> Vahid;
    @FXML
    private TableColumn<AnbarItem,String> Miqdar;
    @FXML
    private TableColumn<AnbarItem,String> Mebleg;


    private Pane pane;

    private static ObservableList<AnbarItem> anbarItemObservableList= FXCollections.observableArrayList();
    private static List<AnbarItem> anbarItemList= new ArrayList<>();

    private DataSource ds;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        Mal.setCellValueFactory(new PropertyValueFactory<>("Mal"));
        Vahid.setCellValueFactory(new PropertyValueFactory<>("Vahid"));
        Miqdar.setCellValueFactory(new PropertyValueFactory<>("Ceki"));
        Mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        initList();
        malText.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        miqdarText.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
        meblegText.textProperty().addListener((observable, oldValue, newValue) -> updateElaveEtButtonState());
    }
    public void createMalSiyahi(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(insertAnbarPane.getScene().getWindow());
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
        }
    }
    public boolean areFieldsValid() {
        String miqdarTextText = miqdarText.getText();
        String meblegtest = meblegText.getText();

        if (miqdarTextText.isEmpty()|| meblegtest.isEmpty()) {
            return false;
        }

        try {
            double qiymetValue = Double.parseDouble(miqdarTextText);
            double meblegvalue = Double.parseDouble(meblegtest);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void updateElaveEtButtonState() {
        if (malText.getText().isEmpty() || miqdarText.getText().isEmpty() || meblegText.getText().isEmpty() || !areFieldsValid()){
            elaveEtButton.setDisable(true);
        }else{
            elaveEtButton.setDisable(false);
        }
    }
    public void initList(){
        anbarItemObservableList.clear();
        for (AnbarItem anbarItem:anbarItemList){
            anbarItemObservableList.add(anbarItem);
        }
        anbarItemsTableView.setItems(anbarItemObservableList);
        if (anbarItemObservableList.isEmpty()) {
            bittiButton.setDisable(true);
        } else {
            bittiButton.setDisable(false);
        }
    }
    public void handleBitti(){
        ds.open();
        for (AnbarItem anbarItem : anbarItemList){
            ds.insertAnbar(anbarItem.getMal(),anbarItem.getCeki(),anbarItem.getMebleg());
        }
        ds.close();

        try {
            anbarItemList.clear();
            anbarItemObservableList.clear();
            pane= FXMLLoader.load(getClass().getResource("/com/example/hicaz/Anbar.fxml"));
            setNode(pane);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public void onElaveEtClicked(){
        AnbarItem anbarItem= new AnbarItem();
        ds.open();
        com.example.hicaz.model.Mal mal=ds.getMal(malText.getText());
        anbarItem.setMal(mal.getAd());
        anbarItem.setVahid(mal.getVahid());
        anbarItem.setCeki(Double.parseDouble(miqdarText.getText()));
        anbarItem.setMebleg(Double.parseDouble(meblegText.getText()));
        anbarItemList.add(anbarItem);
        initList();
        malText.clear();
        miqdarText.clear();
        vahidText.clear();
        meblegText.clear();
        ds.close();
    }
    public void handleDelete(){
        AnbarItem selectedItem = anbarItemsTableView.getSelectionModel().getSelectedItem();
        try {
            anbarItemList.remove(selectedItem);
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
        insertAnbarPane.getChildren().clear();
        insertAnbarPane.getChildren().add(node);
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
            anbarItemList.clear();
            anbarItemObservableList.clear();
            pane= FXMLLoader.load(getClass().getResource("/com/example/hicaz/Anbar.fxml"));
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

package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Kreditor;
import com.example.hicaz.model.Mexaric;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class KreditorController implements Initializable {
    @FXML
    private AnchorPane kreditorPane;
    @FXML
    private TableView<Kreditor> kreditorTableView;
    @FXML
    private TableColumn<Kreditor,String> nr;
    @FXML
    private TableColumn<Kreditor,String> name;
    @FXML
    private TextField searchBar;
    private DataSource ds;


    private static ObservableList<Kreditor> kreditorObservableList= FXCollections.observableArrayList();
    private static List<Kreditor> kreditorList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        nr.setCellValueFactory(new PropertyValueFactory<>("nr"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        initList();

    }

    public void initList(){
        kreditorObservableList.clear();
        kreditorList.clear();
        ds.open();
        kreditorList= ds.queryKreditor();
        for (Kreditor kreditor: kreditorList){
            kreditorObservableList.add(kreditor);
        }
        kreditorTableView.setItems(kreditorObservableList);
    }


    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = searchBar.getText();

            Search(kreditorObservableList,name);
        }
    }
    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (kreditorTableView.getSelectionModel().getSelectedItem() != null) {
                Kreditor selectedItem = kreditorTableView.getSelectionModel().getSelectedItem();
                KreditorInfoController kreditorInfoController = new KreditorInfoController();
                kreditorInfoController.setKreditor(selectedItem);
                createKreditorInfo(kreditorInfoController);
            }
        }
    }
    public void createKreditorInfo(KreditorInfoController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/KreditorInfo.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            setNode(root);
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void setNode(Node node) {
        kreditorPane.getChildren().clear();
        kreditorPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }
    public void createNewKreditor(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(kreditorPane.getScene().getWindow());
        dialog.setTitle("Yeni Müştəri");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/example/hicaz/newKreditor.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        NewKreditorController controller = fxmlLoader.getController();

        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        controller.getKreditorName().textProperty().addListener((observable, oldValue, newValue) -> {
            updateOkButtonDisableProperty(okButton,controller.getKreditorName().getText());
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            controller.insertCustomer();
            initList();
        }

    }
    private void updateOkButtonDisableProperty(Button okButton, String kreditorName) {
        boolean disableButton = kreditorName.isEmpty();
        okButton.setDisable(disableButton);
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



}

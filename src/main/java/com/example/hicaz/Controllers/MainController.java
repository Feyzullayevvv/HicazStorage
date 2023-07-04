package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Optional;

public class MainController {
    @FXML
    private AnchorPane holdPane;
    private AnchorPane Pane;





    public void handleExit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Çıxmağa Əminsiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get()==ButtonType.OK){
            System.exit(0);
        }
    }



    public void showLoggedInUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/hicaz/MainMenu.fxml"));
            Scene scene= new Scene(fxmlLoader.load(),1000,800);
            fxmlLoader.setController(this);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Hicaz");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            e.printStackTrace();
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void setNode(Node node) {
        holdPane.getChildren().clear();
        holdPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void createMedaxil(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/Medaxil.fxml"));
            Pane = loader.load();
            setNode(Pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
    public void createMexaric(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/Mexaric.fxml"));
            Pane = loader.load();
            setNode(Pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void createMalSiyahi(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/MalSiyahi.fxml"));
            Pane = loader.load();
            setNode(Pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void createAnbar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/Anbar.fxml"));
            Pane = loader.load();
            setNode(Pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void createKreditor(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/Kreditor.fxml"));
            Pane = loader.load();
            setNode(Pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void createDashBoard(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/dashboard.fxml"));
            Pane = loader.load();
            setNode(Pane);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }






}

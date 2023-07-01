package com.example.hicaz.Controllers;


import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.User;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;


public class LoginController {
    @FXML
    private TextField userName;

    @FXML
    private PasswordField passWord;


    @FXML
    private Label wronguserinput;

    private List<User> users;

    private DataSource ds;


    @FXML
    public void handleExit(){
        System.exit(0);
    }



    public boolean login(){
        ds= new DataSource();
        ds.open();
        users =ds.queryUsers();
        String username= userName.getText().trim();
        String password= passWord.getText().trim();
        for (User user : users){
            if (username.equals(user.getAd()) && password.equals(user.getPassword())){
                ds.close();
                Stage loginStage = (Stage) userName.getScene().getWindow();
                loginStage.close();
                MainController controller = new MainController();
                controller.showLoggedInUI();
                return true;
            }

        }
        applyShakeAnimation(passWord);
        ds.close();
        return false;


    }
    private void applyShakeAnimation(PasswordField field) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0), new KeyValue(field.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(100), new KeyValue(field.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(200), new KeyValue(field.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(300), new KeyValue(field.translateXProperty(), -10)),
                new KeyFrame(Duration.millis(400), new KeyValue(field.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(500), new KeyValue(field.translateXProperty(), 0))
        );
        timeline.play();
        timeline.setOnFinished(event -> field.getStyleClass().remove("shake"));
        field.getStyleClass().add("shake");
    }

}

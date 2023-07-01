package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Kreditor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewKreditorController implements Initializable {
    private Kreditor kreditor;

    @FXML
    private TextField kreditorName;

    private DataSource ds;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
    }

    public TextField getKreditorName() {
        return kreditorName;
    }


    public void insertCustomer(){
        try {
            ds.open();
            ds.insertKreditor(kreditorName.getText());
            ds.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

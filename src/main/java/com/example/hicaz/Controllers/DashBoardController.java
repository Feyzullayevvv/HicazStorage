package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.AnbarItem;
import com.example.hicaz.model.Medaxil;
import com.example.hicaz.model.Mexaric;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DashBoardController  implements Initializable {
    @FXML
    private TextField anbarQaliq;

    @FXML
    private TextField medaxilSayi;

    @FXML
    private TextField mexaricSayi;

    private  double anbarValue=0;
    private  int medaxilCount=0;
    private  int mexaricCount=0;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");




    private DataSource ds;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds = new DataSource();
        initFields();
    }

    private void initFields(){
        ds.open();
        List<AnbarItem> anbarItemList=ds.queryAnbarItems();
        for (AnbarItem anbarItem : anbarItemList){
            anbarValue+=anbarItem.getMebleg();
        }
        List<Medaxil> medaxilList = ds.queryMedaxil();
        LocalDate today = LocalDate.now();

        for (Medaxil medaxil :medaxilList){
            try {
                Date saleDate = dateFormatter.parse(medaxil.getTarix());
                LocalDate saleLocalDate = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (saleLocalDate.isEqual(today)){
                    medaxilCount+=1;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        List<Mexaric> mexaricList = ds.queryMexaric();
        for (Mexaric mexaric :mexaricList){
            try {
                Date saleDate = dateFormatter.parse(mexaric.getTarix());
                LocalDate saleLocalDate = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (saleLocalDate.isEqual(today)){
                    mexaricCount+=1;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ds.close();
        anbarQaliq.setText(String.valueOf(anbarValue));
        medaxilSayi.setText(String.valueOf(medaxilCount));
        mexaricSayi.setText(String.valueOf(mexaricCount));
    }
}

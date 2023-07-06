package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.Medaxil;
import com.example.hicaz.model.Mexaric;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.control.Alert;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class MexaricController implements Initializable {

    @FXML
    private AnchorPane mexaricPanel;
    @FXML
    private TableView<Mexaric> mexaricView;
    @FXML
    private TableColumn<Mexaric,String> Nr;
    @FXML
    private TableColumn<Mexaric,String> Tarix;
    @FXML
    private TableColumn<Mexaric,String> TehvilAlan;

    @FXML
    private TextField search;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ToggleButton toggleButton;
    @FXML
    private Button printButton;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private AnchorPane Pane;

    private DataSource ds;

    private static ObservableList<Mexaric> mexaricObservableList = FXCollections.observableArrayList();

    private static List<Mexaric> mexaricList = new ArrayList<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        ds= new DataSource();
        Nr.setCellValueFactory(new PropertyValueFactory<>("Nr"));
        Tarix.setCellValueFactory(new PropertyValueFactory<>("Tarix"));
        TehvilAlan.setCellValueFactory(new PropertyValueFactory<>("TehvilAlan"));
        initMexaricList();
        mexaricView.setItems(mexaricObservableList);
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMexaricBetweenDates());
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> showMexaricBetweenDates());
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            showTodaysSales(newValue);
        });


    }
    public void handlePrintButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Table Data");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Nr");
                headerRow.createCell(1).setCellValue("Tarix");
                headerRow.createCell(2).setCellValue("Tehvil Alan");

                int rowIndex = 1;
                for (Mexaric mexaric : mexaricObservableList) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(mexaric.getNr());
                    row.createCell(1).setCellValue(mexaric.getTarix());
                    row.createCell(2).setCellValue(mexaric.getTehvilAlan());
                }

                for (int i = 0; i < 3; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Print");
                alert.setHeaderText(null);
                alert.setContentText("Table data printed to Excel successfully.");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("An error occurred while printing table data to Excel.");
                alert.showAndWait();
            }
        }
    }


    public void initMexaricList(){
        ds.open();
        mexaricList.clear();
        mexaricObservableList.clear();
        mexaricList =ds.queryMexaric();
        for (Mexaric mexaric: mexaricList){
            mexaricObservableList.add(mexaric);
        }
    }

    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = search.getText();

            Search(mexaricObservableList,name);
        }
    }
    private void Search(ObservableList<Mexaric> mexaricObservableList, String name) {
        mexaricObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < mexaricList.size(); i++) {
            if (mexaricList.get(i).getTehvilAlan().toLowerCase().contains(lowercaseName)) {
                mexaricObservableList.add(mexaricList.get(i));
            }
        }
    }
    public void showMexaricBetweenDates() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            try {
                Date startDateObj = dateFormatter.parse(startDate.toString());
                Date endDateObj = dateFormatter.parse(endDate.toString());

                List<Mexaric> filteredSales = mexaricList.stream()
                        .filter(mexaric-> {
                            try {
                                Date paymentDate = dateFormatter.parse(mexaric.getTarix());
                                return (paymentDate.equals(startDateObj) || paymentDate.after(startDateObj))
                                        && (paymentDate.before(endDateObj) || paymentDate.equals(endDateObj));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                mexaricObservableList.clear();

                mexaricObservableList.addAll(filteredSales);
            } catch (Exception e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Xəta");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void showTodaysSales(boolean showTodaySales) {
        try {
            LocalDate today = LocalDate.now();
            mexaricObservableList.clear();
            if (showTodaySales) {
                List<Mexaric> filteredSales = mexaricList.stream()
                        .filter(mexaric -> {
                            try {
                                Date saleDate = dateFormatter.parse(mexaric.getTarix());
                                LocalDate saleLocalDate = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                return saleLocalDate.isEqual(today);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                mexaricObservableList.addAll(filteredSales);
            } else {
                mexaricObservableList.addAll(mexaricList);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void setNode(Node node) {
        mexaricPanel.getChildren().clear();
        mexaricPanel.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }

    public void createNewMexaric(){
        try {
            FXMLLoader  loader= new FXMLLoader(getClass().getResource("/com/example/hicaz/NewMexaric.fxml"));
            Pane = loader.load();
            setNode(Pane);

        }catch (Exception e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
    public void handleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (mexaricView.getSelectionModel().getSelectedItem() != null) {
                Mexaric selectedItem = mexaricView.getSelectionModel().getSelectedItem();
                MexaricInfoController mexaricInfoController = new MexaricInfoController();
                mexaricInfoController.setMexaric(selectedItem);
                createMexaricInfo(mexaricInfoController);
            }
        }
    }
    public void  deleteItem(Mexaric mexaric){
        try {
            ds.open();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Məxarici silmək");
            alert.setHeaderText("Tarix: " + mexaric.getTarix()+ "\nTəhvil alan : " + mexaric.getTehvilAlan());
            alert.setContentText("Əminsiniz?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                ds.deleteMexaric(mexaric.getNr());
                ds.close();
                initMexaricList();
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
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        Mexaric selectedItem = mexaricView.getSelectionModel().getSelectedItem();
        if (selectedItem!=null){
            if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)){
                deleteItem(selectedItem);
            }
        }
    }
    public void createMexaricInfo(MexaricInfoController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/hicaz/MexaricInfo.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            setNode(root);
        } catch (Exception e){
            System.out.println(e.getMessage());
            Alert  alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Xəta");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }




}

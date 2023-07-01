package com.example.hicaz.Controllers;

import com.example.hicaz.data.DataSource;
import com.example.hicaz.model.AnbarItem;
import com.example.hicaz.model.Medaxil;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AnbarController implements Initializable {

    @FXML
    private AnchorPane anbarPane;
    @FXML
    private TextField searchBar;

    @FXML
    private TableView<AnbarItem> anbarTableView;
    @FXML
    private TableColumn<AnbarItem,String> Nr;
    @FXML
    private TableColumn<AnbarItem,String> mal;
    @FXML
    private TableColumn<AnbarItem,String> vahid;
    @FXML
    private TableColumn<AnbarItem,String> miqdar;
    @FXML
    private TableColumn<AnbarItem,String> mebleg;

    private Pane Pane;

    private DataSource ds;

    private static ObservableList<AnbarItem> anbarItemObservableList= FXCollections.observableArrayList();
    private static List<AnbarItem> anbarItemList= new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ds= new DataSource();
        Nr.setCellValueFactory(new PropertyValueFactory<>("Nr"));
        mal.setCellValueFactory(new PropertyValueFactory<>("Mal"));
        vahid.setCellValueFactory(new PropertyValueFactory<>("Vahid"));
        miqdar.setCellValueFactory(new PropertyValueFactory<>("Ceki"));
        mebleg.setCellValueFactory(new PropertyValueFactory<>("mebleg"));
        initList();
    }

    public void initList(){
        anbarItemObservableList.clear();
        anbarItemList.clear();
        ds.open();
        anbarItemList=ds.queryAnbarItems();
        for (AnbarItem anbarItem: anbarItemList){
            anbarItemObservableList.add(anbarItem);
        }
        anbarTableView.setItems(anbarItemObservableList);
    }
    private void setNode(Node node) {
        anbarPane.getChildren().clear();
        anbarPane.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();

    }
    public void handleKeyReleased(KeyEvent event){
        if (event.getEventType() == KeyEvent.KEY_RELEASED){
            String name = searchBar.getText();

            Search(anbarItemObservableList,name);
        }
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
                headerRow.createCell(1).setCellValue("Mal");
                headerRow.createCell(2).setCellValue("Vahid");
                headerRow.createCell(3).setCellValue("Ceki");
                headerRow.createCell(4).setCellValue("Mebleg");

                int rowIndex = 1;
                for (AnbarItem anbarItem : anbarItemObservableList) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(anbarItem.getNr());
                    row.createCell(1).setCellValue(anbarItem.getMal());
                    row.createCell(2).setCellValue(anbarItem.getVahid());
                    row.createCell(3).setCellValue(anbarItem.getCeki());
                    row.createCell(4).setCellValue(anbarItem.getMebleg());
                }

                for (int i = 0; i < 5; i++) {
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
    public void createNewInsertAnbar(){
        try {
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/example/hicaz/insertAnbar.fxml"));
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
    private void Search(ObservableList<AnbarItem> anbarItemObservableList, String name) {
        anbarItemObservableList.clear();
        String lowercaseName = name.toLowerCase();
        for (int i = 0; i < anbarItemList.size(); i++) {
            if (anbarItemList.get(i).getMal().toLowerCase().contains(lowercaseName)) {
                anbarItemObservableList.add(anbarItemList.get(i));
            }
        }
    }

}

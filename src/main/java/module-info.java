module com.example.hicaz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;


    opens com.example.hicaz.Controllers to javafx.fxml;
    opens com.example.hicaz.model to javafx.base;
    exports com.example.hicaz;
    exports com.example.hicaz.Controllers;

}
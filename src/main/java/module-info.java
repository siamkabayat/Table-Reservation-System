module de.sbayat.sbtablereservationmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens de.sbayat.sbtablereservationmanagementsystem to javafx.fxml;
    exports de.sbayat.sbtablereservationmanagementsystem;
    exports de.sbayat.sbtablereservationmanagementsystem.gui;
    opens de.sbayat.sbtablereservationmanagementsystem.gui to javafx.fxml;
}
package de.sbayat.sbtablereservationmanagementsystem.gui.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtility {

    public static final String CONFIRM_ACTION_TITLE   = "Confirm Action";
    public static final String EDIT_ALERT_TITLE       = "Fill All";
    public static final String FILL_ALL               = "Please fill all of the Input fields.";
    public static final String FILL_DATE_TIME_SIZE    = "Fill Party Size, Date and Time to find a table.";
    public static final String FILL_REQUIRED_FIELDS   = "Fill Required Fields";
    public static final String INVALID_GUESTS         = "Invalid Number of Guests";
    public static final String INVALID_DATE           = "Invalid Date";
    public static final String INVALID_PHONE          = "Invalid Phone Number";
    public static final String INVALID_GUESTS_MESSAGE = "Please enter a valid number for the guests.";
    public static final String INVALID_DATE_MESSAGE   = "Please enter the date in YYYY-MM-DD format.";
    public static final String INVALID_PHONE_MESSAGE  = "Phone number should only contain numbers.";


    public static void showInputIsNotValidAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public static void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean isActionConfirmed(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(CONFIRM_ACTION_TITLE);
        alert.setHeaderText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void showMessage(String message, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
        alert.setContentText(content);

        alert.showAndWait();
    }
}

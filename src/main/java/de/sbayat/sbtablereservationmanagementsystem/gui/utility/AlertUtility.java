package de.sbayat.sbtablereservationmanagementsystem.gui.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtility {

    private static final String EDIT_ALERT_TITLE = "Fill All";
    private static final String FILL_ALL = "Please fill all of the Input fields.";
    private static final String CONFIRM_ACTION_TITLE = "Confirm Action";


    public static void showInputIsNotValidAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(EDIT_ALERT_TITLE);
        alert.setHeaderText(null);
        alert.setContentText(FILL_ALL);
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

    public static void showMessage(String message, String content ){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(message);
        alert.setContentText(content);

        alert.showAndWait();
    }
}

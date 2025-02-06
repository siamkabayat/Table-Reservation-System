package de.sbayat.sbtablereservationmanagementsystem.gui;

import de.sbayat.sbtablereservationmanagementsystem.gui.utility.AlertUtility;
import de.sbayat.sbtablereservationmanagementsystem.logic.db.DbManager;
import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryWindowController implements Initializable {

    private static final String CONFIRM_DELETE        = "Are you sure you want to delete the Reservation with ID number %d?";
    private static final String RESOURCE              = "/de/sbayat/sbtablereservationmanagementsystem/reservation-window.fxml";
    private static final String NO_SELECTION_ALERT_TITLE = "No Reservation Selected";
    private static final String EDIT_STAGE_TITLE      = "Edit Reservation";
    private static final String NEW_RESERVATION_TITLE = "New Reservation";
    private static final String SELECT_TO_EDIT        = "Please select a reservation to edit.";
    private static final String SELECT_TO_DELETE      = "Please select a reservation to delete.";

    @FXML
    private ListView<Reservation> reservationsListView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReservationsFromDatabase();
        customizeListView();
    }

    @FXML
    public void handleAddButton(ActionEvent actionEvent) throws IOException {
        openReservationForm(false, null); // false → Add mode, no existing reservation
    }

    @FXML
    public void handleEditButton(ActionEvent actionEvent) throws IOException {
        Reservation selectedReservation = getSelectedReservation(); // Get the selected reservation
        if (selectedReservation != null) {
            openReservationForm(true, selectedReservation); // true → Edit mode, pass existing reservation
        } else {
            AlertUtility.showWarningAlert(NO_SELECTION_ALERT_TITLE, SELECT_TO_EDIT);
        }
    }

    private void openReservationForm(boolean isEditMode, Reservation currentSelectedReservation) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE));

            ReservationController controller = new ReservationController(isEditMode, currentSelectedReservation);
            loader.setController(controller);

            Parent root = loader.load();

            controller.setAddReservationCallback(reservation -> {
                // Add new reservation to the ListView
                reservationsListView.getItems().add(reservation);
                loadReservationsFromDatabase(); // Refresh the list
            });

            Stage reservationStage = new Stage();
            reservationStage.initModality(Modality.APPLICATION_MODAL);
            reservationStage.setScene(new Scene(root, 380, 650));
            if (isEditMode) {
                reservationStage.setTitle(EDIT_STAGE_TITLE);
            } else {
                reservationStage.setTitle(NEW_RESERVATION_TITLE);
            }
            reservationStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void customizeListView() {
        reservationsListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if (empty || reservation == null) {
                    setText(null);
                } else {
                    // Customize how each reservation appears in the list
                    setText(reservation.showReservationInfo());
                }
            }
        });
    }

    private void loadReservationsFromDatabase() {
        // Clear the ListView before reloading data
        reservationsListView.getItems().clear();

        // Populate the ListView
        reservationsListView.getItems().addAll(DbManager.getInstance().readReservations());
    }

    private Reservation getSelectedReservation() {
        return reservationsListView.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void handleDeleteButton(ActionEvent actionEvent) {
        Reservation selectedReservation = getSelectedReservation();
        if (selectedReservation != null) {
            String message = String.format(CONFIRM_DELETE, selectedReservation.getId());
            if (AlertUtility.isActionConfirmed(message)) {
                DbManager.getInstance().deleteReservation(selectedReservation);
                loadReservationsFromDatabase();
            }
        } else {
            AlertUtility.showWarningAlert(NO_SELECTION_ALERT_TITLE, SELECT_TO_DELETE);
        }
    }
}
package de.sbayat.sbtablereservationmanagementsystem.gui;

import de.sbayat.sbtablereservationmanagementsystem.gui.utility.AlertUtility;
import de.sbayat.sbtablereservationmanagementsystem.logic.db.DbManager;
import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {

    private static final String[] RESERVATION_TIMES       = {"12:00", "13:00", "14:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
    private static final String   TIMESLOT_NULL           = "timeSlot is NULL! Check FXML binding.";
    private static final String   NO_RESERVATION_SELECTED = "Error: No reservation selected for editing.";

    // @FXML
    // private TextField reservationId;

    @FXML
    private TextField customerName;

    @FXML
    private TextField partySize;

    @FXML
    private TextField date;

    @FXML
    private ComboBox<String> timeSlot;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField tableNumber;

    @FXML
    private Button checkAvailability;

    @FXML
    private Button saveReservationButton;


    private AddReservationCallback addReservationCallback;

    public void setEditMode(boolean editMode) {
        isEditMode = editMode;
    }

    private boolean     isEditMode;
    private Reservation currentSelectedReservation;

    public ReservationController(boolean isEditMode, Reservation getCurrentSelectedReservation) {
        this.isEditMode                 = isEditMode;
        this.currentSelectedReservation = getCurrentSelectedReservation;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (isEditMode && currentSelectedReservation != null) {
            customerName.setText(currentSelectedReservation.getCustomerName());
            partySize.setText(currentSelectedReservation.getPartySize().toString());
            date.setText(currentSelectedReservation.getDate());
            timeSlot.setValue(currentSelectedReservation.getTime());
            phoneNumber.setText(currentSelectedReservation.getCustomerPhoneNumber());
            tableNumber.setText(currentSelectedReservation.getTableNumber().toString());
        }
        updateTimeSlot();
        saveReservationButton.setDisable(false);
        tableNumber.setDisable(true);
    }

    public interface AddReservationCallback {
        void onAddReservation(Reservation reservation);
    }

    public void setAddReservationCallback(AddReservationCallback callback) {
        this.addReservationCallback = callback;
    }

    public void updateTimeSlot() {
        if (timeSlot != null) {
            timeSlot.getItems().addAll(RESERVATION_TIMES);
        } else {
            System.err.println(TIMESLOT_NULL);
        }
    }

    private Reservation getReservationFromUi() {
        Reservation reservationFromUi = null;

        //String id   = reservationId.getText();
        String nameText        = customerName.getText();
        String partySizeText   = partySize.getText();
        String dateText        = date.getText();
        String timeText        = timeSlot.getValue();
        String tableNumberText = tableNumber.getText();
        String phoneNumberText = phoneNumber.getText();

        String[] inputData = {
                nameText,
                partySizeText,
                tableNumberText,
                dateText,
                timeText,
                phoneNumberText
        };

        if (this.isInputDataFilled(inputData)) {

            // int    defaultId      = Integer.parseInt(reservationId.getText());
            //String phone          = phoneNumber.getText();
            int numberOfGuests = Integer.parseInt(partySize.getText());
            //String insertedDate   = date.getText();
            //String insertedTime   = timeSlot.getValue();
            int tableDedicated = Integer.parseInt(tableNumber.getText());

            reservationFromUi = new Reservation(nameText, phoneNumberText, numberOfGuests, dateText, timeText, tableDedicated);
        }

        return reservationFromUi;
    }

    private boolean isInputDataFilled(String[] inputData) {
        boolean inputDataIsFilled = true;
        int     index             = 0;

        while (inputDataIsFilled && index < inputData.length) {
            inputDataIsFilled = !inputData[index].isBlank();
            ++index;
        }
        return inputDataIsFilled;
    }

    @FXML
    private void handleCheckAvailabilityButton() {
        Reservation reservationFromUi = getReservationFromUi();

        if (reservationFromUi == null) {
            AlertUtility.showInputIsNotValidAlert();
            // TODO: is this return value correct?
            return;
        }

        if (isReservationAvailable(reservationFromUi)) {
            AlertUtility.showMessage("Table is Available.", "You may now save the reservation.");
            saveReservationButton.setDisable(false);
        } else {
            AlertUtility.showWarningAlert("Table not Available!", "Please select another table or time slot");
            saveReservationButton.setDisable(true);
        }
    }

    @FXML
    private boolean isReservationAvailable(Reservation inputReservation) {

        List<Reservation> reservations = DbManager.getInstance().readReservations();

        for (Reservation reservation : reservations) {
            if (isEditMode && currentSelectedReservation != null &&
                    reservation.getId() == currentSelectedReservation.getId()) {
                continue;
            }
            if (inputReservation.getDate().equals(reservation.getDate()) &&
                    inputReservation.getTime().equals(reservation.getTime()) &&
                    inputReservation.getTableNumber() == reservation.getTableNumber()) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void handleSaveButton() {
        Reservation reservationFromUi = getReservationFromUi();
        if (reservationFromUi == null) {
            AlertUtility.showInputIsNotValidAlert();
            return;
        }

        if (isEditMode) {
            // Editing an existing reservation
            if (currentSelectedReservation != null) {
                reservationFromUi.setId(currentSelectedReservation.getId()); // Ensure ID remains the same
                DbManager.getInstance().updateReservation(reservationFromUi);
            } else {
                System.out.println(NO_RESERVATION_SELECTED);
            }
        } else {
            // Adding a new reservation
            DbManager.getInstance().insertReservation(reservationFromUi);
        }

        if (addReservationCallback != null) {
            addReservationCallback.onAddReservation(reservationFromUi);
        }

        Stage stage = (Stage) saveReservationButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openTablesLayoutWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/sbayat/sbtablereservationmanagementsystem/restaurant-layout-window.fxml"));

            Parent root = loader.load();

            DiningTableController controller = loader.getController();

            //TODO: make sure these fields are filled before opening the tables layout window
            if (isEditMode) {
                int tableNumberInserted = Integer.parseInt(tableNumber.getText());
                controller.setSelectedTableNumber(tableNumberInserted);
            }

            String dateInserted      = date.getText();
            String timeInserted      = timeSlot.getValue();
            int    partySizeInserted = Integer.parseInt(partySize.getText());
            controller.setDateTimePartySize(dateInserted, timeInserted, partySizeInserted);


            controller.setAddTableCallback(tableNum -> {
                tableNumber.setText(String.valueOf(tableNum));
                tableNumber.setDisable(true);
            });

            Stage diningTableStage = new Stage();
            diningTableStage.initModality(Modality.APPLICATION_MODAL);
            diningTableStage.setScene(new Scene(root, 800, 700));
            diningTableStage.setTitle("Dining Tables Layout");
            diningTableStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

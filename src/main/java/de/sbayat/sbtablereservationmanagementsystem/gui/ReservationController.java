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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Class Description:
 * <p>
 * The ReservationController class handles the functionality of the reservation form within the Table Reservation
 * Management System. It allows the user to add or edit reservation details such as the customer name, party size, date,
 * time, phone number, and table number. The form validates user inputs and ensures that the required fields are filled
 * correctly. The class interacts with the DbManager for database operations (inserting or updating reservations) and
 * utilizes AlertUtility to show validation messages. It also provides the ability to select a table from a dining table
 * layout window and assign it to the reservation.
 * <p>
 * Key Features:
 * <p>
 * Displays and validates reservation details (name, party size, date, time, etc.).
 * Handles both adding new reservations and editing existing ones.
 * Opens a dining table layout window to assign a table to the reservation.
 * Uses AlertUtility for showing error or validation messages.
 * Supports callback functionality to update the reservation list after changes.
 */
public class ReservationController implements Initializable {

    private static final String[] RESERVATION_TIMES       = {"10:00", "11:00", "12:00", "13:00", "14:00", "17:00", "18:00", "19:00", "20:00", "21:00"};
    private static final String   TIMESLOT_NULL           = "timeSlot is NULL! Check FXML binding.";
    private static final String   NO_RESERVATION_SELECTED = "Error: No reservation selected for editing.";
    private static final String   RESOURCE                = "/de/sbayat/sbtablereservationmanagementsystem/restaurant-layout-window.fxml";
    private static final String   DATE_PATTERN            = "yyyy-MM-dd";
    private static final String   NUMBER_TEMPLATE         = "\\d+";
    private static final String   LAYOUT_TITLE            = "Dining Tables Layout";


    @FXML
    private TextField customerName;

    @FXML
    private TextField partySize;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<String> timeSlot;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField tableNumber;

    @FXML
    private Button saveReservationButton;


    private AddReservationCallback addReservationCallback;

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
            date.setValue(currentSelectedReservation.getDate());
            timeSlot.setValue(currentSelectedReservation.getTime());
            phoneNumber.setText(currentSelectedReservation.getCustomerPhoneNumber());
            tableNumber.setText(currentSelectedReservation.getTableNumber().toString());
        }
        updateTimeSlot();
        saveReservationButton.setDisable(false);
        tableNumber.setDisable(true);
        addFieldChangeListeners();
    }

    private void addFieldChangeListeners() {
        partySize.textProperty().addListener((observable, oldValue, newValue) -> disableSaveTable());
        date.valueProperty().addListener((observable, oldValue, newValue) -> disableSaveTable());
        timeSlot.valueProperty().addListener((observable, oldValue, newValue) -> disableSaveTable());
    }

    private void disableSaveTable() {
        saveReservationButton.setDisable(true);
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
    private void handleSaveButton() {

        String nameText      = customerName.getText();
        String partySizeText = partySize.getText();

        LocalDate         datePicked    = date.getValue();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        String            dateText      = datePicked.format(dateFormatter);

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

        if (!this.isInputDataFilled(inputData)) {
            AlertUtility.showInputIsNotValidAlert(AlertUtility.EDIT_ALERT_TITLE, AlertUtility.FILL_ALL);
            return;
        }

        if (!partySizeText.matches(NUMBER_TEMPLATE)) {
            AlertUtility.showInputIsNotValidAlert(AlertUtility.INVALID_GUESTS, AlertUtility.INVALID_GUESTS_MESSAGE);
            return;
        }

        if (!phoneNumberText.matches(NUMBER_TEMPLATE)) {
            AlertUtility.showInputIsNotValidAlert(AlertUtility.INVALID_PHONE, AlertUtility.INVALID_PHONE_MESSAGE);
            return;
        }

        int numberOfGuests = Integer.parseInt(partySize.getText());
        // table number data type does not need to be validated here
        int tableDedicated = Integer.parseInt(tableNumber.getText());


        Reservation reservationFromUi = new Reservation(nameText, phoneNumberText, numberOfGuests, datePicked, timeText, tableDedicated);


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RESOURCE));

            Parent root = loader.load();

            DiningTableController controller = loader.getController();

            if (isEditMode) {
                int tableNumberInserted = Integer.parseInt(tableNumber.getText());
                controller.setSelectedTableNumber(tableNumberInserted);
            }

            LocalDate datePicked = date.getValue();
            LocalDate today      = LocalDate.now();

            if (datePicked.isBefore(today)) {
                AlertUtility.showInputIsNotValidAlert(AlertUtility.INVALID_DATE, AlertUtility.INVALID_DATE_MESSAGE);
                return;
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            String            dateAsString  = datePicked.format(dateFormatter);

            String    timeInserted = timeSlot.getValue();
            LocalTime selectedTime = LocalTime.parse(timeInserted);
            LocalTime now          = LocalTime.now();

            if (selectedTime.isBefore(now)) {
                AlertUtility.showInputIsNotValidAlert(AlertUtility.INVALID_TIME, AlertUtility.INVALID_TIME_MESSAGE);
                return;
            }

            String partySizeInsertedText = partySize.getText();

            String[] inputData = {
                    dateAsString,
                    partySizeInsertedText
            };

            if (isInputDataFilled(inputData)) {
                if (!partySizeInsertedText.matches(NUMBER_TEMPLATE)) {
                    AlertUtility.showInputIsNotValidAlert(AlertUtility.INVALID_GUESTS, AlertUtility.INVALID_GUESTS_MESSAGE);
                    return;
                }

                int partySizeInserted = Integer.parseInt(partySize.getText());
                if (partySizeInserted <= 0) {
                    AlertUtility.showInputIsNotValidAlert(AlertUtility.INVALID_GUESTS, AlertUtility.INVALID_GUESTS_MESSAGE);
                    return;
                }

                if (timeInserted == null) {
                    AlertUtility.showInputIsNotValidAlert(AlertUtility.TIME_REQUIRED, AlertUtility.TIME_REQUIRED_MESSAGE);
                    return;
                }

                controller.updateLayoutData(datePicked, timeInserted, partySizeInserted);

            } else {
                AlertUtility.showInputIsNotValidAlert(AlertUtility.FILL_REQUIRED_FIELDS, AlertUtility.FILL_DATE_TIME_SIZE);
                return;
            }

            controller.setAddTableCallback(tableNum -> {
                tableNumber.setText(String.valueOf(tableNum));
                tableNumber.setDisable(true);
                saveReservationButton.setDisable(false);
            });

            Stage diningTableStage = new Stage();
            diningTableStage.initModality(Modality.APPLICATION_MODAL);
            diningTableStage.setScene(new Scene(root, 800, 700));
            diningTableStage.setTitle(LAYOUT_TITLE);
            diningTableStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

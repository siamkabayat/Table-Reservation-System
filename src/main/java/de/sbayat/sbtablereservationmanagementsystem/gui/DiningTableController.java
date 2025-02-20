package de.sbayat.sbtablereservationmanagementsystem.gui;

import de.sbayat.sbtablereservationmanagementsystem.logic.db.DbManager;
import de.sbayat.sbtablereservationmanagementsystem.model.DiningTable;
import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * Class Description:
 * <p>
 * The DiningTableController class manages the display and interaction with dining tables in the Table Reservation
 * Management System. It is responsible for showing the table layout, enabling or disabling tables based on party size
 * and reservation status, and allowing the user to select a table for a reservation. The class maps buttons to tables,
 * and handles the logic for selecting a table, including validating table capacity and availability.
 * <p>
 * Key Features:
 * <p>
 * Displays buttons representing dining tables.
 * Maps buttons to tables from the database using DbManager.
 * Disables tables based on reservation status, party size, or table capacity.
 * Allows the user to select a table and assign it to a reservation.
 * Updates button colors based on table availability (light green for selected, gray for disabled, red for reserved).
 * Supports callback functionality to pass the selected table number back to the calling class.
 */
public class DiningTableController implements Initializable {

    private static final String COLOR_DEFAULT         = "";
    private static final String COLOR_LIGHT_GREEN     = "-fx-background-color: lightgreen;";
    private static final String COLOR_GRAY            = "-fx-background-color: gray;";
    private static final String COLOR_RED             = "-fx-background-color: red;";
    private static final int    NO_VALID_TABLE_NUMBER = -1;


    @FXML
    Button tableButton1;
    @FXML
    Button tableButton2;
    @FXML
    Button tableButton3;
    @FXML
    Button tableButton4;
    @FXML
    Button tableButton5;
    @FXML
    Button tableButton6;
    @FXML
    Button tableButton7;
    @FXML
    Button tableButton8;
    @FXML
    Button tableButton9;
    @FXML
    Button tableButton10;
    @FXML
    Button tableButton11;
    @FXML
    Button tableButton12;
    @FXML
    Button tableButton13;
    @FXML
    Button tableButton14;
    @FXML
    Button tableButton15;
    @FXML
    Button tableButton16;
    @FXML
    Button tableButton17;
    @FXML
    Button tableButton18;
    @FXML
    Button tableButton19;
    @FXML
    Button tableButton20;

    @FXML
    public Button selectTableButton; //A map that links buttons to their respective dining tables.

    //Keeps track of the previously selected button to reset its color when a new button is selected.
    private Button previousButton = null;

    private Map<Button, DiningTable> tableButtonMap = new HashMap<>();

    //A callback function that gets invoked when a table is selected.
    private AddTableCallback addTableCallback;

    private int selectedTableNumber = NO_VALID_TABLE_NUMBER;

    private LocalDate dateFromUi;
    private String    timeFromUi;
    private int       partySizeFromUi;
    private boolean   isEditMode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        disableSelectButtonIfNoTableSelected();
        mapButtonsToTables();
    }

    //Disables the "select" button if no valid table is selected.
    private void disableSelectButtonIfNoTableSelected() {
        selectTableButton.setDisable(selectedTableNumber == NO_VALID_TABLE_NUMBER);
    }

    public void setSelectedTableNumber(int selectedTableNumber) {
        this.selectedTableNumber = selectedTableNumber;
    }

    public interface AddTableCallback {
        void onAddTable(int tableNumber);
    }

    //Sets the callback function that will be triggered when a table is selected.
    public void setAddTableCallback(AddTableCallback callback) {
        this.addTableCallback = callback;
    }

    private void mapButtonsToTables() {
        List<DiningTable> tablesFromDatabase = DbManager.getInstance().readDiningTables();
        List<Button> tableButtons = Arrays.asList(tableButton1, tableButton2, tableButton3, tableButton4, tableButton5,
                tableButton6, tableButton7, tableButton8, tableButton9, tableButton10,
                tableButton11, tableButton12, tableButton13, tableButton14, tableButton15,
                tableButton16, tableButton17, tableButton18, tableButton19, tableButton20);

        for (int i = 0; i < tableButtons.size(); i++) {
            tableButtonMap.put(tableButtons.get(i), tablesFromDatabase.get(i));
        }
    }

    //Updates the layout data with the selected date, time, and party size and then updates the table button statuses accordingly.
    public void updateLayoutData(LocalDate dateFromUi, String timeFromUi, int partySizeFromUi) {
        this.dateFromUi      = dateFromUi;
        this.timeFromUi      = timeFromUi;
        this.partySizeFromUi = partySizeFromUi;
        updateTableButtonsStatus(dateFromUi, timeFromUi, partySizeFromUi);
    }

    @FXML
    private void handleTableClick(ActionEvent event) {
        Button      clickedButton = (Button) event.getSource();
        DiningTable selectedTable = tableButtonMap.get(clickedButton);

        if (previousButton != null && previousButton != clickedButton) {
            previousButton.setStyle(COLOR_DEFAULT);
        }

        clickedButton.setStyle(COLOR_LIGHT_GREEN);

        if (selectedTable != null) {
            selectedTableNumber = selectedTable.getNumber();
            selectTableButton.setDisable(false);
        }

        previousButton = clickedButton;
    }

    @FXML
    private void handleSelectTableButton() {
        if (selectedTableNumber != NO_VALID_TABLE_NUMBER) {
            addTableCallback.onAddTable(selectedTableNumber);
        }
        Stage stage = (Stage) selectTableButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Updates the button status based on the reservation data (disables buttons based on party size, table availability,
     * and reservation status).
     */
    private void updateTableButtonsStatus(LocalDate date, String time, int partySize) {

        List<Reservation> reservationsFromDatabase = DbManager.getInstance().readReservations();
        for (Map.Entry<Button, DiningTable> entry : tableButtonMap.entrySet()) {
            Button      button = entry.getKey();
            DiningTable table  = entry.getValue();

            button.setDisable(false);
            button.setStyle(COLOR_DEFAULT);

            // Disable tables that do not match the party size and give them gray color
            if (table.getCapacity() < partySize || table.getCapacity() > partySize + 2) {
                button.setDisable(true);
                button.setStyle(COLOR_GRAY);
            }
        }
        // iterate through reservations and disable tables that are reserved for the sate date and time and give them red color
        for (Reservation reservation : reservationsFromDatabase) {
            LocalDate dateFromDatabase = reservation.getDate();
            String    timeFromDatabase = reservation.getTime();
            int       tableNumber      = reservation.getTableNumber();
            Button    button           = getButtonByTableNumber(tableNumber);
            if ((date.equals(dateFromDatabase) && time.equals(timeFromDatabase))) {
                if (button != null) {
                    if (tableNumber == selectedTableNumber) {
                        int tableCapacity = Objects.requireNonNull(getDiningTableByNumber(tableNumber)).getCapacity();
                        if (tableCapacity < partySize || tableCapacity > partySize + 2) {
                            button.setDisable(true);
                            button.setStyle(COLOR_GRAY);
                        } else {
                            button.setDisable(false);
                            button.setStyle(COLOR_DEFAULT);
                        }
                    } else {
                        button.setDisable(true);
                        button.setStyle(COLOR_RED);
                    }
                }
            }
        }
    }

    private DiningTable getDiningTableByNumber(int tableNumber) {
        for (DiningTable table : tableButtonMap.values()) {
            if (table.getNumber() == tableNumber) {
                return table;
            }
        }
        return null;
    }


    private Button getButtonByTableNumber(int tableNumber) {
        for (Map.Entry<Button, DiningTable> entry : tableButtonMap.entrySet()) {
            DiningTable table = entry.getValue();

            if (table.getNumber() == tableNumber) {
                return entry.getKey();
            }
        }
        return null;
    }

}

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
import java.util.*;

public class DiningTableController implements Initializable {


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
    public Button selectTableButton;

    private Button previousButton = null;

    private Map<Button, DiningTable> tableButtonMap = new HashMap<>();

    private AddTableCallback addTableCallback;

    private int selectedTableNumber = -1;

    private String dateInserted;
    private String timeInserted;
    private int    partySizeInserted;
    private boolean isEditMode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapButtonsToTables();
    }
    // TODO: change this stupid function name
    public void setDateTimePartySize(String dateInserted, String timeInserted, int partySizeInserted) {
        this.dateInserted      = dateInserted;
        this.timeInserted      = timeInserted;
        this.partySizeInserted = partySizeInserted;
        updateTableButtonsStatus(dateInserted, timeInserted, partySizeInserted);
    }

    public void setSelectedTableNumber(int selectedTableNumber) {
        this.selectedTableNumber = selectedTableNumber;
    }

    public interface AddTableCallback {
        void onAddTable(int tableNumber);
    }

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
            tableButtonMap.put(tableButtons.get(i), tablesFromDatabase.get(i)); // Map buttons to tables
        }
    }

    @FXML
    private void handleTableClick(ActionEvent event) {
        Button      clickedButton = (Button) event.getSource();
        DiningTable selectedTable = tableButtonMap.get(clickedButton);

        if (previousButton != null && previousButton != clickedButton) {
            previousButton.setStyle("");
        }

        clickedButton.setStyle("-fx-background-color: lightgreen;");

        if (selectedTable != null) {
            selectedTableNumber = selectedTable.getNumber();
            System.out.println("Table Number: " + selectedTable.getNumber());
        }

        previousButton = clickedButton;
    }

    @FXML
    private void handleSelectTableButton() {
        if (selectedTableNumber != -1) {
            addTableCallback.onAddTable(selectedTableNumber);
        }
        Stage stage = (Stage) selectTableButton.getScene().getWindow();
        stage.close();
    }


    private void updateTableButtonsStatus(String date, String time, int partySize) {
//        for (Button button : tableButtonMap.keySet()) {
//            button.setStyle("-fx-background-color: #C1FFC1;");
//        }
        List<Reservation> reservationsFromDatabase = DbManager.getInstance().readReservations();
        for (Map.Entry<Button, DiningTable> entry : tableButtonMap.entrySet()) {
            Button      button = entry.getKey();
            DiningTable table  = entry.getValue();

            button.setDisable(false);
            button.setStyle("");

            // Disable tables that do not match the party size
            if (table.getCapacity() < partySize || table.getCapacity() > partySize + 2) {
                button.setDisable(true);
                button.setStyle("-fx-background-color: gray;");
            }
        }

        for (Reservation reservation : reservationsFromDatabase) {
            String dateFromDatabase = reservation.getDate();
            String timeFromDatabase = reservation.getTime();
            int    tableNumber      = reservation.getTableNumber();
            // TODO: capacity is calculated earlier using the map. think of removing the function getDiningTableByNumber!
            int    capacity = DbManager.getInstance().getDiningTableByNumber(tableNumber).getCapacity();
            Button button   = getButtonByTableNumber(tableNumber);
            if ((date.equals(dateFromDatabase) && time.equals(timeFromDatabase))) {
                if (button != null) {
                    if (tableNumber == selectedTableNumber) {
                        button.setDisable(false);
                        button.setStyle("");
                    } else {
                        button.setDisable(true);
                        button.setStyle("-fx-background-color: red;");
                    }
                }
            }
        }
    }

    private Button getButtonByTableNumber(int tableNumber) {
        for (Map.Entry<Button, DiningTable> entry : tableButtonMap.entrySet()) {
            DiningTable table = entry.getValue();

            // Compare table number
            if (table.getNumber() == tableNumber) {
                return entry.getKey();  // Return the Button associated with the table number
            }
        }
        return null;  // Return null if no Button matches the table number
    }

}

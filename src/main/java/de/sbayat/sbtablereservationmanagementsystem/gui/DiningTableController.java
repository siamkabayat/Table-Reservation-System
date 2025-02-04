package de.sbayat.sbtablereservationmanagementsystem.gui;

import de.sbayat.sbtablereservationmanagementsystem.logic.db.DbManager;
import de.sbayat.sbtablereservationmanagementsystem.model.DiningTable;
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

    private Map<Button, DiningTable> tableButtonMap = new HashMap<>();

    private AddTableCallback addTableCallback;

    private int selecteTableNumber = -1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapButtonsToTables();


    }

    public interface AddTableCallback {
        void onAddTable(int tableNumber);
    }

    public void setAddTableCallback(AddTableCallback callback) {
        this.addTableCallback = callback;
    }

    private void  mapButtonsToTables() {
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
        Button clickedButton = (Button) event.getSource();
        DiningTable selectedTable = tableButtonMap.get(clickedButton);

        if (selectedTable != null) {
            selecteTableNumber = selectedTable.getNumber();
            System.out.println("Table Number: " + selectedTable.getNumber());
        }
    }

    @FXML
    private void handleSelectTableButton() {
        if (selecteTableNumber != -1) {
            addTableCallback.onAddTable(selecteTableNumber);
        }
        Stage stage = (Stage) selectTableButton.getScene().getWindow();
        stage.close();
    }
}

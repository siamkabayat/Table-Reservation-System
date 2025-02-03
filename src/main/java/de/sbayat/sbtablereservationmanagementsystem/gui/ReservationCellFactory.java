package de.sbayat.sbtablereservationmanagementsystem.gui;

import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ReservationCellFactory implements Callback<ListView<Reservation>, ListCell<Reservation>> {

    @Override
    public ListCell<Reservation> call(ListView<Reservation> param) {
        return new ReservationListViewCell();
    }

}

package de.sbayat.sbtablereservationmanagementsystem.gui;

import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;
import javafx.scene.control.ListCell;
import javafx.scene.text.Font;

public class ReservationListViewCell extends ListCell<Reservation> {

    @Override
    protected void updateItem(Reservation reservationToShow, boolean empty) {
        super.updateItem(reservationToShow, empty);

        if (empty || reservationToShow == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(reservationToShow.showReservationInfo());
            setFont(new Font("Consolas", 12));
//			setStyle("-fx-background-color: #3720a5; -fx-text-fill: white;");
        }
    }

}

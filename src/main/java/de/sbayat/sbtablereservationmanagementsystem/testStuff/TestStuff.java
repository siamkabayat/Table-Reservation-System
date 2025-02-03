package de.sbayat.sbtablereservationmanagementsystem.testStuff;


import de.sbayat.sbtablereservationmanagementsystem.logic.db.DbManager;
import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.util.List;

/**
 * Testet Dinge
 */
public class TestStuff {

    public static void main(String[] args) {

        showDatabaseOnlineStatus();

//        showLogWoodsFromDatabase();

        showInsertReservationInDatabase();
//        showLogWoodsFromDatabase();

        //       showUpdateReservationInDatabase();
        //      showReservationsFromDatabase();

        //	showDeleteLogWoodInDatabase();


    }

    private static void showDatabaseOnlineStatus() {
        System.out.println("Database online:" + DbManager.getInstance().isDatabaseOnline() + "\n");
    }


    private static void showReservationsFromDatabase() {
        List<Reservation> reservationsFromDatabase = DbManager.getInstance().readReservations();

        System.out.println("\nReservations from Database:");
        for (Reservation reservation : reservationsFromDatabase) {
            System.out.print(reservation.getAllAttributesAsCsvLine());
        }
    }

    private static void showInsertReservationInDatabase() {
        Reservation testReservation = TestData.getTestReservations().getFirst();
        System.out.println("INSERT>" + testReservation.getAllAttributesAsCsvLine());
        DbManager.getInstance().insertReservation(testReservation);
    }

    private static void showUpdateReservationInDatabase() {
        Reservation testReservation = TestData.getTestReservations().getFirst();
        testReservation.setId(15);
        testReservation.setCustomerName("ggggggg");
        System.out.println("UPDATE>>" + testReservation.getAllAttributesAsCsvLine());
        DbManager.getInstance().updateReservation(testReservation);
    }

    private static void showDeleteLogWoodInDatabase() {
        Reservation testReservation = TestData.getTestReservations().getFirst();
        testReservation.setId(2);

        System.out.println("DELETE>>" + testReservation.getAllAttributesAsCsvLine());
        DbManager.getInstance().deleteReservation(testReservation);
    }

}
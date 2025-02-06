package de.sbayat.sbtablereservationmanagementsystem.testStuff;


import de.sbayat.sbtablereservationmanagementsystem.logic.db.DbManager;
import de.sbayat.sbtablereservationmanagementsystem.model.DiningTable;
import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.util.List;

/**
 * The TestStuff class is a simple testing utility designed to interact with the database and print the status of
 * reservations and dining tables
 */
public class TestStuff {

    public static void main(String[] args) {

        showDatabaseOnlineStatus();
        showReservationsFromDatabase();
        showDiningTablesFromDatabase();


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

    private static void showDeleteReservationInDatabase() {
        Reservation testReservation = TestData.getTestReservations().getFirst();
        testReservation.setId(2);

        System.out.println("DELETE>>" + testReservation.getAllAttributesAsCsvLine());
        DbManager.getInstance().deleteReservation(testReservation);
    }
    //TODO: add more methods for testing the dining tables
    private static void showDiningTablesFromDatabase() {
        List<DiningTable> DiningTablesFromDatabase = DbManager.getInstance().readDiningTables();

        System.out.println("\nDining Tables from Database:");
        for (DiningTable diningTable : DiningTablesFromDatabase) {
            System.out.print(diningTable.getAllAttributesAsCsvLine());
        }
    }

}
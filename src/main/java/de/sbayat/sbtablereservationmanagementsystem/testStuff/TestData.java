package de.sbayat.sbtablereservationmanagementsystem.testStuff;

import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    //region 0. Konstanten
    private static final int MAX_LOGS = 16;
    //endregion

    //region 1. Decl and Init Attribute
    //endregion

    //region 2. Konstruktoren
    private TestData() {
    }
    //endregion

    //region 3. Test Reservations
    public static synchronized List<Reservation> getTestReservations() {
        List<Reservation> testReservations = new ArrayList<>();

        for (int index = 1; index < MAX_LOGS; index++) {
            Reservation testReservation = new Reservation();

            String testCustomerName = "Robert";
            int    testPartySize    =  0;
            String testDate         = "2025-02-01";
            String testTime         = "18:30";
            String testPhoneNumber = "01763240017";

            if (index < 10) {
                testCustomerName = "Andy";
                testPartySize    = 5;
                testDate         = "2025-02-02";
                testTime         = "19:30";

            } else {
                testCustomerName = "Freddy";
                testPartySize    = 4;
                testDate         = "2025-02-02";
                testTime         = "20:00";
            }

            testReservation.setId(index);
            testReservation.setCustomerName(testCustomerName);
            testReservation.setCustomerPhoneNumber(testPhoneNumber);
            testReservation.setPartySize(index + testPartySize);
            testReservation.setDate(testDate);
            testReservation.setTime(testTime);
            testReservation.setTableNumber(index);


            testReservations.add(testReservation);
        }

        return testReservations;
    }
    //endregion


}

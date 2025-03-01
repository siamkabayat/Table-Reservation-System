package de.sbayat.sbtablereservationmanagementsystem.testStuff;

import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TestData {

    private static final int MAX_LOGS = 16;


    private TestData() {
    }

    public static synchronized List<Reservation> getTestReservations() {
        List<Reservation> testReservations = new ArrayList<>();

        for (int index = 1; index < MAX_LOGS; index++) {
            Reservation testReservation = new Reservation();

            String testCustomerName = "Robert";
            int       testPartySize =  0;
            LocalDate testDate      = LocalDate.of(2025, 2, 2);
            String    testTime      = "18:30";
            String testPhoneNumber = "01763240017";

            if (index < 10) {
                testCustomerName = "Andy";
                testPartySize    = 5;
                testDate         = LocalDate.of(2025, 3, 2);;
                testTime         = "19:30";

            } else {
                testCustomerName = "Freddy";
                testPartySize    = 4;
                testDate         = LocalDate.of(2025, 2, 12);
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

}

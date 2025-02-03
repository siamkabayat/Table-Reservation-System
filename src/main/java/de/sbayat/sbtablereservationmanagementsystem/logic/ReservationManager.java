package de.sbayat.sbtablereservationmanagementsystem.logic;

import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;
import de.sbayat.sbtablereservationmanagementsystem.model.DiningTable;

import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    List<Reservation> reservations;
    List<DiningTable> tables;

    public ReservationManager() {
        this.reservations = new ArrayList<>();
        this.tables       = new ArrayList<>();
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<DiningTable> getTables() {
        return tables;
    }

    public void setTables(List<DiningTable> tables) {
        this.tables = tables;
    }

    private synchronized boolean addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation can not be empty!");
        }
        if (!isTableAvailable(reservation.getTableNumber(), reservation.getDate(), reservation.getTime())) {
            System.out.println("Table is not available!. Reservation can not be added.");
            return false;
        }
        reservations.add(reservation);
        return true;
    }

    private boolean updateReservation(int reservationId, Reservation updatedReservation) {

        Reservation existingReservation = findReservationById(reservationId);
        if (existingReservation == null) {
            return false;
        }

        if (!isTableAvailable(updatedReservation.getTableNumber(), updatedReservation.getDate(), updatedReservation.getTime())) {
            return false;
        }

        existingReservation.setCustomerName(updatedReservation.getCustomerName());
        existingReservation.setCustomerPhoneNumber(updatedReservation.getCustomerPhoneNumber());
        existingReservation.setPartySize(updatedReservation.getPartySize());
        existingReservation.setDate(updatedReservation.getDate());
        existingReservation.setTime(updatedReservation.getTime());
        existingReservation.setTableNumber(updatedReservation.getTableNumber());

        return true;
    }

    private boolean deleteReservation(int reservationId) {
        Reservation reservationToDelete = findReservationById(reservationId);
        if (reservationToDelete == null) {
            return false;
        }

        reservations.remove(reservationToDelete);
        return true;
    }

    private Reservation findReservationById(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                return reservation;
            }
        }
        return null;
    }

    private List<Reservation> findReservationsByCustomer(String customerName) {
        List<Reservation> customerReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomerName().equalsIgnoreCase(customerName)) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    private List<Reservation> getAllReservations() {
        return this.reservations;
    }

    private synchronized boolean isTableAvailable(int tableNumber, String date, String time) {
        for (Reservation existingReservation : this.reservations) {
            if (existingReservation.getTableNumber() == tableNumber &&
                    existingReservation.getDate().equals(date) &&
                    existingReservation.getTime().equals(time)) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> getAvailableTables(String date, String time, List<DiningTable> tables) {
        List<Integer> availableTables = new ArrayList<>();
        for (DiningTable table : tables) {
            if (isTableAvailable(table.getNumber(), date, time)) {
                availableTables.add(table.getNumber());
            }
        }
        return availableTables;
    }

    private DiningTable getTableByNumber(int tableNumber, List<DiningTable> tables) {
        for (DiningTable table : tables) {
            if (table.getNumber() == tableNumber) {
                return table;
            }
        }
        return null;
    }

    private int getTableCapacity(int tableNumber, List<DiningTable> tables) {
        DiningTable table = getTableByNumber(tableNumber, tables);
        return table != null ? table.getCapacity() : 0;
    }

    public List<DiningTable> getAllTables(List<DiningTable> tables) {
        return tables;
    }


}

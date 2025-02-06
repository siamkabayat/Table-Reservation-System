package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import de.sbayat.sbtablereservationmanagementsystem.model.DiningTable;
import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 0.1 org.mariadb.jdbc:mariadb-java-client:3.5.1 als maven- dependency hinzufuegen File->Project Structure->Modules->Dependencies->+->Library->From Maven
 * TODO Kopieren und Einfuegen
 *
 *  Singleton class that manages database connections and CRUD operations for reservations and dining tables.
 */

public class DbManager {


    private static final String DB_LOCAL_SERVER_IP_ADDRESS = "localhost/";
    private static final String DB_LOCAL_NAME              = "reservation_management";

    private static final String DB_LOCAL_CONNECTION_URL =
            "jdbc:mariadb://" + DB_LOCAL_SERVER_IP_ADDRESS + DB_LOCAL_NAME;

    private static final String DB_LOCAL_USER_NAME = "root";
    private static final String DB_LOCAL_USER_PW   = "";

    private static DbManager      instance;
    private        DaoReservation daoReservation;
    private        DaoDiningTable daoDiningTable;


    /**
     * Default constructor
     */
    private DbManager() {
        daoReservation = new DaoReservation();
        daoDiningTable = new DaoDiningTable();
    }

    /**
     * Returns the only instance of this class
     * synchronized to ensure thread-safety
     *
     * @return instance : {@link DbManager} : The only thread-safe instance
     */
    public static synchronized DbManager getInstance() {

        if (instance == null) {
            instance = new DbManager();
        }

        return instance;

    }

    /**
     * Returns a generated database connection with read (r) and write (w) rights,
     * or null if something goes wrong.
     *
     * @return rwDbConnection : {@link Connection} : Connection to the database with read/write rights
     */
    private Connection getDatabaseConnection() throws Exception {
        Connection connection = null;

        try {
            //Offenen einer Verbindung
            connection = DriverManager.getConnection(DB_LOCAL_CONNECTION_URL, DB_LOCAL_USER_NAME, DB_LOCAL_USER_PW);
        } catch (SQLNonTransientConnectionException sqlNoConnectionEx) {
            throw new Exception("No Database Connection");
        }

        return connection;
    }

    /**
     * Checks if the database is online or not,
     * by opening a connection through {@link DbManager#getDatabaseConnection()} and immediately
     * closing it with {@link DbManager#getDatabaseConnection()}.close().
     * If an error occurs, false is returned,
     * meaning the database is not online. This can happen if the Maven dependency
     * org.mariadb.jdbc:mariadb-java-client:3.5.1 was not added to the project.
     *
     * @return isOnline : boolean : true if the database is online, false if not
     */
    public boolean isDatabaseOnline() {
        boolean isOnline = true;
        try {
            this.getDatabaseConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
            isOnline = false;
        }
        return isOnline;
    }

    public void insertReservation(Reservation reservationToInsert) {
        try {
            if (this.isDatabaseOnline()) {
                this.daoReservation.create(this.getDatabaseConnection(), reservationToInsert);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Reservation> readReservations() {
        List<Reservation> reservations = new ArrayList<>();

        try {
            if (this.isDatabaseOnline()) {
                reservations = daoReservation.readAll(this.getDatabaseConnection());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return reservations;
    }

    public void updateReservation(Reservation reservationToUpdate) {
        try {
            if (this.isDatabaseOnline()) {
                this.daoReservation.update(this.getDatabaseConnection(), reservationToUpdate);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteReservation(Reservation reservationToDelete) {
        try {
            if (this.isDatabaseOnline()) {
                this.daoReservation.delete(this.getDatabaseConnection(), reservationToDelete);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public List<DiningTable> readDiningTables() {
        List<DiningTable> diningTables = new ArrayList<>();

        try {
            if (this.isDatabaseOnline()) {
                diningTables = daoDiningTable.readAll(this.getDatabaseConnection());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return diningTables;
    }

    public void updateDiningTable(DiningTable diningTableToUpdate) {
        try {
            if (this.isDatabaseOnline()) {
                this.daoDiningTable.update(this.getDatabaseConnection(), diningTableToUpdate);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteDiningTable(DiningTable diningTableToDelete) {
        try {
            if (this.isDatabaseOnline()) {
                this.daoDiningTable.delete(this.getDatabaseConnection(), diningTableToDelete);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

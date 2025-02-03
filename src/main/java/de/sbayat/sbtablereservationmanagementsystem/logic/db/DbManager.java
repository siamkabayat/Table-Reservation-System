package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 0.1 org.mariadb.jdbc:mariadb-java-client:3.5.1 als maven- dependency hinzufuegen File->Project Structure->Modules->Dependencies->+->Library->From Maven
 * TODO Kopieren und Einfuegen
 * Dieser Singleton verwaltet threadsicher
 */
public class DbManager {

    //region 0. Konstanten

    //	private static final String DB_LOCAL_SERVER_IP_ADDRESS = "127.0.0.1:100/";
    private static final String DB_LOCAL_SERVER_IP_ADDRESS = "localhost/";
    private static final String DB_LOCAL_NAME              = "reservation_management";

    private static final String DB_LOCAL_CONNECTION_URL =
            "jdbc:mariadb://" + DB_LOCAL_SERVER_IP_ADDRESS + DB_LOCAL_NAME;

    private static final String DB_LOCAL_USER_NAME = "root";
    private static final String DB_LOCAL_USER_PW   = "";
    //endregion

    //region 1. Decl and Init Attribute
    private static DbManager  instance;
    private        DaoReservation daoReservation;
    //endregion

    //region 2. Konstruktoren

    /**
     * Standarkontruktor
     */
    private DbManager() {
        daoReservation = new DaoReservation();
    }
    //endregion

    //region 3. Get Instance

    /**
     * Gibt die einzige Instanz dieser Klasse
     * synchronisiert und so mit Threadsicher zurueck
     *
     * @return instance : {@link DbManager} : Einzige threadsichere Instanz
     */
    public static synchronized DbManager getInstance() {

        if (instance == null) {
            instance = new DbManager();
        }

        return instance;

    }
    //endregion

    //region 4. Get Read And Wirte Connection to Database

    /**
     * Gibt eine generierte Datenbankverbindung mit Lese(r) als auch Schreibrechten(w)
     * zurueckt oder null sollte etwas schiefgelaufen sein.
     *
     * @return rwDbConnection : {@link Connection} : Verbindung zur Datenbank mit rw - Rechten
     */
    private Connection getDatabaseConnection() throws Exception {
        Connection connection = null;

        try {
            //Offenen einer Verbindung
            connection = DriverManager.getConnection(DB_LOCAL_CONNECTION_URL, DB_LOCAL_USER_NAME, DB_LOCAL_USER_PW);
        } catch (SQLNonTransientConnectionException sqlNoConnectionEx) {
            throw new Exception("Keine Datenbankverbindung");
        }

        return connection;
    }

    /**
     * Checkt ob die Datenbank online ist oder nicht,
     * indem die Verbindung ueber {@link DbManager#getDatabaseConnection()} geoffenet und direkt
     * wieder mit {@link DbManager#getDatabaseConnection()}.close() geschlossen wird.
     * Gibt es eine Fehlermeldung wird false zurueckgegeben
     * und die Datenbank ist nicht online dann ist das die {@link SQLNonTransientConnectionException}
     * hier ist dann die maven-dependency org.mariadb.jdbc:mariadb-java-client:3.5.1 nicht zum
     * Projekt hinzugefuegt worden.
     *
     * @return isOnline : boolean : true : Dbist Online : false nicht
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
    //endregion

    //region 5.CRUD Operationen fuer Reservation
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

    //endregion
}

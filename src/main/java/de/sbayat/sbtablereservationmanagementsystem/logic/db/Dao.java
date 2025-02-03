package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * TODO Kopieren
 * DAO - Data Access Object - Interface, welches die CRUD-Funktionalität
 * zur Verfügung
 */
public interface Dao<T> {

    /**
     * Methode zum Einfügen eines Objektes in die Datenbank
     *
     * @param connection Datenbankverbindung
     * @param modelToInsert Objekt einer bestimmten Modellklasse
     */
    void create(Connection connection, T modelToInsert);

    /**
     * Methode zum Auslesen von Objekten aus der Datenbank
     * @param connection Datenbankverbindung
     * @return Liste von Objekten
     */
    List<T> readAll(Connection connection);

    /**
     * Methode zum Aktualisieren eines Objektes in der Datenbank
     *
     * @param connection Datenbankverbindung
     * @param modelToUpdate Objekt einer bestimmten Modellklasse
     */
    void update(Connection connection, T modelToUpdate);

    /**
     * Methode zum Löschen eines Objektes aus der Datenbank
     *
     * @param connection Datenbankverbindung
     * @param modelToDelete Objekt einer bestimmten Modellklasse
     */
    void delete(Connection connection, T modelToDelete);


    /**
     * Nimmt die Ergebnismenge und formt ein konkretes Datennmodel daraus
     * @param resultSet : {@link ResultSet} : Ergebnismenge der aktuellen Abfrage
     * @return : {@link T} : Datenmodell aus ResultSet
     */
    T getModelFromResultSet(ResultSet resultSet) throws SQLException;
}

package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object kurz Dao um auf die Datenbanktabelle reservations zuzugreifen.
 * Die CRUD Methoden sind mit dem Interface {@link Dao} zu implementieren.
 * TODO Implementieren
 */
public class DaoReservation implements Dao<Reservation> {


    //region 0. Konstanten
    private static final String COLUMN_ID                    = "id";
    private static final String COLUMN_CUSTOMER_NAME         = "customerName";
    private static final String COLUMN_CUSTOMER_PHONE_NUMBER = "customerPhoneNumber";
    private static final String COLUMN_PARTY_SIZE            = "partySize";
    private static final String COLUMN_DATE                  = "date";
    private static final String COLUMN_TIME                  = "time";
    private static final String COLUMN_TABLE_NUMBER          = "tableNumber";

    /*
     * SQL-Statements
     * Fragezeichen sind Platzhalter die nachher mit
     * dem PreparedStatement durch statementParameterInidzes gezielt
     * mit den Werten des Datenmodells ersetzt werden
     */
    private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM reservations";
    private static final String INSERT_RESERVATION      =
            "INSERT INTO `reservations`(`customerName`, `customerPhoneNumber`, `partySize`, `date`, `time`, `tableNumber`) VALUES (?,?,?,?,?,?)";
    private static final String UPDATE_RESERVATION      =
            "UPDATE `reservations` SET `customerName`=?, `customerPhoneNumber`=?, `partySize`=?,`date`=?,`time`=?, `tableNumber`=? WHERE id = ?";
    private static final String DELETE_RESERVATION      = "DELETE FROM `reservations` WHERE `id`=?";

    //endregion

    //region 1. Decl and Init Attribute
    //endregion

    //region 2. Konstruktoren
    //endregion

    //region 3. CRUD

    /**
     * Methode zum Einfügen eines Objektes in die Datenbank
     *
     * @param connection    Datenbankverbindung
     * @param modelToInsert Objekt einer bestimmten Modellklasse
     */
    @Override
    public void create(Connection connection, Reservation modelToInsert) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_RESERVATION)) {

            //Gibt an welches Fragezeichen im Prepared Statement, durch welchen Wert ersetzt wird
            final int statementParameterIndexCustomerName        = 1;
            final int statementParameterIndexCustomerPhoneNumber = 2;
            final int statementParameterIndexPartySize           = 3;
            final int statementParameterIndexDate                = 4;
            final int statementParameterIndexTime                = 5;
            final int statementParameterIndexTableNumber         = 6;


            statement.setString(statementParameterIndexCustomerName, modelToInsert.getCustomerName());
            statement.setString(statementParameterIndexCustomerPhoneNumber, modelToInsert.getCustomerPhoneNumber());
            statement.setInt(statementParameterIndexPartySize, modelToInsert.getPartySize());
            statement.setString(statementParameterIndexDate, modelToInsert.getDate());
            statement.setString(statementParameterIndexTime, modelToInsert.getTime());
            statement.setInt(statementParameterIndexTableNumber, modelToInsert.getTableNumber());

            statement.executeUpdate();

//			ResultSet statementGeneratedKeys = statement.getGeneratedKeys();
//			if(statementGeneratedKeys.next()){
//				modelToInsert.setId(statementGeneratedKeys.getInt(COLUMN_ID));
//			}


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode zum Auslesen von Objekten aus der Datenbank
     *
     * @param connection Datenbankverbindung
     * @return Liste von Objekten
     */
    @Override
    public List<Reservation> readAll(Connection connection) {
        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_RESERVATIONS)) {
            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Reservation reservationFromDatabaseTable = getModelFromResultSet(resultSet);
                reservations.add(reservationFromDatabaseTable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reservations;
    }

    /**
     * Methode zum Aktualisieren eines Objektes in der Datenbank
     *
     * @param connection    Datenbankverbindung
     * @param modelToUpdate Objekt einer bestimmten Modellklasse
     */
    @Override
    public void update(Connection connection, Reservation modelToUpdate) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_RESERVATION)) {

            //Gibt an welches Fragezeichen im Prepared Statement durch welchen Wert ersetzt wird
            final int statementParameterIndexCustomerName        = 1;
            final int statementParameterIndexCustomerPhoneNumber = 2;
            final int statementParameterIndexPartySize           = 3;
            final int statementParameterIndexDate                = 4;
            final int statementParameterIndexTime                = 5;
            final int statementParameterIndexTableNumber         = 6;
            final int statementParameterIndexId                  = 7;

            statement.setString(statementParameterIndexCustomerName, modelToUpdate.getCustomerName());
            statement.setString(statementParameterIndexCustomerPhoneNumber, modelToUpdate.getCustomerPhoneNumber());
            statement.setInt(statementParameterIndexPartySize, modelToUpdate.getPartySize());
            statement.setString(statementParameterIndexDate, modelToUpdate.getDate());
            statement.setString(statementParameterIndexTime, modelToUpdate.getTime());
            statement.setInt(statementParameterIndexTableNumber, modelToUpdate.getTableNumber());
            statement.setInt(statementParameterIndexId, modelToUpdate.getId());

            statement.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Methode zum Löschen eines Objektes aus der Datenbank
     *
     * @param connection    Datenbankverbindung
     * @param modelToDelete Objekt einer bestimmten Modellklasse
     */
    @Override
    public void delete(Connection connection, Reservation modelToDelete) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_RESERVATION)) {

            final int statementParameterIndexId = 1;

            statement.setInt(statementParameterIndexId, modelToDelete.getId());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Hilfsmethoden und Funktionen

    /**
     * Nimmt die Ergebnismenge und formt ein konkretes Datennmodel daraus
     *
     * @param resultSet : {@link ResultSet} : Ergebnismenge der aktuellen Abfrage
     * @return : {@link Reservation} : Datenmodell aus ResultSet
     */
    @Override
    public Reservation getModelFromResultSet(ResultSet resultSet) throws SQLException {
        int    id                  = resultSet.getInt(COLUMN_ID);
        String customerName        = resultSet.getString(COLUMN_CUSTOMER_NAME);
        String customerPhoneNumber = resultSet.getString(COLUMN_CUSTOMER_PHONE_NUMBER);
        int    partySize           = resultSet.getInt(COLUMN_PARTY_SIZE);
        String date                = resultSet.getString(COLUMN_DATE);
        String time                = resultSet.getString(COLUMN_TIME);
        int    tableNumber         = resultSet.getInt(COLUMN_TABLE_NUMBER);

        return new Reservation(
                id,
                customerName,
                customerPhoneNumber,
                partySize,
                date,
                time,
                tableNumber
        );

    }


    //endregion
}
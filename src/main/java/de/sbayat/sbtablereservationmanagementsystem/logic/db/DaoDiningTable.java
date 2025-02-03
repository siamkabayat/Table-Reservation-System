package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import de.sbayat.sbtablereservationmanagementsystem.model.DiningTable;

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
public class DaoDiningTable implements Dao<DiningTable> {


    //region 0. Konstanten
    private static final String COLUMN_NUMBER       = "number";
    private static final String COLUMN_CAPACITY     = "capacity";
    private static final String COLUMN_IS_AVAILABLE = "isAvailable";
    private static final String COLUMN_LOCATION     = "location";

    /*
     * SQL-Statements
     * Fragezeichen sind Platzhalter die nachher mit
     * dem PreparedStatement durch statementParameterInidzes gezielt
     * mit den Werten des Datenmodells ersetzt werden
     */
    private static final String SELECT_ALL_TABLES = "SELECT * FROM restaurant_tables";
    private static final String INSERT_TABLE      =
            "INSERT INTO `restaurant_tables`(`number`, `capacity`, `isAvailable`, `location`) VALUES (?,?,?,?)";
    private static final String UPDATE_TABLE      =
            "UPDATE `restaurant_tables` SET `number`=?, `capacity`=?, `isAvailable`=?,`location`=? WHERE id = ?";
    private static final String DELETE_TABLE      = "DELETE FROM `restaurant_tables` WHERE `number`=?";

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
    public void create(Connection connection, DiningTable modelToInsert) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TABLE)) {

            //Gibt an welches Fragezeichen im Prepared Statement, durch welchen Wert ersetzt wird
            final int statementParameterIndexNumber      = 1;
            final int statementParameterIndexCapacity    = 2;
            final int statementParameterIndexIsAvailable = 3;
            final int statementParameterIndexLocation    = 4;


            statement.setInt(statementParameterIndexNumber, modelToInsert.getNumber());
            statement.setInt(statementParameterIndexCapacity, modelToInsert.getCapacity());
            statement.setBoolean(statementParameterIndexIsAvailable, modelToInsert.isAvailable());
            statement.setString(statementParameterIndexLocation, modelToInsert.getLocation());


            statement.executeUpdate();


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
    public List<DiningTable> readAll(Connection connection) {
        List<DiningTable> diningTables = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TABLES)) {
            statement.execute();

            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next()) {
                DiningTable tableFromDatabase = getModelFromResultSet(resultSet);
                diningTables.add(tableFromDatabase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diningTables;
    }

    /**
     * Methode zum Aktualisieren eines Objektes in der Datenbank
     *
     * @param connection    Datenbankverbindung
     * @param modelToUpdate Objekt einer bestimmten Modellklasse
     */
    @Override
    public void update(Connection connection, DiningTable modelToUpdate) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TABLE)) {

            //Gibt an welches Fragezeichen im Prepared Statement durch welchen Wert ersetzt wird
            final int statementParameterIndexNumber      = 1;
            final int statementParameterIndexCapacity    = 2;
            final int statementParameterIndexIsAvailable = 3;
            final int statementParameterLocation         = 4;

            statement.setString(statementParameterIndexNumber, modelToUpdate.getNumber().toString());
            statement.setString(statementParameterIndexCapacity, modelToUpdate.getCapacity().toString());
            statement.setBoolean(statementParameterIndexIsAvailable, modelToUpdate.isAvailable());
            statement.setString(statementParameterLocation, modelToUpdate.getLocation());

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
    public void delete(Connection connection, DiningTable modelToDelete) {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_TABLE)) {

            final int statementParameterIndexNumber = 1;

            statement.setInt(statementParameterIndexNumber, modelToDelete.getNumber());

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
     * @return : {@link DiningTable} : Datenmodell aus ResultSet
     */
    @Override
    public DiningTable getModelFromResultSet(ResultSet resultSet) throws SQLException {
        int     number      = resultSet.getInt(COLUMN_NUMBER);
        int     capacity    = resultSet.getInt(COLUMN_CAPACITY);
        boolean isAvailable = resultSet.getBoolean(COLUMN_IS_AVAILABLE);
        String  location    = resultSet.getString(COLUMN_LOCATION);

        return new DiningTable(
                number,
                capacity,
                isAvailable,
                location
        );
    }
    //endregion
}
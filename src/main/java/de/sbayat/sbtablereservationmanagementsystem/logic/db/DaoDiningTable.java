package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import de.sbayat.sbtablereservationmanagementsystem.model.DiningTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DaoDiningTable class implements the {@link Dao} interface to provide CRUD operations
 * for interacting with the "restaurant_tables" database table. It offers methods to insert,
 * update, delete, and retrieve dining table records.
 *
 * This class maps the results of SQL queries to DiningTable model objects and executes
 * database operations using {@link PreparedStatement}.
 */
public class DaoDiningTable implements Dao<DiningTable> {


    //region 0. Konstanten
    private static final String COLUMN_NUMBER       = "number";
    private static final String COLUMN_CAPACITY     = "capacity";
    private static final String COLUMN_LOCATION     = "location";


    private static final String SELECT_ALL_TABLES = "SELECT * FROM restaurant_tables";
    private static final String SELECT_TABLE_BY_NUMBER = "SELECT * FROM restaurant_tables WHERE number = ?";
    private static final String INSERT_TABLE      =
            "INSERT INTO `restaurant_tables`(`number`, `capacity`, `location`) VALUES (?,?,?)";
    private static final String UPDATE_TABLE      =
            "UPDATE `restaurant_tables` SET `number`=?, `capacity`=?,`location`=? WHERE id = ?";
    private static final String DELETE_TABLE      = "DELETE FROM `restaurant_tables` WHERE `number`=?";



    /**
     * Inserts a DiningTable object into the database.
     *
     * @param connection Database connection
     * @param modelToInsert The DiningTable object to insert
     */
    @Override
    public void create(Connection connection, DiningTable modelToInsert) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TABLE)) {

            //Gibt an welches Fragezeichen im Prepared Statement, durch welchen Wert ersetzt wird
            final int statementParameterIndexNumber      = 1;
            final int statementParameterIndexCapacity    = 2;
            final int statementParameterIndexLocation    = 3;


            statement.setInt(statementParameterIndexNumber, modelToInsert.getNumber());
            statement.setInt(statementParameterIndexCapacity, modelToInsert.getCapacity());
            statement.setString(statementParameterIndexLocation, modelToInsert.getLocation());


            statement.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all DiningTable objects from the database.
     *
     * @param connection Database connection
     * @return A list of DiningTable objects
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
     * Updates an existing DiningTable object in the database.
     *
     * @param connection Database connection
     * @param modelToUpdate The DiningTable object to update
     */
    @Override
    public void update(Connection connection, DiningTable modelToUpdate) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TABLE)) {

            //Gibt an welches Fragezeichen im Prepared Statement durch welchen Wert ersetzt wird
            final int statementParameterIndexNumber      = 1;
            final int statementParameterIndexCapacity    = 2;
            final int statementParameterLocation         = 3;

            statement.setString(statementParameterIndexNumber, modelToUpdate.getNumber().toString());
            statement.setString(statementParameterIndexCapacity, modelToUpdate.getCapacity().toString());
            statement.setString(statementParameterLocation, modelToUpdate.getLocation());

            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a DiningTable object from the database.
     *
     * @param connection Database connection
     * @param modelToDelete The DiningTable object to delete
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

    /**
     * Converts a ResultSet into a DiningTable model object.
     *
     * @param resultSet The ResultSet containing the query results
     * @return The DiningTable object mapped from the ResultSet
     * @throws SQLException If a database access error occurs
     */
    @Override
    public DiningTable getModelFromResultSet(ResultSet resultSet) throws SQLException {
        int     number      = resultSet.getInt(COLUMN_NUMBER);
        int     capacity    = resultSet.getInt(COLUMN_CAPACITY);
        String  location    = resultSet.getString(COLUMN_LOCATION);

        return new DiningTable(
                number,
                capacity,
                location
        );
    }

    //endregion
}
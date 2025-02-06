package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * The Dao<T> interface defines the standard CRUD operations
 * (Create, Read, Update, Delete) for database access.
 *
 * It provides a generic structure for interacting with database
 * tables by handling objects of type T.
 * @param <T> The type of the model object handled by the DAO
 */
public interface Dao<T> {

    /**
     * Inserts an object into the database.
     *
     * @param connection Database connection
     * @param modelToInsert The object to be inserted
     */
    void create(Connection connection, T modelToInsert);

    /**
     * Reads all objects of type T from the database.
     *
     * @param connection Database connection
     * @return A list of retrieved objects
     */
    List<T> readAll(Connection connection);

    /**
     * Updates an existing object in the database.
     *
     * @param connection Database connection
     * @param modelToUpdate The object with updated values
     */
    void update(Connection connection, T modelToUpdate);

    /**
     * Deletes an object from the database.
     *
     * @param connection Database connection
     * @param modelToDelete The object to be deleted
     */
    void delete(Connection connection, T modelToDelete);


    /**
     * Converts a {@link ResultSet} into a model object of type T.
     *
     * @param resultSet The result set containing database query results
     * @return The corresponding model object
     * @throws SQLException If an SQL error occurs while processing the ResultSet
     */
    T getModelFromResultSet(ResultSet resultSet) throws SQLException;
}

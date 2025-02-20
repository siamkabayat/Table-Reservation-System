package de.sbayat.sbtablereservationmanagementsystem.logic.db;


import de.sbayat.sbtablereservationmanagementsystem.model.Reservation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


public class DaoReservation implements Dao<Reservation> {


    private static final String COLUMN_ID                    = "id";
    private static final String COLUMN_CUSTOMER_NAME         = "customerName";
    private static final String COLUMN_CUSTOMER_PHONE_NUMBER = "customerPhoneNumber";
    private static final String COLUMN_PARTY_SIZE            = "partySize";
    private static final String COLUMN_DATE                  = "date";
    private static final String COLUMN_TIME                  = "time";
    private static final String COLUMN_TABLE_NUMBER          = "tableNumber";


    private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM reservations";
    private static final String INSERT_RESERVATION      =
            "INSERT INTO `reservations`(`customerName`, `customerPhoneNumber`, `partySize`, `date`, `time`, `tableNumber`) VALUES (?,?,?,?,?,?)";
    private static final String UPDATE_RESERVATION      =
            "UPDATE `reservations` SET `customerName`=?, `customerPhoneNumber`=?, `partySize`=?,`date`=?,`time`=?, `tableNumber`=? WHERE id = ?";
    private static final String DELETE_RESERVATION      = "DELETE FROM `reservations` WHERE `id`=?";


    @Override
    public void create(Connection connection, Reservation modelToInsert) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_RESERVATION)) {

            final int statementParameterIndexCustomerName        = 1;
            final int statementParameterIndexCustomerPhoneNumber = 2;
            final int statementParameterIndexPartySize           = 3;
            final int statementParameterIndexDate                = 4;
            final int statementParameterIndexTime                = 5;
            final int statementParameterIndexTableNumber         = 6;


            statement.setString(statementParameterIndexCustomerName, modelToInsert.getCustomerName());
            statement.setString(statementParameterIndexCustomerPhoneNumber, modelToInsert.getCustomerPhoneNumber());
            statement.setInt(statementParameterIndexPartySize, modelToInsert.getPartySize());
            statement.setDate(statementParameterIndexDate, Date.valueOf(modelToInsert.getDate()));
            statement.setString(statementParameterIndexTime, modelToInsert.getTime());
            statement.setInt(statementParameterIndexTableNumber, modelToInsert.getTableNumber());

            statement.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

    @Override
    public void update(Connection connection, Reservation modelToUpdate) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_RESERVATION)) {

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
            statement.setDate(statementParameterIndexDate, Date.valueOf(modelToUpdate.getDate()));
            statement.setString(statementParameterIndexTime, modelToUpdate.getTime());
            statement.setInt(statementParameterIndexTableNumber, modelToUpdate.getTableNumber());
            statement.setInt(statementParameterIndexId, modelToUpdate.getId());

            statement.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public Reservation getModelFromResultSet(ResultSet resultSet) throws SQLException {
        int       id                  = resultSet.getInt(COLUMN_ID);
        String    customerName        = resultSet.getString(COLUMN_CUSTOMER_NAME);
        String    customerPhoneNumber = resultSet.getString(COLUMN_CUSTOMER_PHONE_NUMBER);
        int       partySize           = resultSet.getInt(COLUMN_PARTY_SIZE);
        LocalDate date                = resultSet.getDate(COLUMN_DATE).toLocalDate();
        String    time                = resultSet.getString(COLUMN_TIME);
        int       tableNumber         = resultSet.getInt(COLUMN_TABLE_NUMBER);

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

}
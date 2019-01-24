package xyz.sakret.api.sql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.sakret.api.donation.entity.Donation;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Author: Starikov Vyacheslav
 * Department: 8011
 * Date: 21.01.19 9:11
 */
public class DataBase implements SQL {
    private static final Logger log = LogManager.getLogger(DataBase.class);
    private static Connection connection;
    private Map<String, String> configOverrides;
    private boolean connect = false;

    //--------------------------------------------------------------------------------
    @Override
    public void initDataBase(Map<String, String> config) {
        this.configOverrides = config;
    }

    //--------------------------------------------------------------------------------
    @Override
    public Connection getConnection() {
        connection = null;
        try {
            Class.forName(configOverrides.get("driverClass"));
            if (!isConnect()) {
                connection = DriverManager.getConnection(configOverrides.get("url"), configOverrides.get("username"), configOverrides.get("password"));
                if (!connection.isClosed()) {
                    log.info("Подключение к базе данных.");
                    connect = true;
                    return connection;
                }
            }
        } catch (Exception e) {
            log.error("Ошибка подключения к базе данных!!! " + e);
        }
        return connection;
    }

    //--------------------------------------------------------------------------------
    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
                log.info("Отключение от бызы данных.");
                connect = false;
            }
        } catch (SQLException e) {
            log.error("Ошибка закрытия соединения: " + e);
        }
    }

    //--------------------------------------------------------------------------------
    @Override
    public boolean isConnect() {
        return connect;
    }

    //--------------------------------------------------------------------------------
    private void closeStatementAndResultSet(Statement statement, ResultSet rs) {
        try {
            if (statement != null) statement.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            log.error("SQLException : code = " + String.valueOf(e.getErrorCode()) + " - " + e.getMessage());
        }
    }

    //--------------------Donation Table--------------------------------

    @Override
    public void addDonation(Donation donation) {
        PreparedStatement statement = null;

    }

    @Override
    public void getLastDonation(Timestamp start, Timestamp end) {

    }

    @Override
    public void getTopDonation(Timestamp start, Timestamp end) {

    }

    @Override
    public List<Donation> getAllDonation() {
        return null;
    }

    @Override
    public List<Donation> getAllDonationFromDonor(String donor) {
        return null;
    }

    @Override
    public List<Donation> getAllDonationFromTime(Timestamp start, Timestamp end) {
        return null;
    }
}

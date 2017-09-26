package org.temqua.databaseService;

import org.temqua.databaseService.dao.LogDAO;
import org.temqua.databaseService.dataSets.LogDataSet;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DatabaseService {
    private final Connection connection;

    public DatabaseService() {
        this.connection = getConnection();
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:mem:logs";
            connection = DriverManager.getConnection(url, "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Long addLog(Timestamp requestTime, Timestamp responseTime, String statusMessage, String toLang, String fromLang, String word, String yandexResponse, String ipAddress) throws DatabaseException {
        try {
            connection.setAutoCommit(false);
            LogDAO dao = new LogDAO(connection);
            dao.createTable();
            dao.insertLog(requestTime, responseTime, toLang, fromLang, yandexResponse, word, statusMessage, ipAddress);
            connection.commit();
            return dao.getId(requestTime);
        } catch (SQLException e) {
            try {
                connection.rollback();
                e.printStackTrace();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
            throw new DatabaseException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {
            }
        }
    }

    public void createTable() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        LogDAO dao = new LogDAO(connection);
    }

    public LogDataSet getLog(Long id) throws DatabaseException{
        try {
            return (new LogDAO(connection).get(id));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

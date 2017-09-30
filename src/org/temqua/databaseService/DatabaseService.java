package org.temqua.databaseService;

import org.temqua.bundles.ResBundle;
import org.temqua.databaseService.dao.LogDAO;
import org.temqua.databaseService.dataSets.LogDataSet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DatabaseService {
    private final Connection connection;

    public DatabaseService() {
        this.connection = getConnection();
    }

    public Connection getConnection() {
        Connection connection = null;
        ResBundle bundle = new ResBundle();
        try {
            Class.forName(bundle.getValue("database.driver"));
            String url = bundle.getValue("database.url");
            connection = DriverManager.getConnection(url, "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Long addLog(Timestamp requestTime, Timestamp responseTime, String statusMessage, String toLang, String fromLang, String word, String yandexResponse, String ipAddress, Integer sessionNumber, String sessionID) throws DatabaseException {
        try {
            connection.setAutoCommit(false);
            LogDAO dao = new LogDAO(connection);
            dao.createTable();
            dao.insertLog(requestTime, responseTime, toLang, fromLang, yandexResponse, word, statusMessage, ipAddress, sessionNumber, sessionID);
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
            new LogDAO(connection).createTable();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public LogDataSet getLog(Long id) throws DatabaseException{
        try {
            return (new LogDAO(connection).get(id));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public List<LogDataSet> getLogsContent() throws DatabaseException {
        try {
            return (new LogDAO(connection).getAll());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}

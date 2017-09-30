package org.temqua.databaseService.dao;

import org.temqua.databaseService.dataSets.LogDataSet;
import org.temqua.databaseService.executor.Executor;
import org.temqua.databaseService.executor.IResultHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogDAO {

    private final String SELECT_LOGS_BY_ID_QUERY = "SELECT * FROM LOGS WHERE ID=?";
    private final String SELECT_ALL = "SELECT * FROM LOGS";
    private final String GET_LOG_BY_DATE_QUERY = "SELECT * FROM LOGS WHERE REQUEST_TIME=?";
    private final String GET_ID_QUERY = "SELECT ID FROM LOGS WHERE REQUEST_TIME=?";
    private final String INSERT_LOGS_QUERY = "INSERT INTO LOGS (REQUEST_TIME,RESPONSE_TIME,TO_LANG,FROM_LANG,TRANSLATING_WORD,YANDEX_RESPONSE,STATUS_MESSAGE,IP_ADDRESS,SESSION_NUMBER,SESSION_ID) values (?,?,?,?,?,?,?,?,?,?);";
    private final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS LOGS(\n" +
            "    ID IDENTITY NOT NULL,\n" +
            "    REQUEST_TIME TIMESTAMP,\n" +
            "    RESPONSE_TIME TIMESTAMP,\n" +
            "    TO_LANG CHAR(4),\n" +
            "    FROM_LANG CHAR(4),\n" +
            "    TRANSLATING_WORD VARCHAR(100),\n" +
            "    YANDEX_RESPONSE VARCHAR(100),\n" +
            "    STATUS_MESSAGE VARCHAR(100),\n" +
            "    IP_ADDRESS CHAR(18),\n" +
            "    SESSION_NUMBER INT,\n" +
            "    SESSION_ID VARCHAR(100)\n" +
            ")";
    private final String DELETE_FROM_LOGS_TABLE_QUERY="DELETE FROM LOGS WHERE ID=?";
    private final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS LOGS";

    private Executor executor;

    public LogDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public LogDataSet get(Long id) throws SQLException {
        return executor.execQuery(SELECT_LOGS_BY_ID_QUERY, id, resultset -> {
            resultset.next();
            return new LogDataSet(resultset.getLong(1), resultset.getTimestamp(2), resultset.getTimestamp(3), resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7), resultset.getString(8), resultset.getString(9), resultset.getInt(10), resultset.getString(11));
        });
    }

    public List<LogDataSet> getAll() throws SQLException {
        List<LogDataSet> table = new ArrayList<>();
        return executor.execQuery(SELECT_ALL, resultset -> {
            while (resultset.next()) {
                table.add(new LogDataSet(resultset.getLong(1), resultset.getTimestamp(2), resultset.getTimestamp(3), resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7), resultset.getString(8), resultset.getString(9), resultset.getInt(10), resultset.getString(11)));
            }
            return table;
        });
    }

    public LogDataSet getLogByDate(Timestamp requestDate) throws SQLException {
        return executor.execQuery(GET_LOG_BY_DATE_QUERY, requestDate, resultset -> {
            resultset.next();
            return new LogDataSet(resultset.getLong(1), resultset.getTimestamp(2), resultset.getTimestamp(3), resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7), resultset.getString(8), resultset.getString(9), resultset.getInt(10), resultset.getString(11));
        });
    }

    public Long getId(Timestamp requestDate) throws SQLException {
        return executor.execQuery(GET_ID_QUERY, requestDate, result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void insertLog(Timestamp requestTime, Timestamp responseTime, String toLang, String fromLang, String yandexResponse, String wordForTranslate, String statusMessage, String ipAddress, Integer sessionNumber, String sessionID) throws SQLException {
        LogDataSet dataSet = new LogDataSet(1L, requestTime, responseTime, fromLang, toLang, wordForTranslate, yandexResponse, statusMessage, ipAddress, sessionNumber, sessionID);
        executor.execUpdate(INSERT_LOGS_QUERY, dataSet);
    }

    public void deleteLog(Long id) throws SQLException {
        executor.execUpdate(DELETE_FROM_LOGS_TABLE_QUERY);
    }

    public void createTable() throws SQLException {
        executor.execUpdate(CREATE_TABLE_QUERY);
    }

    public void dropTable() throws SQLException {
        executor.execUpdate(DROP_TABLE_QUERY);
    }

}

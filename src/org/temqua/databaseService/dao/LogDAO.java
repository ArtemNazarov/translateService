package org.temqua.databaseService.dao;

import org.temqua.databaseService.dataSets.LogDataSet;
import org.temqua.databaseService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

public class LogDAO {

    private Executor executor;

    public LogDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public LogDataSet get(Long id) throws SQLException {
        return executor.execQuery("SELECT * FROM LOGS WHERE ID=" + id, resultset -> {
            resultset.next();
            return new LogDataSet(resultset.getLong(1), resultset.getTimestamp(2),resultset.getTimestamp(3),  resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7), resultset.getString(8), resultset.getString(9));
        });
    }

    public LogDataSet getLogByDate(Timestamp requestDate) throws SQLException {
        return executor.execQuery("SELECT * FROM LOGS WHERE REQUEST_TIME='" + requestDate + "'", resultset -> {
            resultset.next();
            return new LogDataSet(resultset.getLong(1), resultset.getTimestamp(2),resultset.getTimestamp(3),  resultset.getString(4), resultset.getString(5), resultset.getString(6), resultset.getString(7), resultset.getString(8), resultset.getString(9));
        });
    }

    public Long getId(Timestamp requestDate) throws SQLException {
        return executor.execQuery("SELECT ID FROM LOGS WHERE REQUEST_TIME='" + requestDate + "'", result -> {
            result.next();
            return result.getLong(1);
        });
    }

    public void insertLog(Timestamp requestTime, Timestamp responseTime, String toLang, String fromLang, String yandexResponse, String wordForTranslate, String statusMessage, String ipAddress) throws SQLException {
        executor.execUpdate("INSERT INTO LOGS (REQUEST_TIME,RESPONSE_TIME,TO_LANG,FROM_LANG,TRANSLATING_WORD,YANDEX_RESPONSE,STATUS_MESSAGE,IP_ADDRESS) values ('" + requestTime.toString() + "','" + responseTime.toString() + "','" + toLang + "','" + fromLang + "','" + wordForTranslate + "','" + yandexResponse + "','" + statusMessage + "','" + ipAddress + "');");
    }

    public void createTable() throws SQLException {
        executor.execUpdate("CREATE TABLE IF NOT EXISTS LOGS(\n" +
                "    ID IDENTITY NOT NULL,\n" +
                "    REQUEST_TIME TIMESTAMP,\n" +
                "    RESPONSE_TIME TIMESTAMP,\n" +
                "    TO_LANG CHAR(4),\n" +
                "    FROM_LANG CHAR(4),\n" +
                "    TRANSLATING_WORD VARCHAR(100),\n" +
                "    YANDEX_RESPONSE VARCHAR(100),\n" +
                "    STATUS_MESSAGE VARCHAR(100),\n" +
                "    IP_ADDRESS CHAR(18)\n" +
                ")");
    }	

    public void dropTable() throws SQLException {
        executor.execUpdate("DROP TABLE IF EXISTS LOGS");
    }

}

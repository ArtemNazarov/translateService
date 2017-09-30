package org.temqua.databaseService.executor;

import org.temqua.controller.LogData;
import org.temqua.databaseService.dataSets.LogDataSet;

import java.sql.*;

public class Executor {

    private final Connection connection;


    public Executor(Connection connection) {
        this.connection = connection;
    }
    //«апросы на обновление данных в таблице
    public void execUpdate(String update) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(update);
        stmt.close();
    }
    //«апросы на возврат данных из таблицы
    public <T> T execQuery(String query,
                           IResultHandler<T> handler)
            throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();
        return value;
    }

    public void execUpdate(String update, LogDataSet dataSet) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(update);
        preparedStatement.setString(1, dataSet.getRequestTime().toString());
        preparedStatement.setString(2, dataSet.getResponseTime().toString());
        preparedStatement.setString(3, dataSet.getToLang());
        preparedStatement.setString(4, dataSet.getFromLang());
        preparedStatement.setString(5, dataSet.getTranslatingWord());
        preparedStatement.setString(6, dataSet.getYandexResponse());
        preparedStatement.setString(7, dataSet.getStatusMessage());
        preparedStatement.setString(8, dataSet.getIpAddress());
        preparedStatement.setInt(9, dataSet.getSessionNumber());
        preparedStatement.setString(10, dataSet.getSessionID());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public <T> T execQuery(String query, Timestamp requestTime,
                           IResultHandler<T> handler )
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, requestTime.toString());
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        T value = handler.handle(resultSet);
        resultSet.close();
        preparedStatement.close();
        return value;
    }

    public <T> T execQuery(String query, Long id,
                           IResultHandler<T> handler )
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.getResultSet();
        T value = handler.handle(resultSet);
        resultSet.close();
        preparedStatement.close();
        return value;
    }
}

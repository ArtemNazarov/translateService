package org.temqua.databaseService.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IResultHandler<T> {
    T handle (ResultSet resultset) throws SQLException;
}

package ru.ipccenter.travelportal.jdbc.template;

import javax.ejb.Local;
import java.sql.SQLException;


@Local
public interface JdbcTemplate {

    <T> T executeSelect(String sql, Object[][] params, ResultSetHandler<T> handler) throws SQLException;
    <T> T executeSelect(String sql, ResultSetHandler<T> handler) throws SQLException;
    int executeUpdate(String sql, Object[][] params) throws SQLException;
    int executeUpdate(String sql) throws SQLException;
    <T> T executeCall(String plSql, Object[][] params, CallableHandler<T> handler) throws SQLException;
    <T> T executeCall(String plSql, CallableHandler<T> handler) throws SQLException;
    void execute(String sql) throws SQLException;
    <T> T execute(CallableStatementCreator csc, CallableHandler<T> handler) throws SQLException;
}

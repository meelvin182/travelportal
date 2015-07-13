package ru.ipccenter.travelportal.jdbc.template;

import javax.ejb.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;


@Remote
public interface JdbcTemplateRemote {
    int executeUpdate(String sql, Object[][] params) throws SQLException, RemoteException;
    int executeUpdate(String sql) throws SQLException, RemoteException;
    void execute(String sql) throws SQLException, RemoteException;
}
package ru.ipccenter.travelportal.jdbc.template;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vadim on 19/03/15.
 */
public interface CallableStatementCreator {

    /**
     * Create a callable statement in this connection
     * @param con - Connection to use to create statement
     * @return a callable statement
     * @throws SQLException
     */
    CallableStatement createCallableStatement(Connection con) throws SQLException;
}

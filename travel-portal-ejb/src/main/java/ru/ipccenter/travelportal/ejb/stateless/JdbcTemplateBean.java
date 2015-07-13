package ru.ipccenter.travelportal.ejb.stateless;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.utils.SqlParser;
import ru.ipccenter.travelportal.jdbc.template.*;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Stateless
@Local(JdbcTemplate.class)
@Remote(JdbcTemplateRemote.class)
public class JdbcTemplateBean implements JdbcTemplate, JdbcTemplateRemote {

    private static final Logger LOG = Logger.getLogger(JdbcTemplateBean.class);


    @PersistenceContext(unitName = "ru.ipccenter.travelportal.pu")
    EntityManager em;

    private void closeConnection(Connection connection) throws SQLException {
//        if (connection != null && !connection.isClosed()) {
//            connection.close();
//        }
    }

    private void closeStatement(PreparedStatement statement) throws SQLException {
        if (statement != null && !statement.isClosed()) {
            statement.close();
        }
    }

    private void addParams(PreparedStatement statement, Object[][] params) throws SQLException {
            int paramIndex = 0;
            for (Object[] param : params) {
                if (param.length < 2) {
                    LOG.error("Bad params");
                    throw new SQLException();
                }
                Object sqlType = param[0];
                Object parameter = param[1];

                if (!(sqlType instanceof Integer)) {
                    if (LOG.isTraceEnabled())
                        LOG.error("Bad sqlType: " + sqlType.toString());
                    throw new SQLException();
                }
                statement.setObject(++paramIndex, parameter, (Integer) sqlType);

            }
    }

    private void addParams(CallableStatement statement, Object[][] params) throws SQLException {
        int paramIndex = 0;
        for (Object[] param : params) {
            if (param.length < 3) {
                LOG.error("Bad params");
                throw new SQLException();
            }
            Object sqlType = param[0];
            Object parameter = param[1];
            Object inout = param[2];

            if (!(sqlType instanceof Integer && inout instanceof ParameterDirection)) {
                if (LOG.isTraceEnabled())
                    LOG.error("Bad sqlType: " + sqlType.toString());
                throw new SQLException();
            }

            switch ((ParameterDirection)inout) {
                case IN:
                    statement.setObject(++paramIndex, parameter, (Integer) sqlType);
                    break;
                case OUT:
                    statement.registerOutParameter(++paramIndex, (Integer) sqlType);
                    break;
                case INOUT:
                    statement.setObject(++paramIndex, parameter);
                    statement.registerOutParameter(paramIndex, (Integer) sqlType);
                    break;
            }

        }
    }
    @Override
    public <T> T executeSelect(String sql, Object[][] params, ResultSetHandler<T> handler) throws SQLException {
        Connection connection = em.unwrap(Connection.class);
        PreparedStatement statement = null;
        SqlParser.parseInAny(sql);
        try {
            statement = connection.prepareStatement(sql);
            addParams(statement, params);
            return handler.handle(statement.executeQuery());
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    @Override
    public <T> T executeSelect(String sql, ResultSetHandler<T> handler) throws SQLException{
        return executeSelect(sql, new Object[0][0], handler);
    }

    @Override
    public int executeUpdate(String sql, Object[][] params) throws SQLException {
        Connection connection = em.unwrap(Connection.class);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setQueryTimeout(3);
            addParams(statement, params);
            return statement.executeUpdate();
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return executeUpdate(sql, new Object[0][0]);
    }

    @Override
    public <T> T executeCall(String plSql, Object[][] params, CallableHandler<T> handler) throws SQLException {
        Connection connection = em.unwrap(Connection.class);
        CallableStatement statement = null;
        try {
            statement = connection.prepareCall(plSql);
            addParams(statement, params);
            statement.execute();
            return handler.handle(statement);
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    @Override
    public <T> T executeCall(String plSql, CallableHandler<T> handler) throws SQLException {
        return executeCall(plSql, new Object[0][0], handler);
    }

    @Override
    public void execute(String sql) throws SQLException {
        Connection connection = em.unwrap(Connection.class);
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.execute();
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    @Override
    public <T> T execute(CallableStatementCreator csc, CallableHandler<T> handler) throws SQLException{
        Connection connection = em.unwrap(Connection.class);
        CallableStatement statement = csc.createCallableStatement(connection);
        statement.execute();
        return handler.handle(statement);
    }
}

package ru.ipccenter.travelportal.jdbc.template;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class ResultSetHandler<R> implements Serializable {

    abstract public R handle(ResultSet resultSet) throws SQLException;
}

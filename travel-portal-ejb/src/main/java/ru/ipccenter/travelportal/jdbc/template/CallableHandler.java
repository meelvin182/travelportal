package ru.ipccenter.travelportal.jdbc.template;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;

abstract public class CallableHandler<R> implements Serializable {

    abstract public R handle(CallableStatement statement) throws SQLException;
}

package ru.ipccenter.travelportal.ejb.stateless;


import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;
import ru.ipccenter.travelportal.metamodel.DaoParameter;
import ru.ipccenter.travelportal.metamodel.entities.MMParameter;

import javax.ejb.*;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Stateless
@Local(DaoParameter.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class DaoParameterBean implements DaoParameter {


    private static final String SAVE_QUERY =
            " merge into \n" +
            "    tp_params p\n" +
            " using (\n" +
            "    select \n" +
            "      ? object_id, ? attr_id, \n" +
            "      ? value, ? reference, ? list_value_id, ? date_value, \n" +
            "      ? order_num \n" +
            "    from dual) t\n" +
            " on (\n" +
            "  p.object_id = t.object_id \n" +
            "  and p.attr_id = t.attr_id \n" +
            "  and p.order_num = t.order_num)\n" +
            "when matched then \n" +
            "    update set \n" +
            "      p.list_value_id = t.list_value_id, p.date_value = t.date_value, p.value = t.value, p.reference = t.reference\n" +
            "when not matched then \n" +
            "    insert \n" +
            "      (p.object_id, p.attr_id, p.list_value_id, p.date_value, p.order_num, p.value, p.reference)\n" +
            "      values \n" +
            "      (t.object_id, t.attr_id, t.list_value_id, t.date_value, t.order_num, t.value, t.reference)";

    private static final String FIND_QUERY =
            "select\n" +
            "  object_id, attr_id,\n" +
            "  value, reference, list_value_id, date_value,\n" +
            "  (case\n" +
            "    when value is not null then 1\n" +
            "    when reference is not null then 2\n" +
            "    when list_value_id is not null then 3\n" +
            "    when date_value is not null then 4\n" +
            "    else 5\n" +
            "  end) value_type,\n" +
            "  order_num\n" +
            "from\n" +
            "  tp_params\n" +
            "where\n" +
            "  attr_id = ?\n" +
            "  and object_id = ?\n" +
            "order by order_num";

    private static final String REFRESH_QUERY =
            "select\n" +
            "  object_id, attr_id,\n" +
            "  value, reference, list_value_id, date_value,\n" +
            "  (case\n" +
            "    when value is not null then 1\n" +
            "    when reference is not null then 2\n" +
            "    when list_value_id is not null then 3\n" +
            "    when date_value is not null then 4\n" +
            "    else 5\n" +
            "  end) value_type,\n" +
            "  order_num\n" +
            "from\n" +
            "  tp_params\n" +
            "where\n" +
            "  attr_id = ?\n" +
            "  and object_id = ?\n" +
            "  and order_num = ?\n" +
            "order by order_num";

    private static final String DELETE_QUERY =
            "delete from\n" +
            "  tp_params\n" +
            "where\n" +
            "  attr_id = ?\n" +
            "  and object_id = ?\n" +
            "  and order_num = ?";

    public enum Type {
        Simple,
        Reference,
        ListValue,
        DateTimeParameter,

        Unknown;

        public static Type valueOf(int value) {
            if (value >= Simple.ordinal() + 1
                    && value <= DateTimeParameter.ordinal() + 1 ) {
                return Type.values()[value - 1];
            } else {
                return Unknown;
            }
        }
    }

    private static final Logger LOG = Logger.getLogger(DaoParameterBean.class);

    @EJB
    private JdbcTemplate jdbcTemplate;

    public void delete (MMParameter parameter) throws SQLException {
        if (parameter == null)
            return;

        if (parameter.getObjId() == null)
            throw new IllegalArgumentException("object id is not set");
        if (parameter.getAttrId() == null)
            throw new IllegalArgumentException("attribute id is not set");

        jdbcTemplate.executeUpdate(
                DELETE_QUERY,
                new Object[][]{
                        {Types.BIGINT, parameter.getAttrId()},
                        {Types.BIGINT, parameter.getObjId()},
                        {Types.INTEGER, parameter.getOrderNum()}
                });
    }

    public void delete(Collection<MMParameter> parameters) throws SQLException {
        for (MMParameter parameter: parameters) {
            this.delete(parameter);
        }
    }

    public void save(MMParameter parameter) throws SQLException {
        LOG.info(parameter.toString());
        if (parameter == null)
            return;

        if ((parameter.getValue() == null || parameter.getValue().isEmpty())
                && (parameter.getDateValue() == null)
                && parameter.getReference() == null
                && parameter.getListValueId() == null)
            return;

        if (parameter.getObjId() == null)
            throw new IllegalArgumentException("object id is not set");
        if (parameter.getAttrId() == null)
            throw new IllegalArgumentException("attribute id is not set");

        jdbcTemplate.executeUpdate(SAVE_QUERY,
                new Object[][] {
                    {Types.BIGINT, parameter.getObjId()},
                    {Types.BIGINT, parameter.getAttrId()},
                    {Types.VARCHAR, parameter.getValue()},
                    {Types.BIGINT, parameter.getReference()},
                    {Types.BIGINT, parameter.getListValueId()},
                    {Types.TIMESTAMP, parameter.getDateValue()},
                    {Types.INTEGER, parameter.getOrderNum()}
        });

    }

    public void save(Collection<MMParameter> parameters) throws SQLException {
        for (MMParameter parameter: parameters) {
            this.save(parameter);
        }
    }

    public void refresh(final MMParameter parameter) throws SQLException {
        if (parameter == null)
            return;

        if (parameter.getObjId() == null)
            throw new IllegalArgumentException("object id is not set");
        if (parameter.getAttrId() == null)
            throw new IllegalArgumentException("attribute id is not set");

        jdbcTemplate.executeSelect(
                REFRESH_QUERY,
                new Object[][]{
                        {Types.BIGINT, parameter.getAttrId()},
                        {Types.BIGINT, parameter.getObjId()},
                        {Types.INTEGER, parameter.getOrderNum()}
                },
                new ResultSetHandler<Void>() {
                    public Void handle (ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            if (resultSet.next()) {
                                throw new SQLException("More than one item has been found for"
                                        + " object#" + parameter.getAttrId()
                                        + ", attribute#" + parameter.getObjId()
                                        + ", order number#" + parameter.getOrderNum());
                            }

                            Type parameterType = Type.valueOf(resultSet.getInt("value_type"));

                            switch (parameterType) {
                                case Simple:
                                    parameter.setValue(resultSet.getString("value"));
                                    break;
                                case Reference:
                                    parameter.setReference(resultSet.getBigDecimal("reference").toBigInteger());
                                    break;
                                case ListValue:
                                    parameter.setListValueId(resultSet.getBigDecimal("list_value_id").toBigInteger());
                                    break;
                                case DateTimeParameter:
                                    parameter.setDateValue(resultSet.getTimestamp("date_value"));
                                    break;
                                case Unknown:
                            }
                        } else {
                            throw new SQLException("There is no any item for"
                                    + " object#" + parameter.getAttrId()
                                    + ", attribute#" + parameter.getObjId()
                                    + ", order number#" + parameter.getOrderNum());
                        }
                        return null;
                    }
                });

    }

    public void refresh(Collection<MMParameter> parameters) throws SQLException {
        for (MMParameter parameter: parameters) {
            this.refresh(parameter);
        }
        // TODO: Load collections from SQL (implement as bulk sql statement)

    }

    public List<MMParameter> getByObjIdAttrId (final BigInteger objId, final BigInteger attrId) throws SQLException {
        LOG.info("Object: " + objId + ", attribute: " + attrId);
        List<MMParameter> parameters;
        parameters = jdbcTemplate.executeSelect(FIND_QUERY,
                new Object[][]{
                        {Types.BIGINT, attrId},
                        {Types.BIGINT, objId}
                },
                new ResultSetHandler<List<MMParameter>>() {

                    public List<MMParameter> handle(ResultSet resultSet) throws SQLException {
                        List<MMParameter> parameters = new ArrayList<>();
                        while (resultSet.next()) {
                            MMParameter current = new MMParameter(objId, attrId, resultSet.getInt("order_num"));
                            Type parameterType = Type.valueOf(resultSet.getInt("value_type"));

                            LOG.info("got {");

                            switch (parameterType) {
                                case Simple:
                                    current.setValue(resultSet.getString("value"));
                                    LOG.info("      " + current.getValue());
                                    break;
                                case Reference:
                                    current.setReference(resultSet.getBigDecimal("reference").toBigInteger());
                                    LOG.info("      " + current.getReference());
                                    break;
                                case ListValue:
                                    current.setListValueId(resultSet.getBigDecimal("list_value_id").toBigInteger());
                                    LOG.info("      " + current.getListValueId());
                                    break;
                                case DateTimeParameter:
                                    current.setDateValue(resultSet.getTimestamp("date_value"));
                                    LOG.info("      " + current.getDateValue());
                                    break;
                                case Unknown:
                            }

                            LOG.info("}");

                            parameters.add(current);
                        }
                        return parameters;
                    }
                });
                return parameters;
    }

    public MMParameter getByObjIdAttrIdSingle (BigInteger objId, BigInteger attrId) throws SQLException {
        List<MMParameter> result = getByObjIdAttrId(objId, attrId);
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() == 0) {
            return null;
        } else {
            throw new SQLException("More than one item has been found for"
                    + " object#" + objId
                    + ", attribute#" + attrId);
        }
    }
}

package ru.ipccenter.travelportal.services;

import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.utils.ObjectTypeExtractor;
import ru.ipccenter.travelportal.common.model.objects.Position;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ivan on 18.05.2015.
 */
@ApplicationScoped
public class PositionService {

    private static final BigInteger POSITION_OT = ObjectTypeExtractor.extractObjectTypeId(Position.class);
    private static final BigInteger DEPARTMENT_ATTR = Position.DEPARTMENT_ATTRIBUTE;
    private static final String POSITIONS_BY_DEPARTMENT_QYERY =
            "select\n" +
            "  object_id\n" +
            "from\n" +
            "  tp_objects o,\n" +
            "  tp_params p\n" +
            "where\n" +
            "  o.object_type_id = ?\n" +
            "  o.object_id = p.object_id\n" +
            "  and p.attr_id = ?\n" +
            "  and p.reference = ?";

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    public Position getById(BigInteger id) {
        return objectFactory.createObject(id, Position.class);
    }

    public List<Position> getByDepartmentId(final BigInteger departmentId) {
        try {
            return jdbcTemplate.executeSelect(
                    POSITIONS_BY_DEPARTMENT_QYERY,
                    new Object[][]{
                            {Types.NUMERIC, POSITION_OT},
                            {Types.NUMERIC, DEPARTMENT_ATTR},
                            {Types.NUMERIC, departmentId}
                    },
                    new ResultSetHandler<List<Position>>() {
                        @Override
                        public List<Position> handle(ResultSet resultSet) throws SQLException {
                            List<Position> result = new LinkedList<Position>();

                            while (resultSet.next()) {
                                result.add(objectFactory.createObject(resultSet.getBigDecimal(1).toBigInteger(), Position.class));
                            }
                            return result;
                        }
                    }
            );
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }
}

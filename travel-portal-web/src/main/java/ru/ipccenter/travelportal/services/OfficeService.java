package ru.ipccenter.travelportal.services;

import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.utils.ObjectTypeExtractor;
import ru.ipccenter.travelportal.common.model.objects.Office;
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
public class OfficeService {

    private static final BigInteger OFFICE_OT = ObjectTypeExtractor.extractObjectTypeId(Office.class);
    private static final String OFFICES_QUERY =
                    "select\n" +
                    "  object_id\n" +
                    "from\n" +
                    "  tp_objects\n" +
                    "where\n" +
                    "  object_type_id = ?";

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    public Office create() {
        return objectFactory.createObject(Office.class);
    }

    public Office getById(BigInteger id) {
        return objectFactory.createObject(id, Office.class);
    }

    public List<Office> getOffices() {
        try {
            return jdbcTemplate.executeSelect(
                    OFFICES_QUERY,
                    new Object[][]{
                            {Types.NUMERIC, OFFICE_OT}
                    },
                    new ResultSetHandler<List<Office>>() {
                        @Override
                        public List<Office> handle(ResultSet resultSet) throws SQLException {
                            List<Office> result = new LinkedList<Office>();

                            while (resultSet.next()) {
                                result.add(objectFactory.createObject(resultSet.getBigDecimal(1).toBigInteger(), Office.class));
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

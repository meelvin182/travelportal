package ru.ipccenter.travelportal.ejb.stateless;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.ModelValidationException;
import ru.ipccenter.travelportal.common.model.TPObject;
import ru.ipccenter.travelportal.common.model.bridge.annotations.Attribute;
import ru.ipccenter.travelportal.common.model.bridge.annotations.ObjectType;
import ru.ipccenter.travelportal.common.model.bridge.utils.NameConverter;
import ru.ipccenter.travelportal.common.model.objects.*;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan on 26.04.2015.
 */
@Startup
@Singleton
public class StartupModelValidator {

    private static final Logger log = Logger.getLogger(StartupModelValidator.class);

    private static final String OT_ID_NAME_QUERY =
            "select\n" +
            "  object_type_id,\n" +
            "  name\n" +
            "from\n" +
            "  tp_object_types\n" +
            "where\n" +
            "  object_type_id = ?";
    private static final String ATTR_BIND_QUERY =
            "select\n" +
            "  aot.object_type_id,\n" +
            "  aot.attr_id,\n" +
            "  at.name\n" +
            "from\n" +
            "  tp_attributes at,\n" +
            "  tp_attr_obj_types aot\n" +
            "where\n" +
            "  at.attr_id = aot.attr_id\n" +
            "  and aot.object_type_id = ?\n" +
            "  and aot.attr_id = ?";

    @EJB
    private JdbcTemplate jdbcTemplate;
    private List<ModelValidationException> exceptionList;

    @PostConstruct
    public void validate() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        exceptionList = new LinkedList<>();

                        try {
                            validateTPObject(City.class);
                            validateTPObject(Country.class);
                            validateTPObject(Customer.class);
                            validateTPObject(Department.class);
                            validateTPObject(Employee.class);
                            validateTPObject(Office.class);
                            validateTPObject(Position.class);
                            validateTPObject(Role.class);
                            validateTPObject(StatusHistoryEntry.class);
                            validateTPObject(TRF.class);
                            validateTPObject(User.class);

                            log.warn("DB-Java model discrepancy report:");
                            for (ModelValidationException e: exceptionList) {
                                log.warn(e.getMessage());
                            }
                            log.warn("END discrepancy report:");
                        } catch (SQLException e) {
                            log.error(e.getMessage(), e);
                        }

                        return null;
                    }
                },
                10,
                TimeUnit.SECONDS
        );
    }

    public void validateTPObject(final Class<? extends TPObject> clazz) throws SQLException {
        final ObjectType objectType = clazz.getAnnotation(ObjectType.class);

        if (objectType == null) {
            exceptionList.add(new ModelValidationException(clazz.getCanonicalName() + " is not annotated by @ObjectType"));
            return;
        }

        if (objectType.id() == null || objectType.id().isEmpty()) {
            exceptionList.add(new ModelValidationException(clazz.getCanonicalName() + " annotation @ObjectType has empty ID"));
            return;
        }

        try {
            new BigInteger(objectType.id());
        } catch (NumberFormatException e) {
            exceptionList.add(new ModelValidationException(clazz.getCanonicalName()
                    + " annotation @ObjectType has non-castable to BigInteger ID value"));
            return;
        }

        jdbcTemplate.executeSelect(
                OT_ID_NAME_QUERY,
                new Object[][] {
                        { Types.VARCHAR, objectType.id() }
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {
                        if (!resultSet.next()) {
                            exceptionList.add(new ModelValidationException("Can't find object type with id = " + objectType.id()));
                            return null;
                        }

                        String dbName = resultSet.getString("name");
                        if (!objectType.name().equals(NameConverter.convert(dbName))) {
                            exceptionList.add(new ModelValidationException("Name оf object type with id = " + objectType.id()
                                + " is not equal to " + objectType.name() + " (was found: '" + dbName + "')"));
                        }

                        return null;
                    }
                });

        for (Attribute attribute: objectType.attributes()) {
            validateAttribute(objectType, attribute);
        }
    }


    public void validateAttribute(final ObjectType objectType, final Attribute attribute) throws SQLException {
        if (attribute.id() == null || attribute.id().isEmpty()) {
            exceptionList.add(new ModelValidationException(objectType.name() + " has annotation @Attribute with empty ID"));
            return;
        }

        try {
            new BigInteger(attribute.id());
        } catch (NumberFormatException e) {
            exceptionList.add(new ModelValidationException(objectType.name()
                    + " has annotation @Attribute with non-castable to BigInteger ID value"));
            return;
        }

        if (attribute.name() == null || attribute.name().isEmpty()) {
            exceptionList.add(new ModelValidationException(objectType.name() + " has annotation @Attribute with empty NAME"));
            return;
        }

        jdbcTemplate.executeSelect(
                ATTR_BIND_QUERY,
                new Object[][] {
                        { Types.VARCHAR, objectType.id() },
                        { Types.VARCHAR, attribute.id() }
                },
                new ResultSetHandler<Void>() {
                    @Override
                    public Void handle(ResultSet resultSet) throws SQLException {
                        if (!resultSet.next()) {
                            exceptionList.add(new ModelValidationException("Can't find binding of attribute: "
                                    + attribute.id() + " (" + attribute.name() + ")"
                                    + " to object type: "
                                    + objectType.id() + " (" + objectType.name() + ")"));
                            return null;
                        }

                        String dbName = resultSet.getString("name");

                        if (!NameConverter.convert(attribute.name()).equals(dbName)) {
                            exceptionList.add(new ModelValidationException("Name оf attribute with id = " + attribute.id()
                                    + " is not equal to " + attribute.name() + " (was found: '" + dbName + "')"));
                        }

                        return null;
                    }
                });
    }
}

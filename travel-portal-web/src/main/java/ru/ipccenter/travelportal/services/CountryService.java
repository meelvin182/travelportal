package ru.ipccenter.travelportal.services;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.utils.ObjectTypeExtractor;
import ru.ipccenter.travelportal.common.model.objects.Country;
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
 * Created by Ivan on 19.05.2015.
 */
@ApplicationScoped
public class CountryService {

    private static final Logger log = Logger.getLogger(CountryService.class);

    private static final BigInteger COUNTRY_OT = ObjectTypeExtractor.extractObjectTypeId(Country.class);
    private static final String COUNTRIES_QUERY = "select object_id from tp_objects where object_type_id = ? order by order_num";

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    public Country create() {
        return objectFactory.createObject(Country.class);
    }

    public Country getById(BigInteger id) {
        return objectFactory.createObject(id, Country.class);
    }

    public List<Country> getAllCountries() {
        try {
            return jdbcTemplate.executeSelect(
                    COUNTRIES_QUERY,
                    new Object[][]{
                            {Types.NUMERIC, COUNTRY_OT}
                    },
                    new ResultSetHandler<List<Country>>() {
                        @Override
                        public List<Country> handle(ResultSet resultSet) throws SQLException {
                            List<Country> countries = new LinkedList<Country>();
                            while (resultSet.next()) {
                                countries.add(objectFactory.createObject(resultSet.getBigDecimal(1).toBigInteger(), Country.class));
                            }
                            return countries;
                        }
                    }
            );
        } catch (SQLException e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }
}

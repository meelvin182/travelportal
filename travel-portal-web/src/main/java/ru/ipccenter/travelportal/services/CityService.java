package ru.ipccenter.travelportal.services;

import org.apache.log4j.Logger;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.bridge.utils.ObjectTypeExtractor;
import ru.ipccenter.travelportal.common.model.objects.City;
import ru.ipccenter.travelportal.jdbc.template.JdbcTemplate;
import ru.ipccenter.travelportal.jdbc.template.ResultSetHandler;

import javax.ejb.EJB;
import javax.inject.Singleton;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * Created by Ivan on 19.05.2015.
 */
@Singleton
public class CityService {

    private static final Logger log = Logger.getLogger(CityService.class);

    private static final BigInteger CITY_OT = ObjectTypeExtractor.extractObjectTypeId(City.class);
    private static final String CITIES_QUERY = "select object_id from tp_objects where object_type_id = ? order by order_num, name";

    @EJB
    private TPObjectFactory objectFactory;
    @EJB
    private JdbcTemplate jdbcTemplate;

    private Map<BigInteger, City> cities;

    public City create() {
        return objectFactory.createObject(City.class);
    }

    public City getById(BigInteger id) {
        return objectFactory.createObject(id, City.class);
    }

    public List<City> getAllCities() {
        if (cities == null) {
            cities = new HashMap<>(300);
        }

        try {
            return jdbcTemplate.executeSelect(
                    CITIES_QUERY,
                    new Object[][]{
                            {Types.NUMERIC, CITY_OT}
                    },
                    new ResultSetHandler<List<City>>() {
                        @Override
                        public List<City> handle(ResultSet resultSet) throws SQLException {
                            List<City> cityList = new LinkedList<City>();
                            while (resultSet.next()) {
                                BigInteger cityId = resultSet.getBigDecimal(1).toBigInteger();
                                if (cities.containsKey(cityId)) {
                                    cityList.add(cities.get(cityId));
                                } else {
                                    cityList.add(objectFactory.createObject(cityId, City.class));
                                }
                            }
                            return cityList;
                        }
                    }
            );
        } catch (SQLException e) {
            log.error("", e);
            return Collections.emptyList();
        }
    }
}

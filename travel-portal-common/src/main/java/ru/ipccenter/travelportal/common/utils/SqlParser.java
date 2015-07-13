package ru.ipccenter.travelportal.common.utils;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlParser {

    private static final Logger LOG = Logger.getLogger(SqlParser.class);

    private static final String IN_PATTERN = "\\s+IN\\s*\\(\\s*\\?\\s*\\)";

    public static String parseInAny(String sql) {
        sql = sql.toUpperCase();
        String preparedQuery = null;
        Matcher matcher = Pattern.compile(IN_PATTERN).matcher(sql);
        if (matcher.find()) {
            LOG.warn("SQL: 'COLUMN in (?)' clause was found in query string, use 'COLUMN = ANY(?)' for this case");
            preparedQuery = sql.replaceAll(IN_PATTERN, " = ANY(?)");
            LOG.warn("'" + sql + "' was replaced with: '" + preparedQuery + "'");
        }
        return preparedQuery;
    }
}

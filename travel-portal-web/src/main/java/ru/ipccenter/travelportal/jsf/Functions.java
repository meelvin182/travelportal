package ru.ipccenter.travelportal.jsf;


import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by maum on 20.04.2015.
 */
public final class Functions {

    private Functions() {
        // Hide constructor.
    }

    public static String printStackTrace(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter, true));
        return stringWriter.toString();
    }

}
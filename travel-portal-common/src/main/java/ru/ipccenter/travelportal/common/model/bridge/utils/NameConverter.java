package ru.ipccenter.travelportal.common.model.bridge.utils;

/**
 * Created by Ivan on 26.04.2015.
 */
public abstract class NameConverter {
    public static String convert(String source) {
        return source.toLowerCase().replaceAll("[\\s\\-]+", "_").intern();
    }
}

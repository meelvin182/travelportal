package ru.ipccenter.travelportal.standalone.sqlexecutor;

public interface Fragment {

    public enum Type {
        SQL,
        COMMENT,
        SEPARATOR
    }

    String getContent();
    Type getType();
}
